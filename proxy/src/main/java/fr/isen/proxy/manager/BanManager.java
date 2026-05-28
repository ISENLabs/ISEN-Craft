package fr.isen.proxy.manager;

import com.google.gson.*;
import fr.isen.common.config.IManager;
import fr.isen.proxy.ProxyPlugin;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.*;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class BanManager extends IManager<ProxyPlugin> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final File bansFile;
    private final ConcurrentHashMap<String, JsonObject> bans = new ConcurrentHashMap<>();

    public BanManager(ProxyPlugin plugin) {
        super(plugin, plugin.logger, "BanManager");
        this.bansFile = new File(plugin.getDataFolder(), "bans.json");
        plugin.getDataFolder().mkdirs();
        load();
    }

    private void load() {
        if (!bansFile.exists()) return;
        try (FileReader reader = new FileReader(bansFile)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null || !root.has("bans")) return;
            for (JsonElement el : root.getAsJsonArray("bans")) {
                JsonObject ban = el.getAsJsonObject();
                bans.put(ban.get("name").getAsString().toLowerCase(), ban);
            }
            logger.log("BanManager: " + bans.size() + " ban(s) chargé(s)", "INFO");
        } catch (IOException e) {
            logger.log("BanManager: erreur lecture bans.json", "ERROR");
        }
    }

    private void save() {
        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {
            synchronized (BanManager.this) {
                JsonObject root = new JsonObject();
                JsonArray arr = new JsonArray();
                bans.values().forEach(arr::add);
                root.add("bans", arr);
                try (FileWriter writer = new FileWriter(bansFile, false)) {
                    GSON.toJson(root, writer);
                } catch (IOException e) {
                    logger.log("BanManager: ERREUR sauvegarde bans.json", "ERROR");
                }
            }
        });
    }

    public boolean isBanned(String name) {
        return getActiveBanReason(name).isPresent();
    }

    public Optional<String> getActiveBanReason(String name) {
        JsonObject ban = bans.get(name.toLowerCase());
        return ban != null ? Optional.of(ban.get("reason").getAsString()) : Optional.empty();
    }

    public void ban(String name, String reason) {
        ProxiedPlayer online = ProxyServer.getInstance().getPlayer(name);
        String uuid = online != null ? online.getUniqueId().toString() : "";
        JsonObject ban = new JsonObject();
        ban.addProperty("name", name);
        ban.addProperty("uuid", uuid);
        ban.addProperty("reason", reason);
        ban.addProperty("bannedAt", Instant.now().getEpochSecond());
        bans.put(name.toLowerCase(), ban);
        save();
    }

    public boolean unban(String name) {
        boolean removed = bans.remove(name.toLowerCase()) != null;
        if (removed) save();
        return removed;
    }
}
