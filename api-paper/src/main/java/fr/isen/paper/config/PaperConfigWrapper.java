package fr.isen.paper.config;

import fr.isen.common.config.YamlConfigWrapper;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class PaperConfigWrapper extends YamlConfigWrapper {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    public PaperConfigWrapper(JavaPlugin plugin, String fileName) {
        super(plugin.getDataFolder(), fileName);
        this.plugin = plugin;
    }

    @Override
    public void saveDefaults() {
        File file = new File(dataFolder, fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    @Override
    public void load() throws IOException {
        saveDefaults();
        config = YamlConfiguration.loadConfiguration(new File(dataFolder, fileName));
        InputStream resource = plugin.getResource(fileName);
        if (resource != null) {
            YamlConfiguration bundled = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(resource, StandardCharsets.UTF_8));
            config.setDefaults(bundled);
        }
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

    public java.util.List<java.util.Map<?, ?>> getMapList(String path) {
        if (config == null) return java.util.List.of();
        return config.getMapList(path);
    }

    public java.util.List<String> getStringList(String path) {
        if (config == null) return java.util.List.of();
        return config.getStringList(path);
    }
}
