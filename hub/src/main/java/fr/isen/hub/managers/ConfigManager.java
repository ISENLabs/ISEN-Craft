package fr.isen.hub.managers;

import fr.isen.common.config.IManager;
import fr.isen.hub.HubPlugin;
import fr.isen.hub.command.reload.IsenCraftReloadCommand;
import fr.isen.paper.config.PaperConfigWrapper;

import java.io.IOException;

public class ConfigManager extends IManager<HubPlugin> {

    private final PaperConfigWrapper config;

    public ConfigManager(HubPlugin plugin) {
        super(plugin, plugin.logger, "ConfigManager");
        this.config = new PaperConfigWrapper(plugin, "config.yml");
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

    public java.util.List<java.util.Map<?, ?>> getMapList(String path) {
        return config.getMapList(path);
    }

    public java.util.List<String> getStringList(String path) {
        return config.getStringList(path);
    }
}
