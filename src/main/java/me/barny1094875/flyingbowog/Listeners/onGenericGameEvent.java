package me.barny1094875.flyingbowog.Listeners;


import me.barny1094875.flyingbowog.FlyingBow_OG;
import org.bukkit.Bukkit;
import org.bukkit.GameEvent;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.GenericGameEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class onGenericGameEvent implements Listener {

    @EventHandler
    public void onGenericGameEvent(GenericGameEvent event){


        // if the event was a projectile landing
        if(event.getEvent().equals(GameEvent.PROJECTILE_LAND)){

            Entity entity = null;

            if(event.getEntity() != null) {
                entity = event.getEntity();
            }
            else{
                return;
            }

            if(entity.getVehicle() != null) {
                if (entity.getVehicle() instanceof Arrow) {
                    Arrow arrow = (Arrow) entity.getVehicle();

                    // tp the entity to the center of the block they
                    // landed on
                    Location newLocation = entity.getLocation();
                    newLocation.setX(newLocation.getBlockX() + 0.5);
                    newLocation.setY(newLocation.getBlockY());
                    newLocation.setZ(newLocation.getBlockZ() + 0.5);

                    entity.teleport(newLocation);

                    // delete the arrow
                    arrow.remove();

                }
            }

        }

    }

}
