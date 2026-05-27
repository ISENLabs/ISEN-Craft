package fr.isen.proxy.command.find;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.utils.MessageUtils;
import fr.isen.common.command.IsenCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FindCommand extends IsenCommand<BungeeSender> {

    public FindCommand() {
        super();
    }

    @Override
    protected void run(BungeeSender sender, String[] args) {
        if (args.length < 1) {
            MessageUtils.sendMessage(sender, "&cUsage: /find <joueur>");
            return;
        }
        String name = args[0];
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
        if (target == null) {
            MessageUtils.sendMessage(sender, "&cJoueur introuvable ou hors ligne.");
            return;
        }
        String server = target.getServer().getInfo().getName();
        MessageUtils.sendMessage(sender, "&f" + target.getName() + " &7est sur &e" + server + "&7.");
    }
}
