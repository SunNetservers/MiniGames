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
            setItem(9, getTogglePlayersItemEnabledBedrock());
        } else {
            setItem(9, getTogglePlayersItemDisabled());
        }
        setItem(11, getLeaveItem());
        setItem(13, getRestartItemBedrock());

        setAnyClickAction(9, getTogglePlayersAction(MiniGames.getInstance().getDropperArenaPlayerRegistry(), 9));
        setAnyClickAction(11, getLeaveAction());
        setAnyClickAction(13, getRestartAction());
    }

}
