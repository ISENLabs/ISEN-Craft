package fr.isen.proxy.command.mute.args;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.utils.MessageUtils;
import fr.isen.common.command.IsenCommandArgument;
import fr.isen.proxy.manager.MuteManager;
import net.md_5.bungee.api.ProxyServer;

import java.util.Arrays;
import java.util.List;

public class MuteArg implements IsenCommandArgument<BungeeSender> {

    private final MuteManager muteManager;

    public MuteArg(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public String getName() { return "mute"; }

    @Override
    public String getSyntax() { return "/mute <joueur> [durée] [raison]"; }

    @Override
    public String getDescription() { return "Muter un joueur (durée: 30m, 1h, 1d — permanent si absent)"; }

    @Override
    public String getPermission() { return "proxy.admin"; }

    @Override
    public boolean isPlayerOnly() { return false; }

    @Override
    public void execute(BungeeSender sender, String[] args) {
        if (args.length < 1) {
            MessageUtils.sendMessage(sender, "&cUsage: /mute <joueur> [durée] [raison]");
            return;
        }

        String name = args[0];

        if (muteManager.isMuted(name)) {
            MessageUtils.sendMessage(sender, "&c" + name + " est déjà muté.");
            return;
        }

        long duration = -1;
        int reasonStart = 1;

        if (args.length > 1 && MuteManager.isDurationArg(args[1])) {
            duration = MuteManager.parseDuration(args[1]);
            reasonStart = 2;
        }

        String reason = args.length > reasonStart
                ? String.join(" ", Arrays.copyOfRange(args, reasonStart, args.length))
                : "Aucune raison spécifiée";

        muteManager.mute(name, reason, duration);

        String durationLabel = duration <= 0 ? "permanent" : args[1];
        MessageUtils.sendMessage(sender, "&aMute appliqué sur &f" + name + " &a(" + durationLabel + "). Raison: &f" + reason);
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
