package net.knarcraft.dropper.container;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A material container able to be serialized
 *
 * @param material <p>The material stored by this record</p>
 */
public record SerializableMaterial(Material material) implements ConfigurationSerializable {

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", material.name());
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
        Material material = Material.matchMaterial((String) data.getOrDefault("name", "AIR"));
        return new SerializableMaterial(material);
    }

}
