package dev.minecraftplugin.pandoracore.commands;

import com.azortis.azortislib.command.SubCommand;
import com.azortis.azortislib.command.executors.ISubCommandExecutor;
import dev.minecraftplugin.pandoracore.PandoraCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PandoraPatchCommand implements ISubCommandExecutor {
    private final PandoraCore core;

    public PandoraPatchCommand(PandoraCore core) {
        this.core = core;
    }

    @Override
    public boolean onSubCommand(CommandSender commandSender, SubCommand subCommand, String s, String[] strings) {
        if (commandSender.hasPermission("pandoracore.patchmanager")) {
            if (commandSender instanceof Player) {
                core.getPatchManager().openMenu((Player) commandSender);
            }
            return true;
        }
        return true;
    }
}
