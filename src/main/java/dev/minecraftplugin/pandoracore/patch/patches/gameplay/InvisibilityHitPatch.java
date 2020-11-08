package dev.minecraftplugin.pandoracore.patch.patches.gameplay;

import dev.minecraftplugin.pandoracore.PandoraCore;
import dev.minecraftplugin.pandoracore.patch.Patch;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InvisibilityHitPatch extends Patch<Packet<?>> {
    public InvisibilityHitPatch() {
        super("InvisibilityHitPatch", "Invisible players turn visible when hit",
                false, false, null, true);
    }

    @Override
    public void enable(PandoraCore core) {

    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getEntityType() == EntityType.PLAYER) {
            Player p = (Player) entityDamageByEntityEvent.getEntity();
            for (PotionEffect activePotionEffect : p.getActivePotionEffects()) {
                if (activePotionEffect.getType() == PotionEffectType.INVISIBILITY) {
                    if (entityDamageByEntityEvent.getDamager().getType() == EntityType.ENDER_PEARL) {
                        return;
                    }
                    if (entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        if (entityDamageByEntityEvent.getDamager().getType() == EntityType.PLAYER) {
                            p.removePotionEffect(PotionEffectType.INVISIBILITY);
                        }
                    } else if (entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.MAGIC ||
                            entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.CUSTOM ||
                            entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.POISON) {
                        p.removePotionEffect(PotionEffectType.INVISIBILITY);
                    } else if (entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                        Arrow arrow = (Arrow) entityDamageByEntityEvent.getDamager();
                        if (arrow.getShooter() instanceof Player) p.removePotionEffect(PotionEffectType.INVISIBILITY);
                    }
                }
            }
        }
    }

    @Override
    public void disable(PandoraCore core) {

    }
}
