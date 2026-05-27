package fr.isen.proxy.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OpSyncProxyListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("BungeeCord")) return;

        String data = new String(event.getData());

        if (data.startsWith("OpSync:")) {
            String[] parts = data.split(":");
            if (parts.length < 3) return;

            String playerName = parts[1];
            boolean setAdmin = Boolean.parseBoolean(parts[2]);

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
            if (player != null) {
                player.setPermission("proxy.admin", setAdmin);

                ProxyServer.getInstance().getLogger().info("[ISEN-Craft] Permission 'proxy.admin' " +
                        (setAdmin ? "accordée à " : "retirée pour ") + playerName);
            }
        }
    }
}