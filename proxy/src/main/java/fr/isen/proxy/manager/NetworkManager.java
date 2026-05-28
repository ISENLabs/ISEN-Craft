package fr.isen.proxy.manager;

import fr.isen.bungee.utils.MessageUtils;
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
import java.util.HashMap;
import java.util.Map;

public class NetworkManager extends IManager<ProxyPlugin> implements Listener {

    private final ConfigManager configManager;
    private final Map<String, Integer> prevStats = new HashMap<>();

    public NetworkManager(ProxyPlugin plugin, ConfigManager configManager) {
        super(plugin, plugin.logger, "NetworkManager");
        this.configManager = configManager;
        ProxyServer.getInstance().registerChannel("fr.isen:network");
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
        plugin.registerCommand("network", new NetworkCommand(plugin));
    }

    private void sendNetworkStats() {
        Map<String, Integer> currentStats = new HashMap<>();
        for (Map.Entry<String, net.md_5.bungee.api.config.ServerInfo> entry :
                ProxyServer.getInstance().getServers().entrySet()) {
            currentStats.put(entry.getKey(), entry.getValue().getPlayers().size());
        }

        if (configManager.getBoolean("notifications.enabled", true)) {
            broadcastStateChanges(currentStats);
        }

        String hubServerName = configManager.getString("servers.hub", "hub");
        byte[] payload;
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("STATS");
            out.writeInt(currentStats.size());
            for (Map.Entry<String, Integer> entry : currentStats.entrySet()) {
                out.writeUTF(entry.getKey());
                out.writeInt(entry.getValue());
            }
            payload = b.toByteArray();
        } catch (IOException e) {
            plugin.logger.logException("Erreur envoi stats du network", e, "ERROR");
            return;
        }

        prevStats.clear();
        prevStats.putAll(currentStats);

        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (p.getServer() != null &&
                    p.getServer().getInfo().getName().equalsIgnoreCase(hubServerName)) {
                p.getServer().sendData("fr.isen:network", payload);
            }
        }
    }

    private void broadcastStateChanges(Map<String, Integer> currentStats) {
        String onlineMsg = configManager.getString("notifications.server-online",
                "&a[Network] &fLe serveur &a{server} &fest maintenant actif !");
        String emptyMsg = configManager.getString("notifications.server-empty",
                "&7[Network] &fLe serveur &7{server} &fest maintenant vide.");

        for (Map.Entry<String, Integer> entry : currentStats.entrySet()) {
            String server = entry.getKey();
            int current = entry.getValue();
            int previous = prevStats.getOrDefault(server, -1);

            if (previous == -1) continue; // skip on first load — no previous state yet

            if (previous == 0 && current > 0) {
                MessageUtils.broadcast(onlineMsg.replace("{server}", server));
            } else if (previous > 0 && current == 0) {
                MessageUtils.broadcast(emptyMsg.replace("{server}", server));
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
            plugin.logger.logException("Erreur désérialisation message network", e, "ERROR");
        }
    }
}
