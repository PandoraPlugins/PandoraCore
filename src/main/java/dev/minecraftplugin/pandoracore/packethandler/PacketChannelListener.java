package dev.minecraftplugin.pandoracore.packethandler;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PacketChannelListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.networkManager.channel.pipeline()
                .addBefore("packet_handler", "pandoracore_channel", new PacketHandler(event.getPlayer()));
    }

}
