package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.container.SerializableUUID;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A record stored for an arena
 *
 * @param userId <p>The id of the player that achieved the record</p>
 * @param record <p>The record achieved</p>
 * @param <K>    <p>The comparable type of the record</p>
 */
public record ArenaRecord<K extends Comparable<K>>(UUID userId, K record) implements Comparable<ArenaRecord<K>>,
        ConfigurationSerializable {

    @Override
    public boolean equals(Object other) {
        return other instanceof ArenaRecord<?> && userId.equals(((ArenaRecord<?>) other).userId);
    }

    @Override
    public int compareTo(@NotNull ArenaRecord<K> other) {
        return record.compareTo(other.record);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", new SerializableUUID(userId()));
        data.put("record", record);
        return data;
    }

    /**
     * Deserializes the saved arena record
     *
     * @param data <p>The data to deserialize</p>
     * @param <K>  <p>The type of the deserialized record</p>
     * @return <p>The deserialized data</p>
     */
    @SuppressWarnings({"unused", "unchecked"})
    public static <K extends Comparable<K>> ArenaRecord<K> deserialize(@NotNull Map<String, Object> data) {
        return new ArenaRecord<>(((SerializableUUID) data.get("userId")).uuid(), (K) data.get("record"));
    }

}
