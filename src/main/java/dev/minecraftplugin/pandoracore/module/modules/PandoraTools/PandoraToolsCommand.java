package dev.minecraftplugin.pandoracore.module.modules.PandoraTools;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.builders.CommandBuilder;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import com.azortis.azortislib.configuration.Config;
import com.earth2me.essentials.items.FlatItemDb;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PandoraToolsCommand extends Patch<Packet<?>> implements ICommandExecutor {

    private PandoraCore plugin;
    private Config<PandoraToolsCommandData> config;

    @Override
    public Config<PandoraToolsCommandData> getConfiguration() {
        return config;
    }

    public PandoraToolsCommand() {

        super("PandoraTools", "Adds custom factions tools", false, false, null, true, "Factions");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(sender instanceof Player || sender instanceof ConsoleCommandSender){

            if(args.length == 2){

                Player player = Bukkit.getPlayerExact(args[0]);

                if(player != null){

                    ItemData item = getConfiguration().getConfiguration().items.stream().filter(i -> i.getName().equalsIgnoreCase(args[1])).findFirst().orElse(null);

                    if(item != null) {
                        ItemStack itemStack = new ItemStack(item.getMaterial());
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', item.getName()));
                        meta.setLore(item.getLore());
                        itemStack.setItemMeta(meta);

                        net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
                        NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();

                        tag.setString("PandoraTNTBankItem", "MakingBank");
                        stack.setTag(tag);

                        itemStack = CraftItemStack.asCraftMirror(stack);

                        if(!player.getInventory().addItem().isEmpty()){
                            sender.sendMessage(ChatColor.RED+"Failed to add this to their inventory. It is most likely full");
                        }

                        player.sendMessage(ChatColor.GOLD+"Added item: " + itemStack.getItemMeta().getDisplayName() + " to your inventory");

                    }else{
                        sender.sendMessage(ChatColor.RED+"Couldn't find this item");
                    }


                }else{
                    sender.sendMessage(ChatColor.RED+"Couldn't find player");
                }
                return true;

            }else{
                sender.sendMessage(ChatColor.RED+"Invalid command usage");
                return false;
            }

        }

        return false;
    }


    @Override
    public void enable(PandoraCore core) {

        Command c = new CommandBuilder()
                .setExecutor(this)
                .setDescription("Safely quit the server.")
                .setName("ftools")
                .setPlugin(core)
                .setUsage("/ftools <user> <item>")
                .setPermission("pandora.ftools.give")
                .build();
        PandoraCore.injectCommand("ftools", c, true);
        config = core.getConfigManager().loadConfig("/modules/PandoraTools/items.json", new PandoraToolsCommandData());

    }

    @Override
    public void disable(PandoraCore core) {
        PandoraCore.removeCommand("ftools");
        plugin = null;

    }

    private static class PandoraToolsCommandData {
        public Set<ItemData> items = new HashSet<>(Arrays.asList(new ItemData(Material.STICK, "Tool", Collections.emptyList(), "")));

    }

}
