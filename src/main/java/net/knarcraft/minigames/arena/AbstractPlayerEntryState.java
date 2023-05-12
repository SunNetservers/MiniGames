package net.knarcraft.minigames.arena;

import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.container.SerializableUUID;
import net.knarcraft.minigames.property.PersistentDataKey;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/**
 * An abstract representation of a player's entry state
 */
public abstract class AbstractPlayerEntryState implements PlayerEntryState {

    protected final UUID playerId;
    private final Location entryLocation;
    private final boolean originalIsFlying;
    private final GameMode originalGameMode;
    private final boolean originalAllowFlight;
    private final boolean originalInvulnerable;
    private final boolean originalIsSwimming;
    private final boolean originalCollideAble;

    /**
     * Instantiates a new abstract player entry state
     *
     * @param player <p>The player whose state this should keep track of</p>
     */
    public AbstractPlayerEntryState(@NotNull Player player) {
        this.playerId = player.getUniqueId();
        this.entryLocation = player.getLocation().clone();
        this.originalIsFlying = player.isFlying();
        this.originalGameMode = player.getGameMode();
        this.originalAllowFlight = player.getAllowFlight();
        this.originalInvulnerable = player.isInvulnerable();
        this.originalIsSwimming = player.isSwimming();
        this.originalCollideAble = player.isCollidable();
    }

    /**
     * Instantiates a new abstract player entry state
     *
     * @param playerId             <p>The id of the player whose state this should keep track of</p>
     * @param entryLocation        <p>The location the player entered from</p>
     * @param originalIsFlying     <p>Whether the player was flying before entering the arena</p>
     * @param originalGameMode     <p>The game-mode of the player before entering the arena</p>
     * @param originalAllowFlight  <p>Whether the player was allowed flight before entering the arena</p>
     * @param originalInvulnerable <p>Whether the player was invulnerable before entering the arena</p>
     * @param originalIsSwimming   <p>Whether the player was swimming before entering the arena</p>
     * @param originalCollideAble  <p>Whether the player was collide-able before entering the arena</p>
     */
    public AbstractPlayerEntryState(@NotNull UUID playerId, Location entryLocation,
                                    boolean originalIsFlying, GameMode originalGameMode, boolean originalAllowFlight,
                                    boolean originalInvulnerable, boolean originalIsSwimming,
                                    boolean originalCollideAble) {
        this.playerId = playerId;
        this.entryLocation = entryLocation;
        this.originalIsFlying = originalIsFlying;
        this.originalGameMode = originalGameMode;
        this.originalAllowFlight = originalAllowFlight;
        this.originalInvulnerable = originalInvulnerable;
        this.originalIsSwimming = originalIsSwimming;
        this.originalCollideAble = originalCollideAble;
    }

    @Override
    public @NotNull UUID getPlayerId() {
        return this.playerId;
    }

    @Override
    public boolean restore() {
        Player player = getPlayer();
        if (player == null) {
            return false;
        }
        restore(player);
        return true;
    }

    @Override
    public void restore(@NotNull Player player) {
        player.setCollidable(this.originalCollideAble);
        player.setAllowFlight(this.originalAllowFlight);
        player.setFlying(player.getAllowFlight() && this.originalIsFlying);
        player.setGameMode(this.originalGameMode);
        player.setInvulnerable(this.originalInvulnerable);
        player.setSwimming(this.originalIsSwimming);
        removeMenuItem(player);
    }

    @Override
    public Location getEntryLocation() {
        return this.entryLocation;
    }

    /**
     * Gets the player this entry state belongs to
     *
     * @return <p>The player, or null if not currently online</p>
     */
    protected Player getPlayer() {
        Player player = Bukkit.getOfflinePlayer(this.playerId).getPlayer();
        if (player == null) {
            MiniGames.log(Level.WARNING, "Unable to change state for player with id " + this.playerId +
                    " because the player was not found on the server.");
        }
        return player;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("playerId", new SerializableUUID(this.playerId));
        data.put("entryLocation", this.entryLocation);
        data.put("originalIsFlying", this.originalIsFlying);
        data.put("originalGameMode", this.originalGameMode.name());
        data.put("originalAllowFlight", this.originalAllowFlight);
        data.put("originalInvulnerable", this.originalInvulnerable);
        data.put("originalIsSwimming", this.originalIsSwimming);
        data.put("originalCollideAble", this.originalCollideAble);
        return data;
    }

    /**
     * Removes the menu item from the given player's inventory
     *
     * @param player <p>The player to remove the menu item from</p>
     */
    private void removeMenuItem(Player player) {
        Set<ItemStack> itemsToRemove = new HashSet<>();
        player.getInventory().forEach((item) -> {
            if (item == null) {
                return;
            }
            ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                return;
            }
            Integer persistentData = meta.getPersistentDataContainer().get(new NamespacedKey(MiniGames.getInstance(),
                    PersistentDataKey.MENU_ITEM.getKeyName()), PersistentDataType.INTEGER);
            if (persistentData != null && persistentData == PersistentDataKey.MENU_ITEM.getDataValue()) {
                itemsToRemove.add(item);
            }
        });
        for (ItemStack toRemove : itemsToRemove) {
            player.getInventory().remove(toRemove);
        }
    }

    /**
     * Gets a boolean value from a serialization map
     *
     * @param data <p>The serialization data to look through</p>
     * @param key  <p>The key to get</p>
     * @return <p>The boolean value of the key</p>
     */
    protected static boolean getBoolean(Map<String, Object> data, String key) {
        Boolean value = (Boolean) data.get(key);
        return Objects.requireNonNullElse(value, false);
    }

}
