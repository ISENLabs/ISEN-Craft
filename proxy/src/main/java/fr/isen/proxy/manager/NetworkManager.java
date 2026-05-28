package fr.isen.proxy.manager;

import fr.isen.common.config.IManager;
import fr.isen.proxy.ProxyPlugin;
import fr.isen.proxy.command.network.NetworkCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class NetworkManager extends IManager<ProxyPlugin> implements Listener {

    private final ConfigManager configManager;

    public NetworkManager(ProxyPlugin plugin, ConfigManager configManager) {
        super(plugin, plugin.logger, "NetworkManager");
        this.configManager = configManager;
        ProxyServer.getInstance().registerChannel("fr.isen:network");
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
        plugin.registerCommand("network", new NetworkCommand(plugin));
    }

    private void sendNetworkStats() {
        String hubServerName = configManager.getString("servers.hub", "hub");

        byte[] payload;
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            out.writeUTF("STATS");

            Map<String, net.md_5.bungee.api.config.ServerInfo> servers = ProxyServer.getInstance().getServers();
            out.writeInt(servers.size());
            for (Map.Entry<String, net.md_5.bungee.api.config.ServerInfo> entry : servers.entrySet()) {
                out.writeUTF(entry.getKey());
                out.writeInt(entry.getValue().getPlayers().size());
            }

            payload = b.toByteArray();
        } catch (IOException e) {
            plugin.logger.log("Erreur envoi stats réseau", "ERROR");
            e.printStackTrace();
            return;
        }

        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (p.getServer() != null && p.getServer().getInfo().getName().equalsIgnoreCase(hubServerName)) {
                p.getServer().sendData("fr.isen:network", payload);
            }
        }
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        sendNetworkStats();
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        sendNetworkStats();
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("fr.isen:network")) return;
        if (!(event.getSender() instanceof net.md_5.bungee.api.connection.Server)) return;

        event.setCancelled(true);

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
            String type = in.readUTF();

            if ("REQUEST".equals(type)) {
                sendNetworkStats();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
