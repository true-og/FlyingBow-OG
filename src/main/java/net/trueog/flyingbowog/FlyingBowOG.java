// This is free and unencumbered software released into the public domain.
// Authors: Barny1094875, NotAlexNoyle.
package net.trueog.flyingbowog;

import java.util.Collections;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.trueog.flyingbowog.Commands.CommandsTabCompleter;
import net.trueog.flyingbowog.Commands.GiveCommand;
import net.trueog.flyingbowog.Listeners.OnAnvilUpdateResultEvent;
import net.trueog.flyingbowog.Listeners.OnEntityDamageEvent;
import net.trueog.flyingbowog.Listeners.OnEntityShootBowEvent;
import net.trueog.flyingbowog.Listeners.OnGenericGameEvent;
import net.trueog.flyingbowog.Listeners.OnPrepareAnvilEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlyingBowOG extends JavaPlugin implements Listener {

    private static FlyingBowOG plugin;

    @Override
    public void onEnable() {

        // Plugin startup logic
        plugin = this;

        // create the flying bow item
        ItemStack flyingBowItem = new ItemStack(Material.BOW);
        ItemMeta bowMeta = flyingBowItem.getItemMeta();
        bowMeta.displayName(
                Component.text("Flying Bow").color(TextColor.color(0, 255, 255)).decorate(TextDecoration.ITALIC));
        bowMeta.lore(Collections.singletonList(Component.text("Shoot this bow to FLY")));
        flyingBowItem.setItemMeta(bowMeta);

        // adding a useless itemFlag to identify this as a flying bow
        // this was easier for me than adding an attribute, and no other bow should have
        // this flag
        flyingBowItem.addItemFlags(ItemFlag.HIDE_DYE);

        // add the recipe into the game
        ShapedRecipe flyingBowRecipe = new ShapedRecipe(new NamespacedKey(this, "flying_bow"), flyingBowItem);
        flyingBowRecipe.shape("sd ", "sed", "sd ");
        flyingBowRecipe.setIngredient('s', Material.STICK);
        flyingBowRecipe.setIngredient('e', Material.ELYTRA);
        flyingBowRecipe.setIngredient('d', Material.DIAMOND);

        Bukkit.addRecipe(flyingBowRecipe);

        getServer().getPluginManager().registerEvents(new OnEntityShootBowEvent(), this);
        getServer().getPluginManager().registerEvents(new OnGenericGameEvent(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDamageEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPrepareAnvilEvent(), this);
        getServer().getPluginManager().registerEvents(new OnAnvilUpdateResultEvent(), this);

        getCommand("flyingbow").setExecutor(new GiveCommand());
        getCommand("flyingbow").setTabCompleter(new CommandsTabCompleter());

    }

    public static FlyingBowOG getPlugin() {

        return plugin;

    }

    public static String getPrefix() {

        return "&7[&5FlyingBow&f-&4OG&7] ";

    }

}
