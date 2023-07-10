package net.knarcraft.minigames.util;

import net.knarcraft.minigames.arena.Arena;
import net.knarcraft.minigames.arena.reward.Reward;
import net.knarcraft.minigames.arena.reward.RewardCondition;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A helper class for loading and storing rewards
 */
public class RewardStorageHelper {

    /**
     * Loads the rewards contained at the given path
     *
     * @param configurationSection <p>The configuration section containing the reward</p>
     * @param key                  <p>The section key to search</p>
     * @return <p>The loaded rewards</p>
     */
    public static Map<RewardCondition, Set<Reward>> loadRewards(@NotNull ConfigurationSection configurationSection,
                                                                @NotNull String key) {
        Map<RewardCondition, Set<Reward>> rewards = new HashMap<>();
        if (!configurationSection.contains(key)) {
            return rewards;
        }
        for (RewardCondition condition : RewardCondition.values()) {
            String section = key + "." + condition.name();
            if (!configurationSection.contains(section)) {
                continue;
            }
            Set<Reward> rewardSet = new HashSet<>();
            List<?> rewardList = configurationSection.getList(section, new ArrayList<>());
            for (Object object : rewardList) {
                if (object instanceof Reward reward) {
                    rewardSet.add(reward);
                }
            }
            rewards.put(condition, rewardSet);
        }

        return rewards;
    }

    /**
     * Saves rewards for the given arena
     *
     * @param arena                <p>The arena to save rewards for</p>
     * @param configurationSection <p>The configuration section to save the rewards at</p>
     * @param key                  <p>The section key to save at</p>
     */
    public static void saveRewards(@NotNull Arena arena, @NotNull ConfigurationSection configurationSection,
                                   @NotNull String key) {
        for (RewardCondition condition : RewardCondition.values()) {
            configurationSection.set(key + "." + condition.name(),
                    new ArrayList<>(arena.getRewards(condition)));
        }
    }

}
