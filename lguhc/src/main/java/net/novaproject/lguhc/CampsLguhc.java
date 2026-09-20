package net.novaproject.lguhc;

import lombok.experimental.Delegate;
import net.novaproject.novauhc.scenario.role.camps.AbstractCamp;
import net.novaproject.novauhc.scenario.role.camps.Camps;
import org.bukkit.DyeColor;

public enum CampsLguhc implements Camps {

    VILLAGE("Village", DyeColor.LIME),
    LOUPS("Loups", DyeColor.RED),
    SOLITAIRE("Solitaires", DyeColor.YELLOW);

    @Delegate
    private final Camps delegate;

    CampsLguhc(String name, DyeColor color) {
        this.delegate = new AbstractCamp(name, color) {};
    }
}
