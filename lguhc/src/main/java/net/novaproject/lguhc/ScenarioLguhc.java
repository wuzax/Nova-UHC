package net.novaproject.lguhc;

import net.novaproject.novauhc.UHCManager;
import net.novaproject.novauhc.game.VictoryManager;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.player.UHCPlayerManager;
import net.novaproject.novauhc.scenario.Scenario;
import net.novaproject.novauhc.scenario.ScenarioManager;
import net.novaproject.novauhc.scenario.role.Bonds;
import net.novaproject.novauhc.scenario.role.CampWinPolicy;
import net.novaproject.novauhc.scenario.role.ModeKit;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.novauhc.scenario.role.ScenarioRole;
import net.novaproject.novauhc.scenario.role.camps.Camps;
import net.novaproject.novauhc.utils.chat.ChatManager;
import net.novaproject.novauhc.utils.item.ItemCreator;
import net.novaproject.lguhc.roles.Ancien;
import net.novaproject.lguhc.roles.Chasseur;
import net.novaproject.lguhc.roles.Cupidon;
import net.novaproject.lguhc.roles.LoupGarou;
import net.novaproject.lguhc.roles.PetiteFille;
import net.novaproject.lguhc.roles.Sorciere;
import net.novaproject.lguhc.roles.Villageois;
import net.novaproject.lguhc.roles.Voyante;
import net.novaproject.lguhc.win.WinLguhc;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ScenarioLguhc extends ScenarioRole<RoleLguhc> implements Listener {

    public static final String NAME = "LG-UHC";
    private static final String WOLF_CHANNEL = LguhcPower.WOLF_CHAT_ID;

    private static ScenarioLguhc instance;
    private WinLguhc.Outcome lastOutcome = WinLguhc.Outcome.none();

    public static ScenarioLguhc get() {
        return instance;
    }

    @Override
    public Camps[] getCamps() {
        return CampsLguhc.values();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription(Player player) {
        return LangManager.get().get(LangLguhc.SCENARIO_DESC, player);
    }

    @Override
    public ItemCreator getItem() {
        return new ItemCreator(Material.ROTTEN_FLESH).setName("§c§lLG-UHC");
    }

    @Override
    public String getColor() {
        return "§c";
    }

    @Override
    public String getPrefix() {
        return "§c§lLG-UHC §8│";
    }

    @Override
    public void setup() {
        super.setup();
        instance = this;
        lastOutcome = WinLguhc.Outcome.none();
        ModeKit.of(this)
                .camp(CampsLguhc.VILLAGE, CampWinPolicy::together)
                .camp(CampsLguhc.LOUPS, CampWinPolicy::together)
                .camp(CampsLguhc.SOLITAIRE, CampWinPolicy::solo)
                .winCondition(roles -> evaluateWin().win())
                .uniqueRoles(Voyante.class, Sorciere.class, Chasseur.class, PetiteFille.class, Cupidon.class, Ancien.class)
                .role(LoupGarou.class)
                .filler(Villageois.class)
                .revealWithin(CampsLguhc.LOUPS)
                .onDistributed(this::onRolesDistributed)
                .apply();
        seedDefaultComposition();
    }

    private void seedDefaultComposition() {
        bumpOnce(Voyante.class);
        bumpOnce(Sorciere.class);
        bumpOnce(Chasseur.class);
        bumpOnce(PetiteFille.class);
        bumpOnce(Cupidon.class);
        bumpOnce(Ancien.class);
        if (getRoleAmount(LoupGarou.class) == 0) {
            incrementRole(LoupGarou.class);
            incrementRole(LoupGarou.class);
        }
    }

    private void bumpOnce(Class<? extends RoleLguhc> roleClass) {
        if (getRoleAmount(roleClass) == 0) incrementRole(roleClass);
    }

    @Override
    public void toggleActive() {
        super.toggleActive();
        if (!isActive()) return;
        UHCManager.get().setTeam_size(1);
        for (Scenario other : ScenarioManager.get().getSpecialScenarios()) {
            if (other != this && other.isActive()) {
                other.toggleActive();
            }
        }
    }

    @Override
    public void onGameStart() {
        super.onGameStart();
        UHCManager.get().setTeam_size(1);
        VictoryManager.setWinLabelResolver(this::resolveWinLabel);
        ChatManager.get()
                .createCampChannel(WOLF_CHANNEL, CampsLguhc.LOUPS, LguhcPower.WOLF_CHAT_PREFIX)
                .prefixRequiresWhitespaceBoundary(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        VictoryManager.setWinLabelResolver(null);
        ChatManager.get().unregisterChannel(WOLF_CHANNEL);
        lastOutcome = WinLguhc.Outcome.none();
    }

    private void onRolesDistributed(Map<UHCPlayer, Role> roles) {
        LangManager.get().sendAll(LangLguhc.MSG_WOLF_CHAT);
    }

    private WinLguhc.Outcome evaluateWin() {
        lastOutcome = WinLguhc.evaluate(snapshot());
        return lastOutcome;
    }

    private WinLguhc.Snapshot snapshot() {
        int village = 0;
        int loups = 0;
        int solitaire = 0;
        Set<UUID> alive = new HashSet<>();
        for (UHCPlayer player : UHCPlayerManager.get().getPlayingOnlineUHCPlayers()) {
            Role role = getRoleByUHCPlayer(player);
            if (role == null || role.getCamp() == null) continue;
            alive.add(player.getUuid());
            if (role.getCamp().is(CampsLguhc.VILLAGE)) village++;
            else if (role.getCamp().is(CampsLguhc.LOUPS)) loups++;
            else if (role.getCamp().is(CampsLguhc.SOLITAIRE)) solitaire++;
        }
        return new WinLguhc.Snapshot(village, loups, solitaire, coupleCovers(alive));
    }

    private static boolean coupleCovers(Set<UUID> alive) {
        if (alive.size() < 2) return false;
        UUID any = alive.iterator().next();
        Set<UUID> bond = Bonds.VictoryLinks.winGroupOf(any);
        return !bond.isEmpty() && bond.containsAll(alive);
    }

    @Override
    public Camps getWinningCamp() {
        Camps camp = campOf(lastOutcome);
        return camp != null ? camp : super.getWinningCamp();
    }

    private static Camps campOf(WinLguhc.Outcome outcome) {
        if (outcome == null || !outcome.win()) return null;
        return switch (outcome.kind()) {
            case LOUPS -> CampsLguhc.LOUPS;
            case VILLAGE -> CampsLguhc.VILLAGE;
            case SOLITAIRE, COUPLE -> CampsLguhc.SOLITAIRE;
            case NONE -> null;
        };
    }

    private String resolveWinLabel(List<UHCPlayer> winners) {
        return switch (lastOutcome.kind()) {
            case LOUPS -> "§cLes Loups-Garous";
            case VILLAGE -> "§aLe Village";
            case COUPLE -> "§dLe Couple";
            case SOLITAIRE -> "§eSolitaire";
            case NONE -> null;
        };
    }
}
