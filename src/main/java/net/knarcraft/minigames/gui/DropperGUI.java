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
            setItem(0, getTogglePlayersItemEnabled(player));
        } else {
            setItem(0, getTogglePlayersItemDisabled());
        }
        setItem(2, getLeaveItem());
        setItem(4, getRestartItemJava());

        setAnyClickAction(0, getTogglePlayersAction(MiniGames.getInstance().getDropperArenaPlayerRegistry(), 0));
        setAnyClickAction(2, getLeaveAction());
        setAnyClickAction(4, getRestartAction());
    }

}
