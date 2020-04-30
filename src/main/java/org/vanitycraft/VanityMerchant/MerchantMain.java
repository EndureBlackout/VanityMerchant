package org.vanitycraft.VanityMerchant;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MerchantMain extends JavaPlugin {
	public void onEnable() {
		if(!getDataFolder().exists()) {
			getConfig().options().copyDefaults(true);
			saveConfig();
			
			File file = new File(getDataFolder(), "users.yml");
			YamlConfiguration users = new YamlConfiguration();

			try {
				users.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		YamlConfiguration config = (YamlConfiguration) getConfig();
		
		@SuppressWarnings("unused")
		MerchantGUI gui = new MerchantGUI(config);
		
		getServer().getPluginManager().registerEvents(new MerchantEventHandler(this, config), this);
		
		getCommand("merchant").setExecutor(new CommandHandler(this, config));
		getCommand("merch").setExecutor(new CommandHandler(this, config));
	}
}
