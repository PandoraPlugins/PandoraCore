package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.minecraft.server.v1_8_R3.Packet;

public class FactionsTPDenyPatch extends Patch<Packet<?>> {
    // todo: implement this.
    public FactionsTPDenyPatch() {
        super("TpDenyPatch", "Denies players teleportation access in other player's territories.", false, false, null,
                true, "Factions");
    }

    @Override
    public void enable(PandoraCore core) {

    }

    @Override
    public void disable(PandoraCore core) {

    }
}
