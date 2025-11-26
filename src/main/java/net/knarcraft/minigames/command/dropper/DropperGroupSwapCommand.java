package net.knarcraft.minigames.command.dropper;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaHandler;
import net.knarcraft.minigames.arena.dropper.DropperArena;
import net.knarcraft.minigames.arena.dropper.DropperArenaGroup;
import net.knarcraft.minigames.command.ArenaGroupSwapCommand;

/**
 * The command for swapping the order of two arenas in a group
 */
public class DropperGroupSwapCommand extends ArenaGroupSwapCommand<DropperArena, DropperArenaGroup> {

    @Override
    protected ArenaHandler<DropperArena, DropperArenaGroup> getArenaHandler() {
        return MiniGames.getInstance().getDropperArenaHandler();
    }

}
