package net.knarcraft.minigames.arena.reward;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The type of a specific reward
 */
public enum RewardType {

    /**
     * A command reward
     */
    COMMAND(CommandReward.class),

    /**
     * An economy reward
     */
    ECONOMY(EconomyReward.class),

    /**
     * An item reward
     */
    ITEM(ItemReward.class),

    /**
     * A permission reward
     */
    PERMISSION(PermissionReward.class),
    ;

    private final Class<?> classType;

    RewardType(Class<?> classType) {
        this.classType = classType;
    }

    /**
     * Gets the type of reward the given object represents
     *
     * @param object <p>A reward object</p>
     * @return <p>The reward type of the given object, or null if not recognized</p>
     */
    public static <K extends Reward> @Nullable RewardType getFromObject(@NotNull K object) {
        for (RewardType rewardType : RewardType.values()) {
            if (object.getClass() == rewardType.classType) {
                return rewardType;
            }
        }

        return null;
    }

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
