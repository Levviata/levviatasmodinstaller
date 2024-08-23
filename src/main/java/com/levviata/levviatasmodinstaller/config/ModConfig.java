package com.levviata.levviatasmodinstaller.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.*;

public class ModConfig {
	// Configuration fields
	public String args;
	public String path;

	private static final String CONFIG_FILE_NAME = "levviatasmodinstaller.json";
	private File configFile;

	public void loadConfig(FMLPreInitializationEvent event) {
		// Get config directory
		File configDir = event.getModConfigurationDirectory();
		configFile = new File(configDir, CONFIG_FILE_NAME);

		// Check if the config file exists, if not, create it
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
				writeDefaultConfig();
			} catch (IOException e) {
				System.err.println("Could not create configuration file: " + e.getMessage());
			}
		}

		// Now load the JSON configuration
		Gson gson = new GsonBuilder().create();
		try (Reader reader = new InputStreamReader(configFile.toURI().toURL().openStream())) {
			ModConfig config = gson.fromJson(reader, ModConfig.class);
			this.args = config.args;
			this.path = config.path;
		} catch (JsonSyntaxException e) {
			System.err.println("Invalid JSON syntax in " + CONFIG_FILE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeDefaultConfig() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String defaultConfigJson = gson.toJson(new ModConfigDefault());
		try (FileWriter writer = new FileWriter(configFile)) {
			writer.write(defaultConfigJson);
		} catch (IOException e) {
			System.err.println("Could not write default configuration: " + e.getMessage());
		}
	}

	// Class to hold default configuration values
	private static class ModConfigDefault {
		public String args = "ModID+JarName AnotherModID+AnotherJarName";
		public String path = "path=path/to/resource";
	}
}
