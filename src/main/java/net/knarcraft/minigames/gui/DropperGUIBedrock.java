package net.knarcraft.minigames.gui;

import net.knarcraft.minigames.MiniGames;
import org.bukkit.entity.Player;

/**
 * A GUI used in the dropper arena, altered for Bedrock players
 */
public class DropperGUIBedrock extends ArenaGUI {

    /**
     * Instantiates a new dropper gui
     *
     * @param player <p>The player the GUI is created for</p>
     */
    public DropperGUIBedrock(Player player) {
        super(27, "Dropper");
        if (MiniGames.getInstance().getPlayerVisibilityManager().isHidingPlayers(player)) {
            setItem(10, getTogglePlayersItemEnabledBedrock());
        } else {
            setItem(10, getTogglePlayersItemDisabled());
        }
        setItem(12, getLeaveItem());
        setItem(14, getRestartItemBedrock());

        setAnyClickAction(10, getTogglePlayersAction(MiniGames.getInstance().getDropperArenaPlayerRegistry(), 10));
        setAnyClickAction(12, getLeaveAction());
        setAnyClickAction(14, getRestartAction());
    }

}
