package dev.minecraftplugin.pandoracore.patch;

import java.util.HashSet;
import java.util.Set;

public class PatchData {
    public Set<ToggleablePatch> enabled;

    public PatchData() {
        enabled = new HashSet<>();
        for (EPatch value : EPatch.values()) {
            enabled.add(new ToggleablePatch(value.name(), true));
        }
    }


}
