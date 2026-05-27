package fr.isen.hub.listeners;

import fr.isen.hub.HubPlugin;
import fr.isen.hub.managers.LogManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final HubPlugin plugin;
    private final LogManager logManager;

    public ConnectionListener(HubPlugin plugin, LogManager logManager) {
        this.plugin = plugin;
        this.logManager = logManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        logManager.logJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        logManager.logQuit(event.getPlayer());
    }
}
