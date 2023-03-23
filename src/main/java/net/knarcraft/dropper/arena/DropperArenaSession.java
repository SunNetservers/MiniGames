package net.knarcraft.dropper.arena;

import net.knarcraft.dropper.property.ArenaGameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DropperArenaSession {

    private final DropperArena arena;
    private final Player player;
    private final ArenaGameMode gameMode;
    private final int deaths;
    private final long startTime;

    /**
     * Instantiates a new dropper arena session
     *
     * @param dropperArena <p>The arena that's being played in</p>
     * @param player       <p>The player playing the arena</p>
     * @param gameMode     <p>The game-mode</p>
     */
    public DropperArenaSession(@NotNull DropperArena dropperArena, @NotNull Player player,
                               @NotNull ArenaGameMode gameMode) {
        this.arena = dropperArena;
        this.player = player;
        this.gameMode = gameMode;
        this.deaths = 0;
        this.startTime = System.currentTimeMillis();
    }

    public void triggerWin() {
        //TODO: Kick the player from the arena
        //TODO: Register the player's record, if applicable, and announce the result
        //TODO: Give reward?
        //TODO: If a staged arena, register the stage as cleared
        //TODO: Teleport the player out of the dropper arena
    }

    public void triggerLoss() {
        switch (gameMode) {
            case DEFAULT:
                //TODO: Kick the player, and teleport the player away
                break;
            case LEAST_TIME:
                //TODO: Teleport the player back to the top
                break;
            case LEAST_DEATHS:
                //TODO: Add 1 to the death count, and teleport the player back to the top
        }
    }

}
