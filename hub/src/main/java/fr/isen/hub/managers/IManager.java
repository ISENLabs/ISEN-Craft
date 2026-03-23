package fr.isen.hub.managers;

import fr.isen.hub.HubPlugin;
import fr.isen.paper.command.PaperSender;

public abstract class IManager {

    public HubPlugin plugin;
    private final String name;

    public IManager(HubPlugin plugin, String name) {
        this.name = name;
        this.plugin = plugin;

        plugin.logger.log(name + " loaded", "INFO");
    }

    public String getName() {
        return name;
    }
}
