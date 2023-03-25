package me.barny1094875.flyingbowog;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.Map;


public final class FlyingBow_OG extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic

        // create the flying bow item
        ItemStack flyingBowItem = new ItemStack(Material.BOW);
        ItemMeta bowMeta = flyingBowItem.getItemMeta();
        bowMeta.displayName(Component.text("Flying Bow")
                .color(TextColor.color(0, 255, 255))
                .decorate(TextDecoration.ITALIC));
        bowMeta.lore(Collections.singletonList(Component.text("Shoot this bow to FLY")));
        flyingBowItem.setItemMeta(bowMeta);

        // adding a useless itemFlag to identify this as a flying bow
        // this was easier for me than adding an attribute, and no other bow should have
        // this flag
        flyingBowItem.addItemFlags(ItemFlag.HIDE_DYE);


        // add the recipe into the game
        ShapedRecipe flyingBowRecipe = new ShapedRecipe(new NamespacedKey(this, "flying_bow"),
                flyingBowItem);
        flyingBowRecipe.shape("sd ", "sed", "sd ");
        flyingBowRecipe.setIngredient('s', Material.STICK);
        flyingBowRecipe.setIngredient('e', Material.ELYTRA);
        flyingBowRecipe.setIngredient('d', Material.DIAMOND);


        Bukkit.addRecipe(flyingBowRecipe);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {

        Entity eventEntity = event.getEntity();
        ItemStack eventBow = ((Player) eventEntity)
                .getInventory()
                .getItemInMainHand();
        Map<Enchantment, Integer> bowEnchantments = eventBow.getEnchantments();
        Location eventEntityLocation = eventEntity.getLocation();
        Entity eventArrow = event.getProjectile();

        // if the entity that shot the bow is a player
        if (eventEntity instanceof Player) {

            // if the player is holding a flying bow
            if (eventBow.getItemFlags().contains(ItemFlag.HIDE_DYE)) {

                // eject the player from the previous arrow, if they were riding
                // on one before
                if (eventEntity.getVehicle() != null) {
                    eventEntity.getVehicle().removePassenger(eventEntity);
                }

                // tp the player to the location they were at when the arrow was shot 2 ticks after it
                // was shot to force the server to track the player's position
                // then make the player ride the arrow
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    eventEntity.teleport(eventEntityLocation);
                    eventArrow.addPassenger(eventEntity);
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
