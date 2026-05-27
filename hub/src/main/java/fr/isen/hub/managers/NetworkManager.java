package fr.isen.hub.managers;

import fr.isen.common.config.IManager;
import fr.isen.hub.HubPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NetworkManager extends IManager<HubPlugin> implements PluginMessageListener {

    private final ScoreboardManager scoreboardManager;

    public NetworkManager(HubPlugin plugin, ScoreboardManager scoreboardManager) {
        super(plugin, plugin.logger, "NetworkManager");
        this.scoreboardManager = scoreboardManager;
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "fr.isen:network", this);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "fr.isen:network");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("fr.isen:network")) return;

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            String type = in.readUTF();

            if ("STATS".equals(type)) {
                Map<String, Integer> stats = new HashMap<>();
                int serverCount = in.readInt();
                for (int i = 0; i < serverCount; i++) {
                    String serverName = in.readUTF();
                    int count = in.readInt();
                    stats.put(serverName, count);
                }
                if (scoreboardManager != null) {
                    final Map<String, Integer> finalStats = stats;
                    org.bukkit.Bukkit.getScheduler().runTask(plugin, () -> scoreboardManager.updateStats(finalStats));
                }
            } else if ("RESPONSE".equals(type)) {
                // Prévu dans le protocole — pas d'action spécifique à ce stade
            }
        } catch (IOException e) {
            plugin.logger.logException("Erreur désérialisation message du network", e, "ERROR");
        }
    }

    public void sendRequest(Player vecteur) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("REQUEST");
            vecteur.sendPluginMessage(plugin, "fr.isen:network", b.toByteArray());
        } catch (IOException e) {
            plugin.logger.logException("Erreur envoi REQUEST du network", e, "ERROR");
        }
    }
}
