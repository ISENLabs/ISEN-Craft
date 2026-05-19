package fr.isen.bungee.config;

import fr.isen.common.config.YamlConfigWrapper;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BungeeConfigWrapper extends YamlConfigWrapper {

    private final Plugin plugin;
    private Configuration config;

    public BungeeConfigWrapper(Plugin plugin, String fileName) {
        super(plugin.getDataFolder(), fileName);
        this.plugin = plugin;
    }

    @Override
    public void saveDefaults() {
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            plugin.getLogger().severe("[BungeeConfigWrapper] Cannot create data folder: " + dataFolder.getAbsolutePath());
            return;
        }
        File file = new File(dataFolder, fileName);
        if (!file.exists()) {
            try (InputStream in = plugin.getResourceAsStream(fileName)) {
                if (in == null) {
                    plugin.getLogger().warning("[BungeeConfigWrapper] Default resource not found in JAR: " + fileName);
                    return;
                }
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                plugin.getLogger().severe("[BungeeConfigWrapper] Could not save default " + fileName + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void load() throws IOException {
        saveDefaults();
        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(dataFolder, fileName));
    }

    @Override
    public String getString(String path, String def) {
        if (config == null) return def;
        return config.getString(path, def);
    }

    @Override
    public int getInt(String path, int def) {
        if (config == null) return def;
        return config.getInt(path, def);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        if (config == null) return def;
        return config.getBoolean(path, def);
    }
}
