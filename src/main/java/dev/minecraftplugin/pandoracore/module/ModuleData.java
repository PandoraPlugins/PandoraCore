package dev.minecraftplugin.pandoracore.module;

import java.util.HashSet;
import java.util.Set;

public class ModuleData {
    public Set<ToggleableModule> enabled;

    public ModuleData() {
        enabled = new HashSet<>();
        for (EModule module : EModule.values()) {
            enabled.add(new ToggleableModule(module.getModule().getName(), true));
        }
    }
}
