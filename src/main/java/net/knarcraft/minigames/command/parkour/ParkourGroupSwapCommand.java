package net.knarcraft.minigames.command.parkour;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaHandler;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaGroup;
import net.knarcraft.minigames.command.ArenaGroupSwapCommand;

/**
 * The command for swapping the order of two arenas in a group
 */
public class ParkourGroupSwapCommand extends ArenaGroupSwapCommand<ParkourArena, ParkourArenaGroup> {

    @Override
    protected ArenaHandler<ParkourArena, ParkourArenaGroup> getArenaHandler() {
        return MiniGames.getInstance().getParkourArenaHandler();
    }

}
