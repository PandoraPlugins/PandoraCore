package dev.minecraftplugin.pandoracore.module.modules.shop.shop;


import com.azortis.azortislib.inventory.GUI;
import com.azortis.azortislib.inventory.GUIBuilder;
import com.azortis.azortislib.inventory.Page;
import com.azortis.azortislib.inventory.View;
import com.azortis.azortislib.inventory.item.Item;
import com.azortis.azortislib.inventory.item.ItemBuilder;
import dev.minecraftplugin.pandoracore.module.modules.shop.ShopModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;


public class AmountGUI {
    private final GUI amountGUI;

    public AmountGUI(final ShopModule module) {
        GUIBuilder builder = new GUIBuilder("MineShopAmountGUI");
        Item[] items = new Item[45];
        BiConsumer<InventoryCloseEvent, View> action = (event, view) ->
                module.getMineGUI().sellItems(((AmountView) view).getAmount(), ((AmountView) view).ore,
                        (Player) event.getPlayer());
        AmountPage page = new AmountPage(builder.getGui(), 1, "MineShop", false, 45, action);
        ItemStack addSingle = ItemBuilder.start(Material.STAINED_GLASS_PANE).name("&aAdd 1")
                .data((short) 5).build();
        ItemStack addTen = ItemBuilder.start(Material.STAINED_GLASS_PANE).name("&aAdd 10")
                .data((short) 5).build();
        ItemStack addStack = ItemBuilder.start(Material.STAINED_GLASS_PANE).name("&aAdd 64")
                .data((short) 5).build();
        ItemStack filler = ItemBuilder.start(Material.STAINED_GLASS_PANE).name("")
                .data((short) 15).build();
        ItemStack removeSingle = ItemBuilder.start(Material.STAINED_GLASS_PANE).name("&cRemove 1")
                .data((short) 14).build();
        ItemStack removeTen = ItemBuilder.start(Material.STAINED_GLASS_PANE).name("&cRemove 10")
                .data((short) 14).build();
        ItemStack removeStack = ItemBuilder.start(Material.STAINED_GLASS_PANE).name("&cRemove 64")
                .data((short) 14).build();
        ItemStack ore = ItemBuilder.start(Material.BEDROCK).build();
        for (int i = 0; i < 45; i += 9) {
            items[i] = new Item(filler, "dummy", (clickEvent, view) -> clickEvent.setCancelled(true));
            items[i + 1] = new Item(addStack, "addStack", (clickEvent, view) -> {
                clickEvent.setCancelled(true);
                ((AmountView) view).setAmount(((AmountView) view).getAmount() + 64);
                ItemStack stack = ItemBuilder.start(view.getInventory().getItem(22)).lore("&7Sell &bx" +
                        ((AmountView) view).getAmount()).amount(Math.min(((AmountView) view).getAmount(), 64)).build();
                view.getInventory().setItem(22, stack);
            });
            items[i + 2] = new Item(addTen, "addTen", (clickEvent, view) -> {
                clickEvent.setCancelled(true);
                ((AmountView) view).setAmount(((AmountView) view).getAmount() + 10);
                ItemStack stack = ItemBuilder.start(view.getInventory().getItem(22)).lore("&7Sell &bx" +
                        ((AmountView) view).getAmount()).amount(Math.min(((AmountView) view).getAmount(), 64)).build();
                view.getInventory().setItem(22, stack);
            });
            items[i + 3] = new Item(addSingle, "addSingle", (clickEvent, view) -> {
                clickEvent.setCancelled(true);
                ((AmountView) view).setAmount(((AmountView) view).getAmount() + 1);
                ItemStack stack = ItemBuilder.start(view.getInventory().getItem(22)).lore("&7Sell &bx" +
                        ((AmountView) view).getAmount()).amount(Math.min(((AmountView) view).getAmount(), 64)).build();
                view.getInventory().setItem(22, stack);
            });
            items[i + 4] = new Item(filler, "dummy", (clickEvent, view) -> clickEvent.setCancelled(true));
            items[i + 5] = new Item(removeSingle, "removeSingle", (clickEvent, view) -> {
                clickEvent.setCancelled(true);
                if (((AmountView) view).getAmount() > 1) {

                    ((AmountView) view).setAmount(((AmountView) view).getAmount() - 1);
                    ItemStack stack = ItemBuilder.start(view.getInventory().getItem(22)).lore("&7Sell &bx" +
                            ((AmountView) view).getAmount()).amount(Math.min(((AmountView) view).getAmount(), 64)).build();
                    view.getInventory().setItem(22, stack);
                }
            });
            items[i + 6] = new Item(removeTen, "removeTen", (clickEvent, view) -> {
                clickEvent.setCancelled(true);
                if (((AmountView) view).getAmount() > 10) {

                    ((AmountView) view).setAmount(((AmountView) view).getAmount() - 10);
                    ItemStack stack = ItemBuilder.start(view.getInventory().getItem(22)).lore("&7Sell &bx" +
                            ((AmountView) view).getAmount()).amount(Math.min(((AmountView) view).getAmount(), 64)).build();
                    view.getInventory().setItem(22, stack);
                }
            });
            items[i + 7] = new Item(removeStack, "removeStack", (clickEvent, view) -> {
                clickEvent.setCancelled(true);
                if (((AmountView) view).getAmount() > 64) {
                    ((AmountView) view).setAmount(((AmountView) view).getAmount() - 64);
                    ItemStack stack = ItemBuilder.start(view.getInventory().getItem(22)).lore("&7Sell &bx" +
                            ((AmountView) view).getAmount()).amount(Math.min(((AmountView) view).getAmount(), 64)).build();
                    view.getInventory().setItem(22, stack);
                }
            });
            items[i + 8] = new Item(filler, "dummy", (clickEvent, view) -> clickEvent.setCancelled(true));
        }
        items[22] = new Item(ore, "oreItem", (clickEvent, view) -> {
            clickEvent.setCancelled(true);
            clickEvent.getWhoClicked().openInventory(module.getMineGUI().getGui().getPages().get(0).getView().getInventory());
        });

        page.setItems(items);
        builder.getGui().getPages().add(page);
        amountGUI = builder.getGui();
    }

    public View getAmountGUI(final MineGUI.Ore ore) {
        View v = ((AmountPage) amountGUI.getPages().get(0)).getView(ore);
        ItemStack stack = ItemBuilder.start(ore.material).name(ore.name).lore("&7Sell &bx1").build();
        v.getInventory().setItem(22, stack);
        return v;
    }

    private static class AmountPage extends Page {
        public AmountPage(GUI gui, int page, String name, boolean isGlobal, int pageSize, BiConsumer<InventoryCloseEvent, View> closeAction) {
            super(gui, page, name, isGlobal, pageSize, closeAction);
        }

        public AmountView getView(MineGUI.Ore ore) {
            AmountView view = new AmountView(ore, this);
            getViews().add(view);
            return view;
        }
    }

    private static class AmountView extends View {
        private final MineGUI.Ore ore;
        private int amount = 1;

        public AmountView(MineGUI.Ore ore, Page page) {
            super(page);
            this.ore = ore;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
