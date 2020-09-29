package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;

public class InvisibilityHitPatch extends Patch<PacketPlayInChat> {
    public InvisibilityHitPatch(String name, String description, boolean packets, boolean ignoreCancelled, Class<?> packetType, boolean isListener, String... dependencies) {
        super("InvisibilityHitPatch", "Invisible players turn visible when hit",
                false, ignoreCancelled, null, true);
    }

    @Override
    public void enable(PandoraCore core) {

    }

    @Override
    public void disable(PandoraCore core) {

    }
}
