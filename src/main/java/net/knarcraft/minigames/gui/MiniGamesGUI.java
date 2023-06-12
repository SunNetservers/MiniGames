package net.knarcraft.minigames.gui;

import net.knarcraft.minigames.MiniGames;
import org.bukkit.entity.Player;

/**
 * A GUI used outside arenas
 */
public class MiniGamesGUI extends ArenaGUI {

    /**
     * Instantiates a new mini games gui
     *
     * @param player <p>The player the GUI is created for</p>
     */
    public MiniGamesGUI(Player player) {
        super(9, "MiniGames");
        if (MiniGames.getInstance().getPlayerVisibilityManager().isHidingPlayers(player)) {
            setItem(0, getTogglePlayersItemEnabled());
        } else {
            setItem(0, getTogglePlayersItemDisabled());
        }

        setAnyClickAction(0, getTogglePlayersAction(null));
    }

}
