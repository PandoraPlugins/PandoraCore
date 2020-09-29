package dev.minecraftplugin.pandoracore.patch;

public class ToggleablePatch {
    public String name;
    public boolean enabled;

    public ToggleablePatch(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public ToggleablePatch() {
    }
}
