package fr.isen.common.config;

import java.io.File;
import java.io.IOException;

public abstract class YamlConfigWrapper {

    protected final File dataFolder;
    protected final String fileName;

    public YamlConfigWrapper(File dataFolder, String fileName) {
        this.dataFolder = dataFolder;
        this.fileName = fileName;
    }

    public abstract void load() throws IOException;

    public abstract void saveDefaults();

    public abstract String getString(String path, String def);

    public abstract int getInt(String path, int def);

    public abstract boolean getBoolean(String path, boolean def);

    public void reload() throws IOException {
        load();
    }
}
