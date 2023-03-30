package net.knarcraft.dropper;

import net.knarcraft.dropper.arena.DropperArenaData;
import net.knarcraft.dropper.arena.DropperArenaGroup;
import net.knarcraft.dropper.arena.DropperArenaHandler;
import net.knarcraft.dropper.arena.DropperArenaPlayerRegistry;
import net.knarcraft.dropper.arena.DropperArenaRecordsRegistry;
import net.knarcraft.dropper.arena.DropperArenaSession;
import net.knarcraft.dropper.command.CreateArenaCommand;
import net.knarcraft.dropper.command.EditArenaCommand;
import net.knarcraft.dropper.command.EditArenaTabCompleter;
import net.knarcraft.dropper.command.JoinArenaCommand;
import net.knarcraft.dropper.command.JoinArenaTabCompleter;
import net.knarcraft.dropper.command.LeaveArenaCommand;
import net.knarcraft.dropper.command.ListArenaCommand;
import net.knarcraft.dropper.command.ReloadCommand;
import net.knarcraft.dropper.command.RemoveArenaCommand;
import net.knarcraft.dropper.command.RemoveArenaTabCompleter;
import net.knarcraft.dropper.container.SerializableMaterial;
import net.knarcraft.dropper.container.SerializableUUID;
import net.knarcraft.dropper.listener.CommandListener;
import net.knarcraft.dropper.listener.DamageListener;
import net.knarcraft.dropper.listener.MoveListener;
import net.knarcraft.dropper.listener.PlayerLeaveListener;
import net.knarcraft.dropper.property.ArenaGameMode;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

/**
 * The dropper plugin's main class
 */
@SuppressWarnings("unused")
public final class Dropper extends JavaPlugin {

    private static Dropper instance;
    private DropperArenaHandler arenaHandler;
    private DropperArenaPlayerRegistry playerRegistry;

    /**
     * Gets an instance of this plugin
     *
     * @return <p>An instance of this plugin, or null if not initialized yet.</p>
     */
    public static Dropper getInstance() {
        return instance;
    }

    /**
     * Gets the arena handler for this instance
     *
     * @return <p>A dropper arena handler</p>
     */
    public DropperArenaHandler getArenaHandler() {
        return this.arenaHandler;
    }

    /**
     * Gets the arena player registry for this instance
     *
     * @return <p>A dropper arena player registry</p>
     */
    public DropperArenaPlayerRegistry getPlayerRegistry() {
        return this.playerRegistry;
    }

    /**
     * Reloads all configurations and data from disk
     */
    public void reload() {
        // Load all arenas again
        this.arenaHandler.loadArenas();
        this.arenaHandler.loadGroups();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        // Register serialization classes
        ConfigurationSerialization.registerClass(SerializableMaterial.class);
        ConfigurationSerialization.registerClass(DropperArenaRecordsRegistry.class);
        ConfigurationSerialization.registerClass(SerializableUUID.class);
        ConfigurationSerialization.registerClass(DropperArenaData.class);
        ConfigurationSerialization.registerClass(DropperArenaGroup.class);
        ConfigurationSerialization.registerClass(ArenaGameMode.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.playerRegistry = new DropperArenaPlayerRegistry();
        this.arenaHandler = new DropperArenaHandler();
        this.arenaHandler.loadArenas();
        this.arenaHandler.loadGroups();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new DamageListener(), this);
        pluginManager.registerEvents(new MoveListener(), this);
        pluginManager.registerEvents(new PlayerLeaveListener(), this);
        pluginManager.registerEvents(new CommandListener(), this);

        registerCommand("dropperreload", new ReloadCommand(), null);
        registerCommand("droppercreate", new CreateArenaCommand(), null);
        registerCommand("dropperlist", new ListArenaCommand(), null);
        registerCommand("dropperjoin", new JoinArenaCommand(), new JoinArenaTabCompleter());
        registerCommand("dropperleave", new LeaveArenaCommand(), null);
        registerCommand("dropperedit", new EditArenaCommand(), new EditArenaTabCompleter());
        registerCommand("dropperremove", new RemoveArenaCommand(), new RemoveArenaTabCompleter());
    }

    @Override
    public void onDisable() {
        // Throw out currently playing players before exiting
        for (Player player : getServer().getOnlinePlayers()) {
            DropperArenaSession session = playerRegistry.getArenaSession(player.getUniqueId());
            if (session != null) {
                session.triggerQuit(true);
            }
        }
    }

    /**
     * Registers a command
     *
     * @param commandName     <p>The name of the command to register (defined in plugin.yml)</p>
     * @param commandExecutor <p>The executor for the command</p>
     * @param tabCompleter    <p>The tab-completer to use, or null</p>
     */
    private void registerCommand(@NotNull String commandName, @NotNull CommandExecutor commandExecutor,
                                 @Nullable TabCompleter tabCompleter) {
        PluginCommand command = this.getCommand(commandName);
        if (command != null) {
            command.setExecutor(commandExecutor);
            if (tabCompleter != null) {
                command.setTabCompleter(tabCompleter);
            }
        } else {
            getLogger().log(Level.SEVERE, "Unable to register the command " + commandName);
        }
    }

}
