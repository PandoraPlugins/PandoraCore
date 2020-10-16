package dev.minecraftplugin.pandoracore.module.modules.combatlog;

import com.azortis.azortislib.command.CommandInjector;
import com.azortis.azortislib.configuration.Config;
import com.azortis.azortislib.utils.FormatUtil;
import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.module.Module;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

// todo: make updater, module not finished
public class CombatLogModule extends Module<Packet<?>> {
    private Config<CombatLogModuleData> config;
    private Map<Player, Integer> secondsLeft;

    public CombatLogModule() {
        super("CombatLog", "Adds basic combat log functionality.", false, false,
                null, true);
    }


    public Map<Player, Integer> getSecondsLeft() {
        return secondsLeft;
    }

    @Override
    public Config<?> getConfiguration() {
        return config;
    }

    @Override
    public void enable(PandoraCore core) {
        config = core.getConfigManager().loadConfig("/modules/combatlog", new CombatLogModuleData());
        new CombatLeaveCommand(core, config.getConfiguration().leaveCommand);
        secondsLeft = new HashMap<>();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (secondsLeft.containsKey(event.getPlayer())) {
            secondsLeft.remove(event.getPlayer());
            PlayerInventory inventory = event.getPlayer().getInventory();
            Location location = event.getPlayer().getLocation();
            for (ItemStack itemStack : inventory.getArmorContents()) {
                if (itemStack != null)
                    location.getWorld().dropItemNaturally(location, itemStack);
            }
            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack != null)
                    location.getWorld().dropItemNaturally(location, itemStack);
            }
            inventory.clear();
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        secondsLeft.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player player = (Player) event.getEntity();
            if (secondsLeft.containsKey(player)) {
                secondsLeft.remove(player);
                player.sendMessage(FormatUtil.color(config.getConfiguration().combatLeave));
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND ||
                event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            if (secondsLeft.containsKey(event.getPlayer())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCombat(PlayerToggleFlightEvent event) {
        if (secondsLeft.containsKey(event.getPlayer()) && event.isFlying()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCombat(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() == EntityType.PLAYER && event.getFinalDamage() != 0.0) {
            Player damaged = (Player) event.getEntity();
            Player damager;
            if (event.getDamager().getType() == EntityType.PLAYER)
                damager = (Player) event.getDamager();
            else if (event.getDamager().getType() == EntityType.ARROW)
                damager = (Player) ((Arrow) event.getDamager()).getShooter();
            else return;
            if (!secondsLeft.containsKey(damaged))
                damaged.sendMessage(FormatUtil.color(config.getConfiguration().combatEnter
                        .replace("{player}", damager.getDisplayName()).replace("{seconds}",
                                config.getConfiguration().secondsCombatLog + "")));
            if (!secondsLeft.containsKey(damager))
                damager.sendMessage(FormatUtil.color(config.getConfiguration().combatEnter
                        .replace("{player}", damaged.getDisplayName()).replace("{seconds}",
                                config.getConfiguration().secondsCombatLog + "")));
            secondsLeft.put(damaged, config.getConfiguration().secondsCombatLog);
            secondsLeft.put(damager, config.getConfiguration().secondsCombatLog);
            damaged.setFlying(false);
            damager.setFlying(false);
        }
    }

    @Override
    public void disable(PandoraCore core) {
        CommandInjector.removeCommand(config.getConfiguration().leaveCommand);
        config = null;
        secondsLeft = null;
    }

    public static class CombatLogModuleData {
        private final String leaveCommand = "safeleave";
        private final int secondsCombatLog = 20;
        private final int safeLeaveTime = 15;
        private final String combatEnter = "&7You were tagged by &b{player}&7, you are now in combat for &c{seconds}&7 seconds!";
        private final String combatLeave = "&7You are no longer in combat!";
        private final String noLeaveMessage = "&7You may not leave! You are still in combat for &c{seconds}&7 seconds!";
        private final String leaveKickMessage = "&7You will be logged out in &c{seconds}&7 seconds!";
    }
}
