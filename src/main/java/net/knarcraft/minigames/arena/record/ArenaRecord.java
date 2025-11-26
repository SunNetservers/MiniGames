package net.knarcraft.minigames.arena.record;

import net.knarcraft.minigames.container.SerializableUUID;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * A record stored for an arena
 */
public abstract class ArenaRecord<RecordType extends Comparable<RecordType>> implements Comparable<ArenaRecord<RecordType>>, ConfigurationSerializable {

    private final UUID userId;
    private final RecordType record;

    /**
     * @param userId <p>The id of the player that achieved the record</p>
     * @param record <p>The record achieved</p>
     */
    public ArenaRecord(@NotNull UUID userId, @NotNull RecordType record) {
        this.userId = userId;
        this.record = record;
    }

    /**
     * Gets the id of the user this record belongs to
     *
     * @return <p>The record's achiever</p>
     */
    @NotNull
    public UUID getUserId() {
        return userId;
    }

    /**
     * Gets the value of the stored record
     *
     * @return <p>The record value</p>
     */
    @NotNull
    public RecordType getRecord() {
        return record;
    }

    /**
     * Gets this as a string that should be printed on a sign
     *
     * @return <p>This as a string</p>
     */
    public abstract String getAsString();

    @Override
    public boolean equals(@Nullable Object other) {
        return other instanceof ArenaRecord<?> && userId.equals(((ArenaRecord<?>) other).userId);
    }

    @Override
    public int compareTo(@NotNull ArenaRecord<RecordType> other) {
        return record.compareTo(other.record);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", new SerializableUUID(getUserId()));
        data.put("record", record);
        return data;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, record);
    }

    @Override
    public String toString() {
        return userId + ":" + record;
    }

}
