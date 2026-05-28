package fr.isen.proxy.command.network.args;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.utils.MessageUtils;
import fr.isen.common.command.IsenCommandArgument;
import fr.isen.proxy.ProxyPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Collection;
import java.util.List;

public class InfoArg implements IsenCommandArgument<BungeeSender> {

    private final ProxyPlugin plugin;

    public InfoArg(ProxyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getSyntax() {
        return "/network info";
    }

    @Override
    public String getDescription() {
        return "Affiche les stats réseau en direct";
    }

    @Override
    public String getPermission() {
        return "proxy.admin";
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public void execute(BungeeSender sender, String[] args) {
        ProxyServer proxy = ProxyServer.getInstance();
        Collection<ServerInfo> servers = proxy.getServers().values();
        int totalPlayers = proxy.getOnlineCount();

        MessageUtils.sendMessage(sender, " ");
        MessageUtils.sendMessage(sender, "&8&m----------------------------------------");
        MessageUtils.sendMessage(sender, "             &c&lISEN-Craft Réseau");
        MessageUtils.sendMessage(sender, " ");
        MessageUtils.sendMessage(sender, " &8» &7Joueurs en ligne: &e" + totalPlayers);
        MessageUtils.sendMessage(sender, " &8» &7Nombre de serveurs: &e" + servers.size());
        MessageUtils.sendMessage(sender, " ");
        MessageUtils.sendMessage(sender, " &c&lServeurs connectés:");
        for (ServerInfo server : servers) {
            int count = server.getPlayers().size();
            MessageUtils.sendMessage(sender, "  &8• &a" + server.getName() + " &8» &e" + count + " &7joueurs");
        }
        MessageUtils.sendMessage(sender, "&8&m----------------------------------------");
        MessageUtils.sendMessage(sender, " ");
    }

    @Override
    public List<String> onTabComplete(BungeeSender sender, String[] args) {
        return List.of();
    }
}
