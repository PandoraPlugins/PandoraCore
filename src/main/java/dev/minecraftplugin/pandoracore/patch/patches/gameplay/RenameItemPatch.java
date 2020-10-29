package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.builders.CommandBuilder;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import com.azortis.azortislib.configuration.Config;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class RenameItemPatch extends Patch<Packet<?>> implements ICommandExecutor {
    private PandoraCore plugin;
    private Config<RenameItemPatchData> config;
    private final HashMap<UUID, Boolean> usersLooking = new HashMap<>();

    @Override
    public Config<RenameItemPatchData> getConfiguration() {
        return config;
    }

    public RenameItemPatch() {

        super("RenameItemPatch", "Rename items for a price!", false, false,
                null, true, "Essentials");


    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event){

        if(usersLooking.containsKey(event.getPlayer().getUniqueId())){

            usersLooking.remove(event.getPlayer().getUniqueId());
            event.getPlayer().sendMessage(ChatColor.RED+"Cancelled the rename request");

        }

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event){

        if(usersLooking.containsKey(event.getWhoClicked().getUniqueId())){

            Player player = ((Player) event.getWhoClicked());

            ItemStack item = event.getCurrentItem();
            if (TNTBankItem.containsNBT(item, "shop")) {
                String itemName = item.getItemMeta().getDisplayName();

                if(itemName.toLowerCase().contains("confirm")){

                    User user = Essentials.getPlugin(Essentials.class).getUser(player.getUniqueId());

                    if (user.canAfford(BigDecimal.valueOf(config.getConfiguration().cost))) {
                        player.sendMessage(ChatColor.RED + "Insufficient amount to pay. Need at least $" + config.getConfiguration().cost + " to use this command");
                        player.closeInventory();
                        usersLooking.remove(player.getUniqueId());

                        return;
                    }

                    user.takeMoney(BigDecimal.valueOf(config.getConfiguration().cost));
                    player.closeInventory();

                    player.sendMessage(ChatColor.GREEN + "Renamed your current item!");



                }else if(itemName.toLowerCase().contains("cancel")){

                    player.closeInventory();
                    player.sendMessage(ChatColor.GOLD+"Cancelled the rename request");

                }

            }

            usersLooking.remove(player.getUniqueId());
            event.setCancelled(true);

        }

    }


    public Inventory genConfigInv(Player player, ItemStack item){

        Inventory confimInv = Bukkit.createInventory(player, 9, "Rename Confirmation");

        ItemStack greenConf = makeItem(Material.STAINED_GLASS_PANE, ChatColor.DARK_GREEN+""+ChatColor.BOLD+"Confirm?", (short)13);
        ItemMeta itemMeta = greenConf.getItemMeta();
        itemMeta.setLore(Collections.singletonList("Rename for " + ChatColor.GREEN + "$" + config.getConfiguration().cost));
        greenConf.setItemMeta(itemMeta);

        ItemStack redDeny = makeItem(Material.STAINED_GLASS_PANE, ChatColor.DARK_RED+""+ChatColor.BOLD+"Cancel?", (short)14);
        ItemStack border = makeItem(Material.STAINED_GLASS_PANE, ChatColor.BLACK+"-", (short)15);

        confimInv.setItem(0, greenConf);
        confimInv.setItem(8, redDeny);
        confimInv.setItem(4, item);

        for(int i = 0; i < confimInv.getSize(); i++)
            if(confimInv.getItem(i) != null)
                confimInv.setItem(i, border);

        return confimInv;

    }

    public static ItemStack makeItem(Material mat, String name, short amt){


            ItemStack item = new ItemStack(mat, 1, amt);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            item.setItemMeta(meta);
            return item;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if(command.getName().equalsIgnoreCase("rename")){

            if(sender instanceof Player){

                Player player = ((Player) sender);

                if(strings.length > 0) {

                    if (player.getInventory().getItemInHand() != null) {
                        ItemStack item = player.getInventory().getItemInHand().clone();

                        ItemMeta meta = item.getItemMeta();
                        String name = ChatColor.translateAlternateColorCodes('&', String.join(" ", strings));
                        meta.setDisplayName(name);
                        item.setItemMeta(meta);

                        usersLooking.put(player.getUniqueId(), true);

                        player.openInventory(genConfigInv(player, item));

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
        usersLooking.clear();
    }

    private static class RenameItemPatchData {
        public int cost = 1000000;
    }
}
