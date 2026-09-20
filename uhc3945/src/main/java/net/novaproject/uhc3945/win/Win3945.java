package net.novaproject.uhc3945.win;

public final class Win3945 {

    private Win3945() {
    }

    public enum Kind {
        NONE,
        AXE,
        RESISTANCE,
        CIVILIANS
    }

    public record Flags(
            boolean civiliansBlockMilitary,
            boolean axeRequiresCommandantAlive,
            boolean resistanceRequiresScenarioObjectives,
            boolean endWhenNoMilitaryOpposition,
            boolean survivingCiviliansShareVictory
    ) {
        public static Flags defaults() {
            return new Flags(false, false, false, true, true);
        }
    }

    public record Snapshot(
            int axeAlive,
            int resistanceAlive,
            int civilianAlive,
            int unassignedAlive,
            boolean commandantAlive,
            boolean resistanceObjectivesMet,
            boolean axeObjectivesMet
    ) {
    }

    public record Outcome(Kind kind, boolean win) {
        public static Outcome none() {
            return new Outcome(Kind.NONE, false);
        }

        public static Outcome draw() {
            return new Outcome(Kind.NONE, true);
        }

        public boolean hasCivilianShare(Flags flags, int civilianAlive) {
            return flags.survivingCiviliansShareVictory()
                    && civilianAlive > 0
                    && (kind == Kind.AXE || kind == Kind.RESISTANCE);
        }
    }

    public static Outcome evaluate(Snapshot snapshot, Flags flags) {
        int axe = Math.max(0, snapshot.axeAlive());
        int resistance = Math.max(0, snapshot.resistanceAlive());
        int civilians = Math.max(0, snapshot.civilianAlive());
        int unassigned = Math.max(0, snapshot.unassignedAlive());
        int total = axe + resistance + civilians + unassigned;

        if (total == 0) {
            return Outcome.draw();
        }
        if (unassigned > 0) {
            return Outcome.none();
        }

        boolean noAxe = axe == 0;
        boolean noResistance = resistance == 0;
        boolean civiliansBlock = flags.civiliansBlockMilitary() && civilians > 0;

        if (axe > 0 && noResistance && !civiliansBlock) {
            boolean extrasOk = snapshot.axeObjectivesMet()
                    && (!flags.axeRequiresCommandantAlive() || snapshot.commandantAlive());
            if (extrasOk || flags.endWhenNoMilitaryOpposition()) {
                return new Outcome(Kind.AXE, true);
            }
        }

        if (resistance > 0 && noAxe && !civiliansBlock) {
            boolean extrasOk = !flags.resistanceRequiresScenarioObjectives()
                    || snapshot.resistanceObjectivesMet();
            if (extrasOk || flags.endWhenNoMilitaryOpposition()) {
                return new Outcome(Kind.RESISTANCE, true);
            }
        }

        if (noAxe && noResistance && civilians > 0) {
            return new Outcome(Kind.CIVILIANS, true);
        }

        return Outcome.none();
    }
}
