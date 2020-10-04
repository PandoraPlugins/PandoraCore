package dev.minecraftplugin.pandoracore.module;

public enum EModule {
    ;

    private final Module<?> module;

    EModule(Module<?> module) {
        this.module = module;
    }

    public static EModule getValue(String name) {
        for (EModule value : EModule.values()) {
            if (value.getModule().getName().equalsIgnoreCase(name))
                return value;
        }
        return null;
    }

    public Module<?> getModule() {
        return module;
    }

}
