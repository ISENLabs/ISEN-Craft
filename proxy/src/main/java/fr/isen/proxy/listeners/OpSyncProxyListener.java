package fr.isen.proxy.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.isen.proxy.ProxyPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OpSyncProxyListener implements Listener {

    private static final String CHANNEL = "fr.isen:opsync";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final ProxyPlugin plugin;
    private final File adminsFile;
    private final Set<String> admins = ConcurrentHashMap.newKeySet();

    public OpSyncProxyListener(ProxyPlugin plugin) {
        this.plugin = plugin;
        this.adminsFile = new File(plugin.getDataFolder(), "admins.json");
        plugin.getDataFolder().mkdirs();
        loadAdmins();
    }

    private void loadAdmins() {
        if (!adminsFile.exists()) return;
        try (FileReader reader = new FileReader(adminsFile)) {
            JsonObject obj = GSON.fromJson(reader, JsonObject.class);
            if (obj != null && obj.has("admins")) {
                for (JsonElement el : obj.getAsJsonArray("admins")) {
                    admins.add(el.getAsString());
                }
            }
            plugin.logger.log("OpSync: " + admins.size() + " admin(s) chargé(s)", "INFO");
        } catch (IOException e) {
            plugin.logger.log("OpSync: erreur lecture admins.json", "ERROR");
        }
    }

    private void saveAdmins() {
        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {
            JsonObject obj = new JsonObject();
            JsonArray arr = new JsonArray();
            admins.forEach(arr::add);
            obj.add("admins", arr);
            try (FileWriter writer = new FileWriter(adminsFile, false)) {
                GSON.toJson(obj, writer);
            } catch (IOException e) {
                plugin.logger.log("OpSync: ERREUR sauvegarde admins.json — données en mémoire OK mais fichier invalide, relancer pour persister", "ERROR");
            }
        });
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        if (admins.contains(event.getPlayer().getName())) {
            event.getPlayer().setPermission("proxy.admin", true);
        }
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals(CHANNEL)) return;

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
            String playerName = in.readUTF();
            boolean setAdmin = in.readBoolean();

            if (setAdmin) {
                admins.add(playerName);
            } else {
                admins.remove(playerName);
            }
            saveAdmins();

            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
            if (player != null) {
                player.setPermission("proxy.admin", setAdmin);
            }
            plugin.logger.log("Permission 'proxy.admin' " +
                    (setAdmin ? "accordée à " : "retirée pour ") + playerName, "INFO");
        } catch (IOException e) {
            plugin.logger.log("OpSync: erreur désérialisation", "ERROR");
        }
    }
}
