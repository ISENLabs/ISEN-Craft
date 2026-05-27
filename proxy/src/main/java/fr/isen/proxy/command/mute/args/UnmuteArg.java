package fr.isen.proxy.command.mute.args;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.utils.MessageUtils;
import fr.isen.common.command.IsenCommandArgument;
import fr.isen.proxy.manager.MuteManager;

import java.util.List;

public class UnmuteArg implements IsenCommandArgument<BungeeSender> {

    private final MuteManager muteManager;

    public UnmuteArg(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public String getName() { return "unmute"; }

    @Override
    public String getSyntax() { return "/unmute <joueur>"; }

    @Override
    public String getDescription() { return "Démuter un joueur"; }

    @Override
    public String getPermission() { return "proxy.admin"; }

    @Override
    public boolean isPlayerOnly() { return false; }

    @Override
    public void execute(BungeeSender sender, String[] args) {
        if (args.length < 1) {
            MessageUtils.sendMessage(sender, "&cUsage: /unmute <joueur>");
            return;
        }
        String name = args[0];
        if (!muteManager.unmute(name)) {
            MessageUtils.sendMessage(sender, "&c" + name + " n'est pas muté.");
            return;
        }
        MessageUtils.sendMessage(sender, "&a" + name + " a été démuté.");
    }

    @Override
    public List<String> onTabComplete(BungeeSender sender, String[] args) {
        return List.of();
    }
}
