package net.novaproject.uhc3945;

import net.novaproject.novauhc.game.VictoryManager;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.player.UHCPlayerManager;
import net.novaproject.novauhc.scenario.role.CampWinPolicy;
import net.novaproject.novauhc.scenario.role.ModeKit;
import net.novaproject.novauhc.scenario.role.ScenarioRole;
import net.novaproject.novauhc.scenario.role.camps.Camps;
import net.novaproject.novauhc.utils.item.ItemCreator;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.novauhc.event.UhcGameEvents.UhcGameWinEvent;
import net.novaproject.uhc3945.roles.ChefReseau;
import net.novaproject.uhc3945.roles.CivilNeutre;
import net.novaproject.uhc3945.roles.Commandant;
import net.novaproject.uhc3945.roles.Informateur;
import net.novaproject.uhc3945.roles.Resistant;
import net.novaproject.uhc3945.roles.Soldat;
import net.novaproject.uhc3945.win.PersonalObjective;
import net.novaproject.uhc3945.win.PersonalObjectiveTracker;
import net.novaproject.uhc3945.win.Win3945;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.function.BooleanSupplier;

public class Scenario3945 extends ScenarioRole<Role3945> implements Listener {

    private static Scenario3945 instance;

    @Var(name = "Civils bloquent la victoire militaire",
            desc = "Si activé, l'Axe ou la Résistance ne gagne que lorsque plus aucun civil n'est en vie.",
            type = VariableType.BOOLEAN)
    private boolean civiliansBlockMilitary = false;

    @Var(name = "Axe : Commandant requis",
            desc = "Si activé, l'Axe ne gagne que si le Commandant est vivant. Ignoré s'il n'y a plus d'opposition et que la fin forcée est activée.",
            type = VariableType.BOOLEAN)
    private boolean axeRequiresCommandantAlive = false;

    @Var(name = "Résistance : objectifs de scénario",
            desc = "Si activé, la victoire Résistance exige les hooks d'objectifs (cellules, etc.). Ignoré s'il n'y a plus d'opposition et que la fin forcée est activée.",
            type = VariableType.BOOLEAN)
    private boolean resistanceRequiresScenarioObjectives = false;

    @Var(name = "Fin sans opposition militaire",
            desc = "La partie se termine dès qu'un camp n'a plus d'opposition militaire, même si des objectifs annexes ne sont pas remplis.",
            type = VariableType.BOOLEAN)
    private boolean endWhenNoMilitaryOpposition = true;

    @Var(name = "Civils survivants partagent la victoire",
            desc = "Les civils encore en vie sont mentionnés avec le camp militaire vainqueur s'ils remplissent leur objectif personnel.",
            type = VariableType.BOOLEAN)
    private boolean survivingCiviliansShareVictory = true;

    private final PersonalObjectiveTracker personalObjectives = new PersonalObjectiveTracker();
    private BooleanSupplier resistanceScenarioCondition = () -> true;
    private BooleanSupplier axeScenarioCondition = () -> true;
    private Win3945.Outcome lastOutcome = Win3945.Outcome.none();

    public static Scenario3945 get() {
        return instance;
    }

    public PersonalObjectiveTracker personalObjectives() {
        return personalObjectives;
    }

    public void setResistanceScenarioCondition(BooleanSupplier condition) {
        this.resistanceScenarioCondition = condition == null ? () -> true : condition;
    }

    public void setAxeScenarioCondition(BooleanSupplier condition) {
        this.axeScenarioCondition = condition == null ? () -> true : condition;
    }

    public Win3945.Flags winFlags() {
        return new Win3945.Flags(
                civiliansBlockMilitary,
                axeRequiresCommandantAlive,
                resistanceRequiresScenarioObjectives,
                endWhenNoMilitaryOpposition,
                survivingCiviliansShareVictory);
    }

    public Win3945.Outcome lastWinOutcome() {
        return lastOutcome;
    }

    @Override
    public Camps[] getCamps() {
        return Camps3945.values();
    }

    @Override
    public String getName() {
        return "UHC 39-45";
    }

    @Override
    public String getDescription(Player player) {
        return LangManager.get().get(Lang3945.SCENARIO_DESC, player);
    }

    @Override
    public ItemCreator getItem() {
        return new ItemCreator(Material.MAP).setName("§8§lUHC 39-45");
    }

    @Override
    public String getColor() {
        return "§8";
    }

    @Override
    public String getPrefix() {
        return "§8§l39-45 §8│";
    }

    @Override
    public void setup() {
        super.setup();
        instance = this;
        personalObjectives.reset();
        lastOutcome = Win3945.Outcome.none();
        ModeKit.of(this)
                .camp(Camps3945.AXE, CampWinPolicy::together)
                .camp(Camps3945.RESISTANCE, CampWinPolicy::together)
                .camp(Camps3945.CIVILIAN, CampWinPolicy::solo)
                .winCondition(roles -> evaluateWin().win())
                .uniqueRoles(Commandant.class, ChefReseau.class, Informateur.class)
                .role(Soldat.class)
                .role(Resistant.class)
                .filler(CivilNeutre.class)
                .revealWithin(Camps3945.AXE)
                .apply();
        Bukkit.getLogger().info("[UHC 39-45] Scénario initialisé (camps, rôles stub, conditions de victoire).");
    }

    @Override
    public void onGameStart() {
        super.onGameStart();
        VictoryManager.setWinLabelResolver(this::resolveWinLabel);
    }

