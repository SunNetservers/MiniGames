package net.knarcraft.minigames.util;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.ArenaRecordsRegistry;
import net.knarcraft.minigames.arena.dropper.DropperArena;
import net.knarcraft.minigames.arena.dropper.DropperArenaData;
import net.knarcraft.minigames.arena.dropper.DropperArenaGameMode;
import net.knarcraft.minigames.arena.dropper.DropperArenaGroup;
import net.knarcraft.minigames.arena.dropper.DropperArenaRecordsRegistry;
import net.knarcraft.minigames.arena.dropper.DropperArenaStorageKey;
import net.knarcraft.minigames.arena.reward.Reward;
import net.knarcraft.minigames.arena.reward.RewardCondition;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.container.SerializableMaterial;
import net.knarcraft.minigames.container.SerializableUUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import static net.knarcraft.minigames.util.ArenaStorageHelper.getArenaDataFile;

/**
 * A helper class for saving and loading arenas
 */
public final class DropperArenaStorageHelper {

    private final static File dataFolder = MiniGames.getInstance().getDataFolder();
    private final static String dropperArenasConfigurationSection = "dropperArenas";
    private final static String dropperGroupsConfigurationSection = "dropperGroups";
    private static final File dropperArenaFile = new File(dataFolder, "dropper_arenas.yml");
    private static final File dropperGroupFile = new File(dataFolder, "dropper_groups.yml");
    private static final File dropperArenaDataFolder = new File(dataFolder, "dropper_arena_data");

    private DropperArenaStorageHelper() {

    }

    /**
     * Saves the given dropper arena groups
     *
     * @param arenaGroups <p>The arena groups to save</p>
     * @throws IOException <p>If unable to write to the file</p>
     */
    public static void saveDropperArenaGroups(@NotNull Set<DropperArenaGroup> arenaGroups) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        ConfigurationSection groupSection = configuration.createSection(dropperGroupsConfigurationSection);

        for (DropperArenaGroup arenaGroup : arenaGroups) {
            groupSection.set(arenaGroup.getGroupId().toString(), arenaGroup);
        }

