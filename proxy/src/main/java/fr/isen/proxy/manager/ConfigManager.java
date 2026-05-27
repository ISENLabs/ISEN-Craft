package fr.isen.proxy.manager;

import fr.isen.bungee.config.BungeeConfigWrapper;
import fr.isen.common.config.IManager;
import fr.isen.proxy.ProxyPlugin;
import fr.isen.proxy.command.reload.IsenCraftReloadCommand;

import java.io.IOException;

public class ConfigManager extends IManager<ProxyPlugin> {

    private final BungeeConfigWrapper config;

    public ConfigManager(ProxyPlugin plugin) {
        super(plugin, plugin.logger, "ConfigManager");
        this.config = new BungeeConfigWrapper(plugin, "config.yml");
        try {
            config.load();
        } catch (IOException e) {
            plugin.logger.logException("Impossible de charger la configuration", e, "ERROR");
        }
        plugin.registerCommand("isencraft", new IsenCraftReloadCommand(this));
    }

    public void reload() throws IOException {
        config.reload();
        logger.log("Configuration rechargée", "INFO");
    }

    public String getString(String path, String def) {
        return config.getString(path, def);
    }

    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }

    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }
}
