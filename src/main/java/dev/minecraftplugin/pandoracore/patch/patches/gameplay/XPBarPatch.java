package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import com.azortis.azortislib.reflection.Reflections;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.PacketPlayOutExperience;
import org.bukkit.entity.Player;

public class XPBarPatch extends Patch<PacketPlayOutExperience> {

    public XPBarPatch() {
        super("XPBarPatch", "Changes the xp bar level number to the player's actual experience.", true, false,
                PacketPlayOutExperience.class, false);
    }

    @Override
    public void enable(PandoraCore core) {

    }

    /**
     * Called whenever the server writes a packet to a player.
     *
     * @param isCancelled whether the packet has been cancelled
     * @param packet      the packet
     * @param context     the channel context
     * @param promise     the promise
     * @param player      the player the server is sending the packet to
     * @return whether you wish for the packet to be cancelled or not.
     */
    @Override
    public PacketPlayOutExperience write(boolean isCancelled, Object packet, ChannelHandlerContext context, ChannelPromise promise, Player player) {
        PacketPlayOutExperience experience = (PacketPlayOutExperience) packet;
        int totalExperience = Reflections.getField(PacketPlayOutExperience.class, "b", int.class).get(experience);
        return new PacketPlayOutExperience(0, totalExperience, totalExperience);
    }

    @Override
    public void disable(PandoraCore core) {

    }
}
