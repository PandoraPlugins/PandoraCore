package dev.minecraftplugin.pandoracore.module.modules.shop;

import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.module.Module;
import net.minecraft.server.v1_8_R3.Packet;

public class ShopModule extends Module<Packet<?>> {
    public ShopModule() {
        super("ShopModule", "Adds a hardcoded shop for totems, fishing, and mines.", false, false,
                null, true);
    }

    @Override
    public void enable(PandoraCore core) {

    }

    @Override
    public void disable(PandoraCore core) {

    }
}
