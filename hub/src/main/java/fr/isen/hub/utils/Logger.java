package fr.isen.hub.utils;

import fr.isen.hub.HubPlugin;
import org.bukkit.Bukkit;

import java.util.*;

public class Logger {

    private HubPlugin plugin;
    private final String name;

    private final Boolean default_policy;

    private final Map<String, Boolean> prefixPolicies;
    private final Map<String, Integer> prefixColors;
    private final List<String> colors;

    public Logger(HubPlugin plugin) {
        this.plugin = plugin;
        this.name = plugin.getName();

        this.default_policy = true;
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
        return prefixPolicies.getOrDefault(prefix, default_policy);
    }

    public void log(String message, String prefix) {
        String key = prefix.toLowerCase();
        if (checkPolicy(key)) {
            if(!prefixColors.containsKey(key)) {
                prefixColors.put(key, getRandomColor());
            }

            Bukkit.getConsoleSender().sendMessage(
                    "[" + name + "][" +
                            prefix.toUpperCase() +
                            "] " +
                            colors.get(prefixColors.get(key)) +
                            message
            );
        }
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage("[" + name + "] " + message);
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