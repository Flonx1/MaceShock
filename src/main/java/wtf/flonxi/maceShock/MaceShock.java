package wtf.flonxi.maceShock;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MaceShock extends JavaPlugin implements Listener {

    private final Set<UUID> recursing = new HashSet<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onMaceHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        if (recursing.contains(player.getUniqueId())) {
            return;
        }

        if (player.getInventory().getItemInMainHand().getType() != Material.MACE) {
            return;
        }

        FileConfiguration config = getConfig();
        if (player.getFallDistance() <= config.getDouble("min-fall-distance")) {
            return;
        }

        Entity victim = event.getEntity();
        Location impactLocation = victim.getLocation();
        World world = impactLocation.getWorld();
        if (world == null) return;

        recursing.add(player.getUniqueId());

        try {
            if (config.getBoolean("enable-sound")) {
                world.playSound(impactLocation, Sound.ENTITY_BREEZE_WIND_BURST, 1.0f, 0.5f);
            }

            if (config.getBoolean("enable-particles")) {
                world.spawnParticle(Particle.EXPLOSION_EMITTER, impactLocation.add(0, 1, 0), 1);
            }

            double radius = config.getDouble("radius");
            double knockbackStrength = config.getDouble("knockback-strength");

            boolean damageEnabled = config.getBoolean("aoe-damage.enabled");
            double damageAmount = config.getDouble("aoe-damage.amount");

            boolean stunEnabled = config.getBoolean("stun-effect.enabled");
            int stunDuration = config.getInt("stun-effect.duration-ticks");

            Collection<Entity> nearbyEntities = world.getNearbyEntities(impactLocation, radius, radius, radius);

            for (Entity entity : nearbyEntities) {
                if (!(entity instanceof LivingEntity livingEntity)) continue;
                if (entity.equals(player)) continue;
                if (entity.equals(victim)) continue;

                Vector vector = livingEntity.getLocation().toVector().subtract(impactLocation.toVector());
                if (vector.lengthSquared() < 0.01) {
                    vector = new Vector(0, 0, 0);
                } else {
                    vector.normalize();
                }

                vector.multiply(knockbackStrength);
                vector.setY(0.5);
                livingEntity.setVelocity(vector);

                if (damageEnabled) {
                    livingEntity.damage(damageAmount, player);
                }

                if (stunEnabled) {
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, stunDuration, 1));
                }
            }
        } finally {
            recursing.remove(player.getUniqueId());
        }
    }
}