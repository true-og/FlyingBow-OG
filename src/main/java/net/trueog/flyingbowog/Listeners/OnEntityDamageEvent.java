package net.trueog.flyingbowog.Listeners;

import org.bukkit.Color;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionType;

public class OnEntityDamageEvent implements Listener {

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event){

		if (event.getDamager() instanceof Arrow){
			Arrow arrow = (Arrow) event.getDamager();
			Color arrowColor = arrow.getColor();
			Player player = null;
			Player shooter = null;

			if(event.getEntity() instanceof Player){
				player = (Player) event.getEntity();
			}
			else{
				return;
			}
			if(arrow.getShooter() instanceof Player){
				shooter = (Player) arrow.getShooter();
			}
			else{
				return;
			}

			// only disable damage if you are the one that shot the arrow
			if(shooter.getName().equals(player.getName())) {

				if (arrowColor != null) {
					// if the arrow has a certain color
					if (arrowColor.equals(Color.fromRGB(0, 255, 255))) {
						// if the arrow has the UNCRAFTABLE potion on it
						PotionType arrowPotion = arrow.getBasePotionData().getType();

						if (arrowPotion.equals(PotionType.UNCRAFTABLE)) {

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

}