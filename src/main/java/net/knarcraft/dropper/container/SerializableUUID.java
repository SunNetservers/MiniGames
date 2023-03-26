package net.knarcraft.dropper.container;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A UUID container able to be serialized
 *
 * @param uuid <p>The UUID stored by this record</p>
 */
public record SerializableUUID(UUID uuid) implements ConfigurationSerializable {

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", uuid.toString());
        return data;
    }

    /**
     * Deserializes a serialized UUID
     *
     * @param data <p>The serialized data</p>
     * @return <p>The deserialized UUID</p>
     */
    @SuppressWarnings("unused")
    public static SerializableUUID deserialize(Map<String, Object> data) {
        String id = (String) data.getOrDefault("id", null);
        if (id != null) {
            return new SerializableUUID(UUID.fromString(id));
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof SerializableUUID) {
            return this.uuid.equals(((SerializableUUID) object).uuid);
        } else {
            return false;
        }
    }

}
