package net.knarcraft.minigames.util;

import net.knarcraft.knarlib.formatting.StringFormatter;
import net.knarcraft.knarlib.util.MaterialHelper;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.reward.CommandReward;
import net.knarcraft.minigames.arena.reward.EconomyReward;
import net.knarcraft.minigames.arena.reward.ItemReward;
import net.knarcraft.minigames.arena.reward.PermissionReward;
import net.knarcraft.minigames.arena.reward.Reward;
import net.knarcraft.minigames.arena.reward.RewardType;
import net.knarcraft.minigames.config.MiniGameMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

/**
 * A helopr class for getting the reward specified in user input
 */
public final class RewardHelper {

    private RewardHelper() {

    }

    /**
     * Grants the given rewards to the given player
     *
     * @param player  <p>The player to reward</p>
     * @param rewards <p>The rewards to give</p>
     */
    public static void grantRewards(@NotNull Player player, @NotNull Collection<Reward> rewards) {
        StringFormatter stringFormatter = MiniGames.getInstance().getStringFormatter();
        for (Reward reward : rewards) {
            boolean granted = reward.grant(player);
            if (granted) {
                stringFormatter.displaySuccessMessage(player, reward.getGrantMessage());
            }
        }
    }

    /**
     * Parses input describing a reward
     *
     * @param player         <p>The player that specified the reward</p>
     * @param typeString     <p>The string given to specify the reward type</p>
     * @param firstArgument  <p>The first reward argument given by the player, or null</p>
     * @param secondArgument <p>The second reward argument given by the player, or null</p>
     * @param allArguments   <p>A list of all the reward arguments, in case the reward is a command reward</p>
     * @return <p>The parsed reward, or null if some input was invalid</p>
     */
    public static @Nullable Reward parseRewardInput(@NotNull Player player,
                                                    @NotNull String typeString,
                                                    @Nullable String firstArgument,
                                                    @Nullable String secondArgument,
                                                    @NotNull String[] allArguments) {
        RewardType rewardType = RewardType.getFromString(typeString);
        if (rewardType == null) {
            MiniGames.getInstance().getStringFormatter().displayErrorMessage(player,
                    MiniGameMessage.ERROR_REWARD_TYPE_INVALID);
            return null;
        }

        if (rewardType != RewardType.ITEM && firstArgument == null) {
            return null;
        }

        try {
            return switch (rewardType) {
                case COMMAND -> new CommandReward(getArrayAsString(allArguments));
                case ECONOMY -> new EconomyReward(getDouble(firstArgument));
                case PERMISSION -> new PermissionReward(getWorld(secondArgument), firstArgument);
                case ITEM -> new ItemReward(getItem(player, firstArgument, secondArgument));
            };
        } catch (IllegalArgumentException exception) {
            MiniGames.getInstance().getStringFormatter().displayErrorMessage(player, exception.getMessage());
            return null;
        }
    }

    /**
     * Gets a double from the given input
     *
     * @param input <p>The input representing a double</p>
     * @return <p>The double specified</p>
     * @throws IllegalArgumentException <p>If the input is not a number or is not positive</p>
     */
    private static double getDouble(@NotNull String input) throws IllegalArgumentException {
        IllegalArgumentException invalidException = new IllegalArgumentException(
                MiniGames.getInstance().getTranslator().getTranslatedMessage(MiniGameMessage.ERROR_INVALID_NUMBER));
        try {
            double number = Double.parseDouble(input);
            if (number <= 0) {
                throw invalidException;
            }
            return number;
        } catch (NumberFormatException exception) {
            throw invalidException;
        }
    }

    /**
     * Gets the world specified in the given identifier
     *
     * @param worldIdentifier <p>A world UUID or name</p>
     * @return <p>The world, or null if no such world exists</p>
     */
    private static @Nullable World getWorld(@Nullable String worldIdentifier) {
        if (worldIdentifier == null || worldIdentifier.isBlank()) {
            return null;
        }
        World world;
        try {
            UUID worldId = UUID.fromString(worldIdentifier);
            world = Bukkit.getWorld(worldId);
        } catch (IllegalArgumentException exception) {
            world = Bukkit.getWorld(worldIdentifier);
        }
        if (world != null) {
            return world;
        } else {
            throw new IllegalArgumentException(MiniGames.getInstance().getTranslator().getTranslatedMessage(
                    MiniGameMessage.ERROR_INVALID_WORLD));
        }
    }

    /**
     * Gets an item stack according to the given input
     *
     * @param player    <p>The player that caused this method to execute</p>
     * @param argument1 <p>The first argument given by the player, or null</p>
     * @param argument2 <p>The second argument given by the player, or null</p>
     * @return <p>An item stack as described, or the player's held item</p>
     * @throws IllegalArgumentException <p>If an invalid material was specified</p>
     */
    private static @NotNull ItemStack getItem(@NotNull Player player, @Nullable String argument1,
                                              @Nullable String argument2) throws IllegalArgumentException {
        if (argument1 != null) {
            Material material = MaterialHelper.loadMaterialString(argument1, MiniGames.getInstance().getLogger());
            int amount;
            try {
                if (argument2 != null) {
                    amount = Integer.parseInt(argument2);
                } else {
                    amount = 1;
                }
            } catch (NumberFormatException exception) {
                amount = 1;
            }
            if (material == null || material.isAir()) {
                throw new IllegalArgumentException(MiniGames.getInstance().getTranslator().getTranslatedMessage(
                        MiniGameMessage.ERROR_INVALID_MATERIAL));
            }
            return new ItemStack(material, amount);
        } else {
            ItemStack inHand = player.getInventory().getItemInMainHand();
            if (!inHand.getType().isAir()) {
                return inHand;
            } else {
                throw new IllegalArgumentException(MiniGames.getInstance().getTranslator().getTranslatedMessage(
                        MiniGameMessage.ERROR_INVALID_MATERIAL));
            }
        }
    }

    /**
     * Gets the string array as a space separated string
     *
     * @param array <p>The array to get as a string</p>
     * @return <p>The array as a string</p>
     */
    private static String getArrayAsString(@NotNull String[] array) {
        String output = String.join(" ", array);
        if (output.isBlank()) {
            throw new IllegalArgumentException(MiniGames.getInstance().getTranslator().getTranslatedMessage(
                    MiniGameMessage.ERROR_INVALID_COMMAND_STRING));
        } else {
            return output;
        }
    }

}
