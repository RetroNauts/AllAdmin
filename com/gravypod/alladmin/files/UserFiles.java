package com.gravypod.alladmin.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.gravypod.alladmin.AllAdmin;
import com.gravypod.alladmin.permissions.Permissions;

public class UserFiles {

	private static final File userDir;
	private static final HashMap<String, SerializedUser> props = new HashMap<String, SerializedUser>();
	static {
		userDir = new File(AllAdmin.getDataDir(), "users");

		if (!userDir.exists() || !userDir.isDirectory()) {
			userDir.mkdirs();
		}
	}
	
	public static SerializedUser loadUser(String name) {
		
		if (props.containsKey(name)) {
			return props.get(name);
		}
		
		File userFile = new File(userDir, name + ".yml");
		SerializedUser user = null;
		
		if (userFile.exists()) {
			try {
				YamlReader reader = new YamlReader(new FileReader(userFile));
				user = reader.read(SerializedUser.class);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (YamlException e) {
				e.printStackTrace();
			}
		} else {
			user = new SerializedUser();
			user.homes = new HashMap<String, SerializedLocation>();
			user.name = name;
			user.rank = Permissions.getDefaultRank();
		}
		
		props.put(name, user);
		
		return user;
		
	}
	
	public static void unloadUser(String name, SerializedUser serializedUser) throws FileNotFoundException, IOException {
		File userFile = new File(userDir, name + ".yml");
		
		if (props.remove(name).equals(serializedUser) && userFile.exists()) {
			return;
		}
		
		if (!userFile.exists()) {
			userFile.createNewFile();
		}
		YamlWriter writer = new YamlWriter(new FileWriter(userFile));
		writer.write(serializedUser);
		writer.close();
		
	}
	
	
	public static File getUserdir() {
		return userDir;
	}
	
}
