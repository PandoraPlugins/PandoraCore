package dev.minecraftplugin.pandoracore;

import com.azortis.azortislib.experimental.inventory.GUIManager;
import dev.minecraftplugin.pandoracore.commands.PandoraCoreCommand;
import dev.minecraftplugin.pandoracore.configuration.ConfigManager;
import dev.minecraftplugin.pandoracore.module.ModuleManager;
import dev.minecraftplugin.pandoracore.packethandler.PacketChannelListener;
import dev.minecraftplugin.pandoracore.patch.PatchManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

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
        GUIManager.getInstance(this);
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
    }
}
