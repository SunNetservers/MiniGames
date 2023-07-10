package net.knarcraft.minigames.arena.parkour;

import net.knarcraft.knarlib.formatting.StringFormatter;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.AbstractArenaSession;
import net.knarcraft.minigames.arena.PlayerEntryState;
import net.knarcraft.minigames.arena.reward.RewardCondition;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.gui.ArenaGUI;
import net.knarcraft.minigames.gui.ParkourGUI;
import net.knarcraft.minigames.util.PlayerTeleporter;
import net.knarcraft.minigames.util.RewardHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

/**
 * A representation of a player's current session in a parkour arena
 */
public class ParkourArenaSession extends AbstractArenaSession {

    private final @NotNull ParkourArena arena;
    private final @NotNull Player player;
    private final @NotNull ParkourArenaGameMode gameMode;
    private Location reachedCheckpoint = null;

    /**
     * Instantiates a new parkour arena session
     *
     * @param parkourArena <p>The arena that's being played in</p>
     * @param player       <p>The player playing the arena</p>
     * @param gameMode     <p>The game-mode</p>
     */
    public ParkourArenaSession(@NotNull ParkourArena parkourArena, @NotNull Player player,
                               @NotNull ParkourArenaGameMode gameMode) {
        super(parkourArena, player, gameMode);
        this.arena = parkourArena;
        this.player = player;
        this.gameMode = gameMode;

        this.entryState = new ParkourPlayerEntryState(player);
        this.entryState.setArenaState();
    }

    /**
     * Gets the game-mode the player is playing in this session
     *
     * @return <p>The game-mode for this session</p>
     */
    public @NotNull ParkourArenaGameMode getGameMode() {
        return this.gameMode;
    }

    /**
     * Registers the checkpoint this session's player has reached
     *
     * @param location <p>The location of the checkpoint</p>
     */
    public void registerCheckpoint(@NotNull Location location) {
        this.reachedCheckpoint = location;
    }

    /**
     * Gets the checkpoint currently registered as the player's spawn location
     *
     * @return <p>The registered checkpoint, or null if not set</p>
     */
    public @Nullable Location getRegisteredCheckpoint() {
        return this.reachedCheckpoint;
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
        boolean ignore = miniGames.getParkourConfiguration().ignoreRecordsUntilGroupBeatenOnce();
        ParkourArenaGroup group = miniGames.getParkourArenaHandler().getGroup(this.arena.getArenaId());
        if (!ignore || group == null || group.hasBeatenAll(this.gameMode, this.player)) {
            registerRecord();
        }

        StringFormatter stringFormatter = MiniGames.getInstance().getStringFormatter();

        // Mark the arena as cleared
        if (this.arena.getData().setCompleted(this.gameMode, this.player)) {
            stringFormatter.displaySuccessMessage(this.player, MiniGameMessage.SUCCESS_ARENA_FIRST_CLEAR);
            RewardHelper.grantRewards(this.player, this.arena.getRewards(RewardCondition.FIRST_WIN));
        }
        stringFormatter.displaySuccessMessage(this.player, MiniGameMessage.SUCCESS_ARENA_WIN);
        RewardHelper.grantRewards(this.player, this.arena.getRewards(RewardCondition.WIN));

        // Teleport the player out of the arena
        teleportToExit(false);
    }

    @Override
    public void triggerLoss() {
        this.deaths++;
        //Teleport the player back to the top
        Location spawnLocation = this.reachedCheckpoint != null ? this.reachedCheckpoint : this.arena.getSpawnLocation();
        PlayerTeleporter.teleportPlayer(this.player, spawnLocation, true, false);
        this.entryState.setArenaState();
    }

    @Override
    public @NotNull ParkourArena getArena() {
        return this.arena;
    }

    @Override
    public @NotNull ArenaGUI getGUI() {
        return new ParkourGUI(player);
    }

    @Override
    public void reset() {
        this.reachedCheckpoint = null;
        super.reset();
    }

    @Override
    protected void removeSession() {
        // Remove this session for game sessions to stop listeners from fiddling more with the player
        boolean removedSession = MiniGames.getInstance().getParkourArenaPlayerRegistry().removePlayer(
                player.getUniqueId(), true);
        if (!removedSession) {
            MiniGames.log(Level.SEVERE, "Unable to remove parkour arena session for " + player.getName() + ". " +
                    "This will have unintended consequences.");
        }
    }

    @Override
    protected String getGameModeString() {
        return switch (this.gameMode) {
            case DEFAULT -> "default";
            case HARDCORE -> "hardcore";
        };
    }

}
