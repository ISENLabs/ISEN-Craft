package fr.isen.proxy.command.ban.args;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.utils.MessageUtils;
import fr.isen.common.command.IsenCommandArgument;
import fr.isen.proxy.manager.BanManager;

import java.util.List;

public class UnbanArg implements IsenCommandArgument<BungeeSender> {

    private final BanManager banManager;

    public UnbanArg(BanManager banManager) {
        this.banManager = banManager;
    }

    @Override
    public String getName() { return "unban"; }

    @Override
    public String getSyntax() { return "/unban <joueur>"; }

    @Override
    public String getDescription() { return "Débannir un joueur du réseau"; }

    @Override
    public String getPermission() { return "proxy.admin"; }

    @Override
    public boolean isPlayerOnly() { return false; }

    @Override
    public void execute(BungeeSender sender, String[] args) {
        if (args.length < 1) {
            MessageUtils.sendMessage(sender, "&cUsage: /unban <joueur>");
            return;
        }
        String name = args[0];
        if (!banManager.isBanned(name)) {
            MessageUtils.sendMessage(sender, "&c" + name + " n'est pas banni.");
            return;
        }
        banManager.unban(name);
        MessageUtils.sendMessage(sender, "&a" + name + " a été débanni.");
    }

    @Override
    public List<String> onTabComplete(BungeeSender sender, String[] args) {
        return List.of();
    }
}
