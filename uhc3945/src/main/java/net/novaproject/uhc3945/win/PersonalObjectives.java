package net.novaproject.uhc3945.win;

import net.novaproject.novauhc.lang.Lang;
import net.novaproject.uhc3945.Lang3945;

public final class PersonalObjectives {

    public static final PersonalObjective SURVIVE = of("survive", Lang3945.OBJ_SURVIVE, true);
    public static final PersonalObjective EXFILTRATE = of("exfiltrate", Lang3945.OBJ_EXFILTRATE, false);
    public static final PersonalObjective INFORM = of("inform", Lang3945.OBJ_INFORM, true);

    private PersonalObjectives() {
    }

    public static PersonalObjective of(String id, Lang descriptionLang, boolean surviveCounts) {
        String normalized = id == null ? "" : id.trim().toLowerCase();
        return new PersonalObjective() {
            @Override
            public String id() {
                return normalized;
            }

            @Override
            public Lang descriptionLang() {
                return descriptionLang;
            }

            @Override
            public boolean surviveCounts() {
                return surviveCounts;
            }
        };
    }
}
