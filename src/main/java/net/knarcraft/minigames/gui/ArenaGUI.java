package net.knarcraft.minigames.gui;

import net.knarcraft.knargui.AbstractGUI;
import net.knarcraft.knargui.GUIAction;
import net.knarcraft.knargui.item.GUIItemFactory;
import net.knarcraft.knargui.item.PlayerHeadGUIItemFactory;
import net.knarcraft.knargui.item.SimpleGUIItemFactory;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaPlayerRegistry;
import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.arena.PlayerVisibilityManager;
import net.knarcraft.minigames.arena.parkour.ParkourArenaSession;
import net.knarcraft.minigames.util.GeyserHelper;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic GUI for all arenas
 */
public abstract class ArenaGUI extends AbstractGUI {

    /**
     * Instantiates a new arena gui
     *
     * @param inventorySize <p>The size of the GUI's inventory</p>
     * @param inventoryName <p>The name of the inventory</p>
     */
    public ArenaGUI(int inventorySize, @NotNull String inventoryName) {
        super(inventorySize, inventoryName, null);
    }

    /**
     * Gets an item describing a retry arena action (for Java edition)
     *
     * @return <p>An arena restart item</p>
     */
    @NotNull
    protected ItemStack getRestartItemJava() {
        PlayerHeadGUIItemFactory restartItemFactory = new PlayerHeadGUIItemFactory();
        restartItemFactory.useSkin("e23b225ed0443c4eec7cf30a034490485904e6eb3d53ef2ab9e39c73bdf22c30");
        return setRestartItemData(restartItemFactory);
    }

    /**
     * Gets an item describing a retry arena action (for Bedrock edition)
     *
     * @return <p>An arena restart item</p>
     */
    @NotNull
    protected ItemStack getRestartItemBedrock() {
        return setRestartItemData(new SimpleGUIItemFactory(Material.MAGENTA_GLAZED_TERRACOTTA));
    }

    /**
     * Sets the lore and name for a retry item, and returns the finished item
     *
     * @param guiItemFactory <p>The factory to apply the data to</p>
     * @return <p>The finished item, with the data applied</p>
     */
    @NotNull
    protected ItemStack setRestartItemData(@NotNull GUIItemFactory guiItemFactory) {
        List<String> loreLines = getLoreLines();
        loreLines.add(ChatColor.GRAY + "Use this item to retry the arena");
        loreLines.add(ChatColor.GRAY + "(deaths and time is reset to 0)");
        guiItemFactory.setName(ChatColor.BLUE + "Retry arena");
        guiItemFactory.setLore(loreLines);
        return guiItemFactory.build();
    }

    /**
     * Gets an item describing player visibility toggling
     *
     * @return <p>A player toggle item</p>
     */
    @NotNull
    protected ItemStack getTogglePlayersItemDisabled() {
        GUIItemFactory togglePlayersItemFactory = new SimpleGUIItemFactory(Material.PLAYER_HEAD);
        List<String> loreLines = getLoreLines();
        loreLines.add(ChatColor.GRAY + "Use this item to disable the visibility");
        loreLines.add(ChatColor.GRAY + "of other players");
        togglePlayersItemFactory.setName(ChatColor.BLUE + "Disable Players");
        togglePlayersItemFactory.setLore(loreLines);
        return togglePlayersItemFactory.build();
    }

    /**
     * Gets an item describing player visibility toggling
     *
     * @param player <p>The player to get the item for</p>
     * @return <p>A player toggle item</p>
     */
    @NotNull
    protected ItemStack getTogglePlayersItemEnabled(@NotNull Player player) {
        if (GeyserHelper.isGeyserPlayer(player)) {
            return getTogglePlayersItemEnabledBedrock();
        } else {
            return getTogglePlayersItemEnabledJava();
        }
    }

    /**
     * Gets an item describing player visibility toggling
     *
     * @return <p>A player toggle item</p>
     */
    @NotNull
    protected ItemStack getTogglePlayersItemEnabledJava() {
        PlayerHeadGUIItemFactory togglePlayersItemFactory = new PlayerHeadGUIItemFactory();
        togglePlayersItemFactory.useSkin("c10591e6909e6a281b371836e462d67a2c78fa0952e910f32b41a26c48c1757c");
        return setTogglePlayersItemData(togglePlayersItemFactory);
    }

