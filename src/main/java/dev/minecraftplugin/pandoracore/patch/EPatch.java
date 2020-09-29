package dev.minecraftplugin.pandoracore.patch;

import dev.minecraftplugin.pandoracore.patch.patches.exploit.CreativeItemPatch;
import dev.minecraftplugin.pandoracore.patch.patches.exploit.CustomPayloadPatch;
import dev.minecraftplugin.pandoracore.patch.patches.exploit.InvalidSignsPatch;
import dev.minecraftplugin.pandoracore.patch.patches.exploit.NullAdressPatch;
import dev.minecraftplugin.pandoracore.patch.patches.gameplay.CombatLoggedDeathPatch;
import dev.minecraftplugin.pandoracore.patch.patches.gameplay.FactionsTPDenyPatch;
import dev.minecraftplugin.pandoracore.patch.patches.gameplay.InvisibilityHitPatch;

public enum EPatch {
    INVIS(new InvisibilityHitPatch()),
    TPDENYFAC(new FactionsTPDenyPatch()),
    CREATIVEITEM(new CreativeItemPatch()),
    CUSTOMPAYLOAD(new CustomPayloadPatch()),
    INVALIDSIGN(new InvalidSignsPatch()),
    NULLADDRESS(new NullAdressPatch()),
    COMBATLOGDEATH(new CombatLoggedDeathPatch());

    private final Patch<?> patch;

    EPatch(Patch<?> patch) {
        this.patch = patch;
    }

    public Patch<?> getPatch() {
        return patch;
    }

    public static EPatch getValue(String name) {
        for (EPatch value : EPatch.values()) {
            if (value.patch.getName().equals(name))
                return value;
        }
        return null;
    }
}
