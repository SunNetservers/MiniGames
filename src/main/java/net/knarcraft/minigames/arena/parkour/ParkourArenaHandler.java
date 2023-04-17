package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaHandler;
import net.knarcraft.minigames.util.ParkourArenaStorageHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A handler that keeps track of all parkour arenas
 */
public class ParkourArenaHandler extends ArenaHandler<ParkourArena, ParkourArenaGroup> {

    /**
     * Instantiates a new arena handler
     *
     * @param playerRegistry <p>The registry keeping track of player sessions</p>
     */
    public ParkourArenaHandler(ParkourArenaPlayerRegistry playerRegistry) {
        super(playerRegistry);
    }

    @Override
    public void saveGroups() {
        try {
            ParkourArenaStorageHelper.saveParkourArenaGroups(new HashSet<>(this.arenaGroups.values()));
        } catch (IOException e) {
            MiniGames.log(Level.SEVERE, "Unable to save current arena groups! " +
                    "Data loss can occur!");
            MiniGames.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    protected void loadGroups() {
        Set<ParkourArenaGroup> arenaGroups = ParkourArenaStorageHelper.loadParkourArenaGroups();
        Map<UUID, ParkourArenaGroup> arenaGroupMap = new HashMap<>();
        for (ParkourArenaGroup arenaGroup : arenaGroups) {
            for (UUID arenaId : arenaGroup.getArenas()) {
                arenaGroupMap.put(arenaId, arenaGroup);
            }
        }
        this.arenaGroups = arenaGroupMap;
    }

    @Override
    public void saveArenas() {
        try {
            ParkourArenaStorageHelper.saveParkourArenas(this.arenas);
        } catch (IOException e) {
            MiniGames.log(Level.SEVERE, "Unable to save current arenas! " +
                    "Data loss can occur!");
            MiniGames.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    protected void loadArenas() {
        this.arenas = ParkourArenaStorageHelper.loadParkourArenas();

        // Save a map from arena name to arena id for improved performance
        this.arenaNameLookup = new HashMap<>();
        for (Map.Entry<UUID, ParkourArena> arena : this.arenas.entrySet()) {
            String sanitizedName = arena.getValue().getArenaNameSanitized();
            this.arenaNameLookup.put(sanitizedName, arena.getKey());
        }
    }

}