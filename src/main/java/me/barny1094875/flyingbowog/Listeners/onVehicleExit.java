package me.barny1094875.flyingbowog.Listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class onVehicleExit implements Listener {


    // delete the arrow the player was riding on to prevent
    // it from hitting them
    @EventHandler
    public void onVehicleExit(VehicleExitEvent event){
        Entity arrow = event.getVehicle();

        // if the vehicle was an arrow, the bow was used
        // delete the arrow so that the player
        // doesn't get hit by it
        if (event.getVehicle() instanceof Arrow){
            if (event.getExited() instanceof Player){
                event.getVehicle().remove();
            }
        }
    }

}
