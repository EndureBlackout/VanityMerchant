package org.vanitycraft.VanityMerchant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MiscObject {
	YamlConfiguration config;
	
	String item;
	String name;
	List<String> lore = new ArrayList<String>();
	int price;
	int amount;
	
	public MiscObject(YamlConfiguration config) {
		this.config = config;
	}
	
	public void setObject(String name) {
		ConfigurationSection miscSection = config.getConfigurationSection("Misc");
		Set<String> items = miscSection.getKeys(false);
		
		for(String k : items) {
			ConfigurationSection item = miscSection.getConfigurationSection(k);
			
			if(k.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))) {
				this.item = item.getString("item");
				this.name = item.getString("name");
				this.lore = item.getStringList("lore");
				this.price = item.getInt("price");
				this.amount = item.getInt("amount");
			}
		}
	}
	
	public String getItem() {
		return this.item;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<String> getLore() {
		return lore;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public int getAmount() {
		return this.price;
	}
	
	public ItemStack createItem() {
		ItemStack miscItem = new ItemStack(Material.DRAGON_EGG);
		
		if(item.equalsIgnoreCase("splash_healing")) {
			miscItem = new ItemStack(Material.SPLASH_POTION);
			
			PotionMeta pMeta = (PotionMeta) miscItem.getItemMeta();
			
			PotionEffect heal = new PotionEffect(PotionEffectType.HEAL, 1, 1);
			pMeta.addCustomEffect(heal, true);
			pMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			
			
			miscItem.setItemMeta(pMeta);
		} else	{
			miscItem = new ItemStack(Material.getMaterial(item.toUpperCase()));
			ItemMeta miscMeta = miscItem.getItemMeta();
			
			ArrayList<String> loreList = new ArrayList<String>();
			
			if(!(lore.size() == 0) && !(lore == null)) {
				for(String l : lore) {
					loreList.add(ChatColor.translateAlternateColorCodes('&', l));
				}
			}
			
			miscMeta.setLore(loreList);
			miscMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
			miscItem.setAmount(amount);
			miscItem.setItemMeta(miscMeta);
		}
		
		return miscItem;
	}
}
