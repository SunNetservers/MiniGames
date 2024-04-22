package net.knarcraft.minigames.listener;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.arena.ArenaPlayerRegistry;
import net.knarcraft.minigames.arena.parkour.ParkourArena;
import net.knarcraft.minigames.arena.parkour.ParkourArenaSession;
import net.knarcraft.minigames.gui.ArenaGUI;
import net.knarcraft.minigames.gui.ParkourGUI;
import net.knarcraft.minigames.property.PersistentDataKey;
import net.knarcraft.minigames.util.GUIHelper;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * A listener that listens for player interactions
 */
public class InteractListener implements Listener {

    @EventHandler
    public void menuInteractListener(@NotNull PlayerInteractEvent event) {
        handleMenuClick(event);

        Block clicked = event.getClickedBlock();
        if (event.useInteractedBlock() != Event.Result.DENY && event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                clicked != null && clicked.getBlockData() instanceof Switch) {
            ArenaPlayerRegistry<ParkourArena> playerRegistry = MiniGames.getInstance().getParkourArenaPlayerRegistry();
            ParkourArenaSession arenaSession = (ParkourArenaSession) playerRegistry.getArenaSession(event.getPlayer().getUniqueId());
            if (arenaSession != null) {
                arenaSession.registerChangedLever(clicked);
            }
        }
    }

    /**
     * Handles clicking of the menu item
     *
     * @param event <p>The triggered player interact event</p>
     */
    private void handleMenuClick(@NotNull PlayerInteractEvent event) {
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
            event.setUseItemInHand(Event.Result.DENY);
            event.setUseInteractedBlock(Event.Result.DENY);
            GUIHelper.openGUI(event.getPlayer());
            return;
        }

        persistentData = meta.getPersistentDataContainer().get(new NamespacedKey(MiniGames.getInstance(),
                PersistentDataKey.LEAVE_ITEM.getKeyName()), PersistentDataType.INTEGER);
        if (persistentData != null) {
            event.setUseItemInHand(Event.Result.DENY);
            event.setUseInteractedBlock(Event.Result.DENY);
            if (persistentData == PersistentDataKey.LEAVE_ITEM.getDataValue()) {
                ArenaGUI.getLeaveAction().run(event.getPlayer());
            } else if (persistentData == PersistentDataKey.GIVE_UP_ITEM.getDataValue()) {
                ParkourGUI.getGiveUpAction().run(event.getPlayer());
            }
        }
    }

}
