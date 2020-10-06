package dev.minecraftplugin.pandoracore.module.modules.shop.shop;

import com.azortis.azortislib.experimental.inventory.GUI;
import com.azortis.azortislib.experimental.inventory.GUIBuilder;
import com.azortis.azortislib.experimental.inventory.item.Item;
import com.azortis.azortislib.experimental.inventory.item.ItemBuilder;
import com.azortis.azortislib.utils.FormatUtil;
import dev.minecraftplugin.pandoracore.module.modules.shop.ShopModule;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class MineGUI {
    private static final String NBTTAG = "MinedOre";
    private final AmountGUI amountGUI;
    private final Economy economy;
    private final GUI gui;

    public MineGUI(ShopModule module) {
        amountGUI = module.getAmountGUI();
        economy = module.getEconomy();
        GUIBuilder builder = new GUIBuilder("MineShop");
        builder.addPage(45)
                .with(page -> {
                    page.page = 1;
                    page.isGlobal = true;
                    page.name = "&6MineShop";

                    ItemStack filler = ItemBuilder.start(Material.STAINED_GLASS_PANE).name("")
                            .data((short) 15).build();
                    Arrays.fill(page.items, new Item(filler, "dummy", (clickEvent, view)
                            -> clickEvent.setCancelled(true)));
                    for (int i = 0; i < Ores.values().length; i++) {
                        final Ore ore = Ores.values()[i].getOre();
                        ItemStack stack = ItemBuilder.start(ore.material).name(ore.name)
                                .lore("&7Buy Price: &cN/A", "&7Sell Price &a$" + ore.sellPrice, "&9Click with MMB to sell all!")
                                .build();
                        Item item = new Item(stack, ore.name, ((clickEvent, view) -> {
                            clickEvent.setCancelled(true);
                            if (clickEvent.getClick() == ClickType.MIDDLE) {
                                sellItems(-1, ore, (Player) clickEvent.getWhoClicked());
                                return;
                            }
                            clickEvent.getWhoClicked().openInventory(amountGUI.getAmountGUI(ore).getInventory());
                        }));
                        page.items[ore.slot] = item;
                    }
                });
        gui = builder.getGui();
    }


    public GUI getGui() {
        return gui;
    }

    public void sellItems(int amount, Ore ore, Player player) {
        if (amount < 0) {
            // We are selling all possible items for this ore.
            amount = 0;
            int sum = 0;
            for (int i = 0; i < player.getInventory().getContents().length; i++) {
                ItemStack content = player.getInventory().getItem(i);
                if (content != null) {
                    NBTTagCompound compound = CraftItemStack.asNMSCopy(content).getTag();
                    if (compound != null && compound.hasKey(NBTTAG)) {
                        // Is an ore item.
                        if (ChatColor.stripColor(content.getItemMeta().getDisplayName()).equalsIgnoreCase(ore.name) &&
                                content.getType() == ore.material) {
                            // Is our item
                            amount += content.getAmount();
                            sum += (content.getAmount() * ore.sellPrice);
                            player.getInventory().setItem(i, null);
                        }
                    }
                }
            }
            if (amount > 0) {
                economy.depositPlayer(player, sum);
                player.sendMessage(FormatUtil.color("&7You have sold &bx" + amount + " " + ore.name + " for &a$" + sum));
            }
            return;
        }
        // We are selling only the
        int tempAmount = amount;
        amount = 0;
        int sum = 0;
        for (int i = 0; i < player.getInventory().getContents().length && tempAmount > 0; i++) {
            ItemStack content = player.getInventory().getItem(i);
            if (content != null) {
                NBTTagCompound compound = CraftItemStack.asNMSCopy(content).getTag();
                if (compound != null && compound.hasKey(NBTTAG)) {
                    // Is an ore item.
                    if (ChatColor.stripColor(content.getItemMeta().getDisplayName()).equalsIgnoreCase(ore.name) &&
                            content.getType() == ore.material) {
                        // Is our item
                        if (content.getAmount() > tempAmount) {
                            int leftover = content.getAmount() - tempAmount;
                            content.setAmount(leftover);
                            amount += tempAmount;
                            sum += (tempAmount * ore.sellPrice);
                            player.getInventory().setItem(i, content);
                            break;
                        } else {
                            amount += content.getAmount();
                            tempAmount -= content.getAmount();
                            sum += (content.getAmount() * ore.sellPrice);
                            player.getInventory().setItem(i, null);
                        }
                    }
                }
            }
        }
        if (amount > 0) {
            economy.depositPlayer(player, sum);
            player.sendMessage(FormatUtil.color("&7You have sold &bx" + amount + " " + ore.name + " for &a$" + sum));
        }
    }

    private enum Ores {
        QUARTZ(new Ore("Quartz Gem", 10000, 10, Material.QUARTZ)),
        QUARTZP(new Ore("Purified Quartz Gem", 30000, 11, Material.QUARTZ)),
        EMERALD(new Ore("Emerald Gem", 5000, 12, Material.EMERALD)),
        EMERALDP(new Ore("Purified Emerald Gem", 15000, 13, Material.EMERALD)),
        DIAMOND(new Ore("Diamond Gem", 3000, 14, Material.DIAMOND)),
        DIAMONDP(new Ore("Purified Diamond Gem", 9000, 15, Material.DIAMOND)),
        REDSTONE(new Ore("Redstone Dust", 500, 16, Material.REDSTONE)),
        REDSTONEP(new Ore("Purified Redstone Dust", 1500, 19, Material.REDSTONE)),
        GOLD(new Ore("Gold Gem", 1000, 20, Material.GOLD_INGOT)),
        GOLDP(new Ore("Purified Gold Gem", 3000, 21, Material.GOLD_INGOT)),
        IRON(new Ore("Iron Gem", 750, 22, Material.IRON_INGOT)),
        IRONP(new Ore("Purified Iron Gem", 2250, 23, Material.IRON_INGOT)),
        COAL(new Ore("Coal Gem", 100, 24, Material.COAL)),
        COALP(new Ore("Purified Coal Gem", 300, 25, Material.COAL));

        private final Ore ore;

        Ores(Ore ore) {
            this.ore = ore;
        }

        public Ore getOre() {
            return ore;
        }
    }

    public static class Ore {
        public String name;
        public int sellPrice;
        public int slot;
        public Material material;

        public Ore(String name, int sellPrice, int slot, Material material) {
            this.name = name;
            this.sellPrice = sellPrice;
            this.slot = slot;
            this.material = material;
        }
    }
}
