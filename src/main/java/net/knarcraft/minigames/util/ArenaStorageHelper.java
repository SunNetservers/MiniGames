package net.knarcraft.minigames.util;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.PlayerEntryState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A helper class for dealing with arena storage
 */
public final class ArenaStorageHelper {

    private ArenaStorageHelper() {

    }

    /**
     * Stores the given entry states to disk
     *
     * @param key         <p>The key specifying the correct entry state file</p>
     * @param entryStates <p>The entry states to save</p>
     */
    public static void storeArenaPlayerEntryStates(String key, Set<PlayerEntryState> entryStates) {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set(key, new ArrayList<>(entryStates));

        try {
            configuration.save(new File(MiniGames.getInstance().getDataFolder(), key + "EntryStates.yml"));
        } catch (IOException exception) {
            MiniGames.log(Level.SEVERE, "Unable to save entry states to disk");
        }
    }

    /**
     * Gets saved entry states from disk
     *
     * @param key <p>The key specifying the correct entry state file</p>
     * @return <p>The previously saved entry states</p>
     */
    public static Set<PlayerEntryState> getArenaPlayerEntryStates(String key) {
        File entryStateFile = new File(MiniGames.getInstance().getDataFolder(), key + "EntryStates.yml");
        if (!entryStateFile.exists()) {
            return new HashSet<>();
        }
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(entryStateFile);
        Set<PlayerEntryState> output = new HashSet<>();

        List<?> entries = configuration.getList(key, new ArrayList<>());
        for (Object entry : entries) {
            if (entry instanceof PlayerEntryState entryState) {
                output.add(entryState);
            }
        }
        return output;
    }

    /**
     * Gets the file used to store the given arena id's data
     *
     * @param root    <p>The root directory for the file</p>
     * @param arenaId <p>The id of the arena to get a data file for</p>
     * @return <p>The file the arena's data is/should be stored in</p>
     */
    public static @NotNull File getArenaDataFile(File root, @NotNull UUID arenaId) {
        File arenaDataFile = new File(root, arenaId + ".yml");
        if (!root.exists() && !root.mkdirs()) {
            MiniGames.log(Level.SEVERE, "Unable to create the arena data directories");
        }
        return arenaDataFile;
    }

}
