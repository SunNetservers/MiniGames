package net.knarcraft.minigames.gui;

/**
 * A GUI used in the dropper arena
 */
public class DropperGUI extends ArenaGUI {

    /**
     * Instantiates a new dropper gui
     */
    public DropperGUI() {
        super(9, "Dropper");
        setItem(0, getTogglePlayersItem());
        setItem(2, getLeaveItem());

        setAnyClickAction(2, getLeaveAction());
    }

}
