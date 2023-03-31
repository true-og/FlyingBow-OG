package me.barny1094875.flyingbowog.Listeners;

import me.barny1094875.flyingbowog.FlyingBow_OG;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.*;

import java.util.Map;

public class onEntityShootBowEvent implements Listener {

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {

        // if the entity was a player
        if (event.getEntity() instanceof Player) {
            if (event.getProjectile() instanceof Arrow) {
                Player eventEntity = (Player) event.getEntity();
                String eventWorld = eventEntity.getWorld().getKey().toString();
                ItemStack eventBow = ((Player) eventEntity)
                        .getInventory()
                        .getItemInMainHand();
                Map<Enchantment, Integer> bowEnchantments = eventBow.getEnchantments();
                Location eventEntityLocation = eventEntity.getLocation();
                Arrow eventArrow = (Arrow) event.getProjectile();
                Entity previousArrow = null;
                if (eventEntity.getVehicle() != null) {
                    previousArrow = eventEntity.getVehicle();
                }

                // if the player is holding a flying bow
                if (eventBow.getItemFlags().contains(ItemFlag.HIDE_DYE)) {

                    // eject the player from the previous arrow, if they were riding
                    // on one before
                    if (previousArrow != null) {
                        previousArrow.removePassenger(eventEntity);
                        previousArrow.remove();
                    }

                    // add a color to the arrow but not an effect
                    // so that it can be identified later
                    eventArrow.setBasePotionData(new PotionData(PotionType.UNCRAFTABLE));
                    eventArrow.setColor(Color.fromRGB(0, 255, 255));

                    // tp the player to the location they were at when the arrow was shot 2 ticks after it
                    // was shot to force the server to track the player's position
                    // then make the player ride the arrow
                    Bukkit.getScheduler().runTaskLater(FlyingBow_OG.getPlugin(), () -> {
                        // check if the player and arrow are still in the same world
                        // if not, don't try to make the player ride the arrow
                        if (eventEntity.getWorld().getKey().toString().equals(eventWorld)) {
                            eventEntity.teleport(eventEntityLocation);
                            eventArrow.addPassenger(eventEntity);
                        }
                    }, 2L);

                    // if the bow has infinity on it, remove infinity

                    // infinity is just way too powerful on an item like this

                    // I'm leaving the item enchantable since we still want mending and unbreaking
                    // to work for the bow, just like for an elytra

                    // even though mending can't be put on a bow with infinity, grindstones exist
                    // and an infinity bow can be made to last forever, it just takes some time
                    // at an exp farm to repair it
                    if (bowEnchantments.containsKey(Enchantment.ARROW_INFINITE)) {
                        // remove infinity from the bow
                        eventBow.removeEnchantment(Enchantment.ARROW_INFINITE);
                    }
                }
            }
        }
    }

}
