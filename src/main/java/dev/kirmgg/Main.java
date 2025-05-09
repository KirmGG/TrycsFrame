package dev.kirmgg;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.Location;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    private static final int DAMAGE_AMOUNT = 10;
    private static final Particle PARTICLE_TYPE = Particle.END_ROD;
    private static final double PARTICLE_RADIUS = 0.4;
    private static final Sound SOUND_EFFECT = Sound.BLOCK_HONEY_BLOCK_BREAK;
    private static final float SOUND_VOLUME = 1.0f;
    private static final float SOUND_PITCH = 0.8f;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onInteractWithFrame(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame frame)) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        if (!player.isSneaking()) return;

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() != Material.SHEARS) return;

        if (frame.isInvisible()) return;

        short durabilityLeft = (short) (itemInHand.getType().getMaxDurability() - itemInHand.getDurability());

        if (durabilityLeft > DAMAGE_AMOUNT) {
            itemInHand.setDurability((short) (itemInHand.getDurability() + DAMAGE_AMOUNT));
        } else {
            player.getInventory().setItemInMainHand(null);
            player.playSound(player.getLocation(), "entity.item.break", 1.0f, 1.0f);
        }

        frame.setInvisible(true);
        event.setCancelled(true);
        
        Location loc = frame.getLocation().add(0.5, 0.5, 0.5);
        frame.getWorld().spawnParticle(PARTICLE_TYPE, loc, 20, PARTICLE_RADIUS, PARTICLE_RADIUS, PARTICLE_RADIUS, 0.01);
        frame.getWorld().spawnParticle(Particle.FLASH, loc, 1, 0, 0, 0, 0.01);
        frame.getWorld().playSound(frame.getLocation(), SOUND_EFFECT, SOUND_VOLUME, SOUND_PITCH);
    }
}
