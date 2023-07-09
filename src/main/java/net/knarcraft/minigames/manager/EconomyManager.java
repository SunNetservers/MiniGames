package net.knarcraft.minigames.manager;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;

/**
 * A manager that performs all Economy tasks
 */
public final class EconomyManager {

    private static Economy economy;

    private EconomyManager() {

    }

    /**
     * Initializes the economy manager
     *
     * @param economy <p>The economy object to use for everything economy-related</p>
     */
    public static void initialize(Economy economy) {
        EconomyManager.economy = economy;
    }

    /**
     * Checks whether the economy manager has been initialized
     *
     * @return <p>True if the economy manager has been initialized</p>
     */
    public static boolean isInitialized() {
        return EconomyManager.economy != null;
    }

    /**
     * Formats the given amount of currency according to the economy plugin's format
     *
     * @param amount <p>The amount of currency to format</p>
     * @return <p>The formatted string</p>
     */
    public static String format(double amount) {
        return economy.format(amount);
    }

    /**
     * Deposits a given sum into the given player's account
     *
     * @param player <p>The player to deposit money to</p>
     * @param sum    <p>The amount of money to deposit</p>
     */
    public static void deposit(OfflinePlayer player, double sum) {
        economy.depositPlayer(player, sum);
    }

}
