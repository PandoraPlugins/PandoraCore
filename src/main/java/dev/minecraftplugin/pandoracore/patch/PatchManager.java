package dev.minecraftplugin.pandoracore.patch;

import com.azortis.azortislib.utils.FormatUtil;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.configuration.Config;
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
                Bukkit.getPluginManager().registerEvents(patch.getPatch(), core);
            }
        }
    }

    public Config<PatchData> getPatchesData() {
        return patchesData;
    }
}
