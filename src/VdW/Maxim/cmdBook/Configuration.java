package VdW.Maxim.cmdBook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

/* Name:		cmdBook
 * Version: 	1.3.0
 * Made by: 	Maxim Van de Wynckel
 * Build date:	10/01/2013						
 */

public class Configuration {

	// Configuration Files
	public static File configFile;
	public static FileConfiguration config;

	/* Get the plugin information from the main class */
	public static cmdBook plugin;

	public Configuration(cmdBook pluginClass) {
		plugin = pluginClass;
	}

	public void firstRun() throws Exception {
		// Load String Shortcuts
		// Put plugin at the start of every routine
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		//

		try {

			if (!configFile.exists()) { // checks if the yaml does not exists
				configFile.getParentFile().mkdirs(); // creates the
														// /plugins/<pluginName>/
														// directory if not
														// found
				plugin.logger.info(cmdFormat + "Copying config.yml file!");
				copy(plugin.getResource("config.yml"), configFile); // copies
																	// the yaml
																	// from
				// your jar to the
				// folder
				// /plugin/<pluginName>
			}
		} catch (Exception ex) {
			// Critical error
			plugin.logger
					.severe(cmdFormat + "Unable to copy the config files!");
		}
	}

	/*
	 * plugin copy(); method copies the specified file from your jar to your
	 * /plugins/<pluginName>/ folder
	 */
	void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadYamls() {
		// Load String Shortcuts
		// Put plugin at the start of every routine
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		//

		try {
			config.load(configFile); // loads the contents of the File to its
										// FileConfiguration
			// FileConfiguration
		} catch (Exception e) {
			// Error in config
			plugin.logger.severe(cmdFormat
					+ "An error ocured while loading the config!");
			plugin.logger.severe(cmdFormat + e.getMessage());
		}
	}

	public void saveYamls() {
		try {
			config.save(configFile); // saves the FileConfiguration to its File
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
