package fr.isen.proxy.manager;

import fr.isen.bungee.utils.MessageUtils;
import fr.isen.common.config.IManager;
import fr.isen.proxy.ProxyPlugin;
import fr.isen.proxy.command.HubCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class HubManager extends IManager<ProxyPlugin> {

    private final ConfigManager configManager;

    public HubManager(ProxyPlugin plugin, ConfigManager configManager) {
        super(plugin, plugin.logger, "HubManager");
        this.configManager = configManager;
        plugin.registerCommand("hub", new HubCommand(this));
        plugin.registerCommand("lobby", new HubCommand(this));
    }

    public void sendToHub(ProxiedPlayer player) {
        String server = configManager.getString("servers.hub", "hub");
        ServerInfo hub = ProxyServer.getInstance().getServerInfo(server);

        if (hub == null) {
            MessageUtils.sendMessage(player, configManager.getString("messages.not-found", "&cLe serveur est introuvable."));
            return;
        }

        if (player.getServer().getInfo().getName().equalsIgnoreCase(server)) {
            MessageUtils.sendMessage(player, configManager.getString("messages.already-hub", "&cTu es déjà au hub."));
            return;
        }

        MessageUtils.sendMessage(player, configManager.getString("messages.hub-redirect", "&2Téléportation au hub..."));
        player.connect(hub);
    }
}
