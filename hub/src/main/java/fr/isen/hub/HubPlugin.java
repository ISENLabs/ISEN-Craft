package fr.isen.hub;

import fr.isen.hub.managers.HubManager;
import fr.isen.hub.managers.NavigationManager;
import fr.isen.hub.managers.ProtectionsManager;
import fr.isen.paper.command.IsenCommand;
import fr.isen.paper.logger.IsenLogger;
import fr.isen.paper.utils.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class HubPlugin extends JavaPlugin {

    private ProtectionsManager protectionsManager;
    private HubManager hubManager;
    private NavigationManager navigationManager;

    public IsenLogger logger;
    private BungeeUtils bungeeUtils;

    @Override
    public void onDisable() {
        logger.log("Plugin disabled", "INFO");
    }

    @Override
    public void onEnable() {
        this.logger = new IsenLogger(this);
        this.bungeeUtils = new BungeeUtils(this);

        this.protectionsManager = new ProtectionsManager(this);
        this.hubManager = new HubManager(this);
        this.navigationManager = new NavigationManager(this, bungeeUtils);

        logger.log("Plugin enabled", "INFO");
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void registerCommand(String name, IsenCommand command) {
        getCommand(name).setExecutor(command);
    }
}
