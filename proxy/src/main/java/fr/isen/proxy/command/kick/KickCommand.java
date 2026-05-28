package fr.isen.proxy.command.kick;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.utils.MessageUtils;
import fr.isen.common.command.IsenCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.List;

public class KickCommand extends IsenCommand<BungeeSender> {

    public KickCommand() {
        super("proxy.admin");
    }

    @Override
    protected void run(BungeeSender sender, String[] args) {
        if (args.length < 1) {
            MessageUtils.sendMessage(sender, "&cUsage: /kick <joueur> [raison]");
            return;
        }
        String name = args[0];
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
        if (target == null) {
            MessageUtils.sendMessage(sender, "&cJoueur introuvable ou hors ligne.");
            return;
        }
        String reason = args.length > 1
                ? String.join(" ", Arrays.copyOfRange(args, 1, args.length))
                : "Expulsé du réseau";
        target.disconnect(MessageUtils.color("&cVous avez été expulsé du réseau.\n&7Raison: &f" + reason));
        MessageUtils.sendMessage(sender, "&aKick appliqué sur &f" + target.getName() + "&a. Raison: &f" + reason);
    }

    @Override
    public List<String> tabComplete(BungeeSender sender, String[] args) {
        if (args.length == 1) {
            return ProxyServer.getInstance().getPlayers().stream()
                    .map(ProxiedPlayer::getName)
                    .filter(n -> n.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return List.of();
    }
}
