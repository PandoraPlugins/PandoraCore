package dev.minecraftplugin.pandoracore.packethandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.Packet;

public class PacketListener<T extends Packet<?>> {
    private final boolean ignoreCancelled;
    private final Class<T> packetType;

    public PacketListener(boolean ignoreCancelled, Class<T> packetType) {
        this.ignoreCancelled = ignoreCancelled;
        this.packetType = packetType;
    }

    public Class<T> getPacketType() {
        return packetType;
    }

    /**
     * Called whenever the server reads a packet from a player.
     *
     * @param context     the context of the packet
     * @param isCancelled whether the packet has been cancelled.
     * @param packet      The packet
     * @return whether you wish for the packet to be cancelled or not.
     */
    public boolean read(boolean isCancelled, Object packet, ChannelHandlerContext context) {
        return false;
    }

    public boolean doesIgnoreCancelled() {
        return ignoreCancelled;
    }

    /**
     * Called whenever the server writes a packet to a player.
     *
     * @param isCancelled whether the packet has been cancelled
     * @param context     the channel context
     * @param packet      the packet
     * @param promise     the promise
     * @return whether you wish for the packet to be cancelled or not.
     */
    public boolean write(boolean isCancelled, Object packet, ChannelHandlerContext context, ChannelPromise promise) {
        return false;
    }
}
