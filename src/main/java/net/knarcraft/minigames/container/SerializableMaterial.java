package net.knarcraft.minigames.container;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A material container able to be serialized
 */
public class SerializableMaterial extends SerializableContainer<Material> {

    /**
     * Instantiates a new serializable material
     *
     * @param material <p>The material to contain</p>
     */
    public SerializableMaterial(Material material) {
        super(material);
    }

    @Override
    public SerializableContainer<Material> getSerializable(Material value) {
        return new SerializableMaterial(value);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", getRawValue().name());
        return data;
    }

    /**
     * Deserializes a serialized material
     *
     * @param data <p>The serialized data</p>
     * @return <p>The deserialized material</p>
     */
    @SuppressWarnings("unused")
    public static SerializableMaterial deserialize(Map<String, Object> data) {
        Material material = Material.matchMaterial((String) data.get("name"));
        if (material == null) {
            return null;
        } else {
            return new SerializableMaterial(material);
        }
    }

}
