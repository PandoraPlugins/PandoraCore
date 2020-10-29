package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.builders.CommandBuilder;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import com.azortis.azortislib.configuration.Config;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.ess3.api.Economy;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;

public class RenameItemPatch extends Patch<Packet<?>> implements ICommandExecutor {
    private PandoraCore plugin;
    private Config<RenameItemPatchData> config;

    @Override
    public Config<RenameItemPatchData> getConfiguration() {
        return config;
    }

    public RenameItemPatch() {

        super("RenameItemPatch", "Rename items for a price!", false, false,
                null, false, "Essentials");


    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if(command.getName().equalsIgnoreCase("rename")){

            if(sender instanceof Player){

                Player player = ((Player) sender);

                if(strings.length > 0) {

                    if (player.getInventory().getItemInHand() != null) {
                        ItemStack item = player.getInventory().getItemInHand();

                        ItemMeta meta = item.getItemMeta();
                        String name = ChatColor.translateAlternateColorCodes('&', String.join(" ", strings));
                        meta.setDisplayName(name);

                        User user = Essentials.getPlugin(Essentials.class).getUser(player.getUniqueId());

                        if (user.canAfford(BigDecimal.valueOf(config.getConfiguration().cost))) {
                            player.sendMessage(ChatColor.RED + "Insufficient amount to pay. Need at least $" + config.getConfiguration().cost + " to use this command");
                            return true;
                        }

                        item.setItemMeta(meta);

                        user.takeMoney(BigDecimal.valueOf(config.getConfiguration().cost));
                        player.sendMessage(ChatColor.GREEN + "Renamed your current item!");

                    } else {
                        player.sendMessage(ChatColor.RED + "You are currently not holding anything to rename");
                    }
                }else{
                    player.sendMessage(ChatColor.RED+"Please specify what to rename this item to");
                }
            }else {
                sender.sendMessage(ChatColor.RED+"Only players may use this command");
            }
            return true;

        }

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
