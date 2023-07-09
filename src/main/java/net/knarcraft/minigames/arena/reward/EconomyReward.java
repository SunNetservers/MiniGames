package net.knarcraft.minigames.arena.reward;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.manager.EconomyManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * A reward that gives an amount of currency when it's granted
 */
public class EconomyReward implements Reward {

    private final double amount;

    /**
     * Instantiates a new economy reward
     *
     * @param amount <p>The amount of currency granted</p>
     */
    public EconomyReward(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean grant(@NotNull Player player) {
        if (!EconomyManager.isInitialized()) {
            MiniGames.log(Level.SEVERE, "An economy reward has been set, but no Vault-compatible economy" +
                    " plugin has been initialized.");
            return false;
        }
        EconomyManager.deposit(player, amount);
        return true;
    }

    @Override
    public @NotNull String getGrantMessage() {
        return MiniGames.getInstance().getStringFormatter().replacePlaceholder(MiniGameMessage.SUCCESS_ECONOMY_REWARDED,
                "{currency}", EconomyManager.format(amount));
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("amount", amount);
        return data;
    }

    /**
     * Deserializes the economy reward defined in the given data
     *
     * @param data <p>The data to deserialize from</p>
     * @return <p>The deserialized data</p>
     */
    @SuppressWarnings("unused")
    public static EconomyReward deserialize(Map<String, Object> data) {
        return new EconomyReward((Double) data.get("amount"));
    }

}
