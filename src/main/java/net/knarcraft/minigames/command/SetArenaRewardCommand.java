package net.knarcraft.minigames.command;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.reward.Reward;
import net.knarcraft.minigames.arena.reward.RewardCondition;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.util.RewardHelper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * The command used for setting arena rewards
 */
public class SetArenaRewardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] arguments) {
        if (!(commandSender instanceof Player player)) {
            MiniGames.getInstance().getStringFormatter().displayErrorMessage(commandSender,
                    MiniGameMessage.ERROR_PLAYER_ONLY);
            return false;
        }

        if (arguments.length < 4) {
            return false;
        }
        
        /*
        
        /MiniGamesReward add dropper <name> <condition> <type> [data]
        /MiniGamesReward add parkour <name> <condition> <type> [data]
        /MiniGamesReward clear dropper <name> <condition>
        /MiniGamesReward clear parkour <name> <condition>
        
         */

        Arena arena = null;
        if (arguments[1].equalsIgnoreCase("dropper")) {
            arena = MiniGames.getInstance().getDropperArenaHandler().getArena(arguments[2]);
        } else if (arguments[1].equalsIgnoreCase("parkour")) {
            arena = MiniGames.getInstance().getParkourArenaHandler().getArena(arguments[2]);
        }
        if (arena == null) {
            MiniGames.getInstance().getStringFormatter().displayErrorMessage(commandSender,
                    MiniGameMessage.ERROR_ARENA_NOT_FOUND);
            return false;
        }

        RewardCondition condition = RewardCondition.getFromString(arguments[3]);
        if (condition == null) {
            MiniGames.getInstance().getStringFormatter().displayErrorMessage(player,
                    MiniGameMessage.ERROR_REWARD_CONDITION_INVALID);
            return false;
        }

        if (arguments[0].equalsIgnoreCase("clear")) {
            arena.clearRewards(condition);
            MiniGames.getInstance().getStringFormatter().displaySuccessMessage(player,
                    MiniGameMessage.SUCCESS_REWARDS_CLEARED);
            return true;
        }

        if (!arguments[0].equalsIgnoreCase("add") || arguments.length < 5) {
            return false;
        }

        String firstArgument = arguments.length > 5 ? arguments[5] : null;
        String secondArgument = arguments.length > 6 ? arguments[6] : null;

        Reward reward = RewardHelper.parseRewardInput(player, arguments[4], firstArgument, secondArgument,
                Arrays.copyOfRange(arguments, 5, arguments.length));

        if (reward != null) {
            arena.addReward(condition, reward);
            MiniGames.getInstance().getStringFormatter().displaySuccessMessage(player,
                    MiniGameMessage.SUCCESS_REWARD_ADDED);
            return true;
        } else {
            return false;
        }
    }

}
