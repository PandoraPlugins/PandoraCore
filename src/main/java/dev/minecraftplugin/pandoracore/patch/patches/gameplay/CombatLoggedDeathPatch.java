package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.minecraft.server.v1_8_R3.Packet;

public class CombatLoggedDeathPatch extends Patch<Packet<?>> {
    // todo: implement this;
    public CombatLoggedDeathPatch() {
        super("CombatLogDeathPatch", "Removes combat log from players after death.", false, false, null,
                true, "FAIO-Factions-All-In-One");
    }

    @Override
    public void enable(PandoraCore core) {

    }

    @Override
    public void disable(PandoraCore core) {

    }
}
