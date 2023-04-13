package net.knarcraft.minigames.container;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A UUID container able to be serialized
 */
public class SerializableUUID extends SerializableContainer<UUID> {

    /**
     * Instantiates a new serializable uuid
     *
     * @param value <p>The uuid to contain</p>
     */
    public SerializableUUID(UUID value) {
        super(value);
    }

    @Override
    public SerializableContainer<UUID> getSerializable(UUID value) {
        return new SerializableUUID(value);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", getRawValue().toString());
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

}
