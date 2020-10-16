package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.builders.CommandBuilder;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import com.azortis.azortislib.configuration.Config;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.command.CommandSender;

public class RenameItemPatch extends Patch<Packet<?>> implements ICommandExecutor {
    private PandoraCore plugin;
    private Config<RenameItemPatchData> config;

    @Override
    public Config<RenameItemPatchData> getConfiguration() {
        return config;
    }

    public RenameItemPatch() {

        super("RenameItemPatch", "Rename items for a price!", false, false,
                null, false);


    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @Override
    public void enable(PandoraCore core) {
        this.plugin = core;
        Command c = new CommandBuilder()
                .setPlugin(core)
                .setUsage("/rename")
                .setName("rename")
                .setDescription("Rename items for a cost")
                .setPermission("rename.item")
                .setExecutor(this)
                .build();
        PandoraCore.injectCommand("rename", c, true);
        config = core.getConfigManager().loadConfig("/patches/gameplay/rename.json", new RenameItemPatchData());
    }

    @Override
    public void disable(PandoraCore core) {
        PandoraCore.removeCommand("rename");
        plugin = null;
    }

    private static class RenameItemPatchData {
        public int cost = 1000000;
    }
}
