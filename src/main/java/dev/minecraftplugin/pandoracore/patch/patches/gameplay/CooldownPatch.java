package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import com.azortis.azortislib.experimental.configuration.Config;
import com.azortis.azortislib.utils.FormatUtil;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownPatch extends Patch<Packet<?>> {
    private Config<CooldownPatchData> config;
    private Map<Player, Integer> pearlCooldown;
    private Map<Player, Integer> gappleCooldown;
    private Map<Player, Integer> crappleCooldown;
    private int updaterID;

    public CooldownPatch() {
        super("CooldownPatch", "Adds cooldowns to epearls, gapples, and the like.", false,
                false, null, true);
    }


    @Override
    public void enable(PandoraCore core) {
        config = core.getConfigManager().loadConfig("/patches/exploits/cooldowns", new CooldownPatchData());
        pearlCooldown = new ConcurrentHashMap<>();
        gappleCooldown = new ConcurrentHashMap<>();
        crappleCooldown = new ConcurrentHashMap<>();
        updaterID = Bukkit.getScheduler().runTaskTimerAsynchronously(core, new Updater(), 5, 20).getTaskId();
    }

    @Override
    public void disable(PandoraCore core) {
        config = null;
        Bukkit.getScheduler().cancelTask(updaterID);
        pearlCooldown = null;
        gappleCooldown = null;
        crappleCooldown = null;
        updaterID = 0;
    }

    @EventHandler
    public void onPlayerEpearl(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof EnderPearl) {
            if (pearlCooldown.containsKey(event.getEntity().getShooter())) {
                event.setCancelled(true);
                ((Player) event.getEntity().getShooter()).sendMessage(
                        FormatUtil.color(config.getConfiguration().cooldownMessage.replace("{item}", "Gapple")
                                .replace("{time}",
                                        "" + gappleCooldown.get(event.getEntity().getShooter()))));
            } else {
                pearlCooldown.put(((Player) event.getEntity().getShooter()), config.getConfiguration().epearlCooldown);
            }
        }
    }

    @Override
    public Config<?> getConfiguration() {
        return config;
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.GOLDEN_APPLE) {
            if (event.getItem().getDurability() == 1) {
                if (gappleCooldown.containsKey(event.getPlayer())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(
                            FormatUtil.color(config.getConfiguration().cooldownMessage.replace("{item}", "Gapple")
                                    .replace("{time}", "" + gappleCooldown.get(event.getPlayer())))
                    );
                } else {
                    gappleCooldown.put(event.getPlayer(), config.getConfiguration().gappleCooldown);
                }
            } else if (event.getItem().getDurability() == 0) {
                if (crappleCooldown.containsKey(event.getPlayer())) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(
                            FormatUtil.color(config.getConfiguration().cooldownMessage.replace("{item}", "Crapple")
                                    .replace("{time}", "" + gappleCooldown.get(event.getPlayer()))));
                } else {
                    crappleCooldown.put(event.getPlayer(), config.getConfiguration().crappleCooldown);
                }
            }
        }
    }

    public static class CooldownPatchData {
        public int gappleCooldown = 5;
        public int epearlCooldown = 5;
        public int crappleCooldown = 5;
        public String cooldownMessage = "&7You are unable to use &d{item}&7 for another &5{time}&7s!";
    }

    public class Updater implements Runnable {
        @Override
        public void run() {
            for (Player player : pearlCooldown.keySet()) {
                if (pearlCooldown.get(player) - 1 == 0) {
                    pearlCooldown.remove(player);
                } else pearlCooldown.put(player, pearlCooldown.get(player) - 1);
            }
            for (Player player : crappleCooldown.keySet()) {
                if (crappleCooldown.get(player) - 1 == 0) {
                    crappleCooldown.remove(player);
                } else crappleCooldown.put(player, crappleCooldown.get(player) - 1);
            }
            for (Player player : gappleCooldown.keySet()) {
                if (gappleCooldown.get(player) - 1 == 0) {
                    gappleCooldown.remove(player);
                } else gappleCooldown.put(player, gappleCooldown.get(player) - 1);
            }
        }
    }
}
