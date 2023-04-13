package net.knarcraft.dropper.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * The configuration keeping track of parkour settings
 */
public class ParkourConfiguration extends MiniGameConfiguration {

    private final static String rootNode = "parkour.";

    private boolean mustDoGroupedInSequence;
    private boolean ignoreRecordsUntilGroupBeatenOnce;
    private boolean makePlayersInvisible;

    /**
     * Instantiates a new dropper configuration
     *
     * @param configuration <p>The YAML configuration to use internally</p>
     */
    public ParkourConfiguration(FileConfiguration configuration) {
        super(configuration);
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
     * Gets whether players should be made invisible while in an arena
     *
     * @return <p>Whether players should be made invisible</p>
     */
    public boolean makePlayersInvisible() {
        return this.makePlayersInvisible;
    }

    @Override
    public void load() {
        this.mustDoGroupedInSequence = configuration.getBoolean(rootNode + "mustDoGroupedInSequence", true);
        this.ignoreRecordsUntilGroupBeatenOnce = configuration.getBoolean(rootNode + "ignoreRecordsUntilGroupBeatenOnce", false);
        this.makePlayersInvisible = configuration.getBoolean(rootNode + "makePlayersInvisible", false);
    }

    @Override
    public String toString() {
        return "Current configuration:" +
                "\n" + "Must do groups in sequence: " + mustDoGroupedInSequence +
                "\n" + "Ignore records until group beaten once: " + ignoreRecordsUntilGroupBeatenOnce +
                "\n" + "Make players invisible: " + makePlayersInvisible;
    }

}
