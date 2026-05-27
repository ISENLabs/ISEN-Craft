package fr.isen.proxy.command.ban.args;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.utils.MessageUtils;
import fr.isen.common.command.IsenCommandArgument;
import fr.isen.proxy.manager.BanManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.List;

public class BanArg implements IsenCommandArgument<BungeeSender> {

    private final BanManager banManager;

    public BanArg(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public String getName() { return "ban"; }

    @Override
    public String getSyntax() { return "/banproxy <joueur> [raison]"; }

    @Override
    public String getDescription() { return "Bannir un joueur du réseau"; }

    @Override
    public String getPermission() { return "proxy.admin"; }

    @Override
    public boolean isPlayerOnly() { return false; }

    @Override
    public void execute(BungeeSender sender, String[] args) {
        if (args.length < 1) {
            MessageUtils.sendMessage(sender, "&cUsage: /banproxy <joueur> [raison]");
            return;
        }
        String name = args[0];
        String reason = args.length > 1
                ? String.join(" ", Arrays.copyOfRange(args, 1, args.length))
                : "Aucune raison spécifiée";

        if (banManager.isBanned(name)) {
            MessageUtils.sendMessage(sender, "&c" + name + " est déjà banni.");
            return;
        }

        banManager.ban(name, reason);

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
        if (target != null) {
            target.disconnect(MessageUtils.color("&cVous avez été banni du réseau.\n&7Raison: &f" + reason));
        }

        MessageUtils.sendMessage(sender, "&aBan appliqué sur &f" + name + "&a. Raison: &f" + reason);
    }

    @Override
    public List<String> onTabComplete(BungeeSender sender, String[] args) {
        if (args.length == 1) {
            return ProxyServer.getInstance().getPlayers().stream()
                    .map(p -> p.getName())
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return List.of();
    }
}
