package net.knarcraft.dropper.util;

import net.knarcraft.dropper.Dropper;
import net.knarcraft.dropper.arena.DropperArena;
import net.knarcraft.dropper.arena.DropperArenaRecordsRegistry;
import net.knarcraft.dropper.property.ArenaStorageKey;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * A helper class for saving and loading arenas
 */
public final class ArenaStorageHelper {

    private final static String arenasConfigurationSection = "arenas";

    private ArenaStorageHelper() {

    }

    /**
     * Saves the given arenas to the given file
     *
     * @param arenas    <p>The arenas to save</p>
     * @param arenaFile <p>The file to save the arenas to</p>
     * @throws IOException <p>If unable to write to the file</p>
     */
    public static void saveArenas(@NotNull List<DropperArena> arenas, @NotNull File arenaFile) throws IOException {
        YamlConfiguration configuration = new YamlConfiguration();
        ConfigurationSection arenaSection = configuration.createSection(arenasConfigurationSection);
        for (DropperArena arena : arenas) {
            //Note: While the arena name is used as the key, as the key has to be sanitized, the un-sanitized arena name
            // must be stored as well
            @NotNull ConfigurationSection configSection = arenaSection.createSection(sanitizeArenaName(
                    arena.getArenaName()));
            configSection.set(ArenaStorageKey.NAME.getKey(), arena.getArenaName());
            configSection.set(ArenaStorageKey.SPAWN_LOCATION.getKey(), arena.getSpawnLocation());
            configSection.set(ArenaStorageKey.EXIT_LOCATION.getKey(), arena.getExitLocation());
            configSection.set(ArenaStorageKey.PLAYER_VELOCITY.getKey(), arena.getPlayerVelocity());
            configSection.set(ArenaStorageKey.STAGE.getKey(), arena.getStage());
        }
        //TODO: Save records belonging to the arena
        configuration.save(arenaFile);
    }

    /**
     * Loads all arenas from the given file
     *
     * @param arenaFile <p>The file used to store the arenas</p>
     * @return <p>The loaded arenas, or null if the arenas configuration section is missing.</p>
     */
    public static @NotNull List<DropperArena> loadArenas(@NotNull File arenaFile) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(arenaFile);
        ConfigurationSection arenaSection = configuration.getConfigurationSection(arenasConfigurationSection);
        //If no such section exists, it must be the case that there is no data to load
        if (arenaSection == null) {
            return new ArrayList<>();
        }

        List<DropperArena> loadedArenas = new ArrayList<>();

        for (String sectionName : arenaSection.getKeys(false)) {
            ConfigurationSection configurationSection = arenaSection.getConfigurationSection(sectionName);
            //I'm not sure whether this could actually happen
            if (configurationSection == null) {
                continue;
            }

            DropperArena arena = loadArena(configurationSection);
            if (arena != null) {
                loadedArenas.add(arena);
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
        String arenaName = configurationSection.getString(ArenaStorageKey.NAME.getKey());
        Location spawnLocation = (Location) configurationSection.get(ArenaStorageKey.SPAWN_LOCATION.getKey());
        Location exitLocation = (Location) configurationSection.get(ArenaStorageKey.EXIT_LOCATION.getKey());
        double playerVelocity = configurationSection.getDouble(ArenaStorageKey.PLAYER_VELOCITY.getKey());
        Integer stage = (Integer) configurationSection.get(ArenaStorageKey.STAGE.getKey());
        if (arenaName == null || spawnLocation == null) {
            Dropper.getInstance().getLogger().log(Level.SEVERE, "Could not load the arena at configuration " +
                    "section " + configurationSection.getName() + ". Please check the arenas storage file for issues.");
            return null;
        }
        //TODO: Load records for this arena
        return new DropperArena(arenaName, spawnLocation, exitLocation, playerVelocity, stage,
                new DropperArenaRecordsRegistry());
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

}
