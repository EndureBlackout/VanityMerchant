package org.vanitycraft.VanityMerchant;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
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

		if (!account.doesAccountExist()) {
			account.saveUser();
		}
	}

	@EventHandler
	public void inventoryClick(final InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			final Player p = (Player) e.getWhoClicked();

			if (!(e.getCurrentItem() == null) && !(e.getCurrentItem().getType().equals(Material.AIR))) {
				if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Merchant")) {
					if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())
							.equalsIgnoreCase("Combat Items")) {
						p.closeInventory();

						new BukkitRunnable() {

							public void run() {
								p.openInventory(MerchantGUI.combatGUI);
							}
						}.runTaskLater(core, 1);
					}

					if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())
							.equalsIgnoreCase("Mining Items")) {
						p.closeInventory();

						new BukkitRunnable() {
							public void run() {
								p.openInventory(MerchantGUI.mineGUI);
							}
						}.runTaskLater(core, 1);
					}

					if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())
							.equalsIgnoreCase("Misc Items")) {
						p.closeInventory();

						new BukkitRunnable() {
							public void run() {
								p.openInventory(MerchantGUI.miscGUI);
							}
						}.runTaskLater(core, 1);
					}
				}

				if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Combat Items")) {
					e.setCancelled(true);

					final int place = e.getSlot();

					final ConfigurationSection combatItems = config.getConfigurationSection("Combat");
					final Set<String> items = combatItems.getKeys(false);

					new BukkitRunnable() {
						public void run() {
							p.closeInventory();

							for (String k : items) {
								ConfigurationSection item = combatItems.getConfigurationSection(k);
								int itemPlace = item.getInt("location");

								if (place == itemPlace && checkMerchantPoints(p, item.getInt("price"))) {
									CombatObject object = new CombatObject(config);
									object.setObject(k);

									p.getInventory().addItem(object.createItem());
									p.sendMessage(ChatColor.DARK_GREEN + "You bought " + e.getCurrentItem().getItemMeta().getDisplayName() + " for " + item.getInt("price"));
								}
							}
						}
					}.runTaskLater(core, 1);

				}

				if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Mining Items")) {
					e.setCancelled(true);

					if (e.getCurrentItem().getType().equals(Material.DIAMOND_PICKAXE)) {
						Map<Enchantment, Integer> enchants = e.getCurrentItem().getEnchantments();
						
						ConfigurationSection mining = config.getConfigurationSection("Mining");
						Set<String> axes = mining.getKeys(false);
						
						for(String k : axes) {
							ConfigurationSection axe = mining.getConfigurationSection(k);
							final int price = axe.getInt("price");
							List<String> axeEnchants = axe.getStringList("enchants");
							
							for(String l : axeEnchants) {
								String[] enchantSplit = l.split(",");
								
								if(enchantSplit[0].equalsIgnoreCase("dig_speed")) {
									int level = Integer.parseInt(enchantSplit[1]);
									
									int nabLevel = enchants.get(Enchantment.DIG_SPEED);
									
									if(nabLevel == level && checkMerchantPoints(p, price)) {
										p.closeInventory();
										
										new BukkitRunnable() {
											public void run() {
												p.getInventory().addItem(e.getCurrentItem());
												p.sendMessage(ChatColor.DARK_GREEN + "You bought " + e.getCurrentItem().getItemMeta().getDisplayName() + " for " + price + " points");
											}
										}.runTaskLater(core, 1);
									}
								}
							}
						}
					}
				}
				
				if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase("Misc Items")) {
					e.setCancelled(true);

					ConfigurationSection misc = config.getConfigurationSection("Misc");
					Set<String> items = misc.getKeys(false);
					
					for(String k : items) {
						final MiscObject miscObject = new MiscObject(config);
						miscObject.setObject(k);
						
						final ItemStack miscItem = miscObject.createItem();
						
						if(e.getCurrentItem().equals(miscItem) && checkMerchantPoints(p, miscObject.getPrice())) {
							p.closeInventory();
							
							new BukkitRunnable() {
								public void run() {
									p.getInventory().addItem(miscItem);
									p.sendMessage(ChatColor.DARK_GREEN + "You bought " + miscObject.getName() + " for " + miscObject.getPrice() + ".");
								}
							}.runTaskLater(core, 1);
						}
					}
				}
			}
		}
	}

	public boolean checkMerchantPoints(Player p, int points) {
		Account account = new Account(p.getName(), p.getUniqueId(), core);

		if ((account.doesAccountExist()) && (points <= account.getPoints())) {
			int newPoints = account.getPoints() - points;

			account.setPoints(newPoints);

			account.saveUser();

			return true;
		}

		p.sendMessage(ChatColor.DARK_RED + "Sorry but you don't have enough points to buy that!");
		return false;
	}
}
