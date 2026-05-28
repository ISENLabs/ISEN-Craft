package fr.isen.proxy.command.network;

import fr.isen.bungee.command.BungeeSender;
import fr.isen.common.command.IsenCommand;
import fr.isen.proxy.ProxyPlugin;
import fr.isen.proxy.command.network.args.InfoArg;

public class NetworkCommand extends IsenCommand<BungeeSender> {

    private final ProxyPlugin plugin;

    public NetworkCommand(ProxyPlugin plugin) {
        super("proxy.admin");
        this.plugin = plugin;
        registerArgument(new InfoArg(plugin));
    }

    @Override
    protected void run(BungeeSender sender, String[] args) {
        sendHelpMessage(sender);
    }
}
