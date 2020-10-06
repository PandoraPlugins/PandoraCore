package dev.minecraftplugin.pandoracore;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.experimental.configuration.ConfigManager;
import com.azortis.azortislib.experimental.inventory.GUIManager;
import dev.minecraftplugin.pandoracore.commands.PandoraCoreCommand;
import dev.minecraftplugin.pandoracore.module.ModuleManager;
import dev.minecraftplugin.pandoracore.packethandler.PacketChannelListener;
import dev.minecraftplugin.pandoracore.patch.PatchManager;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// todo: add back and next buttons in the module and patch gui.
public final class PandoraCore extends JavaPlugin {
    private static PandoraCore instance;
    private ConfigManager configManager;
    private PatchManager patchManager;
    private ModuleManager moduleManager;

    public static PandoraCore getInstance() {
        return instance;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PatchManager getPatchManager() {
        return patchManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        new GUIManager(this);
        Bukkit.getPluginManager().registerEvents(new PacketChannelListener(), this);
        configManager = new ConfigManager(this);
        moduleManager = new ModuleManager(this);
        patchManager = new PatchManager(this);
        instance = this;
        new PandoraCoreCommand(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        patchManager.disable();
        moduleManager.disable();
    }


    @SuppressWarnings("unchecked")
    public static void injectCommand(String fallBackPrefix, Command command, boolean override) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());
            if (override && commandMap.getCommand(command.getName()) != null) {
                org.bukkit.command.Command conflictCommand = commandMap.getCommand(command.getName());
                List<String> conflictCommandAliases = new ArrayList<>();
                assert conflictCommand != null;
                if (!conflictCommand.getAliases().isEmpty())
                    conflictCommandAliases.addAll(conflictCommand.getAliases());
                Map<String, org.bukkit.command.Command> knownCommands;
                Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                knownCommands = (Map<String, org.bukkit.command.Command>) knownCommandsField.get(commandMap);
                knownCommands.remove(conflictCommand.getName());
                if (!conflictCommandAliases.isEmpty()) {
                    for (String alias : conflictCommandAliases) {
                        knownCommands.remove(alias);
                    }
                }
                knownCommandsField.setAccessible(false);

            }
            commandMap.register(fallBackPrefix, command.getBukkitCommand());
            commandMapField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void removeCommand(String command) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());

            Map<String, org.bukkit.command.Command> knownCommands;
            Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            knownCommands = (Map<String, org.bukkit.command.Command>) knownCommandsField.get(commandMap);

            knownCommands.remove(command);
            knownCommandsField.setAccessible(false);

            commandMapField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}
