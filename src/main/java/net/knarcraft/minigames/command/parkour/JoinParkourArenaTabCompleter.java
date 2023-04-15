package net.knarcraft.minigames.command.parkour;

import net.knarcraft.minigames.arena.parkour.ParkourArenaGameMode;
import net.knarcraft.minigames.command.JoinArenaTabCompleter;
import net.knarcraft.minigames.util.TabCompleteHelper;

/**
 * The tab-completer for the join command
 */
public class JoinParkourArenaTabCompleter extends JoinArenaTabCompleter {

    /**
     * Implements a new join arena tab completer
     */
    public JoinParkourArenaTabCompleter() {
        super(TabCompleteHelper::getParkourArenas, ParkourArenaGameMode.DEFAULT);
    }

}
