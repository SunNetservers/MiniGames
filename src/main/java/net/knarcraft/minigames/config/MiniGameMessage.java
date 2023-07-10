package net.knarcraft.minigames.config;

import net.knarcraft.knarlib.formatting.TranslatableMessage;
import org.jetbrains.annotations.NotNull;

/**
 * A message which ca be displayed to the user
 */
public enum MiniGameMessage implements TranslatableMessage {

    /* ************** *
     * Error messages *
     * ************** */

    /**
     * The message displayed if saving arena groups fails
     */
    ERROR_CANNOT_SAVE_ARENA_GROUPS,

    /**
     * The message displayed if an un-parse-able message is given by a user
     */
    ERROR_MATERIAL_NOT_PARSE_ABLE,

    /**
     * The message displayed if a player tries to be teleported to/from an arena with a passenger
     */
    ERROR_TELEPORT_WITH_PASSENGER,

    /**
     * The message displayed if a player tries to be teleported to/from an arena with a vehicle
     */
    ERROR_TELEPORT_IN_VEHICLE,

    /**
     * The message displayed if an arena cannot be loaded
     */
    ERROR_ARENA_NOT_LOADED,

    /**
     * The message displayed if an arena's data cannot be loaded
     */
    ERROR_ARENA_DATA_NOT_LOADED,

    /**
     * The message displayed if the user specifies an unrecognized arena
     */
    ERROR_ARENA_NOT_FOUND,

    /**
     * The message displayed if the user specifies an unrecognized group
     */
    ERROR_GROUP_NOT_FOUND,

    /**
     * The message displayed if the console tries to execute a player-only command
     */
    ERROR_PLAYER_ONLY,

    /**
     * The message displayed if the name of an arena is duplicated
     */
    ERROR_ARENA_NAME_COLLISION,

    /**
     * The message displayed if the player is required to win on the default difficulty first
     */
    ERROR_NORMAL_MODE_REQUIRED,

    /**
     * The message displayed if the player is required to win on the default difficulty for all arenas in the group first
     */
    ERROR_GROUP_NORMAL_MODE_REQUIRED,

    /**
     * The message displayed if the player is required to beat the previous arena in the group
     */
    ERROR_PREVIOUS_ARENA_REQUIRED,

    /**
     * The message displayed if player teleportation failed for some reason
     */
    ERROR_ARENA_TELEPORT_FAILED,

    /**
     * The message displayed if the player tries to quit the arena while not in an arena
     */
    ERROR_NOT_IN_ARENA,

    /**
     * The message displayed if the player tries to join an arena while already playing
     *
     * <p>This should in theory be impossible, as players cannot use any commands except /miniGamesLeave while playing
     * in an arena.</p>
     */
    ERROR_ALREADY_PLAYING,

    /**
     * The message displayed if a player tries to join an arena with a passenger or riding a vehicle
     */
    ERROR_JOIN_IN_VEHICLE_OR_PASSENGER,

    /**
     * The message displayed if the player tries to change an unrecognized arena property
     */
    ERROR_UNKNOWN_PROPERTY,

    /**
     * The message displayed if the given input to /dEdit or /pEdit's value is invalid
     */
    ERROR_PROPERTY_INPUT_INVALID,

    /**
     * The message displayed if the first arena specified in /dgSwap or /pgSwap is invalid
     */
    ERROR_ARENA_1_NOT_FOUND,

    /**
     * The message displayed if the second arena specified in /dgSwap or /pgSwap is invalid
     */
    ERROR_ARENA_2_NOT_FOUND,

    /**
     * The message displayed if the two groups specified for /dgSwap or /pgSwap are in different arenas
     */
    ERROR_SWAP_DIFFERENT_GROUPS,

    /**
     * The message displayed if a player tries to use any command other than /mLeave while in an arena
     */
    ERROR_ILLEGAL_COMMAND,

    /**
     * The message displayed if the player is trying to join a parkour arena on hardcore which has no checkpoints
     */
    ERROR_HARDCORE_NO_CHECKPOINTS,

