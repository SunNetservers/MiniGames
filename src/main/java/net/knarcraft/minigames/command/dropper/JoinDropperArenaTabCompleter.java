package net.knarcraft.minigames.command.dropper;

import net.knarcraft.minigames.arena.dropper.DropperArenaGameMode;
import net.knarcraft.minigames.command.JoinArenaTabCompleter;
import net.knarcraft.minigames.util.TabCompleteHelper;

/**
 * The tab-completer for the join command
 */
public class JoinDropperArenaTabCompleter extends JoinArenaTabCompleter {

    /**
     * Implements a new join arena tab completer
     */
    public JoinDropperArenaTabCompleter() {
        super(TabCompleteHelper::getDropperArenas, DropperArenaGameMode.DEFAULT);
    }

}
