package net.knarcraft.minigames.arena;

import net.knarcraft.knarlib.formatting.StringFormatter;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.reward.RewardCondition;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.property.RecordResult;
import net.knarcraft.minigames.property.RecordType;
import net.knarcraft.minigames.util.PlayerTeleporter;
import net.knarcraft.minigames.util.RewardHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractArenaSession implements ArenaSession {

    private final @NotNull Arena arena;
    private final @NotNull ArenaGameMode gameMode;
    private final @NotNull Player player;
    protected int deaths;
    protected long startTime;
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
    public void triggerQuit(boolean immediately, boolean removeSession) {
        // Stop this session
        if (removeSession) {
            removeSession();
        }
        // Teleport the player out of the arena
        teleportToExit(immediately);
        // Make the player visible to everyone
        MiniGames.getInstance().getPlayerVisibilityManager().showPlayersFor(player);

        MiniGames.getInstance().getStringFormatter().displaySuccessMessage(player, MiniGameMessage.SUCCESS_ARENA_QUIT);
    }

    @Override
    public void reset() {
        this.deaths = 0;
        this.startTime = System.currentTimeMillis();
        PlayerTeleporter.teleportPlayer(this.player, this.arena.getSpawnLocation(), false, false);
        this.entryState.setArenaState();
    }

    /**
     * Announces a record set by this player
     *
     * @param recordResult <p>The result of the record</p>
     * @param recordType   <p>The type of record set (time or deaths)</p>
     */
    protected void announceRecord(@NotNull RecordResult recordResult, @NotNull RecordType recordType) {
        if (recordResult == RecordResult.NONE) {
            return;
        }

        // Gets a string representation of the played game-mode
        String gameModeString = getGameModeString();

        MiniGameMessage recordInfoMiniGameMessage = switch (recordResult) {
            case WORLD_RECORD -> MiniGameMessage.RECORD_ACHIEVED_GLOBAL;
            case PERSONAL_BEST -> MiniGameMessage.RECORD_ACHIEVED_PERSONAL;
            default -> throw new IllegalStateException("Unexpected value: " + recordResult);
        };
        StringFormatter stringFormatter = MiniGames.getInstance().getStringFormatter();
        String recordInfo = stringFormatter.replacePlaceholder(recordInfoMiniGameMessage, "{recordType}",
                recordType.name().toLowerCase().replace("_", " "));

        stringFormatter.displaySuccessMessage(player, stringFormatter.replacePlaceholders(
                MiniGameMessage.SUCCESS_RECORD_ACHIEVED, new String[]{"{gameMode}", "{recordInfo}"},
                new String[]{gameModeString, recordInfo}));

        // Reward the player
        rewardRecord(recordResult, recordType);
    }

    /**
     * Registers the player's record if necessary, and prints record information to the player
     */
    protected void registerRecord() {
        ArenaRecordsRegistry recordsRegistry = this.arena.getData().getRecordRegistries().get(this.gameMode);
        long timeElapsed = System.currentTimeMillis() - this.startTime;
        announceRecord(recordsRegistry.registerTimeRecord(this.player.getUniqueId(), timeElapsed), RecordType.TIME);
        announceRecord(recordsRegistry.registerDeathRecord(this.player.getUniqueId(), this.deaths), RecordType.DEATHS);
    }

    /**
     * Rewards the specified achieved record
     *
     * @param recordResult <p>The result of the record achieved</p>
     * @param recordType   <p>The type of record achieved</p>
     */
    protected void rewardRecord(RecordResult recordResult, RecordType recordType) {
        RewardCondition condition = null;
        if (recordResult == RecordResult.WORLD_RECORD) {
            if (recordType == RecordType.DEATHS) {
                condition = RewardCondition.GLOBAL_DEATH_RECORD;
            } else if (recordType == RecordType.TIME) {
                condition = RewardCondition.GLOBAL_TIME_RECORD;
            }
        } else if (recordResult == RecordResult.PERSONAL_BEST) {
            if (recordType == RecordType.DEATHS) {
                condition = RewardCondition.PERSONAL_DEATH_RECORD;
            } else if (recordType == RecordType.TIME) {
                condition = RewardCondition.PERSONAL_TIME_RECORD;
            }
        }
        RewardHelper.grantRewards(this.player, this.arena.getRewards(condition));
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
