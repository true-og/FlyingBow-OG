// This is free and unencumbered software released into the public domain.
// Authors: Barny1094875, NotAlexNoyle.
package net.trueog.flyingbowog.Listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent; // Purpur 1.19
import java.util.Map;
import net.trueog.flyingbowog.FlyingBowOG;
import net.trueog.utilitiesog.UtilitiesOG;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

// import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent; // Purpur 1.20

public class OnEntityShootBowEvent implements Listener {

    // Fire flying arrow event.
    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {

        // Only react to players shooting arrows with our Flying Bow.
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getProjectile() instanceof Arrow arrow)) return;
        ItemStack bow = player.getInventory().getItemInMainHand();
        if (!isFlyingBow(bow)) return;

        // Block flying bow usage while player is already wearing an elytra.
        ItemStack chest = player.getInventory().getChestplate();
        if (chest != null && chest.getType() == Material.ELYTRA) {
            UtilitiesOG.trueogMessage(
                    player,
                    FlyingBowOG.getPrefix() + "&cERROR: &6You cannot use the Flying Bow while wearing an elytra!");
            return;
        }

        // Clean up any previous rideable arrow.
        Entity vehicle = player.getVehicle();
        if (vehicle != null) {
            vehicle.removePassenger(player);
            if (vehicle instanceof Arrow) vehicle.remove();
        }

        // Tag the new arrow so we can recognize it later.
        arrow.setBasePotionData(new PotionData(PotionType.UNCRAFTABLE));
        arrow.setColor(Color.fromRGB(0x00FFFF)); // cyan

        // Hop on the arrow one tick later (server position sync trick).
        Player shooter = player;
        String worldKey = shooter.getWorld().getKey().toString();
        Location origin = shooter.getLocation();
        Arrow rideArrow = arrow;

        Bukkit.getScheduler()
                .runTaskLater(
                        FlyingBowOG.getPlugin(),
                        () -> {
                            if (shooter.getWorld().getKey().toString().equals(worldKey)) {
                                shooter.teleport(origin);
                                rideArrow.addPassenger(shooter);
                            }
                        },
                        1L);

        // Strip Infinity from the bow (too OP for this item).
        if (bow.containsEnchantment(Enchantment.ARROW_INFINITE)) {
            bow.removeEnchantment(Enchantment.ARROW_INFINITE);
        }
    }

    // Block elytra and flying bow combination event (1.20)
    /*@EventHandler(ignoreCancelled = true)
    public void onArmorEquip(PlayerArmorChangeEvent event) {

    	if (event.getSlot() != EquipmentSlot.CHEST) return;

    	ItemStack newItem = event.getNewItem();
    	if (newItem == null || newItem.getType() != Material.ELYTRA) return;

    	Player player = event.getPlayer();

    	boolean holdingFlyingBow = isFlyingBow(player.getInventory().getItemInMainHand());

    	boolean ridingFlyingBowArrow = false;
    	if (player.isInsideVehicle() && player.getVehicle() instanceof Arrow ride) {
    		if (ride.getBasePotionData().getType() == PotionType.UNCRAFTABLE
    				&& ride.getColor().asRGB() == 0x00FFFF) {
    			ridingFlyingBowArrow = true;
    		}
    	}

    	if (holdingFlyingBow || ridingFlyingBowArrow) {
    		event.setCancelled(true);
    		UtilitiesOG.trueogMessage(player,
    			FlyingBowOG.getPrefix()
    			+ "&cERROR: &6You cannot equip an elytra while using the Flying Bow!");
    	}

    }*/

    // Block elytra and flying bow combination event (1.19)
    @EventHandler(ignoreCancelled = true)
    public void onArmorEquip(PlayerArmorChangeEvent event) {

        // Only react to chest-slot changes.
        if (event.getSlotType() != PlayerArmorChangeEvent.SlotType.CHEST) return;

        ItemStack newItem = event.getNewItem();
        if (newItem == null || newItem.getType() != Material.ELYTRA) return;

        Player player = event.getPlayer();

        boolean holdingFlyingBow = isFlyingBow(player.getInventory().getItemInMainHand());
        boolean ridingFlyingBowArrow = isRidingTaggedArrow(player);

        if (!(holdingFlyingBow || ridingFlyingBowArrow)) return;

        /* event is NOT cancellable in <1.20 Purpur builds, so revert the change */
        ItemStack previous = event.getOldItem();
        ItemStack elytra = newItem.clone();

        Bukkit.getScheduler().runTask(FlyingBowOG.getPlugin(), () -> {
            player.getInventory().setChestplate(previous);
            Map<Integer, ItemStack> leftovers = player.getInventory().addItem(elytra);
            leftovers.values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
        });

        UtilitiesOG.trueogMessage(
                player, FlyingBowOG.getPrefix() + "&cERROR: &6You cannot equip an elytra while using the Flying Bow!");
    }

    // Identifies your custom “Flying Bow”.
    private boolean isFlyingBow(ItemStack item) {
        return item != null
                && item.getType() == Material.BOW
                && item.getItemFlags().contains(ItemFlag.HIDE_DYE);
    }

    // True if the player is riding one of the cyan, UNCRAFTABLE arrows.
    private boolean isRidingTaggedArrow(Player player) {
        if (!player.isInsideVehicle()) return false;
        if (!(player.getVehicle() instanceof Arrow arrow)) return false;

        return arrow.getBasePotionData().getType() == PotionType.UNCRAFTABLE
                && arrow.getColor().asRGB() == 0x00FFFF;
    }
}
