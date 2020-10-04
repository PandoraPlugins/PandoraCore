package dev.minecraftplugin.pandoracore.commands;

import com.azortis.azortislib.command.SubCommand;
import com.azortis.azortislib.command.executors.ISubCommandExecutor;
import dev.minecraftplugin.pandoracore.PandoraCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PandoraModuleCommand implements ISubCommandExecutor {
    private final PandoraCore core;

    public PandoraModuleCommand(PandoraCore core) {
        this.core = core;
    }

    @Override
    public boolean onSubCommand(CommandSender commandSender, SubCommand subCommand, String s, String[] strings) {
        if (commandSender.hasPermission("pandoracore.modulemanager")) {
            if (commandSender instanceof Player) {
                core.getModuleManager().openMenu((Player) commandSender);
            }
            return true;
        }
        return true;
    }
}
