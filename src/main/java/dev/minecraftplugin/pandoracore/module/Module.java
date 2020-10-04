package dev.minecraftplugin.pandoracore.module;

import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.configuration.Config;
import dev.minecraftplugin.pandoracore.packethandler.PacketListener;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.event.Listener;

public abstract class Module<T extends Packet<?>> extends PacketListener<T> implements Listener {
    private final boolean isPacket;
    private final boolean isListener;
    private final String name;
    private final String description;
    private final String[] dependencies;
    private boolean enabled;

    /**
     * Default constructor
     *
     * @param name            the name of the patch
     * @param description     what the patch should do
     * @param packets         should the patch need packets
     * @param isListener      should the patch need to be registered as a listener.
     * @param packetType      the packet that should be listened for.
     * @param ignoreCancelled if packets are enabled, whether it should ignore if a packet has been cancelled or not.
     */
    public Module(String name, String description, boolean packets, boolean ignoreCancelled, Class<T> packetType, boolean isListener,
                  String... dependencies) {
        super(ignoreCancelled, packetType);
        this.name = name;
        this.description = description;
        this.isPacket = packets;
        this.isListener = isListener;
        this.dependencies = dependencies;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public boolean isListener() {
        return isListener;
    }

    public boolean isPacket() {
        return isPacket;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public abstract void enable(PandoraCore core);

    public abstract void disable(PandoraCore core);

    public Config<?> getConfiguration() {
        return null;
    }
}
