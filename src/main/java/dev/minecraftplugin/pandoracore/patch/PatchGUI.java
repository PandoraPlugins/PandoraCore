package dev.minecraftplugin.pandoracore.patch;

import com.azortis.azortislib.experimental.inventory.*;
import com.azortis.azortislib.experimental.inventory.impl.v1_15.PageableGUIBuilder;
import dev.minecraftplugin.pandoracore.PandoraCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class PatchGUI {
    private final GUI gui;

    public PatchGUI() {
        gui = createGUI();
    }

    private GUI createGUI() {
        return GUIManager.getInstance().getEngine().createGUI(
                new PageableGUIBuilder().with(gui -> {
                    ((PageableGUIBuilder) gui).pageSize = 54;
                    ((PageableGUIBuilder) gui).pages = (EPatch.values().length / 45) + 1 + (EPatch.values().length % 45 == 0 ? 0 : 1);
                    gui.inventorySize = 54;
                    gui.isConfigurable = false;
                    gui.isGlobal = false;
                    gui.uniqueName = "PCPatchManager";
                    gui.inventoryTitle = "&6Patches";

                    for (int i = 0; i < EPatch.values().length; i++) {
                        final int page = (i / 45) + 1;
                        final int finalI = i;
                        final EPatch itemPatch = EPatch.values()[i];
                        gui.item().with(item -> {
                            item.itemName = itemPatch.getPatch().getName();
                            ((PageableGUIBuilder.PageableItemBuilder) item).page = page;
                            item.slot = finalI % 45;
                            item.itemStack = StackBuilder.start(Material.STAINED_GLASS_PANE)
                                    .name("&" + (itemPatch.getPatch().isEnabled() ? "c" : "a")
                                            + itemPatch.getPatch().getName().toUpperCase())
                                    .data((short) (itemPatch.getPatch().isEnabled() ? 5 : 14))
                                    .lore("&b&oClick me to enable/disable this patch!", "&l",
                                            "&b&o" + itemPatch.getPatch().getDescription())
                                    .build();
                            item.action = clickEvent -> {
                                clickEvent.setCancelled(true);
                                EPatch patch = EPatch.getValue(ChatColor.stripColor(
                                        ((Page) clickEvent.getInventory().getHolder())
                                                .getGUI().getItems()[clickEvent.getSlot()].getItemStack().getItemMeta()
                                                .getDisplayName()));
                                if (patch == null) {
                                    System.out.println("Error getting the patch!");
                                    return;
                                }
                                if (patch.getPatch().isEnabled())
                                    PandoraCore.getInstance().getPatchManager().disablePatch(patch);
                                else PandoraCore.getInstance().getPatchManager().enablePatch(patch);
                                Item aItem = new Item(StackBuilder.start(((Page) clickEvent.getInventory().getHolder()).getGUI()
                                        .getItems()[clickEvent.getSlot()].getItemStack())
                                        .data((short) (itemPatch.getPatch().isEnabled() ? 5 : 14))
                                        .name("&" + (itemPatch.getPatch().isEnabled() ? "c" : "a")
                                                + itemPatch.getPatch().getName().toUpperCase())
                                        .build(),
                                        ((Page) clickEvent.getInventory().getHolder()).getGUI().getItems()
                                                [clickEvent.getSlot()].getAction());
                                clickEvent.getInventory().setItem(clickEvent.getSlot(), aItem.getItemStack());
                                ((Page) clickEvent.getInventory().getHolder()).getGUI().getItems()[clickEvent.getSlot()] =
                                        aItem;

                            };
                        }).add();
                    }
                    for (int i = 0; i < ((PageableGUIBuilder) gui).pages; i++) {
                        int finalI = i;
                        for (int slot = 45; slot < 54; slot++) {
                            int finalSlot = slot;
                            if (slot == 45) {
                                gui.item().with(item -> {
                                    item.itemName = "BackButton" + finalI;
                                    ((PageableGUIBuilder.PageableItemBuilder) item).page = finalI;
                                    item.slot = finalSlot;
                                    item.action = clickEvent -> {
                                        clickEvent.setCancelled(true);
                                        if (finalI > 1) {
                                            clickEvent.getWhoClicked()
                                                    .openInventory(((PageableGUI) ((Page) clickEvent.getInventory().getHolder()).getGUI())
                                                            .getInventory(finalI - 1));
                                        }
                                    };
                                    item.itemStack = StackBuilder.start(Material.STAINED_GLASS_PANE)
                                            .name("&bBack Button").data((short) 3).build();

                                }).add();
                            } else if (slot == 53) {
                                gui.item().with(item -> {
                                    item.itemName = "ForwardButton" + finalI;
                                    ((PageableGUIBuilder.PageableItemBuilder) item).page = finalI;
                                    item.slot = finalSlot;
                                    item.action = clickEvent -> {
                                        if (finalI < ((PageableGUIBuilder) gui).pages) {
                                            clickEvent.getWhoClicked()
                                                    .openInventory(((PageableGUI) ((Page) clickEvent.getInventory().getHolder()).getGUI())
                                                            .getInventory(finalI + 1));
                                        }
                                    };
                                    item.itemStack = StackBuilder.start(Material.STAINED_GLASS_PANE)
                                            .name("&bForward Button").data((short) 3).build();

                                }).add();
                            } else {
                                gui.item().with(item -> {
                                    item.itemName = "DummyButton" + finalI;
                                    ((PageableGUIBuilder.PageableItemBuilder) item).page = finalI;
                                    item.slot = finalSlot;
                                    item.action = clickEvent -> clickEvent.setCancelled(true);
                                    item.itemStack = StackBuilder.start(Material.STAINED_GLASS_PANE)
                                            .name("&l").data((short) 15).build();

                                }).add();
                            }

                        }
                    }
                })
        );
    }

    public GUI getGui() {
        return gui;
    }
}
