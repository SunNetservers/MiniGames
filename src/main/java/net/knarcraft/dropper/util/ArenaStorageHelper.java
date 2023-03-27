package net.knarcraft.dropper.util;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import net.knarcraft.dropper.arena.DropperArenaData;
import net.knarcraft.dropper.arena.DropperArenaRecordsRegistry;
import net.knarcraft.dropper.container.SerializableMaterial;
import net.knarcraft.dropper.container.SerializableUUID;
import net.knarcraft.dropper.property.ArenaStorageKey;
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
import java.util.UUID;
import java.util.logging.Level;

/**
 * A helper class for saving and loading arenas
 */
public final class ArenaStorageHelper {

    private final static String arenasConfigurationSection = "arenas";
    private static final File arenaFile = new File(Dropper.getInstance().getDataFolder(), "arenas.yml");
    private static final File arenaDataFolder = new File(Dropper.getInstance().getDataFolder(), "arena_data");

    private ArenaStorageHelper() {

    }

    /**
     * Saves the given arenas to the given file
     *
     * @param arenas <p>The arenas to save</p>
     * @throws IOException <p>If unable to write to the file</p>
     */
    public static void saveArenas(@NotNull Map<UUID, DropperArena> arenas) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        ConfigurationSection arenaSection = configuration.createSection(arenasConfigurationSection);
        for (DropperArena arena : arenas.values()) {
            //Note: While the arena name is used as the key, as the key has to be sanitized, the un-sanitized arena name
            // must be stored as well
            @NotNull ConfigurationSection configSection = arenaSection.createSection(sanitizeArenaName(
                    arena.getArenaName()));
            configSection.set(ArenaStorageKey.ID.getKey(), new SerializableUUID(arena.getArenaId()));
            configSection.set(ArenaStorageKey.NAME.getKey(), arena.getArenaName());
            configSection.set(ArenaStorageKey.SPAWN_LOCATION.getKey(), arena.getSpawnLocation());
            configSection.set(ArenaStorageKey.EXIT_LOCATION.getKey(), arena.getExitLocation());
            configSection.set(ArenaStorageKey.PLAYER_VERTICAL_VELOCITY.getKey(), arena.getPlayerVerticalVelocity());
            configSection.set(ArenaStorageKey.PLAYER_HORIZONTAL_VELOCITY.getKey(), arena.getPlayerHorizontalVelocity());
            configSection.set(ArenaStorageKey.STAGE.getKey(), arena.getStage());
            configSection.set(ArenaStorageKey.WIN_BLOCK_TYPE.getKey(), new SerializableMaterial(arena.getWinBlockType()));
            saveArenaData(arena.getData());
        }
        configuration.save(arenaFile);
    }

    /**
     * Loads all arenas from the given file
     *
     * @return <p>The loaded arenas, or null if the arenas configuration section is missing.</p>
     */
    public static @NotNull Map<UUID, DropperArena> loadArenas() {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(arenaFile);
        ConfigurationSection arenaSection = configuration.getConfigurationSection(arenasConfigurationSection);
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

            DropperArena arena = loadArena(configurationSection);
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
    private static @Nullable DropperArena loadArena(@NotNull ConfigurationSection configurationSection) {
        UUID arenaId = ((SerializableUUID) configurationSection.get(ArenaStorageKey.ID.getKey(),
                new SerializableUUID(UUID.randomUUID()))).uuid();
        String arenaName = configurationSection.getString(ArenaStorageKey.NAME.getKey());
        Location spawnLocation = (Location) configurationSection.get(ArenaStorageKey.SPAWN_LOCATION.getKey());
        Location exitLocation = (Location) configurationSection.get(ArenaStorageKey.EXIT_LOCATION.getKey());
        double verticalVelocity = configurationSection.getDouble(ArenaStorageKey.PLAYER_VERTICAL_VELOCITY.getKey());
        float horizontalVelocity = sanitizeHorizontalVelocity((float) configurationSection.getDouble(
                ArenaStorageKey.PLAYER_HORIZONTAL_VELOCITY.getKey()));
        Integer stage = (Integer) configurationSection.get(ArenaStorageKey.STAGE.getKey());
        SerializableMaterial winBlockType = (SerializableMaterial) configurationSection.get(
                ArenaStorageKey.WIN_BLOCK_TYPE.getKey());

        if (arenaName == null || spawnLocation == null) {
            Dropper.getInstance().getLogger().log(Level.SEVERE, "Could not load the arena at configuration " +
                    "section " + configurationSection.getName() + ". Please check the arenas storage file for issues.");
            return null;
        }
        if (winBlockType == null) {
            winBlockType = new SerializableMaterial(Material.WATER);
        }

        DropperArenaData arenaData = loadArenaData(arenaId);
        if (arenaData == null) {
            Dropper.getInstance().getLogger().log(Level.SEVERE, "Unable to load arena data for " + arenaId);
            arenaData = new DropperArenaData(arenaId, new DropperArenaRecordsRegistry(arenaId), new HashSet<>());
        }

        return new DropperArena(arenaId, arenaName, spawnLocation, exitLocation, verticalVelocity, horizontalVelocity,
                stage, winBlockType.material(), arenaData);
    }

    /**
     * Sanitizes an arena name for usage as a YAML key
     *
     * @param arenaName <p>The arena name to sanitize</p>
     * @return <p>The sanitized arena name</p>
     */
    public static @NotNull String sanitizeArenaName(@NotNull String arenaName) {
        return arenaName.toLowerCase().trim().replaceAll(" ", "_");
    }

    /**
     * Stores the given arena data to a file
     *
     * @param arenaData <p>The arena data to store</p>
     */
    public static void saveArenaData(@NotNull DropperArenaData arenaData) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set(ArenaStorageKey.DATA.getKey(), arenaData);

        configuration.save(getArenaDataFile(arenaData.arenaId()));
    }

    /**
     * Loads arena data for the given arena id
     *
     * @param arenaId <p>The id of the arena to get data for</p>
     * @return <p>The loaded arena data</p>
     */
    private static @Nullable DropperArenaData loadArenaData(@NotNull UUID arenaId) {
        File arenaDataFile = getArenaDataFile(arenaId);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(arenaDataFile);
        return (DropperArenaData) configuration.get(ArenaStorageKey.DATA.getKey());
    }

    /**
     * Gets the file used to store the given arena id's data
     *
     * @param arenaId <p>The id of the arena to get a data file for</p>
     * @return <p>The file the arena's data is/should be stored in</p>
     */
    private static @NotNull File getArenaDataFile(@NotNull UUID arenaId) {
        File arenaDataFile = new File(arenaDataFolder, arenaId + ".yml");
        if (!arenaDataFolder.exists() && !arenaDataFolder.mkdirs()) {
            Dropper.getInstance().getLogger().log(Level.SEVERE, "Unable to create the arena data directories");
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
