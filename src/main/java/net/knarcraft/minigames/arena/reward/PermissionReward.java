package net.knarcraft.minigames.arena.reward;

import net.knarcraft.knarlib.formatting.FormatBuilder;
import net.knarcraft.minigames.MiniGames;
import net.knarcraft.minigames.config.MiniGameMessage;
import net.knarcraft.minigames.manager.PermissionManager;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * A reward that grants a specified permission when granted
 */
public class PermissionReward implements Reward {

    private final @NotNull String permission;
    private final @Nullable World world;

    /**
     * Instantiates a new permission reward
     *
     * @param world      <p>The world to grant the permission for, or null</p>
     * @param permission <p>The permission to grant</p>
     */
    public PermissionReward(@Nullable World world, @NotNull String permission) {
        this.world = world;
        this.permission = permission;
    }

    @Override
    public boolean grant(@NotNull Player player) {
        if (!PermissionManager.isInitialized()) {
            MiniGames.log(Level.SEVERE, "A permission reward has been set, but no Vault-compatible permission" +
                    " plugin has been initialized.");
            return false;
        }
        if (PermissionManager.hasPermission(player, this.permission, this.world != null ? this.world.getName() : null)) {
            return false;
        } else {
            PermissionManager.addPermission(player, this.permission, this.world);
            return true;
        }
    }

    @Override
    @NotNull
    public FormatBuilder getGrantMessage() {
        if (world == null) {
            return new FormatBuilder(MiniGameMessage.SUCCESS_PERMISSION_REWARDED).replace("{permission}",
                    permission);
        } else {
            return new FormatBuilder(MiniGameMessage.SUCCESS_PERMISSION_REWARDED_WORLD).
                    replace("{permission}", permission).replace("{world}", world.getName());
        }
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        if (world != null) {
            data.put("world", world);
        }
        data.put("permission", permission);
        return data;
    }

    /**
     * Deserializes the permission reward defined in the given data
     *
     * @param data <p>The data to deserialize from</p>
     * @return <p>The deserialized data</p>
     */
    @SuppressWarnings("unused")
    @NotNull
    public static PermissionReward deserialize(@NotNull Map<String, Object> data) {
        World world = (World) data.getOrDefault("world", null);
        String permission = (String) data.get("permission");
        return new PermissionReward(world, permission);
    }

}
