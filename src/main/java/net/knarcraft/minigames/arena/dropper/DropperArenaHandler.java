package net.knarcraft.minigames.arena.dropper;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaHandler;
import net.knarcraft.minigames.util.DropperArenaStorageHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A handler that keeps track of all dropper arenas
 */
public class DropperArenaHandler extends ArenaHandler<DropperArena, DropperArenaGroup> {

    /**
     * Instantiates a new arena handler
     *
     * @param playerRegistry <p>The registry keeping track of player sessions</p>
     */
    public DropperArenaHandler(DropperArenaPlayerRegistry playerRegistry) {
        super(playerRegistry);
    }

    @Override
    public void saveGroups() {
        try {
            DropperArenaStorageHelper.saveDropperArenaGroups(new HashSet<>(this.arenaGroups.values()));
        } catch (IOException e) {
            MiniGames.log(Level.SEVERE, "Unable to save current arena groups! " +
                    "Data loss can occur!");
            MiniGames.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    protected void loadGroups() {
        Set<DropperArenaGroup> arenaGroups = DropperArenaStorageHelper.loadDropperArenaGroups();
        Map<UUID, DropperArenaGroup> arenaGroupMap = new HashMap<>();
        for (DropperArenaGroup arenaGroup : arenaGroups) {
            for (UUID arenaId : arenaGroup.getArenas()) {
                arenaGroupMap.put(arenaId, arenaGroup);
            }
        }
        this.arenaGroups = arenaGroupMap;
    }

    @Override
    public void saveArenas() {
        try {
            DropperArenaStorageHelper.saveDropperArenas(this.arenas);
        } catch (IOException e) {
            MiniGames.log(Level.SEVERE, "Unable to save current arenas! " +
                    "Data loss can occur!");
            MiniGames.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    protected void loadArenas() {
        this.arenas = DropperArenaStorageHelper.loadDropperArenas();

        // Save a map from arena name to arena id for improved performance
        this.arenaNameLookup = new HashMap<>();
        for (Map.Entry<UUID, DropperArena> arena : this.arenas.entrySet()) {
            String sanitizedName = arena.getValue().getArenaNameSanitized();
            this.arenaNameLookup.put(sanitizedName, arena.getKey());
        }
    }

}
