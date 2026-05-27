package fr.isen.proxy;

import fr.isen.bungee.command.BungeeCommandBridge;
import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.logger.BungeeLogger;
import fr.isen.common.command.IsenCommand;
import fr.isen.common.logger.IsenLogger;
import fr.isen.proxy.manager.ConfigManager;
import fr.isen.proxy.manager.HubManager;
import fr.isen.proxy.manager.NetworkManager;
import fr.isen.proxy.listeners.OpSyncProxyListener;
import net.md_5.bungee.api.plugin.Plugin;

public class ProxyPlugin extends Plugin {

    private ConfigManager configManager;
    private HubManager hubManager;
    private NetworkManager networkManager;

    public IsenLogger logger;

    @Override
    public void onEnable() {
        logger = new BungeeLogger(this);

        this.configManager = new ConfigManager(this);
        this.hubManager = new HubManager(this, configManager);
        this.networkManager = new NetworkManager(this, configManager);

        getProxy().getPluginManager().registerListener(this, new OpSyncProxyListener());

        logger.log("Plugin enabled", "INFO");
    }

    @Override
    public void onDisable() {
        getProxy().unregisterChannel("fr.isen:network");
        if (logger != null) {
            logger.log("Plugin disabled", "INFO");
        }
    }

    public void registerCommand(String name, IsenCommand<BungeeSender> command) {
        getProxy().getPluginManager().registerCommand(this, new BungeeCommandBridge(name, command));
    }
}
