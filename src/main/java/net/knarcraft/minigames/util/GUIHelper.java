package net.knarcraft.minigames.util;

import net.knarcraft.knargui.item.GUIItemFactory;
import net.knarcraft.knargui.item.PlayerHeadGUIItemFactory;
import net.knarcraft.knargui.item.SimpleGUIItemFactory;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaSession;
import net.knarcraft.minigames.gui.MiniGamesGUI;
import net.knarcraft.minigames.property.PersistentDataKey;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * A helper class for the in-arena GUI
 */
public final class GUIHelper {

    private GUIHelper() {

    }

    /**
     * Gets the GUI open item to give to the specified player
     *
     * @param player <p>The player to give a GUI opening item</p>
     * @return <p>The item to give</p>
     */
    public static ItemStack getGUIOpenItem(@NotNull Player player) {
        if (GeyserHelper.isGeyserPlayer(player)) {
            return getGUIOpenItemBedrock();
        } else {
            return getGUIOpenItemJava();
        }
    }

    /**
     * Gets the item used for opening the mini-games menu
     *
     * @return <p>The item used for opening the GUI</p>
     */
    public static ItemStack getGUIOpenItemJava() {
        PlayerHeadGUIItemFactory factory = new PlayerHeadGUIItemFactory();
        factory.useSkin("3fdab40434ed5d01f58c45ca0c9fada4662e1772ff43e2974979440a5cfe15c9");
        factory.setName(ChatColor.AQUA + "§ MiniGames Menu §");
        ItemStack item = factory.build();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(new NamespacedKey(MiniGames.getInstance(),
                            PersistentDataKey.MENU_ITEM.getKeyName()),
                    PersistentDataType.INTEGER, PersistentDataKey.MENU_ITEM.getDataValue());
        }
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the item used for opening the mini-games menu
     *
     * @return <p>The item used for opening the GUI</p>
     */
    public static ItemStack getGUIOpenItemBedrock() {
        GUIItemFactory factory = new SimpleGUIItemFactory(Material.BEACON);
        factory.setName(ChatColor.AQUA + "MiniGames Menu");
        ItemStack item = factory.build();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(new NamespacedKey(MiniGames.getInstance(),
                            PersistentDataKey.MENU_ITEM.getKeyName()),
                    PersistentDataType.INTEGER, PersistentDataKey.MENU_ITEM.getDataValue());
        }
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Opens the correct GUI for the given player
     *
     * @param player <p>The player to show a GUI for</p>
     */
    public static void openGUI(Player player) {
        ArenaSession existingSession = MiniGames.getInstance().getSession(player.getUniqueId());
        if (existingSession == null) {
            new MiniGamesGUI(player).openFor(player);
        } else {
            existingSession.getGUI().openFor(player);
        }
    }

}
