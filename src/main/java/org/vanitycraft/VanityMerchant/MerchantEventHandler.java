package org.vanitycraft.VanityMerchant;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class MerchantEventHandler implements Listener {
	MerchantMain core;
	
	public MerchantEventHandler(MerchantMain core) {
		this.core = core;
	}
	
	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			final Player p = (Player) e.getWhoClicked();
			
			if(!(e.getCurrentItem() == null) && !(e.getCurrentItem().getType().equals(Material.AIR))) {
				if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Merchant")) {
					if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Combat Items")) {
						p.closeInventory();
						
						new BukkitRunnable() {
							
							public void run() {
								p.openInventory(MerchantGUI.combatGUI);
							}
						}.runTaskLater(core, 1);
					}
					
					if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Mining Items")) {
						p.closeInventory();
						
						new BukkitRunnable() {
							public void run() {
								p.openInventory(MerchantGUI.mineGUI);
							}
						}.runTaskLater(core, 1);
					}
					
					if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Misc Items")) {
						p.closeInventory();
						
						new BukkitRunnable() {
							public void run() {
								p.openInventory(MerchantGUI.miscGUI);
							}
						}.runTaskLater(core, 1);
					}
				}
			}
		}
	}
}
