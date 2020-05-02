package org.vanitycraft.VanityMerchant;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.md_5.bungee.api.ChatColor;

public class CommandHandler implements CommandExecutor, Listener {
	MerchantMain core;
	YamlConfiguration config;

	public CommandHandler(MerchantMain core, YamlConfiguration config) {
		this.core = core;
		this.config = config;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;

			if (cmd.getName().equalsIgnoreCase("merchant") || cmd.getName().equalsIgnoreCase("merch")) {
				if (args.length == 0) {
					p.openInventory(MerchantGUI.mainGUI);

				}

				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("points")) {
						Account account = new Account(p.getName(), p.getUniqueId(), core);

						if (account.doesAccountExist()) {
							p.sendMessage(ChatColor.DARK_GREEN + "You have " + account.getPoints() + " points.");
						}
					}
				}

				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("points")) {
						String pName = args[1];

						for (Player target : Bukkit.getOnlinePlayers()) {
							if (target.getName().equalsIgnoreCase(pName)) {
								Account account = new Account(target.getName(), target.getUniqueId(), core);

								if (account.doesAccountExist()) {
									p.sendMessage(ChatColor.DARK_GREEN + target.getName() + " has "
											+ account.getPoints() + " points.");
								}
							}
						}
					}
				}

				if (args.length == 4) {
					if (args[0].equalsIgnoreCase("points")) {
						if (args[1].equalsIgnoreCase("give") && isInteger(args[3])) {
							String pName = args[2];
							int add = Integer.parseInt(args[3]);

							for (Player target : Bukkit.getOnlinePlayers()) {
								if (target.getName().equalsIgnoreCase(pName)) {
									UUID puuid = target.getUniqueId();

									Account account = new Account(target.getName(), puuid, core);

									if (account.doesAccountExist()) {
										int points = account.getPoints();

										points += add;

										account.setPoints(points);
										account.saveUser();

										p.sendMessage(ChatColor.DARK_GREEN + "You just gave " + target.getName() + " "
												+ add + " points and they now have a total of " + account.getPoints());
									}
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	public boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
