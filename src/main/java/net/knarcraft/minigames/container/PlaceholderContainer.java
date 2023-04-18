package net.knarcraft.minigames.container;

import java.util.HashMap;
import java.util.Map;

/**
 * A container for keeping track of several placeholder to value mappings
 */
public class PlaceholderContainer {

    private final Map<String, String> placeholders;

    /**
     * Instantiates a new placeholder container
     */
    public PlaceholderContainer() {
        this.placeholders = new HashMap<>();
    }

    /**
     * Gets all placeholders
     *
     * @return <p>All placeholders</p>
     */
    public Map<String, String> getPlaceholders() {
        return new HashMap<>(this.placeholders);
    }

    /**
     * Adds a new placeholder
     *
     * @param placeholder <p>The placeholder to register</p>
     * @param value       <p>The value of the placeholder</p>
     * @return <p>This object</p>
     */
    public PlaceholderContainer add(String placeholder, String value) {
        this.placeholders.put(placeholder, value);
        return this;
    }

}
