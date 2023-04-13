package net.knarcraft.minigames.container;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * A container that is serializable
 *
 * @param <K> <p>The type of the contained object</p>
 */
public abstract class SerializableContainer<K> implements ConfigurationSerializable {

    private final K value;

    /**
     * Instantiates a new serializable container
     *
     * @param value <p>The value to contain</p>
     */
    public SerializableContainer(K value) {
        this.value = value;
    }

    /**
     * Gets the raw, non-serializable object
     *
     * @return <p>The raw stored value</p>
     */
    public K getRawValue() {
        return value;
    }

    /**
     * Gets a serializable container containing the given value
     *
     * @param value <p>The value to make serializable</p>
     * @return <p>The serializable value</p>
     */
    public abstract SerializableContainer<K> getSerializable(K value);

    @Override
    public boolean equals(Object object) {
        if (object instanceof SerializableContainer<?>) {
            return this.getRawValue().equals(((SerializableContainer<?>) object).getRawValue());
        } else {
            return false;
        }
    }

}
