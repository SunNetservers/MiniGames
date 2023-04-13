package net.knarcraft.dropper.config;

import net.knarcraft.dropper.MiniGames;
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

/**
 * The configuration keeping track of dropper settings
 */
public class DropperConfiguration extends MiniGameConfiguration {

    private final static String rootNode = "dropper.";

    private double verticalVelocity;
    private float horizontalVelocity;
    private int randomlyInvertedTimer;
    private boolean mustDoGroupedInSequence;
    private boolean ignoreRecordsUntilGroupBeatenOnce;
    private boolean mustDoNormalModeFirst;
    private boolean makePlayersInvisible;
    private boolean disableHitCollision;
    private boolean blockSneaking;
    private boolean blockSprinting;
    private Set<Material> blockWhitelist;

    /**
     * Instantiates a new dropper configuration
     *
     * @param configuration <p>The YAML configuration to use internally</p>
     */
    public DropperConfiguration(FileConfiguration configuration) {
        super(configuration);
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
     * Gets whether players trying to sneak while in a dropper arena to increase their downwards speed should be blocked
     *
     * @return <p>Whether to block sneak to speed up</p>
     */
    public boolean blockSneaking() {
        return blockSneaking;
    }

    /**
     * Gets whether players trying to sprint to improve their horizontal speed while in a dropper arena should be blocked
     *
     * @return <p>Whether to block sprint to speed up</p>
     */
    public boolean blockSprinting() {
        return this.blockSprinting;
    }

    @Override
    public void load() {
        this.verticalVelocity = configuration.getDouble(rootNode + "verticalVelocity", 1.0);
        this.horizontalVelocity = (float) configuration.getDouble(rootNode + "horizontalVelocity", 1.0);
        this.randomlyInvertedTimer = configuration.getInt(rootNode + "randomlyInvertedTimer", 7);
        this.mustDoGroupedInSequence = configuration.getBoolean(rootNode + "mustDoGroupedInSequence", true);
        this.ignoreRecordsUntilGroupBeatenOnce = configuration.getBoolean(rootNode + "ignoreRecordsUntilGroupBeatenOnce", false);
        this.mustDoNormalModeFirst = configuration.getBoolean(rootNode + "mustDoNormalModeFirst", true);
        this.makePlayersInvisible = configuration.getBoolean(rootNode + "makePlayersInvisible", false);
        this.disableHitCollision = configuration.getBoolean(rootNode + "disableHitCollision", true);
        this.blockSprinting = configuration.getBoolean(rootNode + "blockSprinting", true);
        this.blockSneaking = configuration.getBoolean(rootNode + "blockSneaking", true);
        sanitizeValues();

        loadBlockWhitelist();
    }

    /**
     * Sanitizes configuration values to ensure they are within expected bounds
     */
    private void sanitizeValues() {
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
        List<?> blockWhitelist = configuration.getList(rootNode + "blockWhitelist", new ArrayList<>());
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
                MiniGames.log(Level.WARNING, "Unable to parse: " + string);
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
                MiniGames.log(Level.WARNING, "Unable to parse: " + materialName);
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(
                "Current configuration:" +
                        "\n" + "Vertical velocity: " + verticalVelocity +
                        "\n" + "Horizontal velocity: " + horizontalVelocity +
                        "\n" + "Randomly inverted timer: " + randomlyInvertedTimer +
                        "\n" + "Must do groups in sequence: " + mustDoGroupedInSequence +
                        "\n" + "Ignore records until group beaten once: " + ignoreRecordsUntilGroupBeatenOnce +
                        "\n" + "Must do normal mode first: " + mustDoNormalModeFirst +
                        "\n" + "Make players invisible: " + makePlayersInvisible +
                        "\n" + "Disable hit collision: " + disableHitCollision +
                        "\n" + "Block whitelist: ");
        for (Material material : blockWhitelist) {
            builder.append("\n  - ").append(material.name());
        }
        return builder.toString();
    }

}
