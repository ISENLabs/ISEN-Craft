package fr.isen.proxy.manager;

import fr.isen.bungee.utils.MessageUtils;
import fr.isen.proxy.ProxyPlugin;
import fr.isen.proxy.command.HubCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HubManager {

    public HubManager(ProxyPlugin plugin) {
        plugin.registerCommand("hub", new HubCommand(this));
        plugin.registerCommand("lobby", new HubCommand(this));
    }

    public void sendToHub(ProxiedPlayer player, String server) {
        ServerInfo hub = ProxyServer.getInstance().getServerInfo(server);

        if (hub == null) {
            MessageUtils.sendMessage(player, "&cLe serveur est introuvable.");
            return;
        }

        if (player.getServer().getInfo().getName().equalsIgnoreCase(server)) {
            MessageUtils.sendMessage(player, "&cTu es déjà au hub.");
            return;
        }

        player.connect(hub);
        MessageUtils.sendMessage(player, "&2Téléportation au hub...");
    }
}
