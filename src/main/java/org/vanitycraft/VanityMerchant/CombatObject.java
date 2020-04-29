package org.vanitycraft.VanityMerchant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CombatObject {
	YamlConfiguration config;
	
	String type = "";
	List<String> enchants = new ArrayList<String>();
	int price = 0;
	int location = 0;
	
	public CombatObject(YamlConfiguration config) {
		this.config = config;
	}
	
	public void setObject(String name) {
		ConfigurationSection combatSection = config.getConfigurationSection("Combat");
		Set<String> items = combatSection.getKeys(false);
		
		for(String k : items) {
			ConfigurationSection item = combatSection.getConfigurationSection(k);
			
			if(k.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))) {
				this.type = item.getString("type");
				this.enchants = item.getStringList("enchants");
				this.price = item.getInt("price");
				this.location = item.getInt("location");
			}
		}
	}
	
	public String getType() {
		return this.type;
	}
	
	public List<String> getEnchants() {
		return this.enchants;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public int getLocation() {
		return this.location;
	}
	
	@SuppressWarnings("deprecation")
	public ItemStack createItem() {
		ItemStack combatItem = new ItemStack(Material.getMaterial(type.toUpperCase()));
		ItemMeta combatMeta = combatItem.getItemMeta();
		
		for(String ench : enchants) {
			String[] enchString = ench.split(",");
			System.out.println(enchString[0]);
			
			String enchName = enchString[0];
			int level = Integer.parseInt(enchString[1]);
			
			combatMeta.addEnchant(Enchantment.getByName(enchName.toUpperCase()), level, true);
		}
		
		combatItem.setItemMeta(combatMeta);
		
		return combatItem;
	}
}
