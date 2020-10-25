package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.builders.CommandBuilder;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import com.azortis.azortislib.configuration.Config;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TNTBankItem extends Patch<Packet<?>> implements ICommandExecutor {

    private PandoraCore plugin;
    private Config<TNTBankItemData> config;

    @Override
    public Config<TNTBankItemData> getConfiguration() {
        return config;
    }

    public TNTBankItem() {

        super("TNTBankItem", "Add to your faction bank by right clicking a chest!", false, false, null, true, "Factions");

    }


    @EventHandler
    public void onClickContainer(PlayerInteractEvent event){

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){

            if(event.getPlayer().getItemInHand() != null){

                ItemStack item = event.getPlayer().getItemInHand();

                if(containsNBT(item, "PandoraTNTBankItem")){

                    if(event.getClickedBlock().getState() instanceof InventoryHolder){

                        FPlayer p = ((FPlayer) event.getPlayer());

                        if(p.getFaction() != null) {

                            InventoryHolder itemInvHolder = ((InventoryHolder) event.getClickedBlock().getState());

                            Inventory itemInv = itemInvHolder.getInventory();

                            List<ItemStack> collect = Arrays.stream(itemInv.getContents()).filter(i -> i.getType() == Material.TNT).collect(Collectors.toList());

                            Faction f = p.getFaction();
                            int addedAmt = 0;
                            for (ItemStack itemStack : collect) {

                                if(f.getTntBankLimit() > f.getTnt()){

                                    f.addTnt(itemStack.getAmount());
                                    addedAmt += itemStack.getAmount();

                                    itemInv.remove(itemStack);

                                }else{
                                    break;
                                }

                            }

                            int amt = collect.stream().flatMapToInt(i -> IntStream.of(i.getAmount())).reduce(0, Integer::sum);

                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfiguration().getConfiguration()
                                    .amtAdded.replace("{amount}", addedAmt+"").replace("{totalAmtInChest}", amt+"")));

                        }else{
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfiguration().getConfiguration().invalidFaction));
                        }
                    }

                }

            }

        }

    }

    public static boolean containsNBT(ItemStack wand, String key){

        try {
            net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(wand);

            if (stack.hasTag()) {
                NBTTagCompound tag = stack.getTag();

                if (tag != null) {

                    return tag.hasKey(key);

                } else return false;
            } else return false;

        }catch(Exception ignored){
            return false;
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(sender instanceof Player || sender instanceof ConsoleCommandSender){

            if(args.length == 1){

                Player player = Bukkit.getPlayerExact(args[0]);

                if(player != null){

                    ItemStack itemStack = new ItemStack(getConfiguration().getConfiguration().material);
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfiguration().getConfiguration().dispName));
                    meta.setLore(getConfiguration().getConfiguration().lore);
                    itemStack.setItemMeta(meta);

                    net.minecraft.server.v1_8_R3.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);
                    NBTTagCompound tag = stack.getTag() != null ? stack.getTag() : new NBTTagCompound();

                    tag.setString("PandoraTNTBankItem", "MakingBank");
                    stack.setTag(tag);

                    itemStack = CraftItemStack.asCraftMirror(stack);

                    if(!player.getInventory().addItem().isEmpty()){
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfiguration().getConfiguration().failedItemAdd));
                    }

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfiguration().getConfiguration().itemAdded.replace("{item}", itemStack.getItemMeta().getDisplayName())));


                }else{
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfiguration().getConfiguration().invalidUser));
                }
                return true;

            }else{
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfiguration().getConfiguration().invalidUsage));
                return false;
            }

        }

        return false;
    }


    @Override
    public void enable(PandoraCore core) {

        Command c = new CommandBuilder()
                .setExecutor(this)
                .setDescription("Add tnt to your faction bank")
                .setName("tntwand")
                .setPlugin(core)
                .setUsage("/tntwand <player>")
                .setPermission("pandora.ftools.give")
                .build();
        PandoraCore.injectCommand("tntwand", c, true);
        config = core.getConfigManager().loadConfig("/patches/TNTBankItem/items.json", new TNTBankItemData());

    }

    @Override
    public void disable(PandoraCore core) {
        PandoraCore.removeCommand("tntwand");
        plugin = null;

    }

    private static class TNTBankItemData {

        public Material material = Material.STICK;
        public List<String> lore = Collections.emptyList();
        public String dispName = "TNT Bank Add";

        public String invalidUsage = "&cInvalid command usage";
        public String invalidUser = "&cCouldn't find player";
        public String itemAdded = "&6 Added item: {item} to your inventory";
        public String failedItemAdd = "&cFailed to add this to their inventory. It is most likely full";
        public String invalidFaction = "&cYou must be in a faction to use this item";
        public String amtAdded = "Added {amount} / {totalAmtInChest} tnt to your faction's bank";

    }


}
