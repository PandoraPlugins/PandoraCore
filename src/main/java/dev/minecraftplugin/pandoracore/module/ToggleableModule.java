package dev.minecraftplugin.pandoracore.module;

public class ToggleableModule {
    public String name;
    public boolean enabled;

    public ToggleableModule(String name, boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    public ToggleableModule() {
    }
}
