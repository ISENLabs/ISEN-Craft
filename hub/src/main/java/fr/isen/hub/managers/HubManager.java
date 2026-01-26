package fr.isen.hub.managers;

import fr.isen.common.command.IsenSender;
import fr.isen.hub.HubPlugin;
import fr.isen.hub.command.HubCommand;
import fr.isen.hub.command.SpawnCommand;
import fr.isen.hub.listeners.VoidListener;
import fr.isen.paper.command.PaperSender;
import fr.isen.paper.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HubManager extends IManager {

    private File file;
    private FileConfiguration config;

    public HubManager(HubPlugin plugin) {
        super(plugin, "HubManager");

        loadConfig();

        plugin.registerCommand("ihub", new HubCommand(this));
        plugin.registerCommand("spawn", new SpawnCommand(this));

        plugin.registerListener(new VoidListener(this));
    }

    public void sendHelp(PaperSender sender) {
        String[] help = {"&c---------------",
                "&4/&cihub help",
                "&4/&cihub setspawn",
                "&4/&cspawn",
                "&c---------------"
        };

        MessageUtils.sendMessage(sender, List.of(help));
    };

    public void setSpawn(Location location) {
        config.set("spawn", location);
        saveConfig();
        loadConfig();
    }

    public void teleportToSpawn(Player player) {
        Location location = (Location) config.get("spawn");
        MessageUtils.sendMessage(player, "&2Téléportation vers le spawn...");
        if(location != null) player.teleport(location);
    }

    private void loadConfig() {
        file = new File(plugin.getDataFolder(), "locations.yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.logger.log("&cImpossible de créer " + file.getPath());
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.logger.log("&cImpossible de sauvegarder " + file.getPath());
            e.printStackTrace();
        }
    }
}
