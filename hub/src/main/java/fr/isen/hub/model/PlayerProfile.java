package fr.isen.hub.model;

import java.util.UUID;

public class PlayerProfile {

    private final UUID uuid;
    private final String name;
    private final long firstJoinEpoch;
    private long lastSeenEpoch;
    private int totalJoins;

    public PlayerProfile(UUID uuid, String name, long firstJoinEpoch) {
        this.uuid = uuid;
        this.name = name;
        this.firstJoinEpoch = firstJoinEpoch;
        this.lastSeenEpoch = firstJoinEpoch;
        this.totalJoins = 1;
    }

    private PlayerProfile(UUID uuid, String name, long firstJoinEpoch, long lastSeenEpoch, int totalJoins) {
        this.uuid = uuid;
        this.name = name;
        this.firstJoinEpoch = firstJoinEpoch;
        this.lastSeenEpoch = lastSeenEpoch;
        this.totalJoins = totalJoins;
    }

    public static PlayerProfile fromStorage(UUID uuid, String name,
                                            long firstJoinEpoch, long lastSeenEpoch, int totalJoins) {
        return new PlayerProfile(uuid, name, firstJoinEpoch, lastSeenEpoch, totalJoins);
    }

    public UUID getUuid() { return uuid; }
    public String getName() { return name; }
    public long getFirstJoinEpoch() { return firstJoinEpoch; }
    public long getLastSeenEpoch() { return lastSeenEpoch; }
    public int getTotalJoins() { return totalJoins; }

    public void recordJoin(long epochMs) {
        this.lastSeenEpoch = epochMs;
        this.totalJoins++;
    }
}
