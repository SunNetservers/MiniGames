package net.knarcraft.minigames.arena.dropper;

import net.knarcraft.knarlib.formatting.FormatBuilder;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.AbstractArenaSession;
import net.knarcraft.minigames.arena.PlayerEntryState;
import net.knarcraft.minigames.arena.reward.RewardCondition;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.gui.ArenaGUI;
import net.knarcraft.minigames.gui.DropperGUI;
import net.knarcraft.minigames.gui.DropperGUIBedrock;
import net.knarcraft.minigames.util.GeyserHelper;
import net.knarcraft.minigames.util.PlayerTeleporter;
import net.knarcraft.minigames.util.RewardHelper;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

/**
 * A representation of a player's current session in a dropper arena
 */
public class DropperArenaSession extends AbstractArenaSession {

    @NotNull
    private final DropperArena arena;
    @NotNull
    private final Player player;
    @NotNull
    private final DropperArenaGameMode gameMode;
    private boolean startedMoving = false;

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
     * Marks that this arena's player has started moving
     */
    public void setStartedMoving() {
        this.startedMoving = true;
    }

    /**
     * Gets whether the player of this session has started moving in the arena
     *
     * @return <p>True if the player has started moving</p>
     */
    public boolean getStartedMoving() {
        return this.startedMoving;
    }

    /**
     * Gets the player playing in this session
     *
     * @return <p>This session's player</p>
     */
    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the game-mode the player is playing in this session
     *
     * @return <p>The game-mode for this session</p>
     */
    @NotNull
    public DropperArenaGameMode getGameMode() {
        return this.gameMode;
    }

    @Override
    @NotNull
    public PlayerEntryState getEntryState() {
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
            new FormatBuilder(MiniGameMessage.SUCCESS_ARENA_FIRST_CLEAR).success(this.player);
            RewardHelper.grantRewards(this.player, this.arena.getRewards(RewardCondition.FIRST_WIN));
        }
        new FormatBuilder(MiniGameMessage.SUCCESS_ARENA_WIN).success(this.player);
        RewardHelper.grantRewards(this.player, this.arena.getRewards(RewardCondition.WIN));

        // Teleport the player out of the arena
        teleportToExit(false);
    }

    @Override
    public void triggerLoss() {
        this.deaths++;
        //Teleport the player back to the top
        PlayerTeleporter.teleportPlayer(this.player, this.arena.getSpawnLocation(), true, false);
        this.player.playSound(this.player, Sound.ENTITY_CHICKEN_EGG, 5f, 0.5f);
        this.entryState.setArenaState();
    }

    @Override
    @NotNull
    public DropperArena getArena() {
        return this.arena;
    }

    @Override
    @NotNull
    public ArenaGUI getGUI() {
        if (GeyserHelper.isGeyserPlayer(this.player)) {
            return new DropperGUIBedrock(this.player);
        } else {
            return new DropperGUI(this.player);
        }
    }

    @Override
    public void reset() {
        this.startedMoving = false;
        super.reset();
    }

    @Override
    protected void removeSession() {
        // Remove this session for game sessions to stop listeners from fiddling more with the player
        boolean removedSession = MiniGames.getInstance().getDropperArenaPlayerRegistry().removePlayer(
                this.player.getUniqueId(), true);
        if (!removedSession) {
            MiniGames.log(Level.SEVERE, "Unable to remove dropper arena session for " + player.getName() + ". " +
                    "This will have unintended consequences.");
        }
    }

    @Override
    @NotNull
    protected String getGameModeString() {
        return switch (this.gameMode) {
            case DEFAULT -> "default";
            case INVERTED -> "inverted";
            case RANDOM_INVERTED -> "random";
        };
    }

}
