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
//        QUARTZ(new Ore("&7&oQuartz Gem")),
//        QUARTZP(new Ore("&b&oPurified Quartz Gem")),
//        EMERALD(new Ore("&7&oEmerald Gem")),
//        EMERALDP(new Ore("&b&oPurified Emerald Gem")),
//        DIAMOND(new Ore("&7&oDiamond Gem")),
//        DIAMONDP(new Ore("&b&oPurified Diamond Gem")),
//        REDSTONE(new Ore("&7&oRedstone Dust")),
//        REDSTONEP(new Ore("&b&oPurified Redstone Dust")),
//        GOLD(new Ore("&7&oGold Gem")),
//        GOLDP(new Ore("&b&oPurified Gold Gem")),
//        IRON(new Ore("&7&oIron Gem")),
//        IRONP(new Ore("&b&oPurified Iron Gem")),
//        COAL(new Ore("&7&oCoal Gem")),
//        COALP(new Ore("&b&oPurified Coal Gem")),
//        SPECIAL(new Ore("&7&oSpecial Gem")),
//        SPECIALP(new Ore("&b&oSpecial Purified Gem"));
        ;

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
