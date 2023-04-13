package net.knarcraft.dropper;

import net.knarcraft.dropper.arena.dropper.DropperArenaData;
import net.knarcraft.dropper.arena.dropper.DropperArenaGameMode;
import net.knarcraft.dropper.arena.dropper.DropperArenaGroup;
import net.knarcraft.dropper.arena.dropper.DropperArenaHandler;
import net.knarcraft.dropper.arena.dropper.DropperArenaPlayerRegistry;
import net.knarcraft.dropper.arena.dropper.DropperArenaRecordsRegistry;
import net.knarcraft.dropper.arena.dropper.DropperArenaSession;
import net.knarcraft.dropper.arena.parkour.ParkourArenaHandler;
import net.knarcraft.dropper.arena.parkour.ParkourArenaPlayerRegistry;
import net.knarcraft.dropper.arena.record.IntegerRecord;
import net.knarcraft.dropper.arena.record.LongRecord;
import net.knarcraft.dropper.command.CreateArenaCommand;
import net.knarcraft.dropper.command.EditArenaCommand;
import net.knarcraft.dropper.command.EditArenaTabCompleter;
import net.knarcraft.dropper.command.GroupListCommand;
import net.knarcraft.dropper.command.GroupSetCommand;
import net.knarcraft.dropper.command.GroupSwapCommand;
import net.knarcraft.dropper.command.JoinArenaCommand;
import net.knarcraft.dropper.command.JoinArenaTabCompleter;
import net.knarcraft.dropper.command.LeaveArenaCommand;
import net.knarcraft.dropper.command.ListArenaCommand;
import net.knarcraft.dropper.command.ReloadCommand;
import net.knarcraft.dropper.command.RemoveArenaCommand;
import net.knarcraft.dropper.command.RemoveArenaTabCompleter;
import net.knarcraft.dropper.config.DropperConfiguration;
import net.knarcraft.dropper.container.SerializableMaterial;
import net.knarcraft.dropper.container.SerializableUUID;
import net.knarcraft.dropper.listener.CommandListener;
import net.knarcraft.dropper.listener.DamageListener;
import net.knarcraft.dropper.listener.MoveListener;
import net.knarcraft.dropper.listener.PlayerLeaveListener;
import net.knarcraft.dropper.placeholder.DropperRecordExpansion;
import org.bukkit.Bukkit;
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
public final class MiniGames extends JavaPlugin {

    private static MiniGames instance;
    private DropperConfiguration configuration;
    private DropperArenaHandler dropperArenaHandler;
    private DropperArenaPlayerRegistry dropperArenaPlayerRegistry;
    private DropperRecordExpansion dropperRecordExpansion;
    private ParkourArenaHandler parkourArenaHandler;
    private ParkourArenaPlayerRegistry parkourArenaPlayerRegistry;

    /**
     * Gets an instance of this plugin
     *
     * @return <p>An instance of this plugin, or null if not initialized yet.</p>
     */
    public static MiniGames getInstance() {
        return instance;
    }

    /**
     * Gets the dropper arena handler for this instance
     *
     * @return <p>A dropper arena handler</p>
     */
    public DropperArenaHandler getDropperArenaHandler() {
        return this.dropperArenaHandler;
    }

    /**
     * Gets the parkour arena handler for this instance
     *
     * @return <p>A parkour arena handler</p>
     */
    public ParkourArenaHandler getParkourArenaHandler() {
        return this.parkourArenaHandler;
    }

    /**
     * Gets the dropper arena player registry for this instance
     *
     * @return <p>A dropper arena player registry</p>
     */
    public DropperArenaPlayerRegistry getDropperArenaPlayerRegistry() {
        return this.dropperArenaPlayerRegistry;
    }

    /**
     * Gets the parkour arena player registry for this instance
     *
     * @return <p>A parkour arena player registry</p>
     */
    public ParkourArenaPlayerRegistry getParkourArenaPlayerRegistry() {
        return this.parkourArenaPlayerRegistry;
    }

    /**
     * Gets the dropper configuration
     *
     * @return <p>The dropper configuration</p>
     */
    public DropperConfiguration getDropperConfiguration() {
        return this.configuration;
    }

    /**
     * Logs a message
     *
     * @param level   <p>The message level to log at</p>
     * @param message <p>The message to log</p>
     */
    public static void log(Level level, String message) {
        MiniGames.getInstance().getLogger().log(level, message);
    }

    /**
     * Reloads all configurations and data from disk
     */
    public void reload() {
        // Load all arenas again
        this.dropperArenaHandler.load();
        this.parkourArenaHandler.load();

        // Reload configuration
        this.reloadConfig();
        this.configuration.load(this.getConfig());

        // Clear record caches
        this.dropperRecordExpansion.clearCaches();
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
        ConfigurationSerialization.registerClass(DropperArenaGameMode.class);
        ConfigurationSerialization.registerClass(LongRecord.class);
        ConfigurationSerialization.registerClass(IntegerRecord.class);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();
        this.configuration = new DropperConfiguration(this.getConfig());
        this.configuration.load();
        this.dropperArenaPlayerRegistry = new DropperArenaPlayerRegistry();
        this.dropperArenaHandler = new DropperArenaHandler();
        this.dropperArenaHandler.load();

        this.parkourArenaHandler = new ParkourArenaHandler();
        this.parkourArenaHandler.load();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new DamageListener(), this);
        pluginManager.registerEvents(new MoveListener(this.configuration), this);
        pluginManager.registerEvents(new PlayerLeaveListener(), this);
        pluginManager.registerEvents(new CommandListener(), this);

        registerCommand("dropperReload", new ReloadCommand(), null);
        registerCommand("dropperCreate", new CreateArenaCommand(), null);
        registerCommand("dropperList", new ListArenaCommand(), null);
        registerCommand("dropperJoin", new JoinArenaCommand(), new JoinArenaTabCompleter());
        registerCommand("dropperLeave", new LeaveArenaCommand(), null);
        registerCommand("dropperEdit", new EditArenaCommand(this.configuration), new EditArenaTabCompleter());
        registerCommand("dropperRemove", new RemoveArenaCommand(), new RemoveArenaTabCompleter());
        registerCommand("dropperGroupSet", new GroupSetCommand(), null);
        registerCommand("dropperGroupSwap", new GroupSwapCommand(), null);
        registerCommand("dropperGroupList", new GroupListCommand(), null);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.dropperRecordExpansion = new DropperRecordExpansion(this);
            if (!this.dropperRecordExpansion.register()) {
                log(Level.WARNING, "Unable to register PlaceholderAPI expansion!");
            }
        }
    }

    @Override
    public void onDisable() {
        // Throw out currently playing players before exiting
        for (Player player : getServer().getOnlinePlayers()) {
            DropperArenaSession session = dropperArenaPlayerRegistry.getArenaSession(player.getUniqueId());
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
            log(Level.SEVERE, "Unable to register the command " + commandName);
        }
    }

}
