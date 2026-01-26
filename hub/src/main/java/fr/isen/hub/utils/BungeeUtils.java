package fr.isen.hub.utils;

import fr.isen.hub.HubPlugin;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeUtils {

    private final HubPlugin plugin;

    public BungeeUtils(HubPlugin plugin) {
        this.plugin = plugin;

        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    }

    public void connect(Player player, String targetServer) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteArray);

        try {
            out.writeUTF("Connect");
            out.writeUTF(targetServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendPluginMessage(plugin, "BungeeCord", byteArray.toByteArray());
    }
}
