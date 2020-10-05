package dev.minecraftplugin.pandoracore.module.modules.shop;

import com.azortis.azortislib.command.CommandInjector;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.module.Module;
import dev.minecraftplugin.pandoracore.module.modules.shop.amount.AmountGUI;
import dev.minecraftplugin.pandoracore.module.modules.shop.shop.MineGUI;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;

public class ShopModule extends Module<Packet<?>> {
    private Economy economy;
    private MineGUI mineGUI;
    private AmountGUI amountGUI;

    public ShopModule() {
        super("ShopModule", "Adds a hardcoded shop for totems, fishing, and mines.", false, false,
                null, false, "Vault");
    }

    public MineGUI getMineGUI() {
        return mineGUI;
    }

    public AmountGUI getAmountGUI() {
        return amountGUI;
    }

    public Economy getEconomy() {
        return economy;
    }

    @Override
    public void enable(PandoraCore core) {
        economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
        if (economy == null) {
            System.out.println("Error! Could not load ShopModule due to no economy provider!");
            disable(core);
            return;
        }
        new ShopCommand(core, this);
        new AmountGUI();
        new MineGUI(this);
    }

    @Override
    public void disable(PandoraCore core) {
        CommandInjector.removeCommand("mineshop");
        mineGUI = null;
        amountGUI = null;
    }


}
