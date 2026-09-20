package net.novaproject.uhc3945.groups;

import net.novaproject.novauhc.scenario.role.camps.Camps;
import net.novaproject.uhc3945.Camps3945;

public record GroupLimitSettings(
        boolean enabled,
        double radius,
        int graceSeconds,
        int countdownSeconds,
        int axisMax,
        int resistanceMax,
        int civilianMax,
        int preRoleMax,
        boolean disableAtMeetup,
        int meetupAxisMax,
        int meetupResistanceMax,
        int meetupCivilianMax,
        boolean ignoreCombat,
        boolean ignoreFleeing,
        boolean revealZone,
        int revealPrecision,
        boolean suppressAbilities,
        int weaknessLevel,
        int fatigueLevel,
        int slownessLevel
) {

    public int limitFor(Camps camp, boolean rolesDistributed, boolean meetup) {
        if (!rolesDistributed) {
            return Math.max(1, preRoleMax);
        }
        if (meetup && !disableAtMeetup) {
            if (isAxe(camp)) return Math.max(1, meetupAxisMax);
            if (isResistance(camp)) return Math.max(1, meetupResistanceMax);
            return Math.max(1, meetupCivilianMax);
        }
        if (isAxe(camp)) return Math.max(1, axisMax);
        if (isResistance(camp)) return Math.max(1, resistanceMax);
        return Math.max(1, civilianMax);
    }

    public double radiusSquared() {
        double r = Math.max(0, radius);
        return r * r;
    }

    public static boolean isAxe(Camps camp) {
        return camp != null && camp.isOrHasParent(Camps3945.AXE);
    }

    public static boolean isResistance(Camps camp) {
        return camp != null && camp.isOrHasParent(Camps3945.RESISTANCE);
    }
}
