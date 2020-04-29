package org.vanitycraft.VanityMerchant;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandHandler implements CommandExecutor, Listener {
	MerchantMain core;
	YamlConfiguration config;
	
	public CommandHandler(MerchantMain core, YamlConfiguration config) {
		this.core = core;
		this.config = config;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			if(args.length == 0) {
				if(cmd.getName().equalsIgnoreCase("merchant") || cmd.getName().equalsIgnoreCase("merch")) {
					p.openInventory(MerchantGUI.mainGUI);
				}
			}
		}
		
		return true;
	}
}
