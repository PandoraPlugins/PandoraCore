package dev.minecraftplugin.pandoracore.commands;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.CommandInjector;
import com.azortis.azortislib.command.builders.CommandBuilder;
import com.azortis.azortislib.command.builders.SubCommandBuilder;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import com.azortis.azortislib.command.executors.ITabCompleter;
import dev.minecraftplugin.pandoracore.PandoraCore;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class PandoraCoreCommand implements ICommandExecutor, ITabCompleter {
    public PandoraCoreCommand(PandoraCore core) {
        Command c = new CommandBuilder()
                .setUsage("/core")
                .setPlugin(core)
                .setName("core")
                .setExecutor(this)
                .setTabCompleter(this)
                .setDescription("Pandora core command")
                .addSubCommands(new SubCommandBuilder()
                                .setExecutor(new PandoraPatchCommand(core))
                                .setName("patches"),
                        new SubCommandBuilder()
                                .setExecutor(new PandoraModuleCommand(core))
                                .setName("modules")
                ).build();
        PandoraCore.injectCommand("core", c, true);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings, Location location) {
        List<String> list = new ArrayList<>();
        if (commandSender.hasPermission("pandoracore.patchmanager")) {
            list.add("patches");
        }
        if (commandSender.hasPermission("pandoracore.modulemanager")) {
            list.add("modules");
        }
        return list;
    }
}
