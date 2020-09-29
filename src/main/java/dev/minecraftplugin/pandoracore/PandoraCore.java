package dev.minecraftplugin.pandoracore;

import dev.minecraftplugin.pandoracore.configuration.ConfigManager;
import dev.minecraftplugin.pandoracore.packethandler.PacketHandler;
import dev.minecraftplugin.pandoracore.patch.PatchManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PandoraCore extends JavaPlugin {
    private ConfigManager configManager;
    private PatchManager patchManager;

    public ConfigManager getConfigManager() {
        return configManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(PacketHandler.getInstance(), this);
        configManager = new ConfigManager(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
