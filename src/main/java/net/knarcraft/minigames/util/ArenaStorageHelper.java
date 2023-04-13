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
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaData;
import net.knarcraft.minigames.arena.parkour.ParkourArenaStorageKey;
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

/**
 * A helper class for saving and loading arenas
 */
public final class ArenaStorageHelper {

    private final static File dataFolder = MiniGames.getInstance().getDataFolder();
    private final static String dropperArenasConfigurationSection = "dropperArenas";
    private final static String dropperGroupsConfigurationSection = "dropperGroups";
    private final static String parkourArenasConfigurationSection = "parkourArenas";
    private final static String parkourGroupsConfigurationSection = "parkourGroups";
    private static final File arenaFile = new File(dataFolder, "arenas.yml");
    private static final File groupFile = new File(dataFolder, "groups.yml");
    private static final File dropperArenaDataFolder = new File(dataFolder, "dropper_arena_data");
    private static final File parkourArenaDataFolder = new File(dataFolder, "parkour_arena_data");

    private ArenaStorageHelper() {

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

        configuration.save(groupFile);
    }

    /**
     * Loads all existing dropper arena groups
     *
     * @return <p>The loaded arena groups</p>
     */
    public static @NotNull Set<DropperArenaGroup> loadDropperArenaGroups() {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(groupFile);
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
            saveDropperArenaData(arena.getData());
        }
        configuration.save(arenaFile);
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
            //Note: While the arena name is used as the key, as the key has to be sanitized, the un-sanitized arena name
            // must be stored as well
            @NotNull ConfigurationSection configSection = arenaSection.createSection(arena.getArenaId().toString());
            configSection.set(ParkourArenaStorageKey.ID.getKey(), new SerializableUUID(arena.getArenaId()));
            configSection.set(ParkourArenaStorageKey.NAME.getKey(), arena.getArenaName());
            configSection.set(ParkourArenaStorageKey.SPAWN_LOCATION.getKey(), arena.getSpawnLocation());
            configSection.set(ParkourArenaStorageKey.EXIT_LOCATION.getKey(), arena.getExitLocation());
            configSection.set(ParkourArenaStorageKey.WIN_BLOCK_TYPE.getKey(), new SerializableMaterial(arena.getWinBlockType()));
            configSection.set(ParkourArenaStorageKey.WIN_LOCATION.getKey(), arena.getWinLocation());
            configSection.set(ParkourArenaStorageKey.KILL_PLANE_BLOCKS.getKey(), arena.getKillPlaneBlocks());
            configSection.set(ParkourArenaStorageKey.CHECKPOINTS.getKey(), arena.getCheckpoints());
            saveParkourArenaData(arena.getData());
        }
        configuration.save(arenaFile);
    }

    /**
     * Loads all arenas
     *
     * @return <p>The loaded arenas, or null if the arenas configuration section is missing.</p>
     */
    public static @NotNull Map<UUID, DropperArena> loadDropperArenas() {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(arenaFile);
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
            MiniGames.log(Level.SEVERE, "Could not load the arena at configuration " +
                    "section " + configurationSection.getName() + ". Please check the arenas storage file for issues.");
            return null;
        }
        if (winBlockType == null) {
            winBlockType = new SerializableMaterial(Material.WATER);
        }

        DropperArenaData arenaData = loadDropperArenaData(arenaId);
        if (arenaData == null) {
            MiniGames.log(Level.SEVERE, "Unable to load arena data for " + arenaId);

            Map<ArenaGameMode, ArenaRecordsRegistry> recordRegistries = new HashMap<>();
            for (ArenaGameMode arenaGameMode : DropperArenaGameMode.values()) {
                recordRegistries.put(arenaGameMode, new DropperArenaRecordsRegistry(arenaId));
            }
            arenaData = new DropperArenaData(arenaId, recordRegistries, new HashMap<>());
        }

        return new DropperArena(arenaId, arenaName, spawnLocation, exitLocation, verticalVelocity, horizontalVelocity,
                winBlockType.getRawValue(), arenaData, MiniGames.getInstance().getDropperArenaHandler());
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
    public static boolean removeDropperArenaData(@NotNull UUID arenaId) {
        return getDropperArenaDataFile(arenaId).delete();
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
    private static @NotNull File getDropperArenaDataFile(@NotNull UUID arenaId) {
        return getArenaDataFile(dropperArenaDataFolder, arenaId);
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

    /**
     * Gets the file used to store the given arena id's data
     *
     * @param root    <p>The root directory for the file</p>
     * @param arenaId <p>The id of the arena to get a data file for</p>
     * @return <p>The file the arena's data is/should be stored in</p>
     */
    private static @NotNull File getArenaDataFile(File root, @NotNull UUID arenaId) {
        File arenaDataFile = new File(root, arenaId + ".yml");
        if (!root.exists() && !root.mkdirs()) {
            MiniGames.log(Level.SEVERE, "Unable to create the arena data directories");
        }
        return arenaDataFile;
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
