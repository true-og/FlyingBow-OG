package me.barny1094875.flyingbowog.Listeners;

import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class onEntityDamageEvent implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event){

        if (event.getDamager() instanceof Arrow){
//            System.out.println("arrow");
            Arrow arrow = (Arrow) event.getDamager();
            Color arrowColor = arrow.getColor();

            if (arrowColor != null) {
                // if the arrow has a certain color
                if (arrowColor.equals(Color.fromRGB(0, 255, 255))) {
//                    System.out.println("cyan");
                    // if the arrow has the UNCRAFTABLE potion on it
                    PotionType arrowPotion = arrow.getBasePotionData().getType();

                    if (arrowPotion.equals(PotionType.UNCRAFTABLE)) {
//                        System.out.println("removing");

                        // delete the arrow
                        arrow.remove();
                        // make the arrow do no damage
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
