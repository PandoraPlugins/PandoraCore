package dev.minecraftplugin.pandoracore.patch;

import dev.minecraftplugin.pandoracore.patch.patches.exploit.*;
import dev.minecraftplugin.pandoracore.patch.patches.gameplay.CombatLoggedDeathPatch;
import dev.minecraftplugin.pandoracore.patch.patches.gameplay.FactionsTPDenyPatch;
import dev.minecraftplugin.pandoracore.patch.patches.gameplay.InvisibilityHitPatch;

public enum EPatch {
    INVIS(new InvisibilityHitPatch()),
    TPDENYFAC(new FactionsTPDenyPatch()),
    CREATIVEITEM(new CreativeItemPatch()),
    CUSTOMPAYLOAD(new CustomPayloadPatch()),
    INVALIDSIGN(new InvalidSignsPatch()),
    NULLADDRESS(new NullAddressPatch()),
    UUIDSPOOFPATCH(new UUIDSpoofPatch()),
    SNOWBALLLIMITERPATCH(new ProjectileLimiterPatch()),
    COMBATLOGDEATH(new CombatLoggedDeathPatch());

    private final Patch<?> patch;

    EPatch(Patch<?> patch) {
        this.patch = patch;
    }

    public static EPatch getValue(String name) {
        for (EPatch value : EPatch.values()) {
            if (value.patch.getName().equalsIgnoreCase(name))
                return value;
        }
        return null;
    }

    public Patch<?> getPatch() {
        return patch;
    }
}
