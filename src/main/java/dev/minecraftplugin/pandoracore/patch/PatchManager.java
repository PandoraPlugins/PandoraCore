package dev.minecraftplugin.pandoracore.patch;

import com.azortis.azortislib.utils.FormatUtil;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.configuration.Config;
import dev.minecraftplugin.pandoracore.packethandler.PacketHandler;
import org.bukkit.Bukkit;

public class PatchManager {
    private final Config<PatchData> patchesData;

    public PatchManager(PandoraCore core) {
        System.out.println(FormatUtil.getSeparator("PATCHES"));
        System.out.println(FormatUtil.color("Loading Patches"));
        patchesData = core.getConfigManager().loadConfig("enabledPatches", new PatchData());
        for (ToggleablePatch toggleablePatch : patchesData.getConfiguration().enabled) {
            if (toggleablePatch.enabled) {
                EPatch patch = EPatch.getValue(toggleablePatch.name);
                if (patch == null) {
                    System.out.println(FormatUtil.color("&4Warning! Failed to load patch due to name mismatch. " + toggleablePatch.name));
                    continue;
                }
                boolean dependencies = true;
                for (String s : patch.getPatch().getDependencies()) {
                    if (Bukkit.getPluginManager().getPlugin(s) == null || !Bukkit.getPluginManager().getPlugin(s).isEnabled())
                        dependencies = false;
                }
                if (!dependencies) {
                    System.out.println(FormatUtil.color("&4Warning! Failed to load dependencies for patch " + toggleablePatch.name));
                    continue;
                }
                if (patch.getPatch().isListener())
                    Bukkit.getPluginManager().registerEvents(patch.getPatch(), core);
                if (patch.getPatch().isPacket())
                    PacketHandler.getInstance().registerListener(patch.getPatch());
                patch.getPatch().setEnabled(true);
                patch.getPatch().enable(core);
            }
        }
    }

    public Config<PatchData> getPatchesData() {
        return patchesData;
    }
}
