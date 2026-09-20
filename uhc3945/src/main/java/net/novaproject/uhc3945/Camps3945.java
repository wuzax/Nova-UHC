package net.novaproject.uhc3945;

import lombok.experimental.Delegate;
import net.novaproject.novauhc.scenario.role.camps.AbstractCamp;
import net.novaproject.novauhc.scenario.role.camps.Camps;
import org.bukkit.DyeColor;

public enum Camps3945 implements Camps {

    AXE("Axe", DyeColor.GRAY),
    RESISTANCE("Résistance", DyeColor.BLUE),
    CIVILIAN("Civils", DyeColor.WHITE);

    @Delegate
    private final Camps delegate;

    Camps3945(String name, DyeColor color) {
        this.delegate = new AbstractCamp(name, color) {};
    }
}
