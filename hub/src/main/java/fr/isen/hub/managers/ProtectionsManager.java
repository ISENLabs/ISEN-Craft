package fr.isen.hub.managers;

import fr.isen.common.config.IManager;
import fr.isen.hub.HubPlugin;
import fr.isen.hub.listeners.ProtectionsListener;
import org.bukkit.entity.Player;

public class ProtectionsManager extends IManager<HubPlugin> {


    public ProtectionsManager(HubPlugin plugin) {
        super(plugin, plugin.logger, "ProtectionsManager");

        plugin.registerListener(new ProtectionsListener(this));
    }

    public boolean isBypass(Player player) {
        return player.hasPermission("hub.bypass");
    }
}
