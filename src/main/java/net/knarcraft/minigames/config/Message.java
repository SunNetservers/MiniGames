package net.knarcraft.minigames.config;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.container.PlaceholderContainer;
import net.knarcraft.minigames.util.ColorHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * A message which ca be displayed to the user
 */
public enum Message {

    /* ************** *
     * Error messages *
     * ************** */

    /**
     * The message displayed if saving arena groups fails
     */
    ERROR_CANNOT_SAVE_ARENA_GROUPS("&cUnable to save current arena groups! Data loss can occur!"),

    /**
     * The message displayed if an un-parse-able message is given by a user
     */
    ERROR_MATERIAL_NOT_PARSE_ABLE("&cUnable to parse material: {material}"),

    /**
     * The message displayed if a player tries to be teleported to/from an arena with a passenger
     */
    ERROR_TELEPORT_WITH_PASSENGER("&cYou cannot be teleported with a passenger!"),

    /**
     * The message displayed if a player tries to be teleported to/from an arena with a vehicle
     */
    ERROR_TELEPORT_IN_VEHICLE("&cYou cannot be teleported while in a vehicle"),

    /**
     * The message displayed if an arena cannot be loaded
     */
    ERROR_ARENA_NOT_LOADED("&cCould not load the arena at configuration section {section}. Please check " +
            "the {file} storage file for issues."),

    /**
     * The message displayed if an arena's data cannot be loaded
     */
    ERROR_ARENA_DATA_NOT_LOADED("&cUnable to load arena data for dropper arena: {arena}"),

    /**
     * The message displayed if the user specifies an unrecognized arena
     */
    ERROR_ARENA_NOT_FOUND("&cUnable to find the specified arena."),

    /**
     * The message displayed if the user specifies an unrecognized group
     */
    ERROR_GROUP_NOT_FOUND("&cUnable to find the specified group!"),

    /**
     * The message displayed if the console tries to execute a player-only command
     */
    ERROR_PLAYER_ONLY("&cThis command must be used by a player"),

    /**
     * The message displayed if the name of an arena is duplicated
     */
    ERROR_ARENA_NAME_COLLISION("&cThere already exists an arena with that name!"),

    /**
     * The message displayed if the player is required to win on the default difficulty first
     */
    ERROR_NORMAL_MODE_REQUIRED("&cYou must complete this arena in normal mode first!"),

    /**
     * The message displayed if the player is required to win on the default difficulty for all arenas in the group first
     */
    ERROR_GROUP_NORMAL_MODE_REQUIRED("&cYou have not yet beaten the default game-mode for all arenas in this group!"),

    /**
     * The message displayed if the player is required to beat the previous arena in the group
     */
    ERROR_PREVIOUS_ARENA_REQUIRED("&cYou have not yet beaten the previous arena!"),

    /**
     * The message displayed if player teleportation failed for some reason
     */
    ERROR_ARENA_TELEPORT_FAILED("&cUnable to teleport you to the arena."),

    /**
     * The message displayed if the player tries to quit the arena while not in an arena
     */
    ERROR_NOT_IN_ARENA("&cYou are not in a mini-games arena!"),

    /**
     * The message displayed if the player tries to join an arena while already playing
     *
     * <p>This should in theory be impossible, as players cannot use any commands except /miniGamesLeave while playing
     * in an arena.</p>
     */
    ERROR_ALREADY_PLAYING("&cYou are already playing a mini-game!"),

    /**
     * The message displayed if a player tries to join an arena with a passenger or riding a vehicle
     */
    ERROR_JOIN_IN_VEHICLE_OR_PASSENGER("&cYou cannot join an arena while inside a vehicle or carrying a passenger."),

    /**
     * The message displayed if the player tries to change an unrecognized arena property
     */
    ERROR_UNKNOWN_PROPERTY("&cUnknown property specified."),

    /**
     * The message displayed if the given input to /dEdit or /pEdit's value is invalid
     */
    ERROR_PROPERTY_INPUT_INVALID("&cUnable to change the property. Make sure your input is valid!"),

    /**
     * The message displayed if the first arena specified in /dgSwap or /pgSwap is invalid
     */
    ERROR_ARENA_1_NOT_FOUND("&cUnable to find the first specified arena."),

    /**
     * The message displayed if the second arena specified in /dgSwap or /pgSwap is invalid
     */
    ERROR_ARENA_2_NOT_FOUND("&cUnable to find the second specified dropper arena."),

    /**
     * The message displayed if the two groups specified for /dgSwap or /pgSwap are in different arenas
     */
    ERROR_SWAP_DIFFERENT_GROUPS("&cYou cannot swap arenas in different groups!"),

    /**
     * The message displayed if a player tries to use any command other than /mLeave while in an arena
     */
    ERROR_ILLEGAL_COMMAND("&cYou cannot use that command while in an arena!"),

    /* **************** *
     * Success messages *
     * **************** */

