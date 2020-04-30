package org.vanitycraft.VanityMerchant;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MerchantEventHandler implements Listener {
	MerchantMain core;
	YamlConfiguration config;
	
	public MerchantEventHandler(MerchantMain core, YamlConfiguration config) {
		this.core = core;
		this.config = config;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		Account account = new Account(p.getName(), p.getUniqueId(), core);
		
		if(!account.doesAccountExist()) {
			account.saveUser();
		}
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
				
				if(ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Combat Items")) {
					e.setCancelled(true);
					
					final int place = e.getSlot();
					
					final ConfigurationSection combatItems = config.getConfigurationSection("Combat");
					final Set<String> items = combatItems.getKeys(false);
					
					new BukkitRunnable() {
						public void run() {
							p.closeInventory();
							
							for(String k : items) {
								ConfigurationSection item = combatItems.getConfigurationSection(k);
								int itemPlace = item.getInt("location");
								
								if(place == itemPlace && checkMerchantPoints(p, item.getInt("price"))) {
									CombatObject object = new CombatObject(config);
									object.setObject(k);
									
									p.getInventory().addItem(object.createItem());
								}
							}
						}
					}.runTaskLater(core, 1);

				}
			}
		}
	}
	
	public boolean checkMerchantPoints(Player p, int points) {
		Account account = new Account(p.getName(), p.getUniqueId(), core);
		
		if((account.doesAccountExist()) && (points <= account.getPoints())) {
			int newPoints = account.getPoints() - points;
			
			account.setPoints(newPoints);
			
			account.saveUser();
			
			return true;
		}
		
		p.sendMessage(ChatColor.DARK_RED + "Sorry but you don't have enough points to buy that!");
		return false;
	}
}
