package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import com.azortis.azortislib.command.Command;
import com.azortis.azortislib.command.builders.CommandBuilder;
import com.azortis.azortislib.command.executors.ICommandExecutor;
import com.azortis.azortislib.configuration.Config;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.minecraft.server.v1_8_R3.Packet;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomPotionPatch extends Patch<Packet<?>> implements ICommandExecutor {

    private PandoraCore plugin;

    @Override
    public void enable(PandoraCore core) {

        this.plugin = core;
        Command c = new CommandBuilder()
                .setPlugin(core)
                .setUsage("/custpotion <player> <potion data>")
                .setName("custpotion")
                .setDescription("Custom Potion Effects")
                .setPermission("custompotion.give")
                .setExecutor(this)
                .build();
        PandoraCore.injectCommand("custpotion", c, true);

    }

    @Override
    public void disable(PandoraCore core) {

        PandoraCore.removeCommand("custpotion");
        plugin = null;

    }

    public CustomPotionPatch() {

        super("CustomPotionPatch", "Gives Custom Stackable potion effects", false, false,
                null, false);


    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player || sender instanceof ConsoleCommandSender){

            if(args.length > 0){

                Player given = Bukkit.getPlayerExact(args[0]);

                if(given != null){

                    String potionData = String.join(" ", args).substring(given.getName().length()+1);

                    List<Map<String, String>> potionMap = getPotionDataFromString(potionData.toLowerCase());

                    ItemStack pot = getPotion(5, potionMap, getQuotes(String.join(" ", args)));
                    given.getInventory().addItem(pot);

                    return true;
                }else{
                    sender.sendMessage(ChatColor.RED+"Invalid Player");
                    return true;
                }

            }

        }

        return false;
    }

    public static String getQuotes(String str){

        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(str);
        if(m.find())
        return m.group(1);

        return str;
    }

    public static ItemStack getPotion(int amount, List<Map<String, String>> potData, String name){

        ItemStack potion = new ItemStack(Material.POTION, amount);
        PotionMeta potMeta = (PotionMeta) potion.getItemMeta();
        potMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        for (Map<String, String> potDatum : potData) {

            potMeta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(potDatum.get("effect")),
                    Integer.parseInt(potDatum.get("duration")),
                    Integer.parseInt(potDatum.get("power"))), true);

            List<String> lore = potMeta.getLore() != null ? potMeta.getLore() : new ArrayList<>();
            lore.add(ChatColor.BLUE+ StringUtils.capitalize(potDatum.get("effect").replace("_", " ")) + ChatColor.WHITE + ": " +
                    ChatColor.GRAY + potDatum.get("power") + " (" +
                    formatSeconds((Integer.parseInt(potDatum.get("duration"))/20)) + ")");
            potMeta.setLore(lore);


        }

        potion.setItemMeta(potMeta);

        return potion;

    }

    public static String formatSeconds(int timeInSeconds)
    {
        int secondsLeft = timeInSeconds % 3600 % 60;
        int minutes = (int) Math.floor(timeInSeconds % 3600 / 60);
        int hours = (int) Math.floor(timeInSeconds / 3600);

        String HH = ((hours       < 10) ? "0" : "") + hours;
        String MM = ((minutes     < 10) ? "0" : "") + minutes;
        String SS = ((secondsLeft < 10) ? "0" : "") + secondsLeft;

        return HH + ":" + MM + ":" + SS;
    }

    public static List<Map<String, String>> getPotionDataFromString(String effectList){


        String[] effects = (effectList + " effect:null").split(" ");

        List<Map<String, String>> effectData = new ArrayList<>();
        Map<String, String> effectMap = new HashMap<>();

        for(String e : effects){

            String[] data = e.split(":");

            if(data[0].equals("effect")){

                effectData.add(new HashMap<>(effectMap));

            }

            effectMap.put(data[0], data[1]);

        }

        effectData.remove(0);
        return effectData;
    }


}












