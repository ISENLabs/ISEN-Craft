package fr.isen.paper.logger;

import fr.isen.common.logger.IsenLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperLogger extends IsenLogger {

    public PaperLogger(JavaPlugin plugin) {
        super(plugin.getName());
    }

    @Override
    protected void print(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }
}