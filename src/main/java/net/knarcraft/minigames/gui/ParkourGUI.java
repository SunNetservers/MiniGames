package net.knarcraft.minigames.gui;

import net.knarcraft.knargui.GUIAction;
import net.knarcraft.knargui.item.GUIItemFactory;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.arena.parkour.ParkourArenaSession;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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
        super(9, "Parkour", MiniGames.getInstance().getParkourArenaPlayerRegistry());
        if (MiniGames.getInstance().getPlayerVisibilityManager().isHidingPlayers(player)) {
            setItem(0, getTogglePlayersItemEnabled());
        } else {
            setItem(0, getTogglePlayersItemDisabled());
        }
        setItem(2, getGiveUpItem());
        setItem(4, getLeaveItem());

        setAnyClickAction(0, getTogglePlayersAction());
        setAnyClickAction(2, getGiveUpAction());
        setAnyClickAction(4, getLeaveAction());
    }

    /**
     * Gets an item describing a give up action
     *
     * @return <p>A give up item</p>
     */
    private ItemStack getGiveUpItem() {
        GUIItemFactory giveUpItemFactory = new GUIItemFactory(Material.RESPAWN_ANCHOR);
        List<String> loreLines = getLoreLines();
        loreLines.add(ChatColor.GRAY + "Use this item to give up");
        loreLines.add(ChatColor.GRAY + "and go to the last checkpoint");
        giveUpItemFactory.setName(ChatColor.RED + "Give up");
        giveUpItemFactory.setLore(loreLines);
        return giveUpItemFactory.build();
    }

    /**
     * Gets the action to run when triggering the give up item
     *
     * @return <p>The give up action</p>
     */
    private GUIAction getGiveUpAction() {
        return (player) -> {
            ArenaSession session = MiniGames.getInstance().getSession(player.getUniqueId());
            if (session instanceof ParkourArenaSession) {
                session.triggerLoss();
            }
        };
    }

}
