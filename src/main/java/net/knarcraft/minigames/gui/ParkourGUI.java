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
            setItem(1, getTogglePlayersItemEnabled(player));
        } else {
            setItem(1, getTogglePlayersItemDisabled());
        }
        setItem(3, getGiveUpItem());
        setItem(5, getLeaveItem());
        setItem(7, getRestartItemJava());

        setAnyClickAction(1, getTogglePlayersAction(MiniGames.getInstance().getParkourArenaPlayerRegistry(), 1));
        setAnyClickAction(3, getGiveUpAction());
        setAnyClickAction(5, getLeaveAction());
        setAnyClickAction(7, getRestartAction());
    }

}
