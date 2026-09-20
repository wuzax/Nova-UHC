package net.novaproject.uhc3945;

import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.novauhc.scenario.role.RoleDescription;
import net.novaproject.uhc3945.win.PersonalObjective;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class Role3945 extends Role {

    public abstract Lang getDescriptionLang();

    public PersonalObjective personalObjective() {
        return null;
    }

    public void completePersonalObjective() {
        completePersonalObjective(null);
    }

    public void completePersonalObjective(String objectiveId) {
        Scenario3945 scenario = Scenario3945.get();
        UHCPlayer owner = getOwner();
        if (scenario == null || owner == null) return;
        PersonalObjective objective = personalObjective();
        String id = objectiveId != null ? objectiveId : (objective == null ? null : objective.id());
        if (id == null || id.isBlank()) return;
        scenario.personalObjectives().complete(owner.getUuid(), id);
    }

    public void failPersonalObjective() {
        Scenario3945 scenario = Scenario3945.get();
        UHCPlayer owner = getOwner();
        PersonalObjective objective = personalObjective();
        if (scenario == null || owner == null || objective == null) return;
        scenario.personalObjectives().fail(owner.getUuid(), objective.id());
    }

    @Override
    public void appendFeatures(RoleDescription features, Player player) {
        features.line(getDescriptionLang());
        PersonalObjective objective = personalObjective();
        if (objective != null && getCamp() != null && getCamp().is(Camps3945.CIVILIAN)) {
            features.line(Lang3945.ROLE_PERSONAL_OBJECTIVE, Map.of(
                    "%objective%", objective.descriptionLang() == null
                            ? objective.id()
                            : LangManager.get().get(objective.descriptionLang(), player)));
        }
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
