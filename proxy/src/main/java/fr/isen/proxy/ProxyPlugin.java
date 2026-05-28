package fr.isen.proxy;

import fr.isen.bungee.command.BungeeCommandBridge;
import fr.isen.bungee.command.BungeeSender;
import fr.isen.bungee.logger.BungeeLogger;
import fr.isen.common.command.IsenCommand;
import fr.isen.common.logger.IsenLogger;
import fr.isen.proxy.command.ban.BanCommand;
import fr.isen.proxy.command.ban.args.BanArg;
import fr.isen.proxy.command.ban.args.UnbanArg;
import fr.isen.proxy.command.find.FindCommand;
import fr.isen.proxy.command.kick.KickCommand;
import fr.isen.proxy.command.mute.MuteCommand;
import fr.isen.proxy.command.mute.args.MuteArg;
import fr.isen.proxy.command.mute.args.UnmuteArg;
import fr.isen.proxy.listeners.JoinLeaveListener;
import fr.isen.proxy.listeners.ModerationListener;
import fr.isen.proxy.manager.BanManager;
import fr.isen.proxy.manager.ConfigManager;
import fr.isen.proxy.manager.HubManager;
import fr.isen.proxy.manager.MuteManager;
import fr.isen.proxy.manager.NetworkManager;
import fr.isen.proxy.listeners.OpSyncProxyListener;
import net.md_5.bungee.api.plugin.Plugin;

public class ProxyPlugin extends Plugin {

    private ConfigManager configManager;
    private HubManager hubManager;
    private NetworkManager networkManager;
    private BanManager banManager;
    private MuteManager muteManager;

    public IsenLogger logger;

    @Override
    public void onEnable() {
        logger = new BungeeLogger(this);

        this.configManager = new ConfigManager(this);
        this.hubManager = new HubManager(this, configManager);
        this.networkManager = new NetworkManager(this, configManager);
        this.banManager = new BanManager(this);
        this.muteManager = new MuteManager(this);

        registerCommand("ban", new BanCommand(new BanArg(banManager)));
        logger.log("Registered command: ban", "INFO");
        registerCommand("unban", new BanCommand(new UnbanArg(banManager)));
        logger.log("Registered command: unban", "INFO");
        registerCommand("mute", new MuteCommand(new MuteArg(muteManager)));
        logger.log("Registered command: mute", "INFO");
        registerCommand("unmute", new MuteCommand(new UnmuteArg(muteManager)));
        logger.log("Registered command: unmute", "INFO");
        registerCommand("find", new FindCommand());
        logger.log("Registered command: find", "INFO");
        registerCommand("kick", new KickCommand());
        logger.log("Registered command: kick", "INFO");

        getProxy().registerChannel("fr.isen:opsync");
        getProxy().getPluginManager().registerListener(this, new OpSyncProxyListener(this));
        getProxy().getPluginManager().registerListener(this, new ModerationListener(banManager, muteManager));
        getProxy().getPluginManager().registerListener(this, new JoinLeaveListener(configManager));

        logger.log("Plugin enabled", "INFO");
    }

    @Override
    public void onDisable() {
        getProxy().unregisterChannel("fr.isen:network");
        getProxy().unregisterChannel("fr.isen:opsync");
        if (logger != null) {
            logger.log("Plugin disabled", "INFO");
        }
    }

    public void registerCommand(String name, IsenCommand<BungeeSender> command) {
        getProxy().getPluginManager().registerCommand(this, new BungeeCommandBridge(name, command));
    }
}
