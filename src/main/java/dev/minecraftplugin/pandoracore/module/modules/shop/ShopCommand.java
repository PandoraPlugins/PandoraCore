package dev.minecraftplugin.pandoracore.module.modules.shop;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.CommandInjector;
import com.azortis.azortislib.command.builders.CommandBuilder;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import dev.minecraftplugin.pandoracore.PandoraCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShopCommand implements ICommandExecutor {
    private final ShopModule module;

    public ShopCommand(PandoraCore core, ShopModule module) {
        Command c = new CommandBuilder()
                .setPlugin(core)
                .setUsage("/mineshop")
                .setName("mineshop")
                .setDescription("Command to sell ores.")
                .setExecutor(this)
                .build();
        CommandInjector.injectCommand("shop", c, true);
        this.module = module;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            p.openInventory(module.getMineGUI().getGui().getPages().get(0).getView().getInventory());
        }
        return true;
    }
}
