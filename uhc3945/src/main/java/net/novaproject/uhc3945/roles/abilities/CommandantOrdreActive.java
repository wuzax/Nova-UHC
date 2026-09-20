package net.novaproject.uhc3945.roles.abilities;

import net.novaproject.novauhc.ability.AbilityHooks.Dying;
import net.novaproject.novauhc.ability.template.UseAbility;
import net.novaproject.novauhc.ability.toolbox.Fx;
import net.novaproject.novauhc.ability.toolbox.TargetSelector;
import net.novaproject.novauhc.player.UHCPlayer;
import net.novaproject.novauhc.player.UHCPlayerManager;
import net.novaproject.novauhc.scenario.role.Role;
import net.novaproject.novauhc.scenario.role.reveal.KPIBuilder;
import net.novaproject.novauhc.utils.variable.Var;
import net.novaproject.novauhc.utils.variable.VariableType;
import net.novaproject.uhc3945.Camps3945;
import net.novaproject.uhc3945.Lang3945;
import net.novaproject.uhc3945.Power3945;
import net.novaproject.uhc3945.roles.Infiltre;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;

public class CommandantOrdreActive extends UseAbility implements Dying {

    @Var(name = "Épisode minimum", desc = "Épisode à partir duquel l'ordre est disponible.", type = VariableType.INTEGER, min = 1, max = 10)
    private int minEpisode = 2;

    @Var(name = "Rayon", desc = "Rayon autour du Commandant.", type = VariableType.DOUBLE, min = 4, max = 40)
    private double radius = 12;

    @Var(name = "Durée (s)", desc = "Durée des effets.", type = VariableType.TIME, min = 3, max = 20)
    private int duration = 8;

    public CommandantOrdreActive() {
        setCooldown(300);
        setMaxUse(-1);
    }

    @Override
    public String getName() {
        return "Ordre";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD_HELMET;
    }

    @Override
    public String getDescription(Player player) {
        return Power3945.text(player, Lang3945.ABILITY_DESC_COMMANDANT_ORDRE);
    }

    @Override
    public boolean onEnable(Player player) {
        if (Power3945.denyIfTooEarly(player, minEpisode)) return false;

        int ticks = duration * 20;
        apply(player, ticks);
        int allies = 0;
        for (Player nearby : TargetSelector.playersInRadius(player, radius)) {
            if (!Power3945.sameCamp(nearby, Camps3945.AXE)) continue;
            if (Power3945.roleOf(nearby) instanceof Infiltre) continue;
            apply(nearby, ticks);
            allies++;
        }
        Power3945.send(player, Lang3945.MSG_COMMANDANT_ORDRE, Map.of("%count%", allies));
        Fx.Sounds.success(player);
        return true;
    }

    private void apply(Player target, int ticks) {
        target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, ticks, 0), true);
        target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, ticks, 0), true);
    }

    @Override
    public void onDeath(UHCPlayer uhcPlayer, UHCPlayer killer, PlayerDeathEvent event) {
        if (getOwner() == null || uhcPlayer == null) return;
        if (!getOwner().getUuid().equals(uhcPlayer.getUuid())) return;

        Player self = uhcPlayer.getPlayer();
        if (self != null) {
            Power3945.send(self, Lang3945.MSG_COMMANDANT_DEATH_SELF);
        }
        for (UHCPlayer other : UHCPlayerManager.get().getPlayingOnlineUHCPlayers()) {
            if (other.getUuid().equals(uhcPlayer.getUuid())) continue;
            Role role = KPIBuilder.roleOf(other);
            if (role == null || role.getCamp() == null || !role.getCamp().is(Camps3945.AXE)) continue;
            Player p = other.getPlayer();
            if (p == null || !p.isOnline()) continue;
            Power3945.send(p, Lang3945.MSG_COMMANDANT_DEATH);
            Fx.Sounds.fail(p);
        }
    }
}
