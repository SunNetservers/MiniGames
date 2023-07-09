package net.knarcraft.minigames.listener;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.gui.ArenaGUI;
import net.knarcraft.minigames.gui.ParkourGUI;
import net.knarcraft.minigames.property.PersistentDataKey;
import net.knarcraft.minigames.util.GUIHelper;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * A listener that listens for player interactions
 */
public class InteractListener implements Listener {

    @EventHandler
    public void menuInteractListener(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        Integer persistentData = meta.getPersistentDataContainer().get(new NamespacedKey(MiniGames.getInstance(),
                PersistentDataKey.MENU_ITEM.getKeyName()), PersistentDataType.INTEGER);

        if (persistentData != null && persistentData == PersistentDataKey.MENU_ITEM.getDataValue()) {
            event.setCancelled(true);
            GUIHelper.openGUI(event.getPlayer());
            return;
        }

        persistentData = meta.getPersistentDataContainer().get(new NamespacedKey(MiniGames.getInstance(),
                PersistentDataKey.LEAVE_ITEM.getKeyName()), PersistentDataType.INTEGER);
        if (persistentData != null) {
            event.setCancelled(true);
            if (persistentData == PersistentDataKey.LEAVE_ITEM.getDataValue()) {
                ArenaGUI.getLeaveAction().run(event.getPlayer());
            } else if (persistentData == PersistentDataKey.GIVE_UP_ITEM.getDataValue()) {
                ParkourGUI.getGiveUpAction().run(event.getPlayer());
            }
        }
    }

}
