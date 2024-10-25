package net.trueog.flyingbowog.Listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import de.tr7zw.nbtapi.NBT;
import net.kyori.adventure.text.Component;
import net.trueog.flyingbowog.FlyingBowOG;

public class OnPrepareAnvilEvent implements Listener {

	@EventHandler
	public void onPrepareAnvilEvent(PrepareAnvilEvent event){
		AnvilInventory anvil = event.getInventory();

		ItemStack firstItem = anvil.getFirstItem();
		ItemStack secondItem = anvil.getSecondItem();

		if(firstItem != null){
			if(secondItem != null){
				if(firstItem.getItemFlags().contains(ItemFlag.HIDE_DYE) && firstItem.getType().equals(Material.BOW)){
					if(secondItem.getType().equals(Material.PHANTOM_MEMBRANE)){
						// create the flying bow item
						ItemStack flyingBowItem = anvil.getFirstItem().clone();

						// set its durability to 25% more than the other one
						// make sure the cap the durability to full using Math.max()
						Damageable flyingBowItemMeta = (Damageable) flyingBowItem.getItemMeta();
						// the amount that the bow gets healed by. 96 is 1/4 the max durability of a bow, which is 384
						final int bowHeal = 96;
						flyingBowItemMeta.setDamage(Math.max(0, flyingBowItemMeta.getDamage() - (bowHeal * anvil.getSecondItem().getAmount())));

						// set the bow name IF the player is trying to rename it
						if(!anvil.getRenameText().equals("Flying Bow")){
							flyingBowItemMeta.displayName(Component.text(anvil.getRenameText()));
						}

						flyingBowItem.setItemMeta(flyingBowItemMeta);

						int previousRepairCost = NBT.get(anvil.getFirstItem(), nbt -> nbt.getInteger("RepairCost"));
						int newRepairCost = previousRepairCost + 1;

						NBT.modify(flyingBowItem, nbt -> {
							nbt.setInteger("RepairCost", newRepairCost);
						});

						// set the anvil result to this new flying bow
						event.setResult(flyingBowItem);

						// set the exp cost for the repair
						// it's very long, and ugly, but it works
						// it runs a syncrounous task that sets the anvil cost
						// before the client is told about the change
						// so that the client can register what the exp amount is
						// (I did not figure this out, I just feel I should say that)
						FlyingBowOG.getPlugin().getServer().getScheduler().runTask(
								FlyingBowOG.getPlugin(),
								() -> anvil.setRepairCost(Math.min(4, anvil.getSecondItem().getAmount()) + newRepairCost)
								);
					}
				}


				// check that a normal bow is not being used to repair the flying bow
				// if the first item is a bow
				if(firstItem.getType().equals(Material.BOW)){
					// if the second item is a bow
					if(secondItem.getType().equals(Material.BOW)){
						// if only one bow has the HIDE_DYE flag
						if(firstItem.getItemFlags().contains(ItemFlag.HIDE_DYE) ^ secondItem.getItemFlags().contains(ItemFlag.HIDE_DYE)){
							// set the result to null
							event.setResult(null);
						}
					}
				}
			}
		}

	}

}