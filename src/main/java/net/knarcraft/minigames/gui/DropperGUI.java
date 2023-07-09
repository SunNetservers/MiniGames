package net.knarcraft.minigames.gui;

import net.knarcraft.minigames.MiniGames;
import org.bukkit.entity.Player;

/**
 * A GUI used in the dropper arena
 */
public class DropperGUI extends ArenaGUI {

    /**
     * Instantiates a new dropper gui
     *
     * @param player <p>The player the GUI is created for</p>
     */
    public DropperGUI(Player player) {
        super(9, "Dropper");
        if (MiniGames.getInstance().getPlayerVisibilityManager().isHidingPlayers(player)) {
            setItem(0, getTogglePlayersItemEnabled());
        } else {
            setItem(0, getTogglePlayersItemDisabled());
        }
        setItem(2, getLeaveItem());
        setItem(4, getRestartItem());

        setAnyClickAction(0, getTogglePlayersAction(MiniGames.getInstance().getDropperArenaPlayerRegistry()));
        setAnyClickAction(2, getLeaveAction());
        setAnyClickAction(4, getRestartAction());
    }

}
