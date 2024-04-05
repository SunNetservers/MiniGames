package net.knarcraft.minigames.gui;

import net.knarcraft.minigames.MiniGames;
import org.bukkit.entity.Player;

/**
 * A GUI used in the parkour arena
 */
public class ParkourGUI extends ArenaGUI {

    /**
     * Instantiates a new parkour gui
     *
     * @param player <p>The player the GUI is created for</p>
     */
    public ParkourGUI(Player player) {
        super(9, "Parkour");
        if (MiniGames.getInstance().getPlayerVisibilityManager().isHidingPlayers(player)) {
            setItem(0, getTogglePlayersItemEnabled());
        } else {
            setItem(0, getTogglePlayersItemDisabled());
        }
        setItem(2, getGiveUpItem());
        setItem(4, getLeaveItem());
        setItem(6, getRestartItem());

        setAnyClickAction(0, getTogglePlayersAction(MiniGames.getInstance().getParkourArenaPlayerRegistry(), 0));
        setAnyClickAction(2, getGiveUpAction());
        setAnyClickAction(4, getLeaveAction());
        setAnyClickAction(6, getRestartAction());
    }

}
