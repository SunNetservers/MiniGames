package net.knarcraft.minigames.arena.reward;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.config.MiniGameMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A reward that gives an item stack when granted
 */
public class ItemReward implements Reward {

    private final ItemStack item;

    /**
     * Instantiates a new item reward
     *
     * @param item <p>The item rewarded</p>
     */
    public ItemReward(@NotNull ItemStack item) {
        if (item.getAmount() > item.getMaxStackSize()) {
            throw new IllegalArgumentException("Item stack exceeds the maximum stack size");
        }
        this.item = item;
    }

    @Override
    public boolean grant(@NotNull Player player) {
        Inventory inventory = player.getInventory();
        if (canFitItem(inventory)) {
            inventory.addItem(item);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public @NotNull String getGrantMessage() {
        return MiniGames.getInstance().getStringFormatter().replacePlaceholders(MiniGameMessage.SUCCESS_ITEM_REWARDED,
                new String[]{"{amount}", "{item}"}, new String[]{String.valueOf(item.getAmount()),
                        item.getType().getKey().getKey().replace("_", " ")});
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("item", item);
        return data;
    }

    /**
     * Deserializes the item reward defined in the given data
     *
     * @param data <p>The data to deserialize from</p>
     * @return <p>The deserialized data</p>
     */
    @SuppressWarnings("unused")
    public static ItemReward deserialize(Map<String, Object> data) {
        return new ItemReward((ItemStack) data.get("item"));
    }

    /**
     * Checks whether the given inventory is able to fit this item reward
     *
     * @param inventory <p>The inventory to check</p>
     * @return <p>True if the inventory can fit the item</p>
     */
    private boolean canFitItem(Inventory inventory) {
        // If a slot is available, there is no problem
        if (inventory.firstEmpty() != -1) {
            return true;
        }

        // If the inventory doesn't contain the correct type of item, stacking is impossible
        if (!inventory.contains(item.getType())) {
            return false;
        }

        // Check if the item stack can fit in the inventory if stacked with existing items
        int availableSlots = 0;
        for (ItemStack itemStack : inventory.getStorageContents()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            ItemMeta targetMeta = item.getItemMeta();
            // Skip items of a different type, or with metadata that would prevent stacking
            if (itemStack.getType() != item.getType() ||
                    (itemMeta != null && targetMeta != null && !itemMeta.equals(targetMeta))) {
                continue;
            }

            availableSlots += itemStack.getMaxStackSize() - itemStack.getAmount();

            if (availableSlots < item.getAmount()) {
                return true;
            }
        }

        return false;
    }

}
