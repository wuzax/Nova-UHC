package net.novaproject.uhc3945;

import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.scenario.role.CampWinPolicy;
import net.novaproject.novauhc.scenario.role.ModeKit;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.novauhc.scenario.role.ScenarioRole;
import net.novaproject.novauhc.scenario.role.camps.Camps;
import net.novaproject.novauhc.utils.item.ItemCreator;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.auth.AuthService;
import net.novaproject.uhc3945.auth.NetworkCommands;
import net.novaproject.uhc3945.cell.CellService;
import net.novaproject.uhc3945.cell.ResistanceCell;
import net.novaproject.uhc3945.knowledge.KnowledgeService;
import net.novaproject.uhc3945.roles.ChefReseau;
import net.novaproject.uhc3945.roles.CivilNeutre;
import net.novaproject.uhc3945.roles.Commandant;
import net.novaproject.uhc3945.roles.Infiltre;
import net.novaproject.uhc3945.roles.Informateur;
import net.novaproject.uhc3945.roles.Resistant;
import net.novaproject.uhc3945.roles.Soldat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Scenario3945 extends ScenarioRole<Role3945> {

    private static Scenario3945 instance;

    private final CellService cells = new CellService();
    private final KnowledgeService knowledge = new KnowledgeService();
    private final AuthService auth = new AuthService();
    private boolean commandsRegistered;

    @Var(name = "Isolé si effectif % 3", desc = "Si la Résistance a au moins 7 membres et un multiple de 3, un agent isolé est tiré (ex. 9 → 3+3+2+1).", type = VariableType.BOOLEAN)
    private boolean isolateWhenDivisibleBy3 = true;

    public static Scenario3945 get() {
        return instance;
    }

    public CellService cells() {
        return cells;
    }

    public KnowledgeService knowledge() {
        return knowledge;
    }

    public AuthService auth() {
        return auth;
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
        ModeKit.of(this)
                .camp(Camps3945.AXE, CampWinPolicy::together)
                .camp(Camps3945.RESISTANCE, CampWinPolicy::together)
                .camp(Camps3945.CIVILIAN, CampWinPolicy::solo)
                .uniqueRoles(Commandant.class, ChefReseau.class, Informateur.class, Infiltre.class)
                .role(Soldat.class)
                .role(Resistant.class)
                .filler(CivilNeutre.class)
                .onDistributed(this::onRolesDistributed)
                .apply();
        Bukkit.getLogger().info("[UHC 39-45] Scénario initialisé (cellules, knowledge, auth, infiltration).");
    }

    @Override
    public void onGameStart() {
        if (!commandsRegistered) {
            NetworkCommands.register();
            commandsRegistered = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        cells.clear();
        knowledge.clear();
        auth.clear();
    }

    private void onRolesDistributed(Map<UHCPlayer, Role> roles) {
        Random random = new Random();
        cells.assign(roles, isolateWhenDivisibleBy3, random);
        knowledge.grantInitial(roles, cells);
        auth.issueCredentials(cells, random);
        for (Map.Entry<UHCPlayer, Role> entry : roles.entrySet()) {
            Role role = entry.getValue();
            if (role == null || entry.getKey() == null) {
                continue;
            }
            ResistanceCell cell = cells.cellOf(entry.getKey().getUuid());
            if (cell == null) {
                continue;
            }
            for (UUID other : cell.getMembers()) {
                if (!other.equals(entry.getKey().getUuid())) {
                    role.addPartner(other);
                }
            }
        }
    }
}
