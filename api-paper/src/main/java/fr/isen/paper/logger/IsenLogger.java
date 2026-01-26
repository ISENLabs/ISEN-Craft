package fr.isen.paper.logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class IsenLogger {

    private final String pluginName;
    private final Boolean defaultPolicy;
    private final Map<String, Boolean> prefixPolicies;
    private final Map<String, Integer> prefixColors;
    private final List<String> colors;

    public IsenLogger(JavaPlugin plugin) {
        this.pluginName = plugin.getName();

        this.defaultPolicy = true;
        this.prefixPolicies = new HashMap<>();
        this.prefixColors = new HashMap<>();
        this.colors = Arrays.asList(
                "§4", "§c", "§6", "§e", "§2", "§a", "§b", "§3", "§9", "§d", "§5", "§f"
        );
    }

    private Integer getRandomColor() {
        return (int) (Math.random() * colors.size());
    }

    private Boolean checkPolicy(String prefix) {
        return prefixPolicies.getOrDefault(prefix, defaultPolicy);
    }

    public void log(String message, String prefix) {
        String key = prefix.toLowerCase();
        if (checkPolicy(key)) {
            if(!prefixColors.containsKey(key)) {
                prefixColors.put(key, getRandomColor());
            }

            Bukkit.getConsoleSender().sendMessage(
                    "[" + pluginName + "][" + prefix.toUpperCase() + "] " +
                            colors.get(prefixColors.get(key)) +
                            message
            );
        }
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage("[" + pluginName + "] " + message);
    }

    public void disable(String prefix) {
        prefixPolicies.remove(prefix);
        prefixPolicies.put(prefix, false);
    }

    public void enable(String prefix) {
        prefixPolicies.remove(prefix);
        prefixPolicies.put(prefix, true);
    }
}