package net.knarcraft.dropper.config;

import net.knarcraft.dropper.Dropper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DropperConfiguration {

    private final FileConfiguration configuration;

    private double verticalVelocity;
    private float horizontalVelocity;
    private int randomlyInvertedTimer;
    private boolean mustDoGroupedInSequence;
    private boolean ignoreRecordsUntilGroupBeatenOnce;
    private boolean mustDoNormalModeFirst;
    private boolean makePlayersInvisible;
    private boolean disableHitCollision;
    private boolean overrideVerticalVelocity;
    private double liquidHitBoxDepth;
    private double solidHitBoxDistance;
    private Set<Material> blockWhitelist;

    /**
     * Instantiates a new dropper configuration
     *
     * @param configuration <p>The YAML configuration to use internally</p>
     */
    public DropperConfiguration(FileConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Gets whether vertical velocity should be overridden
     *
     * @return <p>Whether vertical velocity should be overridden</p>
     */
    public boolean overrideVerticalVelocity() {
        return this.overrideVerticalVelocity;
    }

    /**
     * Gets the default vertical velocity
     *
     * @return <p>The default vertical velocity</p>
     */
    public double getVerticalVelocity() {
        return this.verticalVelocity;
    }

    /**
     * Gets the default horizontal velocity
     *
     * @return <p>The default horizontal velocity</p>
     */
    public float getHorizontalVelocity() {
        return this.horizontalVelocity;
    }

    /**
     * Gets the number of seconds before the randomly inverted game-mode toggles
     *
     * @return <p>Number of seconds before the inversion toggles</p>
     */
    public int getRandomlyInvertedTimer() {
        return this.randomlyInvertedTimer;
    }

    /**
     * Gets whether grouped arenas must be done in the set sequence
     *
     * @return <p>Whether grouped arenas must be done in sequence</p>
     */
    public boolean mustDoGroupedInSequence() {
        return this.mustDoGroupedInSequence;
    }

    /**
     * Gets whether the normal/default mode must be beaten before playing another game-mode
     *
     * @return <p>Whether the normal game-mode must be beaten first</p>
     */
    public boolean mustDoNormalModeFirst() {
        return this.mustDoNormalModeFirst;
    }

    /**
     * Gets the types of block which should not trigger a loss
     *
     * @return <p>The materials that should not trigger a loss</p>
     */
    public Set<Material> getBlockWhitelist() {
        return new HashSet<>(this.blockWhitelist);
    }

    /**
     * Gets whether records should be discarded, unless the player has already beaten all arenas in the group
     *
     * @return <p>Whether to ignore records on the first play-through</p>
     */
    public boolean ignoreRecordsUntilGroupBeatenOnce() {
        return this.ignoreRecordsUntilGroupBeatenOnce;
    }

    /**
     * Gets whether players should be made invisible while in an arena
     *
     * @return <p>Whether players should be made invisible</p>
     */
    public boolean makePlayersInvisible() {
        return this.makePlayersInvisible;
    }

    /**
     * Gets whether entity hit-collision of players in an arena should be disabled
     *
     * @return <p>Whether to disable hit collision</p>
     */
    public boolean disableHitCollision() {
        return this.disableHitCollision;
    }

    /**
     * Gets the negative depth a player must reach in a liquid block for fail/win detection to trigger
     *
     * <p>This decides how far inside a non-solid block the player must go before detection triggers. The closer to -1
     * it is, the more accurate it will seem to the player, but the likelihood of not detecting the hit increases.</p>
     *
     * @return <p>The liquid hit box depth to use</p>
     */
    public double getLiquidHitBoxDepth() {
        return this.liquidHitBoxDepth;
    }

    /**
     * Gets the positive distance a player must at most be from a block for fail/win detection to trigger
     *
     * <p>This decides the distance the player must be from a block below them before a hit triggers. If too low, the
     * likelihood of detecting the hit decreases, but it won't look like the player hit the block without being near.</p>
     *
     * @return <p>The solid hit box distance to use</p>
     */
    public double getSolidHitBoxDistance() {
        return this.solidHitBoxDistance;
    }

    /**
     * Loads all configuration values from disk
     */
    public void load() {
        this.verticalVelocity = configuration.getDouble("verticalVelocity", 1.0);
        this.horizontalVelocity = (float) configuration.getDouble("horizontalVelocity", 1.0);
        this.randomlyInvertedTimer = configuration.getInt("randomlyInvertedTimer", 7);
        this.mustDoGroupedInSequence = configuration.getBoolean("mustDoGroupedInSequence", true);
        this.ignoreRecordsUntilGroupBeatenOnce = configuration.getBoolean("ignoreRecordsUntilGroupBeatenOnce", false);
        this.mustDoNormalModeFirst = configuration.getBoolean("mustDoNormalModeFirst", true);
        this.makePlayersInvisible = configuration.getBoolean("makePlayersInvisible", false);
        this.disableHitCollision = configuration.getBoolean("disableHitCollision", true);
        this.overrideVerticalVelocity = configuration.getBoolean("overrideVerticalVelocity", true);
        this.liquidHitBoxDepth = configuration.getDouble("liquidHitBoxDepth", -0.8);
        this.solidHitBoxDistance = configuration.getDouble("solidHitBoxDistance", 0.2);
        sanitizeValues();

        loadBlockWhitelist();
    }

    /**
     * Sanitizes configuration values to ensure they are within expected bounds
     */
    private void sanitizeValues() {
        if (this.liquidHitBoxDepth <= -1 || this.liquidHitBoxDepth > 0) {
            this.liquidHitBoxDepth = -0.8;
        }

        if (this.solidHitBoxDistance <= 0 || this.solidHitBoxDistance > 1) {
            this.solidHitBoxDistance = 0.2;
        }

        if (this.horizontalVelocity > 1 || this.horizontalVelocity <= 0) {
            this.horizontalVelocity = 1;
        }

        if (this.verticalVelocity <= 0 || this.verticalVelocity > 75) {
            this.verticalVelocity = 1;
        }

        if (this.randomlyInvertedTimer <= 0 || this.randomlyInvertedTimer > 3600) {
            this.randomlyInvertedTimer = 7;
        }
    }

    /**
     * Loads the materials specified in the block whitelist
     */
    private void loadBlockWhitelist() {
        this.blockWhitelist = new HashSet<>();
        List<?> blockWhitelist = configuration.getList("blockWhiteList", new ArrayList<>());
        for (Object value : blockWhitelist) {
            if (!(value instanceof String string)) {
                continue;
            }

            // Try to parse a material tag first
            if (parseMaterialTag(string)) {
                continue;
            }

            // Try to parse a material name
            Material matched = Material.matchMaterial(string);
            if (matched != null) {
                this.blockWhitelist.add(matched);
            } else {
                Dropper.log(Level.WARNING, "Unable to parse: " + string);
            }
        }
    }

    /**
     * Tries to parse the material tag in the specified material name
     *
     * @param materialName <p>The material name that might be a material tag</p>
     * @return <p>True if a tag was found</p>
     */
    private boolean parseMaterialTag(String materialName) {
        Pattern pattern = Pattern.compile("^\\+([a-zA-Z_]+)");
        Matcher matcher = pattern.matcher(materialName);
        if (matcher.find()) {
            // The material is a material tag
            Tag<Material> tag = Bukkit.getTag(Tag.REGISTRY_BLOCKS, NamespacedKey.minecraft(
                    matcher.group(1).toLowerCase()), Material.class);
            if (tag != null) {
                this.blockWhitelist.addAll(tag.getValues());
            } else {
                Dropper.log(Level.WARNING, "Unable to parse: " + materialName);
            }
            return true;
        }
        return false;
    }

}
