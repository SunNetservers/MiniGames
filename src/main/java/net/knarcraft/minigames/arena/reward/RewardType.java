package net.knarcraft.minigames.arena.reward;

import org.jetbrains.annotations.NotNull;

/**
 * The type of a specific reward
 */
public enum RewardType {

    /**
     * A command reward
     */
    COMMAND,

    /**
     * An economy reward
     */
    ECONOMY,

    /**
     * An item reward
     */
    ITEM,

    /**
     * A permission reward
     */
    PERMISSION,
    ;

    /**
     * Gets a reward type from the given string
     *
     * @param condition <p>The string specifying a reward type</p>
     * @return <p>The matching reward type, or null if not found</p>
     */
    public static RewardType getFromString(@NotNull String condition) {
        for (RewardType rewardType : RewardType.values()) {
            if (rewardType.name().equalsIgnoreCase(condition.replace("-", "_"))) {
                return rewardType;
            }
        }

        return null;
    }

}
