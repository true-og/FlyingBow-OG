package me.barny1094875.flyingbowog;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class FlyingBow_OG extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic

        // create the flying bow item
        ItemStack flyingBowItem = new ItemStack(Material.BOW);
        ItemMeta bowMeta = flyingBowItem.getItemMeta();
        bowMeta.setDisplayName(ChatColor.AQUA + ChatColor.ITALIC.toString() +  "Flying Bow");
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
    public void onEntityShootBow(EntityShootBowEvent event){
        // if the entity that shot the bow is a player
        if(event.getEntity() instanceof Player){
            // if the player is holding a flying bow
            if(((Player) event.getEntity())
                    .getInventory()
                    .getItemInMainHand()
                    .getItemFlags().contains(ItemFlag.HIDE_DYE)){
                event.getProjectile().addPassenger(event.getEntity());

                // if the bow has infinity on it, remove infinity

                // infinity is just way too powerful on an item like this

                // I'm leaving the item enchantable since we still want mending and unbreaking
                // to work for the bow, just like for an elytra

                // even though mending can't be put on a bow with infinity, grindstones exist
                // and an infinity bow can be made to last forever, it just takes some time
                // at an exp farm to repair it
                if((((Player) event.getEntity())
                        .getInventory()
                        .getItemInMainHand()
                        .getEnchantments().containsKey(Enchantment.ARROW_INFINITE))){
                    // remove infinity from the bow
                    ((Player) event.getEntity())
                            .getInventory()
                            .getItemInMainHand()
                            .removeEnchantment(Enchantment.ARROW_INFINITE);
                }
            }
        }
    }
}
