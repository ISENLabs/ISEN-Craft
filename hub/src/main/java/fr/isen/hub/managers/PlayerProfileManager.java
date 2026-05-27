package fr.isen.hub.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.isen.common.config.IManager;
import fr.isen.hub.HubPlugin;
import fr.isen.hub.model.PlayerProfile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class PlayerProfileManager extends IManager<HubPlugin> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final File profilesDir;

    public PlayerProfileManager(HubPlugin plugin) {
        super(plugin, plugin.logger, "PlayerProfileManager");
        this.profilesDir = new File(plugin.getDataFolder(), "profiles");
        profilesDir.mkdirs();
    }

    public PlayerProfile loadOrCreate(UUID uuid, String name) {
        File file = profileFile(uuid);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                JsonObject obj = GSON.fromJson(reader, JsonObject.class);
                return PlayerProfile.fromStorage(
                        uuid,
                        obj.get("name").getAsString(),
                        obj.get("firstJoinEpoch").getAsLong(),
                        obj.get("lastSeenEpoch").getAsLong(),
                        obj.get("totalJoins").getAsInt()
                );
            } catch (IOException e) {
                plugin.logger.logException("Erreur lecture profil " + uuid, e, "ERROR");
            }
        }
        return new PlayerProfile(uuid, name, System.currentTimeMillis());
    }

    public void recordJoinAndSave(PlayerProfile profile) {
        profile.recordJoin(System.currentTimeMillis());
        save(profile);
    }

    public PlayerProfile getProfile(UUID uuid) {
        File file = profileFile(uuid);
        if (!file.exists()) return null;
        try (FileReader reader = new FileReader(file)) {
            JsonObject obj = GSON.fromJson(reader, JsonObject.class);
            return PlayerProfile.fromStorage(
                    uuid,
                    obj.get("name").getAsString(),
                    obj.get("firstJoinEpoch").getAsLong(),
                    obj.get("lastSeenEpoch").getAsLong(),
                    obj.get("totalJoins").getAsInt()
            );
        } catch (IOException e) {
            plugin.logger.logException("Erreur lecture profil " + uuid, e, "ERROR");
            return null;
        }
    }

    public void save(PlayerProfile profile) {
        JsonObject obj = new JsonObject();
        obj.addProperty("uuid", profile.getUuid().toString());
        obj.addProperty("name", profile.getName());
        obj.addProperty("firstJoinEpoch", profile.getFirstJoinEpoch());
        obj.addProperty("lastSeenEpoch", profile.getLastSeenEpoch());
        obj.addProperty("totalJoins", profile.getTotalJoins());

        File file = profileFile(profile.getUuid());
        try (FileWriter writer = new FileWriter(file, false)) {
            GSON.toJson(obj, writer);
        } catch (IOException e) {
            plugin.logger.logException("Erreur sauvegarde profil " + profile.getUuid(), e, "ERROR");
        }
    }

    private File profileFile(UUID uuid) {
        return new File(profilesDir, uuid.toString() + ".json");
    }
}
