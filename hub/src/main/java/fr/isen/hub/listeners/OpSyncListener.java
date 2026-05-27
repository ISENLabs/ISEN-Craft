package fr.isen.hub.listeners;

import fr.isen.hub.HubPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OpSyncListener implements Listener {

    private final HubPlugin plugin;

    public OpSyncListener(HubPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onOpCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase();

        if (message.startsWith("/op ") || message.startsWith("/deop ")) {
            String[] args = event.getMessage().split(" ");
            if (args.length < 2) return;

            String targetPlayer = args[1];
            boolean isOp = message.startsWith("/op ");

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                String payload = "OpSync:" + targetPlayer + ":" + isOp;

                event.getPlayer().sendPluginMessage(plugin, "BungeeCord", payload.getBytes());
            }, 2L);
        }
    }
}