package org.vanitycraft.VanityMerchant;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class Account {
	String name;
	UUID uuid;
	int points;
	MerchantMain core;

	public Account(String name, MerchantMain core) {
		this.name = name;
		this.core = core;

		File pFile = new File(core.getDataFolder(), "users.yml");
		YamlConfiguration userConfig = YamlConfiguration.loadConfiguration(pFile);

		Set<String> users = userConfig.getKeys(false);

		for (String id : users) {
			ConfigurationSection user = userConfig.getConfigurationSection(id);

			String uName = user.getString("name");

			if (uName.equalsIgnoreCase(name)) {
				this.uuid = UUID.fromString(id);
				this.points = user.getInt("points");
			}
		}
	}

	public Account(UUID uuid, MerchantMain core) {
		this.uuid = uuid;
		this.core = core;
	}

	public Account(String name, UUID uuid, MerchantMain core) {
		this.name = name;
		this.uuid = uuid;
		this.points = 0;
		this.core = core;
	}

	public Account(String name, UUID uuid, int points, MerchantMain core) {
		this.name = name;
		this.uuid = uuid;
		this.points = points;
		this.core = core;
	}

	public String getName() {
		return this.name;
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public int getPoints() {
		return this.points;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean doesAccountExist() {
		File pFile = new File(core.getDataFolder(), "users.yml");
		YamlConfiguration userConfig = YamlConfiguration.loadConfiguration(pFile);

		Set<String> users = userConfig.getKeys(false);

		for (String id : users) {
			if (uuid.toString().equalsIgnoreCase(id)) {
				ConfigurationSection user = userConfig.getConfigurationSection(id);

				if (user.getString("name").equalsIgnoreCase(this.name)) {
					System.out.println(this.name + " has an account");
					this.points = user.getInt("points");
					System.out.println("And has " + this.points + " points");
					return true;
				}
			}
		}
		System.out.println(this.name + " Does not have an account");
		return false;
	}

	public void saveUser() {
		try {
			System.out.println("In saveUser");
			File pFile = new File(core.getDataFolder(), "users.yml");
			YamlConfiguration userConfig = YamlConfiguration.loadConfiguration(pFile);

			Set<String> users = userConfig.getKeys(false);

			System.out.println("Going to loop");
			for (String id : users) {
				if (this.uuid.toString().equalsIgnoreCase(id)) {
					ConfigurationSection user = userConfig.getConfigurationSection(id);

					user.set("name", this.name);
					user.set("points", this.points);
					
					userConfig.save(pFile);
					
					return;
				}
			}
			
			userConfig.set(this.uuid + ".name", this.name);
			userConfig.set(this.uuid + ".points", this.points);
			
			userConfig.save(pFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