    @Override
    public void onStop() {
        super.onStop();
        VictoryManager.setWinLabelResolver(null);
        personalObjectives.reset();
        lastOutcome = Win3945.Outcome.none();
        resistanceScenarioCondition = () -> true;
        axeScenarioCondition = () -> true;
    }

    @Override
    public Camps getWinningCamp() {
        Camps camp = campOf(lastOutcome);
        return camp != null ? camp : super.getWinningCamp();
    }

    private static Camps campOf(Win3945.Outcome outcome) {
        if (outcome == null || !outcome.win()) return null;
        return switch (outcome.kind()) {
            case AXE -> Camps3945.AXE;
            case RESISTANCE -> Camps3945.RESISTANCE;
            case CIVILIANS -> Camps3945.CIVILIAN;
            case NONE -> null;
        };
    }

    @EventHandler
    public void onGameWin(UhcGameWinEvent event) {
        if (!isActive()) return;
        Win3945.Outcome outcome = lastOutcome;
        if (outcome == null || !outcome.win() || outcome.kind() == Win3945.Kind.NONE) return;

        int civilianWinners = countCivilianWinners(event.getWinners());
        boolean share = outcome.hasCivilianShare(winFlags(), civilianWinners);
        LangManager.get().sendAll(detailLang(outcome.kind(), share));
    }

    private Win3945.Outcome evaluateWin() {
        Win3945.Snapshot snapshot = snapshotAlive();
        lastOutcome = Win3945.evaluate(snapshot, winFlags());
        if (lastOutcome.win()) {
            Bukkit.getLogger().info("[UHC 39-45] Victoire détectée : " + lastOutcome.kind()
                    + " (Axe=" + snapshot.axeAlive()
                    + ", Résistance=" + snapshot.resistanceAlive()
                    + ", Civils=" + snapshot.civilianAlive() + ").");
        }
        return lastOutcome;
    }

    private Win3945.Snapshot snapshotAlive() {
        int axe = 0;
        int resistance = 0;
        int civilians = 0;
        int unassigned = 0;
        boolean commandantAlive = false;
        for (UHCPlayer player : UHCPlayerManager.get().getPlayingOnlineUHCPlayers()) {
            Role3945 role = getRoleByUHCPlayer(player);
            if (role == null || role.getCamp() == null) {
                unassigned++;
                continue;
            }
            if (role.getCamp().is(Camps3945.AXE)) {
                axe++;
                if (role instanceof Commandant) commandantAlive = true;
            } else if (role.getCamp().is(Camps3945.RESISTANCE)) {
                resistance++;
            } else if (role.getCamp().is(Camps3945.CIVILIAN)) {
                civilians++;
            } else {
                unassigned++;
            }
        }
        return new Win3945.Snapshot(
                axe,
                resistance,
                civilians,
                unassigned,
                commandantAlive,
                safeCondition(resistanceScenarioCondition),
                safeCondition(axeScenarioCondition));
    }

    private String resolveWinLabel(List<UHCPlayer> winners) {
        if (!isActive() || lastOutcome == null || !lastOutcome.win()) return null;
        int civilianWinners = countCivilianWinners(winners);
        boolean share = lastOutcome.hasCivilianShare(winFlags(), civilianWinners);
        Lang3945 key = switch (lastOutcome.kind()) {
            case AXE -> share ? Lang3945.WIN_LABEL_AXE_AND_CIVILS : Lang3945.WIN_LABEL_AXE;
            case RESISTANCE -> share ? Lang3945.WIN_LABEL_RESISTANCE_AND_CIVILS : Lang3945.WIN_LABEL_RESISTANCE;
            case CIVILIANS -> Lang3945.WIN_LABEL_CIVILIANS;
            case NONE -> null;
        };
        return key == null ? null : LangManager.get().get(key);
    }

    private int countCivilianWinners(List<UHCPlayer> winners) {
        if (winners == null || winners.isEmpty()) return 0;
        int count = 0;
        for (UHCPlayer winner : winners) {
            if (isPersonalCivilianWinner(winner)) count++;
        }
        return count;
    }

    private boolean isPersonalCivilianWinner(UHCPlayer player) {
        if (player == null) return false;
        Role3945 role = getRoleByUHCPlayer(player);
        if (role == null || role.getCamp() == null || !role.getCamp().is(Camps3945.CIVILIAN)) return false;
        PersonalObjective objective = role.personalObjective();
        return personalObjectives.hasPersonalWin(player.getUuid(), objective, player.isPlaying());
    }

    private static Lang3945 detailLang(Win3945.Kind kind, boolean civilianShare) {
        return switch (kind) {
            case AXE -> civilianShare ? Lang3945.WIN_DETAIL_AXE_AND_CIVILS : Lang3945.WIN_DETAIL_AXE;
            case RESISTANCE -> civilianShare ? Lang3945.WIN_DETAIL_RESISTANCE_AND_CIVILS : Lang3945.WIN_DETAIL_RESISTANCE;
            case CIVILIANS -> Lang3945.WIN_DETAIL_CIVILIANS;
            case NONE -> Lang3945.WIN_DETAIL_CIVILIANS;
        };
    }

    private static boolean safeCondition(BooleanSupplier condition) {
        if (condition == null) return true;
        try {
            return condition.getAsBoolean();
        } catch (Throwable t) {
            Bukkit.getLogger().warning("[UHC 39-45] Hook d'objectif de scénario en échec : " + t.getMessage());
            return true;
        }
    }
}
