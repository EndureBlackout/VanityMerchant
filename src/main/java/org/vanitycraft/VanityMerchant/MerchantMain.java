package org.vanitycraft.VanityMerchant;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MerchantMain extends JavaPlugin {
	public void onEnable() {
		if(!getDataFolder().exists()) {
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
		
		YamlConfiguration config = (YamlConfiguration) getConfig();
		
		@SuppressWarnings("unused")
		MerchantGUI gui = new MerchantGUI(config);
		
		getServer().getPluginManager().registerEvents(new MerchantEventHandler(this), this);
		
		getCommand("merchant").setExecutor(new CommandHandler(this, config));
		getCommand("merch").setExecutor(new CommandHandler(this, config));
	}
}
