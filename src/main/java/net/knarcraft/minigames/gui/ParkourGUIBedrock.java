package net.knarcraft.minigames.gui;

import net.knarcraft.minigames.MiniGames;
import org.bukkit.entity.Player;

/**
 * A GUI used in the parkour arena, altered for Bedrock players
 */
public class ParkourGUIBedrock extends ArenaGUI {

    /**
     * Instantiates a new parkour gui
     *
     * @param player <p>The player the GUI is created for</p>
     */
    public ParkourGUIBedrock(Player player) {
        super(27, "Parkour");
        if (MiniGames.getInstance().getPlayerVisibilityManager().isHidingPlayers(player)) {
            setItem(10, getTogglePlayersItemEnabled(player));
        } else {
            setItem(10, getTogglePlayersItemDisabled());
        }
        setItem(12, getGiveUpItem());
        setItem(14, getLeaveItem());
        setItem(16, getRestartItemBedrock());

        setAnyClickAction(10, getTogglePlayersAction(MiniGames.getInstance().getParkourArenaPlayerRegistry(), 10));
        setAnyClickAction(12, getGiveUpAction());
        setAnyClickAction(14, getLeaveAction());
        setAnyClickAction(16, getRestartAction());
    }

}
