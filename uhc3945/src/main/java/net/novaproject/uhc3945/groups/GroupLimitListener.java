package net.novaproject.uhc3945.groups;

import net.novaproject.novauhc.UHCManager;
import net.novaproject.novauhc.event.UhcGameEvents.UhcGameSecondEvent;
import net.novaproject.novauhc.event.UhcGameEvents.UhcGameStateChangeEvent;
import net.novaproject.novauhc.event.UhcWorldEvents.UhcBorderStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class GroupLimitListener implements Listener {

    @EventHandler
    public void onSecond(UhcGameSecondEvent event) {
        GroupLimitService.get().tick();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMeetup(UhcBorderStartEvent event) {
        GroupLimitService.get().onMeetup();
    }

    @EventHandler
    public void onState(UhcGameStateChangeEvent event) {
        if (event.getOldState() == UHCManager.GameState.INGAME
                || event.getNewState() == UHCManager.GameState.INGAME) {
            GroupLimitService.get().reset();
        }
    }
}
