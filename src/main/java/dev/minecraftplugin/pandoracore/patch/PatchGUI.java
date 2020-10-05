package dev.minecraftplugin.pandoracore.patch;

import com.azortis.azortislib.experimental.inventory.GUI;
import com.azortis.azortislib.experimental.inventory.GUIBuilder;
import com.azortis.azortislib.experimental.inventory.View;
import com.azortis.azortislib.experimental.inventory.item.Item;
import com.azortis.azortislib.experimental.inventory.item.ItemBuilder;
import dev.minecraftplugin.pandoracore.PandoraCore;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class PatchGUI {
    private GUI gui;

    public PatchGUI() {
        gui = createGUI();
    }

    private GUI createGUI() {
        final int pageSize = 45;
        final String pageName = "PatchGUI ";
        int pages = (EPatch.values().length / pageSize) + 1;
        GUIBuilder builder = new GUIBuilder("PatchGUI");
        for (int i = 0; i < pages; i++) {
            int finalI = i;
            builder.addPage(pageSize)
                    .with(page -> {
                        page.isGlobal = true;
                        page.page = finalI;
                        page.name = pageName + "Page: &b" + finalI;
                        Item[] items = new Item[pageSize];
                        for (int ia = 0; ia < EPatch.values().length; ia++) {
                            final boolean enabled = EPatch.values()[ia].getPatch().isEnabled();
                            ItemStack itemStack = ItemBuilder.start(Material.STAINED_GLASS_PANE)
                                    .data(enabled ? (short) 5 : (short) 14)
                                    .name((enabled ? "&a" : "&c") + EPatch.values()[ia].getPatch().getName())
                                    .lore(enabled ? "&a&oENABLED" : "&c&oDISABLED",
                                            "&b&o" + EPatch.values()[ia].getPatch().getDescription()
                                            , "&b&oRequires: " + Arrays.toString(EPatch.values()[ia].getPatch().getDependencies()),
                                            (enabled ? "&b&nClick me to &c&nDISABLE&b&n this patch!"
                                                    : "&b&nClick me to &a&nENABLE&b&n this patch!"))
                                    .build();
                            final int finalIa = ia;
                            final BiConsumer<InventoryClickEvent, View> action = (clickEvent, view) -> {
                                clickEvent.setCancelled(true);
                                if (EPatch.values()[finalIa].getPatch().isEnabled()) {
                                    EPatch.values()[finalIa].getPatch().disable(PandoraCore.getInstance());
                                } else {
                                    EPatch.values()[finalIa].getPatch().enable(PandoraCore.getInstance());
                                }
                                ItemStack newStack = ItemBuilder.start(Material.STAINED_GLASS_PANE)
                                        .data(enabled ? (short) 5 : (short) 14)
                                        .name((enabled ? "&a" : "&c") + EPatch.values()[finalIa].getPatch().getName())
                                        .lore(enabled ? "&a&oENABLED" : "&c&oDISABLED",
                                                "&b&o" + EPatch.values()[finalIa].getPatch().getDescription()
                                                , "&b&oRequires: " +
                                                        Arrays.toString(EPatch.values()[finalIa].getPatch().getDependencies()),
                                                (enabled ? "&b&nClick me to &c&nDISABLE&b&n this patch!"
                                                        : "&b&nClick me to &a&nENABLE&b&n this patch!"))
                                        .build();
                                view.getInventory().setItem(clickEvent.getSlot(), newStack);
                                Item item = view.getPage().getItems()[clickEvent.getSlot()];
                                view.getPage().getItems()[clickEvent.getSlot()]
                                        = new Item(newStack, item.getUniqueName(), item.getEventConsumer());
                            };
                            items[ia] = new Item(itemStack, EPatch.values()[ia].getPatch().getName(),
                                    action);
                        }
                        page.items = items;
                    });
        }
        gui = builder.getGui();
        return gui;
    }

    public GUI getGui() {
        return gui;
    }
}
