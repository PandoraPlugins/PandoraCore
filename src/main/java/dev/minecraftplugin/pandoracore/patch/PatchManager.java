package dev.minecraftplugin.pandoracore.patch;

import com.azortis.azortislib.configuration.Config;
import com.azortis.azortislib.utils.FormatUtil;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.packethandler.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;


public class PatchManager {
    private final Config<PatchData> patchesData;
    private final PandoraCore core;
    private final PatchGUI gui;

    public PatchManager(PandoraCore core) {
        this.core = core;
        System.out.println(FormatUtil.getSeparator("PATCHES"));
        System.out.println(FormatUtil.color("Loading Patches"));
        patchesData = core.getConfigManager().loadConfig("/patches/enabledPatches", new PatchData());
        for (ToggleablePatch toggleablePatch : patchesData.getConfiguration().enabled) {
            if (toggleablePatch.enabled) {
                EPatch patch = EPatch.getValue(toggleablePatch.name);
                if (patch == null) {
                    System.out.println(FormatUtil.color("&4Warning! Failed to load patch due to name mismatch. " + toggleablePatch.name));
                    continue;
                }
                enablePatch(patch);
            }
        }
        gui = new PatchGUI();

    }

    public void openMenu(Player player) {
        player.openInventory(gui.getGui().getPages().get(0).getView().getInventory());
    }

    public void disablePatch(EPatch patch) {
        HandlerList.unregisterAll(patch.getPatch());
        PacketHandler.unRegisterListener(patch.getPatch());
        patch.getPatch().setEnabled(false);
        patch.getPatch().disable(core);
        patchesData.getConfiguration().enabled.iterator().forEachRemaining(data -> {
            if (data.name.equalsIgnoreCase(patch.getPatch().getName())) {
                data.enabled = false;
            }
        });
    }

    public void enablePatch(EPatch patch) {
        boolean dependencies = true;
        for (String s : patch.getPatch().getDependencies()) {
            if (Bukkit.getPluginManager().getPlugin(s) == null || !Bukkit.getPluginManager().getPlugin(s).isEnabled())
                dependencies = false;
        }
        if (!dependencies) {
            System.out.println(FormatUtil.color("&4Warning! Failed to load dependencies for patch " + patch.getPatch().getName()));
            return;
        }
        if (patch.getPatch().isListener())
            Bukkit.getPluginManager().registerEvents(patch.getPatch(), core);
        if (patch.getPatch().isPacket())
            PacketHandler.registerListener(patch.getPatch());
        patch.getPatch().setEnabled(true);
        patch.getPatch().enable(core);
        patchesData.getConfiguration().enabled.iterator().forEachRemaining(data -> {
            if (data.name.equalsIgnoreCase(patch.getPatch().getName())) {
                data.enabled = true;
            }
        });
    }

    public void disable() {
        patchesData.saveConfig();
    }

    public Config<PatchData> getPatchesData() {
        return patchesData;
    }
}
