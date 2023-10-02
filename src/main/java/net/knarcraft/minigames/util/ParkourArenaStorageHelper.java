package net.knarcraft.minigames.util;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaGameMode;
import net.knarcraft.minigames.arena.ArenaRecordsRegistry;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaData;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGameMode;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGroup;
import net.knarcraft.minigames.arena.parkour.ParkourArenaRecordsRegistry;
import net.knarcraft.minigames.arena.parkour.ParkourArenaStorageKey;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import static net.knarcraft.minigames.util.ArenaStorageHelper.getArenaDataFile;

/**
 * A helper class for saving and loading parkour arenas
 */
public final class ParkourArenaStorageHelper {

    private ParkourArenaStorageHelper() {

    }

    private final static File dataFolder = MiniGames.getInstance().getDataFolder();
    private final static String parkourArenasConfigurationSection = "parkourArenas";
    private final static String parkourGroupsConfigurationSection = "parkourGroups";
    private static final File parkourGroupFile = new File(dataFolder, "parkour_groups.yml");
    private static final File parkourArenaFile = new File(dataFolder, "parkour_arenas.yml");
    private static final File parkourArenaDataFolder = new File(dataFolder, "parkour_arena_data");

    /**
     * Saves the given parkour arena groups
     *
     * @param arenaGroups <p>The arena groups to save</p>
     * @throws IOException <p>If unable to write to the file</p>
     */
    public static void saveParkourArenaGroups(@NotNull Set<ParkourArenaGroup> arenaGroups) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        ConfigurationSection groupSection = configuration.createSection(parkourGroupsConfigurationSection);

        for (ParkourArenaGroup arenaGroup : arenaGroups) {
            groupSection.set(arenaGroup.getGroupId().toString(), arenaGroup);
        }

