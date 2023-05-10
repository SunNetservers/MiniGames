package net.knarcraft.minigames.gui;

import net.knarcraft.knargui.AbstractGUI;
import net.knarcraft.knargui.GUIAction;
import net.knarcraft.knargui.item.GUIItemFactory;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaPlayerRegistry;
import net.knarcraft.minigames.arena.ArenaSession;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic GUI for all arenas
 */
public abstract class ArenaGUI extends AbstractGUI {

    protected final ArenaPlayerRegistry<?> playerRegistry;

    /**
     * Instantiates a new arena gui
     *
     * @param inventorySize  <p>The size of the GUI's inventory</p>
     * @param inventoryName  <p>The name of the inventory</p>
     * @param playerRegistry <p>The player registry used for this GUI</p>
     */
    public ArenaGUI(int inventorySize, String inventoryName, ArenaPlayerRegistry<?> playerRegistry) {
        super(inventorySize, inventoryName, null);
        this.playerRegistry = playerRegistry;
    }

    /**
     * Gets an item describing player visibility toggling
     *
     * @return <p>A player toggle item</p>
     */
    protected ItemStack getTogglePlayersItem() {
        GUIItemFactory togglePlayersItemFactory = new GUIItemFactory(Material.PLAYER_HEAD);
        List<String> loreLines = getLoreLines();
        loreLines.add(ChatColor.GRAY + "Use this item to toggle the visibility");
        loreLines.add(ChatColor.GRAY + "of other players");
        togglePlayersItemFactory.setName(ChatColor.BLUE + "Toggle Players");
        togglePlayersItemFactory.setLore(loreLines);
        return togglePlayersItemFactory.build();
    }

    /**
     * Gets an item describing a leave arena action
     *
     * @return <p>A leave item</p>
     */
    protected ItemStack getLeaveItem() {
        GUIItemFactory leaveItemFactory = new GUIItemFactory(Material.BARRIER);
        List<String> loreLines = getLoreLines();
        loreLines.add(ChatColor.GRAY + "Use this item to leave the arena");
        leaveItemFactory.setName(ChatColor.DARK_RED + "Leave");
        leaveItemFactory.setLore(loreLines);
        return leaveItemFactory.build();
    }

    /**
     * Gets an arraylist with one blank line lore-lines can be added to
     *
     * @return <p>An arraylist with one blank line</p>
     */
    protected List<String> getLoreLines() {
        List<String> loreLines = new ArrayList<>();
        loreLines.add("");
        return loreLines;
    }

    /**
     * Sets a click action for both right-click and left-click
     *
     * @param inventorySlot <p>The inventory slot the action should be added to</p>
     * @param action        <p>The action to register</p>
     */
    protected void setAnyClickAction(int inventorySlot, GUIAction action) {
        setClickAction(inventorySlot, ClickType.LEFT, action);
        setClickAction(inventorySlot, ClickType.RIGHT, action);
    }

    /**
     * Gets the action to run when triggering the leave item
     *
     * @return <p>The leave action</p>
     */
    protected GUIAction getLeaveAction() {
        return (player) -> {
            ArenaSession session = MiniGames.getInstance().getSession(player.getUniqueId());
            if (session != null) {
                session.triggerQuit(false, true);
            }
        };
    }

    /**
     * Gets the action to run when triggering the toggle players action
     *
     * @return <p>The action for triggering player visibility</p>
     */
    protected GUIAction getTogglePlayersAction() {
        return (player) -> MiniGames.getInstance().getPlayerVisibilityManager().toggleHidePlayers(playerRegistry,
                player);
    }

}
