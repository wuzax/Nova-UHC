package net.novaproject.lguhc.win;

public final class WinLguhc {

    private WinLguhc() {
    }

    public enum Kind {
        NONE,
        VILLAGE,
        LOUPS,
        SOLITAIRE,
        COUPLE
    }

    public record Snapshot(
            int villageAlive,
            int loupsAlive,
            int solitaireAlive,
            boolean coupleCoversAllAlive
    ) {
    }

    public record Outcome(Kind kind, boolean win) {
        public static Outcome none() {
            return new Outcome(Kind.NONE, false);
        }
    }

    public static Outcome evaluate(Snapshot snapshot) {
        int village = Math.max(0, snapshot.villageAlive());
        int loups = Math.max(0, snapshot.loupsAlive());
        int solitaire = Math.max(0, snapshot.solitaireAlive());
        int total = village + loups + solitaire;

        if (total == 0) {
            return new Outcome(Kind.NONE, true);
        }
        if (snapshot.coupleCoversAllAlive() && total >= 2) {
            return new Outcome(Kind.COUPLE, true);
        }
        if (loups > 0 && village == 0) {
            return new Outcome(Kind.LOUPS, true);
        }
        if (village > 0 && loups == 0) {
            return new Outcome(Kind.VILLAGE, true);
        }
        if (village == 0 && loups == 0 && solitaire > 0) {
            return new Outcome(Kind.SOLITAIRE, true);
        }
        return Outcome.none();
    }
}
