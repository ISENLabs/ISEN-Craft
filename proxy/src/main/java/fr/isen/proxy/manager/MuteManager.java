package fr.isen.proxy.manager;

import com.google.gson.*;
import fr.isen.common.config.IManager;
import fr.isen.proxy.ProxyPlugin;
import net.md_5.bungee.api.ProxyServer;

import java.io.*;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MuteManager extends IManager<ProxyPlugin> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final File mutesFile;
    private final ConcurrentHashMap<String, JsonObject> mutes = new ConcurrentHashMap<>();

    public MuteManager(ProxyPlugin plugin) {
        super(plugin, plugin.logger, "MuteManager");
        this.mutesFile = new File(plugin.getDataFolder(), "mutes.json");
        plugin.getDataFolder().mkdirs();
        load();
    }

    private void load() {
        if (!mutesFile.exists()) return;
        try (FileReader reader = new FileReader(mutesFile)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null || !root.has("mutes")) return;
            for (JsonElement el : root.getAsJsonArray("mutes")) {
                JsonObject mute = el.getAsJsonObject();
                mutes.put(mute.get("name").getAsString().toLowerCase(), mute);
            }
            logger.log("MuteManager: " + mutes.size() + " mute(s) chargé(s)", "INFO");
        } catch (IOException e) {
            logger.log("MuteManager: erreur lecture mutes.json", "ERROR");
        }
    }

    private void save() {
        ProxyServer.getInstance().getScheduler().runAsync(plugin, () -> {
            synchronized (MuteManager.this) {
                JsonObject root = new JsonObject();
                JsonArray arr = new JsonArray();
                mutes.values().forEach(arr::add);
                root.add("mutes", arr);
                try (FileWriter writer = new FileWriter(mutesFile, false)) {
                    GSON.toJson(root, writer);
                } catch (IOException e) {
                    logger.log("MuteManager: ERREUR sauvegarde mutes.json", "ERROR");
                }
            }
        });
    }

    public boolean isMuted(String name) {
        return getActiveMuteReason(name).isPresent();
    }

    public Optional<String> getActiveMuteReason(String name) {
        JsonObject mute = mutes.get(name.toLowerCase());
        if (mute == null) return Optional.empty();
        long expiresAt = mute.get("expiresAt").getAsLong();
        if (expiresAt != -1 && Instant.now().getEpochSecond() >= expiresAt) {
            mutes.remove(name.toLowerCase());
            return Optional.empty();
        }
        return Optional.of(mute.get("reason").getAsString());
    }

    public void mute(String name, String reason, long durationSeconds) {
        long expiresAt = durationSeconds <= 0 ? -1 : Instant.now().getEpochSecond() + durationSeconds;
        JsonObject mute = new JsonObject();
        mute.addProperty("name", name);
        mute.addProperty("reason", reason);
        mute.addProperty("mutedAt", Instant.now().getEpochSecond());
        mute.addProperty("expiresAt", expiresAt);
        mutes.put(name.toLowerCase(), mute);
        save();
    }

    public boolean unmute(String name) {
        boolean removed = mutes.remove(name.toLowerCase()) != null;
        if (removed) save();
        return removed;
    }

    public static long parseDuration(String s) {
        if (s == null || s.length() < 2) return -1;
        try {
            char unit = Character.toLowerCase(s.charAt(s.length() - 1));
            long value = Long.parseLong(s.substring(0, s.length() - 1));
            return switch (unit) {
                case 'm' -> value * 60;
                case 'h' -> value * 3600;
                case 'd' -> value * 86400;
                default -> -1;
            };
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static boolean isDurationArg(String s) {
        if (s == null || s.length() < 2) return false;
        char unit = Character.toLowerCase(s.charAt(s.length() - 1));
        if (unit != 'm' && unit != 'h' && unit != 'd') return false;
        try {
            Long.parseLong(s.substring(0, s.length() - 1));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