    /**
     * The message displayed if a user specifies an invalid material
     */
    ERROR_INVALID_MATERIAL,

    /**
     * The message displayed if a user specifies an invalid world
     */
    ERROR_INVALID_WORLD,

    /**
     * The message displayed if a user specifies an invalid number
     */
    ERROR_INVALID_NUMBER,

    /**
     * The message displayed if a user specifies an invalid command (for a command reward)
     */
    ERROR_INVALID_COMMAND_STRING,

    /**
     * The message displayed if a user specified an invalid reward type
     */
    ERROR_REWARD_TYPE_INVALID,

    /**
     * The message displayed if a user specified an invalid reward condition
     */
    ERROR_REWARD_CONDITION_INVALID,

    /* **************** *
     * Success messages *
     * **************** */

    /**
     * The message displayed if an arena's group has been changed
     */
    SUCCESS_ARENA_GROUP_UPDATED,

    /**
     * The message displayed if the MiniGames plugin is reloaded
     */
    SUCCESS_PLUGIN_RELOADED,

    /**
     * The message displayed if a new arena has been created
     */
    SUCCESS_ARENA_CREATED,

    /**
     * The message displayed if a player clears/wins an arena for the first time
     */
    SUCCESS_ARENA_FIRST_CLEAR,

    /**
     * The message displayed when a player wins an arena
     */
    SUCCESS_ARENA_WIN,

    /**
     * The message displayed when a player quits an arena
     */
    SUCCESS_ARENA_QUIT,

    /**
     * The message used to display the current value of an arena property
     */
    SUCCESS_CURRENT_VALUE,

    /**
     * The message used to announce that an arena property has been changed
     */
    SUCCESS_PROPERTY_CHANGED,

    /**
     * The message displayed when two arenas' order in a group have been swapped
     */
    SUCCESS_ARENAS_SWAPPED,

    /**
     * The message displayed when an arena has been removed
     */
    SUCCESS_ARENA_REMOVED,

    /**
     * The header displayed before listing all dropper arenas
     */
    SUCCESS_DROPPER_ARENAS_LIST,

    /**
     * The header displayed before listing all parkour arenas
     */
    SUCCESS_PARKOUR_ARENAS_LIST,

    /**
     * The message displayed when a player reaches a new checkpoint in a parkour arena
     */
    SUCCESS_CHECKPOINT_REACHED,

    /**
     * The header displayed before listing all arenas (stages) in a group
     */
    SUCCESS_GROUP_STAGES,

    /**
     * The message displayed when a new record has been achieved
     */
    SUCCESS_RECORD_ACHIEVED,

    /**
     * The partial message used to describe that the player achieved a world record
     */
    RECORD_ACHIEVED_GLOBAL,

    /**
     * The partial message used to describe that the player achieved a personal best record
     */
    RECORD_ACHIEVED_PERSONAL,

    /**
     * The message displayed when a player joins an arena
     */
    SUCCESS_ARENA_JOINED,

    /**
     * The message displayed when a player is rewarded with an item
     */
    SUCCESS_ITEM_REWARDED,

    /**
     * The message displayed when a player is rewarded with a permission
     */
    SUCCESS_PERMISSION_REWARDED,

    /**
     * The message displayed when a player is rewarded with a permission, for a specific world
     */
    SUCCESS_PERMISSION_REWARDED_WORLD,

    /**
     * The message displayed when a player is rewarded by a command being run
     */
    SUCCESS_COMMAND_REWARDED,

    /**
     * The message displayed when a player is rewarded with an amount of currency
     */
    SUCCESS_ECONOMY_REWARDED,

    /**
     * The message displayed when an arena reward has been successfully added
     */
    SUCCESS_REWARD_ADDED,

    /**
     * The message displayed when arena rewards have been cleared
     */
    SUCCESS_REWARDS_CLEARED,
    ;

    @Override
    public @NotNull TranslatableMessage[] getAllMessages() {
        return MiniGameMessage.values();
    }

}
