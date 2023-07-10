package net.knarcraft.minigames.arena.reward;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The condition for granting a reward
 */
public enum RewardCondition implements ConfigurationSerializable {

    /**
     * The reward is granted each time the player wins/clears the arena
     */
    WIN,

    /**
     * The reward is granted the first time the player wins/clears the arena
     */
    FIRST_WIN,

    /**
     * The reward is granted if the player beats their personal least deaths record
     */
    PERSONAL_DEATH_RECORD,

    /**
     * The reward is granted if the player beats their personal least time record
     */
    PERSONAL_TIME_RECORD,

    /**
     * The reward is granted if the player beats the global least deaths record
     */
    GLOBAL_DEATH_RECORD,

    /**
     * The reward is granted if the player beats the global least time record
     */
    GLOBAL_TIME_RECORD,
    ;

    /**
     * Gets a reward condition from the given string
     *
     * @param condition <p>The string specifying a reward condition</p>
     * @return <p>The matching reward condition, or null if not found</p>
     */
    public static @Nullable RewardCondition getFromString(@NotNull String condition) {
        for (RewardCondition rewardCondition : RewardCondition.values()) {
            if (rewardCondition.name().equalsIgnoreCase(condition.replace("-", "_"))) {
                return rewardCondition;
            }
        }

        return null;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("condition", this.name());
        return data;
    }

    /**
     * Deserializes a reward condition from the given data
     *
     * @param data <p>The data to deserialize</p>
     * @return <p>The deserialized reward condition</p>
     */
    @SuppressWarnings({"unused"})
    public static @NotNull RewardCondition deserialize(@NotNull Map<String, Object> data) {
        RewardCondition rewardCondition = getFromString(String.valueOf(data.get("condition")));
        return Objects.requireNonNullElse(rewardCondition, RewardCondition.FIRST_WIN);
    }

}
