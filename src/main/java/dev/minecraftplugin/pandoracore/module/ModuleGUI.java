package dev.minecraftplugin.pandoracore.module;


import com.azortis.azortislib.inventory.GUI;
import com.azortis.azortislib.inventory.GUIBuilder;
import com.azortis.azortislib.inventory.View;
import com.azortis.azortislib.inventory.item.Item;
import com.azortis.azortislib.inventory.item.ItemBuilder;
import dev.minecraftplugin.pandoracore.PandoraCore;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.function.BiConsumer;

public class ModuleGUI {
    private GUI gui;

    public ModuleGUI() {
        gui = createGUI();
    }

    private GUI createGUI() {
        final int pageSize = 45;
        final String pageName = "ModuleGUI ";
        int pages = (EModule.values().length / pageSize) + 1;
        GUIBuilder builder = new GUIBuilder("ModuleGUI");
        for (int i = 0; i < pages; i++) {
            int finalI = i;
            builder.addPage(pageSize)
                    .with(page -> {
                        page.isGlobal = true;
                        page.page = finalI;
                        page.name = pageName + "Page: &b" + (finalI + 1);
                        Item[] items = new Item[pageSize];
                        for (int ia = 0; ia < EModule.values().length; ia++) {
                            final boolean enabled = EModule.values()[ia].getModule().isEnabled();
                            ItemStack itemStack = ItemBuilder.start(Material.STAINED_GLASS_PANE)
                                    .data(enabled ? (short) 5 : (short) 14)
                                    .name((enabled ? "&a" : "&c") + EModule.values()[ia].getModule().getName())
                                    .lore(enabled ? "&a&oENABLED" : "&c&oDISABLED",
                                            "&b&o" + EModule.values()[ia].getModule().getDescription()
                                            , "&b&oRequires: " + Arrays.toString(EModule.values()[ia].getModule().getDependencies()),
                                            (enabled ? "&b&nClick me to &c&nDISABLE&b&n this patch!"
                                                    : "&b&nClick me to &a&nENABLE&b&n this patch!"))
                                    .build();
                            final int finalIa = ia;
                            final BiConsumer<InventoryClickEvent, View> action = (clickEvent, view) -> {
                                clickEvent.setCancelled(true);
                                if (EModule.values()[finalIa].getModule().isEnabled()) {
                                    PandoraCore.getInstance().getModuleManager().disableModule(EModule.values()[finalIa]);
                                } else {
                                    PandoraCore.getInstance().getModuleManager().enableModule(EModule.values()[finalIa]);
                                }
                                ItemStack newStack = ItemBuilder.start(Material.STAINED_GLASS_PANE)
                                        .data(EModule.values()[finalIa].getModule().isEnabled() ? (short) 5 : (short) 14)
                                        .name((EModule.values()[finalIa].getModule().isEnabled() ? "&a" : "&c") + EModule.values()[finalIa].getModule().getName())
                                        .lore(EModule.values()[finalIa].getModule().isEnabled() ? "&a&oENABLED" : "&c&oDISABLED",
                                                "&b&o" + EModule.values()[finalIa].getModule().getDescription()
                                                , "&b&oRequires: " +
                                                        Arrays.toString(EModule.values()[finalIa].getModule().getDependencies()),
                                                (EModule.values()[finalIa].getModule().isEnabled() ? "&b&nClick me to &c&nDISABLE&b&n this patch!"
                                                        : "&b&nClick me to &a&nENABLE&b&n this patch!"))
                                        .build();
                                view.getInventory().setItem(clickEvent.getSlot(), newStack);
                                Item item = view.getPage().getItems()[clickEvent.getSlot()];
                                view.getPage().getItems()[clickEvent.getSlot()]
                                        = new Item(newStack, item.getUniqueName(), item.getEventConsumer());
                            };
                            items[ia] = new Item(itemStack, EModule.values()[ia].getModule().getName(),
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
