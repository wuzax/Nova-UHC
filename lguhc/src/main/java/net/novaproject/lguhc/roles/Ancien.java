package net.novaproject.lguhc.roles;

import net.novaproject.novauhc.game.PendingDeathManager;
import net.novaproject.novauhc.lang.Lang;
import net.novaproject.novauhc.lang.LangManager;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.lguhc.CampsLguhc;
import net.novaproject.lguhc.LangLguhc;
import net.novaproject.lguhc.RoleLguhc;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Ancien extends RoleLguhc {

    private boolean firstLife = true;

    public Ancien() {
        setCamp(CampsLguhc.VILLAGE);
        setStrength(0);
        setResistance(0.15);
    }

    @Override
    public String getName() {
        return "Ancien";
    }

    @Override
    public Material getIconMaterial() {
        return Material.GOLD_CHESTPLATE;
    }

    @Override
    public Lang getDescriptionLang() {
        return LangLguhc.ROLE_DESC_ANCIEN;
    }

    @Override
    public void onDeath(UHCPlayer uhcPlayer, UHCPlayer killer, PlayerDeathEvent event) {
        super.onDeath(uhcPlayer, killer, event);
        if (uhcPlayer != getOwner() || !firstLife) return;
        PendingDeathManager manager = PendingDeathManager.get();
        manager.beginPendingDeath(uhcPlayer, killer, event, 40L);
        if (!manager.isPending(uhcPlayer.getUuid())) return;
        firstLife = false;
        manager.registerResurrection(uhcPlayer, 40, () -> {
            manager.cancelPendingDeath(uhcPlayer);
            Player player = uhcPlayer.getPlayer();
            if (player != null && player.isOnline()) {
                LangManager.get().send(LangLguhc.MSG_ANCIEN_REVIVE, player);
            }
        });
    }
}
