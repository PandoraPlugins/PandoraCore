package dev.minecraftplugin.pandoracore;

import dev.minecraftplugin.pandoracore.commands.PandoraCoreCommand;
import dev.minecraftplugin.pandoracore.configuration.ConfigManager;
import dev.minecraftplugin.pandoracore.packethandler.PacketHandler;
import dev.minecraftplugin.pandoracore.patch.PatchManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PandoraCore extends JavaPlugin {
    private static PandoraCore instance;
    private ConfigManager configManager;
    private PatchManager patchManager;

    public static PandoraCore getInstance() {
        return instance;
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
        Bukkit.getPluginManager().registerEvents(PacketHandler.getInstance(), this);
        configManager = new ConfigManager(this);
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