    /**
     * The message displayed if an arena's group has been changed
     */
    SUCCESS_ARENA_GROUP_UPDATED("&aThe arena's group has been updated"),

    /**
     * The message displayed if the MiniGames plugin is reloaded
     */
    SUCCESS_PLUGIN_RELOADED("&aPlugin reloaded!"),

    /**
     * The message displayed if a new arena has been created
     */
    SUCCESS_ARENA_CREATED("&aThe arena was successfully created!"),

    /**
     * The message displayed if a player clears/wins an arena for the first time
     */
    SUCCESS_ARENA_FIRST_CLEAR("&aYou cleared the arena!"),

    /**
     * The message displayed when a player wins an arena
     */
    SUCCESS_ARENA_WIN("&aYou won!"),

    /**
     * The message displayed when a player quits an arena
     */
    SUCCESS_ARENA_QUIT("&aYou quit the arena!"),

    /**
     * The message used to display the current value of an arena property
     */
    SUCCESS_CURRENT_VALUE("&aCurrent value of {property} is: {value}"),

    /**
     * The message used to announce that an arena property has been changed
     */
    SUCCESS_PROPERTY_CHANGED("&aProperty {property} successfully changed"),

    /**
     * The message displayed when two arenas' order in a group have been swapped
     */
    SUCCESS_ARENAS_SWAPPED("&aThe arenas have been swapped!"),

    /**
     * The message displayed when an arena has been removed
     */
    SUCCESS_ARENA_REMOVED("&aThe specified arena has been successfully removed"),

    /**
     * The header displayed before listing all dropper arenas
     */
    SUCCESS_DROPPER_ARENAS_LIST("&aDropper arenas:&r"),

    /**
     * The header displayed before listing all parkour arenas
     */
    SUCCESS_PARKOUR_ARENAS_LIST("&aParkour arenas:&r"),

    /**
     * The message displayed when a player reaches a new checkpoint in a parkour arena
     */
    SUCCESS_CHECKPOINT_REACHED("&aCheckpoint reached!"),

    /**
     * The header displayed before listing all arenas (stages) in a group
     */
    SUCCESS_GROUP_STAGES("&a{group}'s stages:&r"),

    /**
     * The message displayed when a new record has been achieved
     */
    SUCCESS_RECORD_ACHIEVED("&aYou just set a {recordInfo} on the {gameMode} game-mode!"),

    /**
     * The partial message used to describe that the player achieved a world record
     */
    RECORD_ACHIEVED_GLOBAL("new {recordType} record"),

    /**
     * The partial message used to describe that the player achieved a personal best record
     */
    RECORD_ACHIEVED_PERSONAL("personal {recordType} record"),

    /**
     * The message displayed when a player joins an arena
     */
    SUCCESS_ARENA_JOINED("&aYou joined the arena."),
    ;

    private final @NotNull String defaultMessage;

    /**
     * Instantiates a new message
     *
     * @param defaultMessage <p>The default value of the message</p>
     */
    Message(@NotNull String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    /**
     * Gets the message this enum represents
     *
     * @return <p>The formatted message</p>
     */
    public @NotNull String getMessage() {
        return formatMessage(this.defaultMessage);
    }

    /**
     * Gets the message this enum represents
     *
     * @param placeholder <p>The placeholder to replace</p>
     * @param replacement <p>The replacement to use</p>
     * @return <p>The formatted message</p>
     */
    public @NotNull String getMessage(@NotNull String placeholder, @NotNull String replacement) {
        return formatMessage(this.defaultMessage.replace(placeholder, replacement));
    }

    /**
     * Gets the message this enum represents, intended for display within another message
     *
     * @param placeholder <p>The placeholder to replace</p>
     * @param replacement <p>The replacement to use</p>
     * @return <p>The formatted message</p>
     */
    public @NotNull String getPartialMessage(@NotNull String placeholder, @NotNull String replacement) {
        return ColorHelper.translateAllColorCodes(this.defaultMessage.replace(placeholder, replacement));
    }

    /**
     * Gets the message this enum represents
     *
     * @param placeholders <p>The placeholder -> replacement map specifying necessary replacements</p>
     * @return <p>The formatted message</p>
     */
    public @NotNull String getMessage(@NotNull PlaceholderContainer placeholders) {
        String replaced = this.defaultMessage;
        for (Map.Entry<String, String> entry : placeholders.getPlaceholders().entrySet()) {
            replaced = replaced.replace(entry.getKey(), entry.getValue());
        }
        return formatMessage(replaced);
    }

    /**
     * Gets the formatted version of the given message
     *
     * @param message <p>The message to format</p>
     * @return <p>The formatted message</p>
     */
    private @NotNull String formatMessage(@NotNull String message) {
        String prefix = MiniGames.getInstance().getDescription().getPrefix();
        return ColorHelper.translateAllColorCodes("#546EED[&r&l" + prefix + "#546EED]&r " + message);
    }

}