        configuration.save(dropperGroupFile);
    }

    /**
     * Loads all existing dropper arena groups
     *
     * @return <p>The loaded arena groups</p>
     */
    public static @NotNull Set<DropperArenaGroup> loadDropperArenaGroups() {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(dropperGroupFile);
        ConfigurationSection groupSection = configuration.getConfigurationSection(dropperGroupsConfigurationSection);
        //If no such section exists, it must be the case that there is no data to load
        if (groupSection == null) {
            return new HashSet<>();
        }

        Set<DropperArenaGroup> arenaGroups = new HashSet<>();

        for (String sectionName : groupSection.getKeys(false)) {
            arenaGroups.add((DropperArenaGroup) groupSection.get(sectionName));
        }

        return arenaGroups;
    }

    /**
     * Saves the given arenas
     *
     * @param arenas <p>The arenas to save</p>
     * @throws IOException <p>If unable to write to the file</p>
     */
    public static void saveDropperArenas(@NotNull Map<UUID, DropperArena> arenas) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        ConfigurationSection arenaSection = configuration.createSection(dropperArenasConfigurationSection);
        for (DropperArena arena : arenas.values()) {
            saveDropperArena(arenaSection, arena);
        }
        configuration.save(dropperArenaFile);
    }

    /**
     * Saves a single arena
     *
     * @param arena <p>The arena to save</p>
     * @throws IOException <p>If unable to write to the file</p>
     */
    public static void saveSingleDropperArena(DropperArena arena) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        ConfigurationSection arenaSection = configuration.createSection(dropperArenasConfigurationSection);
        saveDropperArena(arenaSection, arena);
        configuration.save(dropperArenaFile);
    }

    /**
     * Updates the given configuration section with the arena's data, and stores arena data for the arena
     *
     * @param arenaSection <p>The configuration section to update</p>
     * @param arena        <p>The arena to save</p>
     * @throws IOException <p>If unable to save the arena data</p>
     */
    private static void saveDropperArena(ConfigurationSection arenaSection, DropperArena arena) throws IOException {
        //Note: While the arena name is used as the key, as the key has to be sanitized, the un-sanitized arena name
        // must be stored as well
        @NotNull ConfigurationSection configSection = arenaSection.createSection(arena.getArenaId().toString());
        configSection.set(DropperArenaStorageKey.ID.getKey(), new SerializableUUID(arena.getArenaId()));
        configSection.set(DropperArenaStorageKey.NAME.getKey(), arena.getArenaName());
        configSection.set(DropperArenaStorageKey.SPAWN_LOCATION.getKey(), arena.getSpawnLocation());
        configSection.set(DropperArenaStorageKey.EXIT_LOCATION.getKey(), arena.getExitLocation());
        configSection.set(DropperArenaStorageKey.PLAYER_VERTICAL_VELOCITY.getKey(), arena.getPlayerVerticalVelocity());
        configSection.set(DropperArenaStorageKey.PLAYER_HORIZONTAL_VELOCITY.getKey(), arena.getPlayerHorizontalVelocity());
        configSection.set(DropperArenaStorageKey.WIN_BLOCK_TYPE.getKey(), new SerializableMaterial(arena.getWinBlockType()));
        RewardStorageHelper.saveRewards(arena, configSection, DropperArenaStorageKey.REWARDS.getKey());
        saveDropperArenaData(arena.getData());
    }

    /**
     * Loads all arenas
     *
     * @return <p>The loaded arenas, or null if the arenas configuration section is missing.</p>
     */
    public static @NotNull Map<UUID, DropperArena> loadDropperArenas() {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(dropperArenaFile);
        ConfigurationSection arenaSection = configuration.getConfigurationSection(dropperArenasConfigurationSection);
        //If no such section exists, it must be the case that there is no data to load
        if (arenaSection == null) {
            return new HashMap<>();
        }

        Map<UUID, DropperArena> loadedArenas = new HashMap<>();

        for (String sectionName : arenaSection.getKeys(false)) {
            ConfigurationSection configurationSection = arenaSection.getConfigurationSection(sectionName);
            //I'm not sure whether this could actually happen
            if (configurationSection == null) {
                continue;
            }

            DropperArena arena = loadDropperArena(configurationSection);
            if (arena != null) {
                loadedArenas.put(arena.getArenaId(), arena);
            }
        }

        return loadedArenas;
    }

    /**
     * Loads an arena from the given configuration section
     *
     * @param configurationSection <p>The configuration section containing arena data</p>
     * @return <p>The loaded arena, or null if invalid</p>
     */
    private static @Nullable DropperArena loadDropperArena(@NotNull ConfigurationSection configurationSection) {
        UUID arenaId = ((SerializableUUID) configurationSection.get(DropperArenaStorageKey.ID.getKey(),
                new SerializableUUID(UUID.randomUUID()))).getRawValue();
        String arenaName = configurationSection.getString(DropperArenaStorageKey.NAME.getKey());
        Location spawnLocation = (Location) configurationSection.get(DropperArenaStorageKey.SPAWN_LOCATION.getKey());
        Location exitLocation = (Location) configurationSection.get(DropperArenaStorageKey.EXIT_LOCATION.getKey());
        double verticalVelocity = configurationSection.getDouble(DropperArenaStorageKey.PLAYER_VERTICAL_VELOCITY.getKey());
        float horizontalVelocity = sanitizeHorizontalVelocity((float) configurationSection.getDouble(
                DropperArenaStorageKey.PLAYER_HORIZONTAL_VELOCITY.getKey()));
        SerializableMaterial winBlockType = (SerializableMaterial) configurationSection.get(
                DropperArenaStorageKey.WIN_BLOCK_TYPE.getKey());

        if (arenaName == null || spawnLocation == null) {
            MiniGames.log(Level.SEVERE, MiniGames.getInstance().getStringFormatter().replacePlaceholders(
                    MiniGameMessage.ERROR_ARENA_NOT_LOADED, new String[]{"{section}", "{file}"},
                    new String[]{configurationSection.getName(), "dropper_arenas"}));
            return null;
        }
        if (winBlockType == null) {
            winBlockType = new SerializableMaterial(Material.WATER);
        }

        Map<RewardCondition, Set<Reward>> rewards = RewardStorageHelper.loadRewards(configurationSection,
                DropperArenaStorageKey.REWARDS.getKey());

        // Generate new, empty arena data if not available
        DropperArenaData arenaData = loadDropperArenaData(arenaId);
        if (arenaData == null) {
            MiniGames.log(Level.SEVERE, MiniGames.getInstance().getStringFormatter().replacePlaceholder(
                    MiniGameMessage.ERROR_ARENA_DATA_NOT_LOADED, "{arena}", arenaId.toString()));
            arenaData = getEmptyDropperData(arenaId);
        }

        return new DropperArena(arenaId, arenaName, spawnLocation, exitLocation, verticalVelocity, horizontalVelocity,
                winBlockType.getRawValue(), rewards, arenaData, MiniGames.getInstance().getDropperArenaHandler());
    }

    /**
     * Gets empty dropper data
     *
     * @param arenaId <p>The id to get parkour data for</p>
     * @return <p>Empty parkour data</p>
     */
    private static @NotNull DropperArenaData getEmptyDropperData(@NotNull UUID arenaId) {
        Map<ArenaGameMode, ArenaRecordsRegistry> recordRegistries = new HashMap<>();
        Map<ArenaGameMode, Set<UUID>> playersCompleted = new HashMap<>();
        for (ArenaGameMode arenaGameMode : DropperArenaGameMode.values()) {
            recordRegistries.put(arenaGameMode, new DropperArenaRecordsRegistry(arenaId));
            playersCompleted.put(arenaGameMode, new HashSet<>());
        }
        return new DropperArenaData(arenaId, recordRegistries, playersCompleted);
    }

    /**
     * Stores the given arena data to a file
     *
     * @param arenaData <p>The arena data to store</p>
     */
    public static void saveDropperArenaData(@NotNull DropperArenaData arenaData) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set(DropperArenaStorageKey.DATA.getKey(), arenaData);

        configuration.save(getDropperArenaDataFile(arenaData.getArenaId()));
    }

    /**
     * Loads arena data for the given arena id
     *
     * @param arenaId <p>The id of the arena to get data for</p>
     * @return <p>The loaded arena data</p>
     */
    private static @Nullable DropperArenaData loadDropperArenaData(@NotNull UUID arenaId) {
        File arenaDataFile = getDropperArenaDataFile(arenaId);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(arenaDataFile);
        return (DropperArenaData) configuration.get(DropperArenaStorageKey.DATA.getKey());
    }

    /**
     * Removes data for the arena with the given id
     *
     * @param arenaId <p>The id of the arena to remove data for</p>
     * @return <p>True if the data was successfully removed</p>
     */
    public static boolean removeDropperArenaData(@NotNull UUID arenaId) {
        return getDropperArenaDataFile(arenaId).delete();
    }

    /**
     * Gets the file used to store the given arena id's data
     *
     * @param arenaId <p>The id of the arena to get a data file for</p>
     * @return <p>The file the arena's data is/should be stored in</p>
     */
    private static @NotNull File getDropperArenaDataFile(@NotNull UUID arenaId) {
        return getArenaDataFile(dropperArenaDataFolder, arenaId);
    }

    /**
     * Sanitizes the given horizontal velocity to make sure it doesn't leave its bounds
     *
     * @param horizontalVelocity <p>The horizontal velocity to sanitize</p>
     * @return <p>The sanitized horizontal velocity</p>
     */
    private static float sanitizeHorizontalVelocity(float horizontalVelocity) {
        if (horizontalVelocity < -1) {
            return -1;
        } else if (horizontalVelocity > 1) {
            return 1;
        } else {
            return horizontalVelocity;
        }
    }

}