        configuration.save(parkourGroupFile);
    }

    /**
     * Loads all existing parkour arena groups
     *
     * @return <p>The loaded arena groups</p>
     */
    public static @NotNull Set<ParkourArenaGroup> loadParkourArenaGroups() {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(parkourGroupFile);
        ConfigurationSection groupSection = configuration.getConfigurationSection(parkourGroupsConfigurationSection);
        //If no such section exists, it must be the case that there is no data to load
        if (groupSection == null) {
            return new HashSet<>();
        }

        Set<ParkourArenaGroup> arenaGroups = new HashSet<>();

        for (String sectionName : groupSection.getKeys(false)) {
            arenaGroups.add((ParkourArenaGroup) groupSection.get(sectionName));
        }

        return arenaGroups;
    }

    /**
     * Saves the given arenas
     *
     * @param arenas <p>The arenas to save</p>
     * @throws IOException <p>If unable to write to the file</p>
     */
    public static void saveParkourArenas(@NotNull Map<UUID, ParkourArena> arenas) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        ConfigurationSection arenaSection = configuration.createSection(parkourArenasConfigurationSection);
        for (ParkourArena arena : arenas.values()) {
            saveParkourArena(arenaSection, arena);
        }
        configuration.save(parkourArenaFile);
    }

    /**
     * Saves a single arena
     *
     * @param arena <p>The arena to save</p>
     * @throws IOException <p>If unable to write to the file</p>
     */
    public static void saveSingleParkourArena(ParkourArena arena) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        ConfigurationSection arenaSection = configuration.createSection(parkourArenasConfigurationSection);
        saveParkourArena(arenaSection, arena);
        configuration.save(parkourArenaFile);
    }

    /**
     * Updates the given configuration section with the arena's data, and stores arena data for the arena
     *
     * @param arenaSection <p>The configuration section to update</p>
     * @param arena        <p>The arena to save</p>
     * @throws IOException <p>If unable to save the arena data</p>
     */
    private static void saveParkourArena(ConfigurationSection arenaSection, ParkourArena arena) throws IOException {
        //Note: While the arena name is used as the key, as the key has to be sanitized, the un-sanitized arena name
        // must be stored as well
        @NotNull ConfigurationSection configSection = arenaSection.createSection(arena.getArenaId().toString());
        configSection.set(ParkourArenaStorageKey.ID.getKey(), new SerializableUUID(arena.getArenaId()));
        configSection.set(ParkourArenaStorageKey.NAME.getKey(), arena.getArenaName());
        configSection.set(ParkourArenaStorageKey.SPAWN_LOCATION.getKey(), arena.getSpawnLocation());
        configSection.set(ParkourArenaStorageKey.EXIT_LOCATION.getKey(), arena.getExitLocation());
        configSection.set(ParkourArenaStorageKey.WIN_BLOCK_TYPE.getKey(), new SerializableMaterial(arena.getWinBlockType()));
        configSection.set(ParkourArenaStorageKey.WIN_LOCATION.getKey(), arena.getWinLocation());
        configSection.set(ParkourArenaStorageKey.KILL_PLANE_BLOCKS.getKey(), getKillPlaneBlocks(arena));
        configSection.set(ParkourArenaStorageKey.OBSTACLE_BLOCKS.getKey(), getObstacleBlocks(arena));
        configSection.set(ParkourArenaStorageKey.CHECKPOINTS.getKey(), arena.getCheckpoints());
        RewardStorageHelper.saveRewards(arena, configSection, ParkourArenaStorageKey.REWARDS.getKey());
        saveParkourArenaData(arena.getData());
    }

    /**
     * Gets a list of the kill plane blocks for the given arena
     *
     * @param arena <p>The arena to get kill plane blocks for</p>
     * @return <p>The kill plane blocks</p>
     */
    private static List<String> getKillPlaneBlocks(ParkourArena arena) {
        if (arena.getKillPlaneBlockNames() == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(arena.getKillPlaneBlockNames());
        }
    }

    /**
     * Gets a list of the obstacle blocks for the given arena
     *
     * @param arena <p>The arena to get obstacle blocks for</p>
     * @return <p>The obstacle blocks</p>
     */
    private static List<String> getObstacleBlocks(ParkourArena arena) {
        if (arena.getObstacleBlockNames() == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(arena.getObstacleBlockNames());
        }
    }

    /**
     * Loads all arenas
     *
     * @return <p>The loaded arenas, or null if the arenas configuration section is missing.</p>
     */
    public static @NotNull Map<UUID, ParkourArena> loadParkourArenas() {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(parkourArenaFile);
        ConfigurationSection arenaSection = configuration.getConfigurationSection(parkourArenasConfigurationSection);
        //If no such section exists, it must be the case that there is no data to load
        if (arenaSection == null) {
            return new HashMap<>();
        }

        Map<UUID, ParkourArena> loadedArenas = new HashMap<>();

        for (String sectionName : arenaSection.getKeys(false)) {
            ConfigurationSection configurationSection = arenaSection.getConfigurationSection(sectionName);
            //I'm not sure whether this could actually happen
            if (configurationSection == null) {
                continue;
            }

            ParkourArena arena = loadParkourArena(configurationSection);
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
    @SuppressWarnings("unchecked")
    private static @Nullable ParkourArena loadParkourArena(@NotNull ConfigurationSection configurationSection) {
        UUID arenaId = ((SerializableUUID) configurationSection.get(ParkourArenaStorageKey.ID.getKey(),
                new SerializableUUID(UUID.randomUUID()))).getRawValue();
        String arenaName = configurationSection.getString(ParkourArenaStorageKey.NAME.getKey());
        Location spawnLocation = (Location) configurationSection.get(ParkourArenaStorageKey.SPAWN_LOCATION.getKey());
        Location exitLocation = (Location) configurationSection.get(ParkourArenaStorageKey.EXIT_LOCATION.getKey());
        Location winLocation = (Location) configurationSection.get(ParkourArenaStorageKey.WIN_LOCATION.getKey());
        SerializableMaterial winBlockType = (SerializableMaterial) configurationSection.get(
                ParkourArenaStorageKey.WIN_BLOCK_TYPE.getKey());
        List<?> killPlaneBlockNamesList = configurationSection.getList(ParkourArenaStorageKey.KILL_PLANE_BLOCKS.getKey());
        Set<String> killPlaneBlockNames;
        if (killPlaneBlockNamesList == null) {
            killPlaneBlockNames = null;
        } else {
            killPlaneBlockNames = new HashSet<>((List<String>) killPlaneBlockNamesList);
        }
        List<?> obstacleBlockNamesList = configurationSection.getList(ParkourArenaStorageKey.OBSTACLE_BLOCKS.getKey());
        Set<String> obstacleBlockNames;
        if (obstacleBlockNamesList == null) {
            obstacleBlockNames = null;
        } else {
            obstacleBlockNames = new HashSet<>((List<String>) obstacleBlockNamesList);
        }
        List<Location> checkpoints = (List<Location>) configurationSection.get(ParkourArenaStorageKey.CHECKPOINTS.getKey());

        Map<RewardCondition, Set<Reward>> rewards = RewardStorageHelper.loadRewards(configurationSection,
                ParkourArenaStorageKey.REWARDS.getKey());

        // The arena name and spawn location must be present
        if (arenaName == null || spawnLocation == null) {
            MiniGames.log(Level.SEVERE, MiniGames.getInstance().getStringFormatter().replacePlaceholders(
                    MiniGameMessage.ERROR_ARENA_NOT_LOADED, new String[]{"{section}", "{file}"},
                    new String[]{configurationSection.getName(), "parkour_arena"}));
            return null;
        }

        // Fall back to the default win block
        if (winBlockType == null) {
            winBlockType = new SerializableMaterial(Material.EMERALD_BLOCK);
        }

        // Generate new, empty arena data if not available
        ParkourArenaData arenaData = loadParkourArenaData(arenaId);
        if (arenaData == null) {
            MiniGames.log(Level.SEVERE, MiniGames.getInstance().getStringFormatter().replacePlaceholder(
                    MiniGameMessage.ERROR_ARENA_DATA_NOT_LOADED, "{arena}", arenaId.toString()));
            arenaData = getEmptyParkourData(arenaId);
        }

        if (checkpoints == null) {
            checkpoints = new ArrayList<>();
        }

        return new ParkourArena(arenaId, arenaName, spawnLocation, exitLocation, winBlockType.getRawValue(), winLocation,
                killPlaneBlockNames, obstacleBlockNames, checkpoints, rewards, arenaData,
                MiniGames.getInstance().getParkourArenaHandler());
    }

    /**
     * Gets empty parkour data
     *
     * @param arenaId <p>The id to get parkour data for</p>
     * @return <p>Empty parkour data</p>
     */
    private static @NotNull ParkourArenaData getEmptyParkourData(@NotNull UUID arenaId) {
        Map<ArenaGameMode, ArenaRecordsRegistry> recordRegistries = new HashMap<>();
        Map<ArenaGameMode, Set<UUID>> playersCompleted = new HashMap<>();
        for (ArenaGameMode arenaGameMode : ParkourArenaGameMode.values()) {
            recordRegistries.put(arenaGameMode, new ParkourArenaRecordsRegistry(arenaId));
            playersCompleted.put(arenaGameMode, new HashSet<>());
        }
        return new ParkourArenaData(arenaId, recordRegistries, playersCompleted);
    }

    /**
     * Stores the given arena data to a file
     *
     * @param arenaData <p>The arena data to store</p>
     */
    public static void saveParkourArenaData(@NotNull ParkourArenaData arenaData) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set(ParkourArenaStorageKey.DATA.getKey(), arenaData);

        configuration.save(getParkourArenaDataFile(arenaData.getArenaId()));
    }

    /**
     * Loads arena data for the given arena id
     *
     * @param arenaId <p>The id of the arena to get data for</p>
     * @return <p>The loaded arena data</p>
     */
    private static @Nullable ParkourArenaData loadParkourArenaData(@NotNull UUID arenaId) {
        File arenaDataFile = getParkourArenaDataFile(arenaId);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(arenaDataFile);
        return (ParkourArenaData) configuration.get(ParkourArenaStorageKey.DATA.getKey());
    }

    /**
     * Removes data for the arena with the given id
     *
     * @param arenaId <p>The id of the arena to remove data for</p>
     * @return <p>True if the data was successfully removed</p>
     */
    public static boolean removeParkourArenaData(@NotNull UUID arenaId) {
        return getParkourArenaDataFile(arenaId).delete();
    }

    /**
     * Gets the file used to store the given arena id's data
     *
     * @param arenaId <p>The id of the arena to get a data file for</p>
     * @return <p>The file the arena's data is/should be stored in</p>
     */
    private static @NotNull File getParkourArenaDataFile(@NotNull UUID arenaId) {
        return getArenaDataFile(parkourArenaDataFolder, arenaId);
    }

}
