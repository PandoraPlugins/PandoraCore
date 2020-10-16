package dev.minecraftplugin.pandoracore.module.modules.combatlog;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.CommandInjector;
import com.azortis.azortislib.command.builders.CommandBuilder;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import dev.minecraftplugin.pandoracore.PandoraCore;
import org.bukkit.command.CommandSender;

public class CombatLeaveCommand implements ICommandExecutor {
    public CombatLeaveCommand(PandoraCore core, String commandName) {
        Command c = new CommandBuilder()
                .setExecutor(this)
                .setDescription("Safely quit the server.")
                .setName(commandName)
                .setPlugin(core)
                .setUsage("/" + commandName)
                .build();
        PandoraCore.injectCommand("combatlog", c, true);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
//        if (commandSender instanceof Player) {
//            Player p = ((Player) commandSender).getPlayer();
//            CombatLogModule module = (CombatLogModule) EModule.COMBATLOG.getModule();
//            if (module.getSecondsLeft().containsKey(p)) {
//
//                return true;
//            }
//
//        }
        return true;
    }
}
