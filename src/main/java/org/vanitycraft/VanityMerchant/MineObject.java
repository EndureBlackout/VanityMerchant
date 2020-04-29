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

public class MineObject {
	YamlConfiguration config;

	public String type = "";
	public List<String> enchants = new ArrayList<String>();
	public int price = 0;

	public MineObject(YamlConfiguration config) {
		this.config = config;
	}

	public void setObject(String name) {
		ConfigurationSection mineSection = config.getConfigurationSection("Mining");
		Set<String> items = mineSection.getKeys(false);

		for (String k : items) {
			ConfigurationSection item = mineSection.getConfigurationSection(k);

			if (k.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', name))) {
				this.type = item.getString("type");
				this.enchants = item.getStringList("enchants");
				this.price = item.getInt("price");
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

	@SuppressWarnings("deprecation")
	public ItemStack createItem() {
		ItemStack miningItem = new ItemStack(Material.getMaterial(type.toUpperCase()));
		ItemMeta miningMeta = miningItem.getItemMeta();

		for (String ench : enchants) {
			String[] enchantments = ench.split(",");

			String enchName = enchantments[0];
			int level = Integer.parseInt(enchantments[1]);

			miningMeta.addEnchant(Enchantment.getByName(enchName.toUpperCase()), level, true);
		}

		miningItem.setItemMeta(miningMeta);
		return miningItem;
	}

}
