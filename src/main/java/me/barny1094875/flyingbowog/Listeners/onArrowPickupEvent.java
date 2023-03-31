package me.barny1094875.flyingbowog.Listeners;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class onArrowPickupEvent implements Listener {


    // The player picks up their arrow when they hit the ground, so that is being used to
    // see if the player landed with their head in a block

    // there's some stuff here that will be used later
    // the current commit is just to fix the suffocation glitch
    @EventHandler
    public void onArrowPickup(PlayerPickupArrowEvent event) {

//        System.out.println("Checking Stuff");

        // if the player was riding the arrow
        // not possible unless they used the flying bow
        if (event.getArrow().getPassengers().contains(event.getPlayer())) {

//            System.out.println("Using bow");

            Player player = event.getPlayer();
            World world = player.getWorld();
            Location playerLocation = player.getLocation();
            Location playerHeadLocation = playerLocation;
            playerHeadLocation.setY(playerHeadLocation.getY() + 1);
            Block playerHeadBlock = world.getBlockAt(playerHeadLocation);
            // if the player left the arrow inside of anything but air
            // not used right now
            if (playerHeadBlock.getType() != Material.AIR) {
                // begin a search algorithm for the nearest safe block to teleport to
                // looking for the nearest space with two blocks to stand in
//                System.out.println("Not in air");
            }

            // set the player to the center of the block they are on
            // to prevent them from going through a walls as easily
            Location newLocation = playerLocation;
            newLocation.setX(newLocation.getBlockX() + 0.5);
            newLocation.setY(newLocation.getBlockY() - 1.0);
            newLocation.setZ(newLocation.getBlockZ() + 0.5);

            player.teleport(newLocation);


            // keep the player from picking up their arrows
            // since they have special modifiers
            event.getArrow().remove();

        }
    }

}
