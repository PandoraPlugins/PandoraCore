package dev.minecraftplugin.pandoracore.module;

import dev.minecraftplugin.pandoracore.module.modules.shop.ShopModule;
import dev.minecraftplugin.pandoracore.patch.EPatch;

public enum EModule {
    MINESHOP(new ShopModule());
    //COMBATLOG(new CombatLogModule());
    ;
    private final Module<?> module;

    EModule(Module<?> module) {
        this.module = module;
    }


    public static EModule getValue(String name) {
        for (EModule value : EModule.values()) {
            if (value.module.getName().equalsIgnoreCase(name))
                return value;
        }
        return null;
    }

    public Module<?> getModule() {
        return module;
    }

}
