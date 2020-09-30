package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.PacketPlayOutCombatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class RemoveDeathScreenPatch extends Patch<PacketPlayOutCombatEvent> {
    public RemoveDeathScreenPatch() {
        super("RemoveDeathScreenPatch", "Removes the death screen when a player dies.", true, false, PacketPlayOutCombatEvent.class,
                false);
    }

    @Override
    public void enable(PandoraCore core) {

    }

    @Override
    public boolean write(boolean isCancelled, Object packet, ChannelHandlerContext context, ChannelPromise promise) {
        PacketPlayOutCombatEvent event = (PacketPlayOutCombatEvent) packet;
        if (event.a == PacketPlayOutCombatEvent.EnumCombatEventType.ENTITY_DIED) {
            int entityID = event.b; // todo: make sure this gets the player's entity id.
            // Get player from id.
            Player p = Bukkit.getOnlinePlayers().stream().filter(player -> player.getEntityId() == entityID)
                    .collect(Collectors.toList()).get(0);
            // Respawn player.
            p.spigot().respawn();
            // We return true to let the handler know we want to intercept the packet.
            return true;
        }
        return super.write(isCancelled, packet, context, promise);
    }

    @Override
    public void disable(PandoraCore core) {

    }
}
