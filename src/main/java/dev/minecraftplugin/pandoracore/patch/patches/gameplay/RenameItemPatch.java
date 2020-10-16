package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.builders.CommandBuilder;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import com.azortis.azortislib.configuration.Config;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.command.CommandSender;

public class RenameItemPatch extends Patch<Packet<?>> {


    public RenameItemPatch(PandoraCore core) {

        super()
        Command c = new CommandBuilder()
                .setPlugin(core)
                .setUsage("/rename")
                .setName("rename")
                .setDescription("Rename items for a cost")
                .setPermission("rename.item")
                .setExecutor(this)
                .build();
        PandoraCore.injectCommand("rename", c, true);

    }



    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @Override
    public void enable(PandoraCore core) {

    }

    @Override
    public void disable(PandoraCore core) {

    }
}
