package com.levviata.levviatasmodinstaller;

import com.levviata.levviatasmodinstaller.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Mod(modid = "levviatasmodinstaller", name = "Levviata's Mod Installer", version = "0.8.0")
public class LevviatasModInstaller {
    public static ModConfig config = new ModConfig();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        try {
            // Load configuration safely
            config.loadConfig(event);
        } catch (Exception e) {
            // Handle configuration loading exceptions
            System.err.println("Error loading configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        runExternalJar();
    }

    private void runExternalJar() {
        // Find the Minecraft directory
		// For client side, go up one level from the data directory
		File minecraftDir = new File(Minecraft.getMinecraft().mcDataDir, ""); // Server side, usually just the current path

        File externalJar = new File(minecraftDir, "LevviatasLauncher-0.8.0.jar"); // Assuming the JAR is in the same folder as the Minecraft base directory

        // Check if the external JAR file exists
        if (!externalJar.exists()) {
            System.err.println("External JAR file does not exist: " + externalJar.getAbsolutePath());
            return;
        }

        try {
            // Run the external JAR
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", externalJar.getAbsolutePath(), config.path, config.args);
            processBuilder.inheritIO(); // Redirect standard output and error to the console
            Process process = processBuilder.start();
            int exitCode = process.waitFor(); // Wait for the process to finish
            System.out.println("External process exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); // Log any exceptions
        }
    }
}