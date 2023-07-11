package net.knarcraft.minigames.arena.reward;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.config.MiniGameMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A reward that executes a specified command when it's granted
 */
public class CommandReward implements Reward {

    private final @NotNull String command;

    /**
     * Instantiates a new command reward
     *
     * @param command <p>The command to execute when granting this reward</p>
     */
    public CommandReward(@NotNull String command) {
        this.command = command;
    }

    @Override
    public boolean grant(@NotNull Player player) {
        return Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), replaceNamePlaceholder(player, command));
    }

    @Override
    public @NotNull String getGrantMessage() {
        return MiniGames.getInstance().getStringFormatter().replacePlaceholder(
                MiniGameMessage.SUCCESS_COMMAND_REWARDED, "{command}", command);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("command", command);
        return data;
    }

    /**
     * Replaces the name placeholder in the given input with the given player's name
     *
     * @param player <p>The player whose name should be used</p>
     * @param input  <p>The input containing a name placeholder</p>
     * @return <p>The input with the placeholder replaced</p>
     */
    private String replaceNamePlaceholder(@NotNull Player player, @NotNull String input) {
        return input.replaceAll("[<%(\\[{]player[_\\-]?(name)?[>%)\\]}]", player.getName());
    }

    /**
     * Deserializes the command reward defined in the given data
     *
     * @param data <p>The data to deserialize from</p>
     * @return <p>The deserialized data</p>
     */
    @SuppressWarnings("unused")
    public static CommandReward deserialize(Map<String, Object> data) {
        return new CommandReward((String) data.get("command"));
    }

}
