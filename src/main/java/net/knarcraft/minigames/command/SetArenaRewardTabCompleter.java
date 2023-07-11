package net.knarcraft.minigames.command;

import net.knarcraft.knarlib.util.MaterialHelper;
import net.knarcraft.knarlib.util.TabCompletionHelper;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.reward.RewardCondition;
import net.knarcraft.minigames.arena.reward.RewardType;
import net.knarcraft.minigames.util.TabCompleteHelper;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static net.knarcraft.knarlib.util.TabCompletionHelper.filterMatchingContains;
import static net.knarcraft.knarlib.util.TabCompletionHelper.filterMatchingStartsWith;

/**
 * The tab completer for the reward setting command
 */
public class SetArenaRewardTabCompleter implements TabCompleter {

    private static final List<String> materials = getMaterials();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                                      @NotNull String[] arguments) {
        if (arguments.length == 1) {
            // The first argument is either clear or add
            return TabCompletionHelper.filterMatchingStartsWith(Arrays.asList("add", "clear"), arguments[0]);
        }
        if (arguments.length >= 2) {
            // If the first argument is invalid, stop further tab completion
            if (!arguments[0].equalsIgnoreCase("add") && !arguments[0].equalsIgnoreCase("clear")) {
                return new ArrayList<>();
            }
        }
        if (arguments.length == 2) {
            // The second argument is the type of arena to change rewards for
            return TabCompletionHelper.filterMatchingStartsWith(Arrays.asList("dropper", "parkour"), arguments[1]);
        } else if (arguments.length == 3) {
            // The third argument is the name of the arena to change rewards for
            if (arguments[1].equalsIgnoreCase("dropper")) {
                return filterMatchingContains(TabCompleteHelper.getDropperArenas(), arguments[2]);
            } else if (arguments[1].equalsIgnoreCase("parkour")) {
                return filterMatchingContains(TabCompleteHelper.getParkourArenas(), arguments[2]);
            }
        }
        if (arguments.length >= 4) {
            // Make sure a valid dropper or arena name has been given
            Arena arena = null;
            if (arguments[1].equalsIgnoreCase("dropper")) {
                arena = MiniGames.getInstance().getDropperArenaHandler().getArena(arguments[2]);
            } else if (arguments[1].equalsIgnoreCase("parkour")) {
                arena = MiniGames.getInstance().getParkourArenaHandler().getArena(arguments[2]);
            }
            if (arena == null) {
                return new ArrayList<>();
            }
        }
        if (arguments.length == 4) {
            // The fourth argument is the condition to change the reward for
            return filterMatchingContains(getRewardConditions(), arguments[3]);
        }
        if (arguments.length >= 5) {
            // If the condition is invalid, or it's the clear action, stop tab-completion
            if (RewardCondition.getFromString(arguments[3]) == null ||
                    arguments[0].equalsIgnoreCase("clear")) {
                return new ArrayList<>();
            }
        }
        if (arguments.length == 5) {
            // The fifth argument is the type of reward to grant
            return filterMatchingContains(getRewardTypes(), arguments[4]);
        }
        if (arguments.length >= 6) {
            // Make sure a valid reward type has been given
            RewardType rewardType = RewardType.getFromString(arguments[4]);
            if (rewardType == null) {
                return new ArrayList<>();
            }

            if (arguments.length == 6) {
                return switch (rewardType) {
                    case ITEM -> filterMatchingContains(materials, arguments[5]);
                    case PERMISSION -> TabCompleteHelper.tabCompletePermission(arguments[5]);
                    case ECONOMY -> filterMatchingStartsWith(Arrays.asList("1", "5", "10", "25", "50"), arguments[5]);
                    case COMMAND -> filterMatchingStartsWith(getCommands(), arguments[5]);
                };
            }

            if (rewardType == RewardType.ITEM && arguments.length == 7) {
                // If a valid item material has been given, give potential amounts
                if (MaterialHelper.loadMaterialString(arguments[5], MiniGames.getInstance().getLogger()) == null) {
                    return new ArrayList<>();
                }
                return Arrays.asList("1", "5", "10", "16", "32", "48", "64");
            }
        }
        return new ArrayList<>();
    }

    /**
     * Gets example command rewards
     *
     * @return <p>Example command rewards</p>
     */
    private static List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        commands.add("f powerboost player add %player% 1");
        commands.add("minecraft:xp give (player_name) 1000");
        return commands;
    }

    /**
     * Gets all materials grant-able as item rewards
     *
     * @return <p>All grant-able materials</p>
     */
    private static List<String> getMaterials() {
        List<String> materials = new ArrayList<>();
        Set<Material> invalid = EnumSet.of(Material.WATER, Material.LAVA, Material.POWDER_SNOW);
        for (Material material : Material.values()) {
            if (material.isAir() || invalid.contains(material) || (material.isBlock() &&
                    (material.getHardness() == -1 || material.getHardness() == Double.MAX_VALUE))) {
                continue;
            }
            materials.add(material.name());
        }
        return materials;
    }

    /**
     * Gets a list of all reward types
     *
     * @return <p>All reward types</p>
     */
    private List<String> getRewardTypes() {
        List<String> types = new ArrayList<>();
        for (RewardType rewardType : RewardType.values()) {
            types.add(rewardType.name());
        }
        return types;
    }

    /**
     * Gets a list of all reward conditions
     *
     * @return <p>All reward conditions</p>
     */
    private List<String> getRewardConditions() {
        List<String> conditions = new ArrayList<>();
        for (RewardCondition rewardCondition : RewardCondition.values()) {
            conditions.add(rewardCondition.name());
        }
        return conditions;
    }

}
