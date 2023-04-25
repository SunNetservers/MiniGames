package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.config.Message;
import net.knarcraft.minigames.container.PlaceholderContainer;
import net.knarcraft.minigames.property.RecordResult;
import net.knarcraft.minigames.util.PlayerTeleporter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractArenaSession implements ArenaSession {

    private final @NotNull Arena arena;
    private final @NotNull ArenaGameMode gameMode;
    private final @NotNull Player player;
    protected int deaths;
    protected final long startTime;
    protected PlayerEntryState entryState;

    /**
     * Instantiates a new abstract arena session
     *
     * @param arena    <p>The arena that's being played in</p>
     * @param player   <p>The player playing the arena</p>
     * @param gameMode <p>The game-mode</p>
     */
    public AbstractArenaSession(@NotNull Arena arena, @NotNull Player player, @NotNull ArenaGameMode gameMode) {
        this.arena = arena;
        this.player = player;
        this.gameMode = gameMode;
        this.deaths = 0;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void triggerQuit(boolean immediately) {
        // Stop this session
        stopSession();
        // Teleport the player out of the arena
        teleportToExit(immediately);

        player.sendMessage(Message.SUCCESS_ARENA_QUIT.getMessage());
    }

    /**
     * Announces a record set by this player
     *
     * @param recordResult <p>The result of the record</p>
     * @param type         <p>The type of record set (time or deaths)</p>
     */
    protected void announceRecord(@NotNull RecordResult recordResult, @NotNull String type) {
        if (recordResult == RecordResult.NONE) {
            return;
        }

        // Gets a string representation of the played game-mode
        String gameModeString = getGameModeString();

        Message recordInfoMessage = switch (recordResult) {
            case WORLD_RECORD -> Message.RECORD_ACHIEVED_GLOBAL;
            case PERSONAL_BEST -> Message.RECORD_ACHIEVED_PERSONAL;
            default -> throw new IllegalStateException("Unexpected value: " + recordResult);
        };
        String recordInfo = recordInfoMessage.getMessage("{recordType}", type);

        PlaceholderContainer placeholderContainer = new PlaceholderContainer().add("{gameMode}", gameModeString);
        placeholderContainer.add("{recordInfo}", recordInfo);
        player.sendMessage(Message.SUCCESS_RECORD_ACHIEVED.getMessage(placeholderContainer));
    }

    /**
     * Registers the player's record if necessary, and prints record information to the player
     */
    protected void registerRecord() {
        ArenaRecordsRegistry recordsRegistry = this.arena.getData().getRecordRegistries().get(this.gameMode);
        long timeElapsed = System.currentTimeMillis() - this.startTime;
        announceRecord(recordsRegistry.registerTimeRecord(this.player.getUniqueId(), timeElapsed), "time");
        announceRecord(recordsRegistry.registerDeathRecord(this.player.getUniqueId(), this.deaths), "least deaths");
    }

    /**
     * Teleports the playing player out of the arena
     */
    protected void teleportToExit(boolean immediately) {
        // Teleport the player out of the arena
        Location exitLocation;
        if (this.arena.getExitLocation() != null) {
            exitLocation = this.arena.getExitLocation();
        } else {
            exitLocation = this.entryState.getEntryLocation();
        }
        PlayerTeleporter.teleportPlayer(this.player, exitLocation, true, immediately);
    }

    /**
     * Stops this session, and disables flight mode
     */
    protected void stopSession() {
        // Remove this session from game sessions to stop listeners from fiddling more with the player
        removeSession();

        // Remove flight mode
        entryState.restore();
    }

    /**
     * Gets the string representation of the session's game-mode
     *
     * @return <p>The string representation</p>
     */
    protected abstract String getGameModeString();

    /**
     * Removes this session from current sessions
     */
    protected abstract void removeSession();

}
