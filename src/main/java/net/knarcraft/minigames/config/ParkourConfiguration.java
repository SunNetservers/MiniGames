package net.knarcraft.minigames.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;

/**
 * The configuration keeping track of parkour settings
 */
public class ParkourConfiguration extends MiniGameConfiguration {

    private final static String rootNode = "parkour.";

    private boolean enforceCheckpointOrder;
    private boolean mustDoGroupedInSequence;
    private boolean ignoreRecordsUntilGroupBeatenOnce;
    private Set<Material> killPlaneBlocks;

    /**
     * Instantiates a new dropper configuration
     *
     * @param configuration <p>The YAML configuration to use internally</p>
     */
    public ParkourConfiguration(FileConfiguration configuration) {
        super(configuration);
    }

    /**
     * Gets whether all checkpoints must be triggered in the order they are set when configuring the parkour arena
     *
     * @return <p>Whether checkpoints must be triggered in order</p>
     */
    public boolean enforceCheckpointOrder() {
        return this.enforceCheckpointOrder;
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
     * Gets whether records should be discarded, unless the player has already beaten all arenas in the group
     *
     * @return <p>Whether to ignore records on the first play-through</p>
     */
    public boolean ignoreRecordsUntilGroupBeatenOnce() {
        return this.ignoreRecordsUntilGroupBeatenOnce;
    }

    /**
     * Gets all types of blocks constituting parkour arenas' kill planes
     *
     * @return <p>The types of blocks causing a player to fail a parkour map</p>
     */
    public Set<Material> getKillPlaneBlocks() {
        return new HashSet<>(this.killPlaneBlocks);
    }

    @Override
    protected void load() {
        this.enforceCheckpointOrder = configuration.getBoolean(rootNode + "enforceCheckpointOrder", false);
        this.mustDoGroupedInSequence = configuration.getBoolean(rootNode + "mustDoGroupedInSequence", true);
        this.ignoreRecordsUntilGroupBeatenOnce = configuration.getBoolean(rootNode + "ignoreRecordsUntilGroupBeatenOnce", false);
        this.killPlaneBlocks = loadMaterialList(rootNode + "killPlaneBlocks");
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(
                "Current configuration:" +
                        "Current configuration:" +
                        "\n" + "Must do groups in sequence: " + mustDoGroupedInSequence +
                        "\n" + "Ignore records until group beaten once: " + ignoreRecordsUntilGroupBeatenOnce +
                        "\n" + "Kill plane blocks: ");
        for (Material material : killPlaneBlocks) {
            builder.append("\n  - ").append(material.name());
        }
        return builder.toString();
    }

}
