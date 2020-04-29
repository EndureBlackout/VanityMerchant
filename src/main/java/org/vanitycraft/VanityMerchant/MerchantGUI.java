package org.vanitycraft.VanityMerchant;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MerchantGUI {
	YamlConfiguration config;
	
	public static Inventory mainGUI;
	public static Inventory combatGUI;
	public static Inventory mineGUI;
	public static Inventory miscGUI;
	
	public MerchantGUI(YamlConfiguration config) {
		this.config = config;
		
		setupMainGUI();
		setupMineGUI();
		setupMiscGUI();
		setupCombatGUI();
	}
	
	private void setupMainGUI() {
		ItemStack combat = new ItemStack(Material.DIAMOND_HELMET);
		ItemMeta combatMeta = combat.getItemMeta();
		
		combatMeta.setDisplayName(ChatColor.RED + "Combat Items");
		combat.setItemMeta(combatMeta);
		
		ItemStack mine = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta mineMeta = mine.getItemMeta();
		
		mineMeta.setDisplayName(ChatColor.DARK_GRAY + "Mining Items");
		mine.setItemMeta(mineMeta);
		
		ItemStack misc = new ItemStack(Material.DRAGON_EGG);
		ItemMeta miscMeta = misc.getItemMeta();
		
		miscMeta.setDisplayName(ChatColor.GOLD + "Misc Items");
		misc.setItemMeta(miscMeta);
		
		mainGUI = Bukkit.createInventory(null, 27, ChatColor.GREEN + "Merchant");
		mainGUI.setItem(12, combat);
		mainGUI.setItem(13, mine);
		mainGUI.setItem(14, misc);
	}
	
	private void setupMineGUI() {
		Inventory mineInv = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY + "Mining Items");
		
		ConfigurationSection mineSection = config.getConfigurationSection("Mining");
		Set<String> items = mineSection.getKeys(false);
		
		for(String k : items) {
			MineObject object = new MineObject(config);
			object.setObject(k);
			
			mineInv.addItem(object.createItem());
		}
		
		mineGUI = mineInv;
	}
	
	private void setupMiscGUI() {
		Inventory miscInv = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Misc Items");
		
		ConfigurationSection miscSection = config.getConfigurationSection("Misc");
		Set<String> miscItems = miscSection.getKeys(false);
		
		for(String k : miscItems) {
			MiscObject object = new MiscObject(config);
			object.setObject(k);
			
			miscInv.addItem(object.createItem());
		}
		
		miscGUI = miscInv;
	}
	
	public void setupCombatGUI() {
		Inventory combatInv = Bukkit.createInventory(null, 54, ChatColor.RED + "Combat Items");
		
		ConfigurationSection combatSection = config.getConfigurationSection("Combat");
		Set<String> combatItems = combatSection.getKeys(false);
		
		for(String k : combatItems) {
			CombatObject object = new CombatObject(config);
			object.setObject(k);
			
			combatInv.setItem(object.getLocation(), object.createItem());
		}
		
		combatGUI = combatInv;
	}
}
