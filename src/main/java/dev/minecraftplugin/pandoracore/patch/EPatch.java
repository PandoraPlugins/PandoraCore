package dev.minecraftplugin.pandoracore.patch;

import dev.minecraftplugin.pandoracore.patch.patches.exploit.*;
import dev.minecraftplugin.pandoracore.patch.patches.gameplay.*;

public enum EPatch {
    INVIS(new InvisibilityHitPatch()),
    NULLADDRESS(new NullAddressPatch()),
    UUIDSPOOFPATCH(new UUIDSpoofPatch()),
    SNOWBALLLIMITERPATCH(new ProjectileLimiterPatch()),
    REMOVEDEATH(new RemoveDeathScreenPatch()),
    XPBARPATCH(new XPBarPatch()),
    NOQUILL(new DenyItemPatch()),
    RENAME(new RenameItemPatch()),
    COOLDOWN(new CooldownPatch()),
    TNTBANKITEM(new TNTBankItem());

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