    /**
     * Gets an item describing player visibility toggling
     *
     * @return <p>A player toggle item</p>
     */
    @NotNull
    protected ItemStack getTogglePlayersItemEnabledBedrock() {
        return setTogglePlayersItemData(new SimpleGUIItemFactory(Material.SKELETON_SKULL));
    }

    /**
     * Sets the lore and name for a toggle players item, and returns the finished item
     *
     * @param itemFactory <p>The factory to apply the data to</p>
     * @return <p>The finished item, with the data applied</p>
     */
    @NotNull
    protected ItemStack setTogglePlayersItemData(@NotNull GUIItemFactory itemFactory) {
        List<String> loreLines = getLoreLines();
        loreLines.add(ChatColor.GRAY + "Use this item to enable the visibility");
        loreLines.add(ChatColor.GRAY + "of other players");
        itemFactory.setName(ChatColor.BLUE + "Enable Players");
        itemFactory.setLore(loreLines);
        return itemFactory.build();
    }

    /**
     * Gets an item describing a give up action
     *
     * @return <p>A give up item</p>
     */
    @NotNull
    protected ItemStack getGiveUpItem() {
        GUIItemFactory giveUpItemFactory = new SimpleGUIItemFactory(Material.RESPAWN_ANCHOR);
        List<String> loreLines = getLoreLines();
        loreLines.add(ChatColor.GRAY + "Use this item to give up and");
        loreLines.add(ChatColor.GRAY + "go to your current checkpoint");
        giveUpItemFactory.setName(ChatColor.RED + "Give up");
        giveUpItemFactory.setLore(loreLines);
        return giveUpItemFactory.build();
    }

    /**
     * Gets an item describing a leave arena action
     *
     * @return <p>A leave item</p>
     */
    @NotNull
    protected ItemStack getLeaveItem() {
        GUIItemFactory leaveItemFactory = new SimpleGUIItemFactory(Material.BARRIER);
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
    @NotNull
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
    protected void setAnyClickAction(int inventorySlot, @NotNull GUIAction action) {
        setClickAction(inventorySlot, ClickType.LEFT, action);
        setClickAction(inventorySlot, ClickType.RIGHT, action);
    }

    /**
     * Gets the action to run when triggering the leave item
     *
     * @return <p>The leave action</p>
     */
    @NotNull
    public static GUIAction getLeaveAction() {
        return (player) -> {
            ArenaSession session = MiniGames.getInstance().getSession(player.getUniqueId());
            if (session != null) {
                session.triggerQuit(false, true);
            }
        };
    }

    /**
     * Gets the action to run when triggering the restart action
     *
     * @return <p>The action for triggering a session restart</p>
     */
    @NotNull
    public static GUIAction getRestartAction() {
        return (player -> {
            ArenaSession session = MiniGames.getInstance().getSession(player.getUniqueId());
            if (session != null) {
                session.reset();
            }
        });
    }

    /**
     * Gets the action to run when triggering the toggle players action
     *
     * @param playerRegistry <p>The registry containing relevant players</p>
     * @param inventorySlot  <p>The inventory slot to replace when toggling</p>
     * @return <p>The action for triggering player visibility</p>
     */
    @NotNull
    public GUIAction getTogglePlayersAction(@Nullable ArenaPlayerRegistry<?> playerRegistry, int inventorySlot) {
        return (player) -> {
            PlayerVisibilityManager visibilityManager = MiniGames.getInstance().getPlayerVisibilityManager();
            visibilityManager.toggleHidePlayers(playerRegistry, player);
            if (MiniGames.getInstance().getPlayerVisibilityManager().isHidingPlayers(player)) {
                setItem(inventorySlot, getTogglePlayersItemEnabled(player));
            } else {
                setItem(inventorySlot, getTogglePlayersItemDisabled());
            }
        };
    }

    /**
     * Gets the action to run when triggering the give up item
     *
     * @return <p>The give up action</p>
     */
    @NotNull
    public static GUIAction getGiveUpAction() {
        return (player) -> {
            ArenaSession session = MiniGames.getInstance().getSession(player.getUniqueId());
            if (session instanceof ParkourArenaSession) {
                session.triggerLoss();
            }
        };
    }

}
