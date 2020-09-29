package dev.minecraftplugin.pandoracore.packethandler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;
import java.util.Set;

public class PacketHandler extends ChannelDuplexHandler implements Listener {
    private final Set<PacketListener<?>> listeners = new HashSet<>();

    private PacketHandler() {
    }

    public static PacketHandler getInstance() {
        return InstanceHandler.getInstance();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.networkManager.channel.pipeline()
                .addBefore("packethandler_pandoracore", "pandoracore_channel", this);
    }

    public void registerListener(PacketListener<?> listener) {
        listeners.add(listener);
    }

    public void unRegisterListener(PacketListener<?> listener) {
        listeners.remove(listener);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        boolean intercept = false;
        for (PacketListener<?> listener : listeners) {
            if (intercept && listener.doesIgnoreCancelled() && o.getClass().isAssignableFrom(listener.getPacketType())) {
                intercept = listener.read(true, o, channelHandlerContext);
            } else if (!intercept && o.getClass().isAssignableFrom(listener.getPacketType()))
                intercept = listener.read(false, o, channelHandlerContext);
        }
        if (!intercept)
            super.channelRead(channelHandlerContext, o);
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object o, ChannelPromise channelPromise) throws Exception {
        boolean intercept = false;
        for (PacketListener<?> listener : listeners) {
            if (intercept && listener.doesIgnoreCancelled() && o.getClass().isAssignableFrom(listener.getPacketType())) {
                intercept = listener.write(true, o, channelHandlerContext, channelPromise);
            } else if (!intercept && o.getClass().isAssignableFrom(listener.getPacketType()))
                intercept = listener.write(false, o, channelHandlerContext, channelPromise);
        }
        if (!intercept)
            super.write(channelHandlerContext, o, channelPromise);
    }

    private static class InstanceHandler {
        private static final PacketHandler INSTANCE = new PacketHandler();

        public static PacketHandler getInstance() {
            return INSTANCE;
        }
    }
}
