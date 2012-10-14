/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.gravypod.AllAdmin.user;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gravypod.AllAdmin.AllAdmin;
import com.gravypod.AllAdmin.utils.PermissionsTesting;
import com.gravypod.AllAdmin.utils.TeleportUtils;

public class AllAdminUser implements IUser {
	
	private final Player bukkitPlayer;
	
	private Location lastLocation;
	
	private final File userDataFile;
	
	private final FileConfiguration userData = new YamlConfiguration();
	
	public AllAdminUser(final Player _bukkitPlayer) {
	
		bukkitPlayer = _bukkitPlayer;
		
		userDataFile = new File(AllAdmin.getInstance().getDataFolder(), "userdata/" + bukkitPlayer.getName() + ".yml");
		
		final AllAdmin plugin = AllAdmin.getInstance();
		
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
			
				try {
					
					if (userDataFile.exists()) {
						userDataFile.createNewFile();
					}
					
					userData.load(userDataFile);
					
					userData.save(userDataFile);
					
				} catch (final Exception e) {
				}
				
			}
			
		});
		
		updateLastLocation();
		
	}
	
	public final String getUserName() {
	
		return bukkitPlayer.getName();
	}
	
	public final String getDisplayName() {
	
		return bukkitPlayer.getDisplayName();
	}
	
	public final Inventory getInventory() {
	
		return bukkitPlayer.getInventory();
	}
	
	public final boolean doesHaveItem(final Material m) {
	
		return getInventory().contains(m);
	}
	
	public final boolean doesHaveItem(final ItemStack m) {
	
		return getInventory().contains(m);
	}
	
	public final boolean doesHaveItem(final int m) {
	
		return getInventory().contains(m);
	}
	
	@Override
	public void sendCommandFaliure(final String command, final String why) {
	
		bukkitPlayer.sendMessage(AllAdmin.getMessages(why));
		
	}
	
	@Override
	public boolean canUseCommand(final String command) {
	
		return PermissionsTesting.canUseCommand(bukkitPlayer, command);
		
	}
	
	@Override
	public final boolean hasPermission(final String permission) {
	
		return PermissionsTesting.hasPermission(bukkitPlayer, permission);
	}
	
	public final Player getBukkitPlayer() {
	
		return bukkitPlayer;
	}
	
	public final Location getLastLocation() {
	
		return lastLocation;
	}
	
	public void setData(final String path, final Object data) {
	
		AllAdmin.getInstance().getServer().getScheduler().scheduleAsyncDelayedTask(AllAdmin.getInstance(), new Runnable() {
			
			@Override
			public void run() {
			
				synchronized(userData) {
					userData.set(path, data);
				}
				
			}
			
		});
		
	}
	
	public void setHome(final Location loc, final String homeName) {
	
		final String name = homeName != null ? homeName : "home";
		
		TeleportUtils.setLocation(userData, "homes", name, bukkitPlayer.getLocation());
		
		try {
			
			userData.save(userDataFile);
			
		} catch (final IOException e) {
		}
		
	}
	
	public final Location getHome(final String name) {
	
		return TeleportUtils.getLocation(userData, "homes", name != null ? name : "home");
		
	}
	
	public void updateLastLocation() {
	
		lastLocation = bukkitPlayer.getLocation();
		
	}
	
	@Override
	public void sendMessage(final String message) {
	
		bukkitPlayer.sendMessage(message);
		
	}
	
	@Override
	public void saveData() {
	
		try {
			
			userData.set("lastLocation.x", lastLocation.getBlockX());
			userData.set("lastLocation.y", lastLocation.getBlockY());
			userData.set("lastLocation.z", lastLocation.getBlockZ());
			
			userData.save(userDataFile);
			
		} catch (final Exception e) {
		}
		
	}
	
	@Override
	public boolean isPlayer() {
	
		return true;
	}
	
}
