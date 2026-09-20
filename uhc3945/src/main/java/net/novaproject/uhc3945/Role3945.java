package net.novaproject.uhc3945;

import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.novauhc.scenario.role.RoleDescription;
import org.bukkit.entity.Player;

public abstract class Role3945 extends Role {

    public abstract Lang getDescriptionLang();

    @Override
    public void appendFeatures(RoleDescription features, Player player) {
        features.line(getDescriptionLang());
        Scenario3945 scenario = Scenario3945.get();
        if (scenario != null) {
            scenario.knowledge().appendRoleCard(features, player, this, scenario.cells());
        }
    }

    @Override
    public void registerKnowPlayers() {
        Scenario3945 scenario = Scenario3945.get();
        if (scenario != null) {
            scenario.knowledge().applyToRole(this);
        }
    }
}
