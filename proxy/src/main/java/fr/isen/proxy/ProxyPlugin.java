package fr.isen.proxy;

import fr.isen.bungee.command.BungeeCommandBridge;
import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.logger.BungeeLogger;
import fr.isen.common.command.IsenCommand;
import fr.isen.proxy.manager.HubManager;
import net.md_5.bungee.api.plugin.Plugin;

public class ProxyPlugin extends Plugin {

    private HubManager hubManager;

    private BungeeLogger logger;

    @Override
    public void onEnable() {
        logger = new BungeeLogger(this);

        this.hubManager = new HubManager(this);

        logger.log("Plugin enabled", "INFO");
    }

    public void registerCommand(String name, IsenCommand<BungeeSender> command) {
        getProxy().getPluginManager().registerCommand(this, new BungeeCommandBridge(name, command));
    }
}
