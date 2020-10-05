package dev.minecraftplugin.pandoracore.module.modules.shop.shop;

import com.azortis.azortislib.experimental.inventory.GUI;
import com.azortis.azortislib.experimental.inventory.GUIBuilder;
import dev.minecraftplugin.pandoracore.module.modules.shop.ShopModule;
import dev.minecraftplugin.pandoracore.module.modules.shop.amount.AmountGUI;

public class MineGUI {
    private final AmountGUI amountGUI;
    private final GUI gui;

    public MineGUI(ShopModule module) {
        amountGUI = module.getAmountGUI();
        GUIBuilder builder = new GUIBuilder("MineShop");
        builder.addPage(45)
                .with(page -> {
                    page.page = 1;
                    page.isGlobal = true;
                    page.name = "&6MineShop";

                    String colorPrefix = "&o";
                    String colorPurifiedPrefix = "&b&o";

                    for (int i = 0; i < Ores.values().length; i++) {

                    }
                });
        gui = builder.getGui();
    }


    public GUI getGui() {
        return gui;
    }

    private enum Ores {
        QUARTZ(new Ore("&7&oQuartz Gem", 10000, 0)),
        QUARTZP(new Ore("&b&oPurified Quartz Gem", 30000, 0)),
        EMERALD(new Ore("&7&oEmerald Gem", 5000, 0)),
        EMERALDP(new Ore("&b&oPurified Emerald Gem", 15000, 0)),
        DIAMOND(new Ore("&7&oDiamond Gem", 3000, 0)),
        DIAMONDP(new Ore("&b&oPurified Diamond Gem", 9000, 0)),
        REDSTONE(new Ore("&7&oRedstone Dust",  500, 0)),
        REDSTONEP(new Ore("&b&oPurified Redstone Dust", 1500, 0)),
        GOLD(new Ore("&7&oGold Gem", 1000, 0)),
        GOLDP(new Ore("&b&oPurified Gold Gem", 3000, 0)),
        IRON(new Ore("&7&oIron Gem", 750, 0)),
        IRONP(new Ore("&b&oPurified Iron Gem", 2250, 0)),
        COAL(new Ore("&7&oCoal Gem", 100, 0)),
        COALP(new Ore("&b&oPurified Coal Gem", 300, 0));

        private final Ore ore;

        Ores(Ore ore) {
            this.ore = ore;
        }

        public Ore getOre() {
            return ore;
        }
    }

    private static class Ore {
        public String name;
        public int sellPrice;
        public int slot;

        public Ore(String name, int sellPrice, int slot) {
            this.name = name;
            this.sellPrice = sellPrice;
            this.slot = slot;
        }
    }
}
