package net.knarcraft.minigames.config;

import net.knarcraft.knarlib.util.MaterialHelper;
import net.knarcraft.minigames.MiniGames;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        this.load();
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
    protected abstract void load();

    /**
     * Loads the materials specified in the block whitelist
     */
    public @NotNull Set<Material> loadMaterialList(@NotNull String path) {
        List<?> blockWhitelist = configuration.getList(path, new ArrayList<>());
        return MaterialHelper.loadMaterialList(blockWhitelist, "+", MiniGames.getInstance().getLogger());
    }

}
