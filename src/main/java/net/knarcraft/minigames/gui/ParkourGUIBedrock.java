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
            setItem(9, getTogglePlayersItemEnabledBedrock());
        } else {
            setItem(9, getTogglePlayersItemDisabled());
        }
        setItem(11, getGiveUpItem());
        setItem(13, getLeaveItem());
        setItem(15, getRestartItemBedrock());

        setAnyClickAction(9, getTogglePlayersAction(MiniGames.getInstance().getParkourArenaPlayerRegistry(), 9));
        setAnyClickAction(11, getGiveUpAction());
        setAnyClickAction(13, getLeaveAction());
        setAnyClickAction(15, getRestartAction());
    }

}
