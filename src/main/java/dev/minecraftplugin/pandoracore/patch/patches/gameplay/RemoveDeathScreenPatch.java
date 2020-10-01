package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

public class RemoveDeathScreenPatch extends Patch<Packet<?>> {
    private PandoraCore core;

    public RemoveDeathScreenPatch() {
        super("RemoveDeathScreenPatch", "Removes the death screen when a player dies.", false, false, null,
                true);
    }

    @Override
    public void enable(PandoraCore core) {
        this.core = core;
    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent entityDeathEvent) {
        if (entityDeathEvent.getEntityType() == EntityType.PLAYER) {
            Bukkit.getScheduler().runTaskLater(core, () -> ((Player) entityDeathEvent.getEntity()).spigot().respawn(), 1);
        }
    }


    @Override
    public void disable(PandoraCore core) {

    }
}
