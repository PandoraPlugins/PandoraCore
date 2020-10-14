package dev.minecraftplugin.pandoracore.module.modules.tags;

import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.module.Module;
import net.minecraft.server.v1_8_R3.Packet;

public class TagModule extends Module<Packet<?>> {

    public TagModule() {
        super("Tags", "Adds tags to players using suffixes", false, false, null,
                false, "UltraPermissions");
    }

    @Override
    public void enable(PandoraCore core) {

    }

    @Override
    public void disable(PandoraCore core) {

    }
}
