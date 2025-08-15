// This is free and unencumbered software released into the public domain.
// Authors: Barny1094875, NotAlexNoyle.
package net.trueog.flyingbowog.Listeners;

import java.util.ArrayList;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.purpurmc.purpur.event.inventory.AnvilUpdateResultEvent;

public class OnAnvilUpdateResultEvent implements Listener {

    @EventHandler
    public void onAnvilUpdateResultEvent(AnvilUpdateResultEvent event) {

        // Cast each HumanEntity to a player so that we can update their inventories.
        ArrayList<Player> players = new ArrayList<>();
        for (HumanEntity human : event.getViewers()) {

            if (human instanceof Player) {

                players.add((Player) human);

            }

        }

        for (Player player : players) {

            player.updateInventory();

        }

    }

}
