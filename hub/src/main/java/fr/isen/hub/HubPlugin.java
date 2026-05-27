package fr.isen.hub;

import fr.isen.common.command.IsenCommand;
import fr.isen.common.logger.IsenLogger;
import fr.isen.hub.managers.ConfigManager;
import fr.isen.hub.managers.HubManager;
import fr.isen.hub.managers.LogManager;
import fr.isen.hub.managers.NavigationManager;
import fr.isen.hub.managers.PlayerProfileManager;
import fr.isen.hub.command.profil.ProfilCommand;
import fr.isen.hub.managers.NetworkManager;
import fr.isen.hub.listeners.OpSyncListener;
import fr.isen.hub.managers.ProtectionsManager;
import fr.isen.hub.managers.ScoreboardManager;
import fr.isen.hub.managers.TitleManager;
import fr.isen.paper.command.PaperCommandBridge;
import fr.isen.paper.logger.PaperLogger;
import fr.isen.paper.utils.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class HubPlugin extends JavaPlugin {

    public ConfigManager configManager;
    public TitleManager titleManager;
    public LogManager logManager;
    private ProtectionsManager protectionsManager;
    private HubManager hubManager;
    private NavigationManager navigationManager;
    private NetworkManager networkManager;
    public ScoreboardManager scoreboardManager;
    public PlayerProfileManager playerProfileManager;
    private OpSyncListener opSyncListener;

    public IsenLogger logger;
    private BungeeUtils bungeeUtils;

    @Override
    public void onDisable() {
        if (opSyncListener != null) opSyncListener.shutdown();
        getServer().getMessenger().unregisterIncomingPluginChannel(this, "fr.isen:network");
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, "fr.isen:network");
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, "fr.isen:opsync");
        if (logger != null) {
            logger.log("Plugin disabled", "INFO");
        }
    }

    @Override
    public void onEnable() {
        this.logger = new PaperLogger(this);
        this.bungeeUtils = new BungeeUtils(this);

        this.configManager = new ConfigManager(this);
        this.titleManager = new TitleManager(this);
        this.logManager = new LogManager(this);
        this.playerProfileManager = new PlayerProfileManager(this);
        registerCommand("profil", new ProfilCommand(this));
        this.protectionsManager = new ProtectionsManager(this);
        this.hubManager = new HubManager(this);
        this.navigationManager = new NavigationManager(this, bungeeUtils);
        this.scoreboardManager = new ScoreboardManager(this);
        this.networkManager = new NetworkManager(this, scoreboardManager);
        this.scoreboardManager.setNetworkManager(this.networkManager);

        this.opSyncListener = new OpSyncListener(this);
        registerListener(this.opSyncListener);

        logger.log("Plugin enabled", "INFO");
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void registerCommand(String name, IsenCommand command) {
        if (getCommand(name) == null) {
            logger.log("&cCommande non déclarée dans plugin.yml : " + name, "ERROR");
            return;
        }
        getCommand(name).setExecutor(new PaperCommandBridge(command));
    }
}
