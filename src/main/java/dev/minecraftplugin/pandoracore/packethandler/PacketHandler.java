package dev.minecraftplugin.pandoracore.packethandler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class PacketHandler extends ChannelDuplexHandler {
    private final static Set<PacketListener<?>> listeners = new HashSet<>();
    private final Player player;

    protected PacketHandler(Player player) {
        this.player = player;
    }

    public static void registerListener(PacketListener<?> listener) {
        listeners.add(listener);
    }

    public static void unRegisterListener(PacketListener<?> listener) {
        listeners.remove(listener);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        Packet<?> intercept = (Packet<?>) o;
        for (PacketListener<?> listener : listeners) {
            if (listener.getPacketType().isAssignableFrom(o.getClass()))
                intercept = listener.read(intercept == null, intercept, channelHandlerContext, player);
            if (intercept == null) break;
        }
        if (intercept != null)
            super.channelRead(channelHandlerContext, intercept);
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
        Packet<?> intercept = (Packet<?>) o;
        for (PacketListener<?> listener : listeners) {
            if (listener.getPacketType().isAssignableFrom(o.getClass()))
                intercept = listener.write(intercept == null, intercept, channelHandlerContext, channelPromise, player);
            if (intercept == null) break;
        }
        if (intercept != null)
            super.write(channelHandlerContext, intercept, channelPromise);
    }
}
