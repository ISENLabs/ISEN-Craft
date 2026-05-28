package fr.isen.hub.listeners;

import fr.isen.hub.managers.HubManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class VoidListener implements Listener {

    private final HubManager manager;

    public VoidListener(HubManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onFall(PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) return;
        if (event.getTo().getY() <= -90) {
            manager.teleportToSpawn(event.getPlayer());
        }
    }
}
