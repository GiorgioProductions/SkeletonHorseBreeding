package es.giorgioproductions.skeletonhorsebreeding;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.List;

public class SkeletonHorseBreedListener implements Listener {

    private final Main plugin;

    public SkeletonHorseBreedListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent event) {

        Player p = event.getPlayer();
        Location playerLocation = p.getLocation();

        if (event.getHand() == EquipmentSlot.HAND) {
            if (event.getRightClicked() instanceof SkeletonHorse) {
                SkeletonHorse skeletonHorse = (SkeletonHorse) event.getRightClicked();
                if (skeletonHorse.getLoveModeTicks()==0 && skeletonHorse.isAdult()) {
                    ItemStack itemInHand = new ItemStack(p.getInventory().getItemInMainHand());
                    if (itemInHand.getType() == Material.BONE_MEAL) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            skeletonHorse.eject();
                            p.teleport(playerLocation);
                        }, 1L);
                        if (p.getGameMode() != GameMode.CREATIVE) {
                            p.getInventory().setItemInMainHand(new ItemStack(Material.BONE_MEAL,itemInHand.getAmount()-1));
                        }
                        skeletonHorse.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, skeletonHorse.getLocation().getX(),skeletonHorse.getLocation().getY()+1,skeletonHorse.getLocation().getZ(), 10);
                        skeletonHorse.setLoveModeTicks(400);
                        List<Entity> ents = skeletonHorse.getNearbyEntities(8,1,8);
                        for (Entity ent : ents) {
                            if (ent instanceof SkeletonHorse) {
                                SkeletonHorse possibleBreed = (SkeletonHorse) ent;
                                if (possibleBreed.getLoveModeTicks()>0 && possibleBreed.isAdult()) {
                                    skeletonHorse.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, possibleBreed.getLocation().getX(),possibleBreed.getLocation().getY()+1,possibleBreed.getLocation().getZ(), 10);
                                    Location babySkeletonHorseSpawnLocation = new Location(skeletonHorse.getWorld(),(skeletonHorse.getLocation().getX()+possibleBreed.getLocation().getX())/2,skeletonHorse.getLocation().getY(),(skeletonHorse.getLocation().getZ()+possibleBreed.getLocation().getZ())/2);
                                    skeletonHorse.getWorld().strikeLightningEffect(babySkeletonHorseSpawnLocation);
                                    SkeletonHorse babySkeletonHorse = (SkeletonHorse) skeletonHorse.getWorld().spawnEntity(babySkeletonHorseSpawnLocation,EntityType.SKELETON_HORSE);
                                    babySkeletonHorse.setBaby();
                                    babySkeletonHorse.setLoveModeTicks(0);
                                    skeletonHorse.setLoveModeTicks(0);
                                    possibleBreed.setLoveModeTicks(0);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}
