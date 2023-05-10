package net.knarcraft.minigames.arena.dropper;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.AbstractArenaSession;
import net.knarcraft.minigames.arena.PlayerEntryState;
import net.knarcraft.minigames.config.Message;
import net.knarcraft.minigames.gui.ArenaGUI;
import net.knarcraft.minigames.gui.DropperGUI;
import net.knarcraft.minigames.util.PlayerTeleporter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * A representation of a player's current session in a dropper arena
 */
public class DropperArenaSession extends AbstractArenaSession {

    private static final ArenaGUI gui = new DropperGUI();

    private final @NotNull DropperArena arena;
    private final @NotNull Player player;
    private final @NotNull DropperArenaGameMode gameMode;

    /**
     * Instantiates a new dropper arena session
     *
     * @param dropperArena <p>The arena that's being played in</p>
     * @param player       <p>The player playing the arena</p>
     * @param gameMode     <p>The game-mode</p>
     */
    public DropperArenaSession(@NotNull DropperArena dropperArena, @NotNull Player player,
                               @NotNull DropperArenaGameMode gameMode) {
        super(dropperArena, player, gameMode);
        this.arena = dropperArena;
        this.player = player;
        this.gameMode = gameMode;

        this.entryState = new DropperPlayerEntryState(player, gameMode, dropperArena.getPlayerHorizontalVelocity());
        this.entryState.setArenaState();
    }

    /**
     * Gets the player playing in this session
     *
     * @return <p>This session's player</p>
     */
    public @NotNull Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the game-mode the player is playing in this session
     *
     * @return <p>The game-mode for this session</p>
     */
    public @NotNull DropperArenaGameMode getGameMode() {
        return this.gameMode;
    }

    @Override
    public @NotNull PlayerEntryState getEntryState() {
        return this.entryState;
    }

    @Override
    public void triggerWin() {
        // Stop this session
        removeSession();

        // Check for, and display, records
        MiniGames miniGames = MiniGames.getInstance();
        boolean ignore = miniGames.getDropperConfiguration().ignoreRecordsUntilGroupBeatenOnce();
        DropperArenaGroup group = miniGames.getDropperArenaHandler().getGroup(this.arena.getArenaId());
        if (!ignore || group == null || group.hasBeatenAll(this.gameMode, this.player)) {
            registerRecord();
        }

        // Mark the arena as cleared
        if (this.arena.getData().setCompleted(this.gameMode, this.player)) {
            this.player.sendMessage(Message.SUCCESS_ARENA_FIRST_CLEAR.getMessage());
        }
        this.player.sendMessage(Message.SUCCESS_ARENA_WIN.getMessage());

        // Teleport the player out of the arena
        teleportToExit(false);
    }

    @Override
    public void triggerLoss() {
        this.deaths++;
        //Teleport the player back to the top
        PlayerTeleporter.teleportPlayer(this.player, this.arena.getSpawnLocation(), true, false);
        this.entryState.setArenaState();
    }

    @Override
    public @NotNull DropperArena getArena() {
        return this.arena;
    }

    @Override
    public @NotNull ArenaGUI getGUI() {
        return gui;
    }

    @Override
    protected void removeSession() {
        // Remove this session for game sessions to stop listeners from fiddling more with the player
        boolean removedSession = MiniGames.getInstance().getDropperArenaPlayerRegistry().removePlayer(
                player.getUniqueId(), true);
        if (!removedSession) {
            MiniGames.log(Level.SEVERE, "Unable to remove dropper arena session for " + player.getName() + ". " +
                    "This will have unintended consequences.");
        }
    }

    @Override
    protected String getGameModeString() {
        return switch (this.gameMode) {
            case DEFAULT -> "default";
            case INVERTED -> "inverted";
            case RANDOM_INVERTED -> "random";
        };
    }

}
