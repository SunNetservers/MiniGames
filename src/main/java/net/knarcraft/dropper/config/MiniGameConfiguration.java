package net.knarcraft.dropper.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * A configuration for a mini-game
 */
public abstract class MiniGameConfiguration {

    protected FileConfiguration configuration;

    /**
     * Instantiates a new mini-game configuration
     *
     * @param configuration <p>The YAML configuration to use internally</p>
     */
    public MiniGameConfiguration(@NotNull FileConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Loads all configuration values from disk
     *
     * @param configuration <p>The configuration to load</p>
     */
    public void load(FileConfiguration configuration) {
        this.configuration = configuration;
        this.load();
    }

    /**
     * Loads all configuration values from disk, using the current file configuration
     */
    public abstract void load();

}
