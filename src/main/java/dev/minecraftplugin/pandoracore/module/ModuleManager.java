package dev.minecraftplugin.pandoracore.module;

import com.azortis.azortislib.utils.FormatUtil;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.configuration.Config;
import dev.minecraftplugin.pandoracore.packethandler.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class ModuleManager {
    private final Config<ModuleData> moduleData;
    private final PandoraCore core;
    private final ModuleGUI gui;

    public ModuleManager(PandoraCore core) {
        this.core = core;
        System.out.println(FormatUtil.getSeparator("MODULES"));
        System.out.println(FormatUtil.color("Loading Modules..."));
        moduleData = core.getConfigManager().loadConfig("/modules/enabledModules", new ModuleData());
        for (ToggleableModule toggleableModule : moduleData.getConfiguration().enabled) {
            if (toggleableModule.enabled) {
                EModule module = EModule.getValue(toggleableModule.name);
                if (module == null) {
                    System.out.println(FormatUtil.color("&4Warning! Failed to load module due to name mismatch. " + toggleableModule.name));
                    continue;
                }
                enableModule(module);
            }
        }
        gui = new ModuleGUI();

    }

    public void openMenu(Player player) {
        player.openInventory(gui.getGui().getInventory());
    }

    public void disableModule(EModule module) {
        HandlerList.unregisterAll(module.getModule());
        PacketHandler.unRegisterListener(module.getModule());
        module.getModule().setEnabled(false);
        module.getModule().disable(core);
        moduleData.getConfiguration().enabled.iterator().forEachRemaining(data -> {
            if (data.name.equalsIgnoreCase(module.getModule().getName())) {
                data.enabled = false;
            }
        });
    }

    public void enableModule(EModule module) {
        boolean dependencies = true;
        for (String s : module.getModule().getDependencies()) {
            if (Bukkit.getPluginManager().getPlugin(s) == null || !Bukkit.getPluginManager().getPlugin(s).isEnabled())
                dependencies = false;
        }
        if (!dependencies) {
            System.out.println(FormatUtil.color("&4Warning! Failed to load dependencies for patch " + module.getModule().getName()));
            return;
        }
        if (module.getModule().isListener())
            Bukkit.getPluginManager().registerEvents(module.getModule(), core);
        if (module.getModule().isPacket())
            PacketHandler.registerListener(module.getModule());
        module.getModule().setEnabled(true);
        module.getModule().enable(core);
        moduleData.getConfiguration().enabled.iterator().forEachRemaining(data -> {
            if (data.name.equalsIgnoreCase(module.getModule().getName())) {
                data.enabled = true;
            }
        });
    }

    public void disable() {
        moduleData.saveConfig();
    }

    public Config<ModuleData> getModuleData() {
        return moduleData;
    }
}
