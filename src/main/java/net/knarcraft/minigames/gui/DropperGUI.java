package net.knarcraft.minigames.gui;

import net.knarcraft.minigames.MiniGames;

/**
 * A GUI used in the dropper arena
 */
public class DropperGUI extends ArenaGUI {

    /**
     * Instantiates a new dropper gui
     */
    public DropperGUI() {
        super(9, "Dropper", MiniGames.getInstance().getDropperArenaPlayerRegistry());
        setItem(0, getTogglePlayersItem());
        setItem(2, getLeaveItem());

        setAnyClickAction(0, getTogglePlayersAction());
        setAnyClickAction(2, getLeaveAction());
    }

}
