package net.knarcraft.minigames.config;

import net.knarcraft.minigames.MiniGames;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    protected @NotNull Set<Material> loadMaterialList(@NotNull String path) {
        Set<Material> parsedMaterials = new HashSet<>();
        List<?> blockWhitelist = configuration.getList(path, new ArrayList<>());
        for (Object value : blockWhitelist) {
            if (!(value instanceof String string)) {
                continue;
            }

            // Try to parse a material tag first
            if (parseMaterialTag(parsedMaterials, string)) {
                continue;
            }

            // Try to parse a material name
            Material matched = Material.matchMaterial(string);
            if (matched != null) {
                parsedMaterials.add(matched);
            } else {
                MiniGames.log(Level.WARNING, "Unable to parse: " + string);
            }
        }
        return parsedMaterials;
    }

    /**
     * Tries to parse the material tag in the specified material name
     *
     * @param targetSet    <p>The set all parsed materials should be added to</p>
     * @param materialName <p>The material name that might be a material tag</p>
     * @return <p>True if a tag was found</p>
     */
    protected boolean parseMaterialTag(@NotNull Set<Material> targetSet, @NotNull String materialName) {
        Pattern pattern = Pattern.compile("^\\+([a-zA-Z_]+)");
        Matcher matcher = pattern.matcher(materialName);
        if (matcher.find()) {
            // The material is a material tag
            Tag<Material> tag = Bukkit.getTag(Tag.REGISTRY_BLOCKS, NamespacedKey.minecraft(
                    matcher.group(1).toLowerCase()), Material.class);
            if (tag != null) {
                targetSet.addAll(tag.getValues());
            } else {
                MiniGames.log(Level.WARNING, "Unable to parse: " + materialName);
            }
            return true;
        }
        return false;
    }

}
