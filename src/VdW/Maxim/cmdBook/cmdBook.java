package VdW.Maxim.cmdBook;

/* Name:		cmdBook
 * Version: 	1.1.1
 * Made by: 	Maxim Van de Wynckel
 * Build date:	2/09/2012						
 */

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import VdW.Maxim.cmdBook.Metrics.Metrics;

public class cmdBook extends JavaPlugin {
	// Create global variables
	public final Logger logger = Logger.getLogger("Minecraft"); // Console
	public cmdBook plugin = this; // Plugin will now refer to cmdBook
	private CommandClass CommandListener; // Wait for commands in a different
											// class
	public String splitCmd = "|"; // The default split command char
	public boolean allowChat = true; // Allow chat to be executed in cmdBooks
	public final PlayerListener pl = new PlayerListener(this);

	@Override
	public void onEnable() {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = this.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// This function will be started when the plugin is Enabled
		// Load everything here

		// Show plugin information in console
		this.logger.info(cmdFormat + "Made by: Maxim Van de Wynckel");

		// Start Player listener
		this.logger.info(cmdFormat + "Starting player listener...");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(pl, this);
		this.logger.info(cmdFormat + "Player listener loaded!");

		// Now start Command Listener - This will wait for commands
		this.logger.info(cmdFormat + "Starting command listener...");
		CommandListener = new CommandClass(this);
		// List all commands that have to be heard by the Command Listener
		try{
			getCommand("cb").setExecutor(CommandListener);	
			getCommand("cmdbook").setExecutor(CommandListener); // Might be used by CommandBook
		}catch (Exception ex){
			// Error while enabling the commands
			// Do not show this cuz its a compatibility bug
		}
		this.logger.info(cmdFormat + "Command listener loaded!");

		// Load Metrics
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
			this.logger.info(cmdFormat + "Metrics Stats loaded!");
		} catch (IOException e) {
			// Failed to submit the stats :-(
			this.logger.warning(cmdFormat + "Unable to load Metrics!");
		}

		// Load settings
		this.logger.info(cmdFormat + "Loading configuration file...");
		Configuration configClass = new Configuration(this);
		// first we have to initialize all Files and FileConfigurations
		Configuration.configFile = new File(getDataFolder(), "config.yml"); // //
		// then we use firstRun(); method
		try {
			configClass.firstRun();
		} catch (Exception e) {
			// Error
			this.logger.warning(cmdFormat + "Unable to start Config saver! - This might result in an error!");
		}
		
		try{
			Configuration.config = new YamlConfiguration();
			configClass.loadYamls();
			this.splitCmd = Configuration.config.getString("cmd_split"); // Get split character
			this.logger.info(cmdFormat + "Using split character '" + splitCmd + "'");
			if (splitCmd == "")
			{
				this.logger.severe(cmdFormat + "No split character found! Using default '|'");
				splitCmd = "|";
			}
		} catch (Exception e) {
			// Error
			this.logger.warning(cmdFormat + "Unable to load configuration!");
		}
		
		//Check for updates
		if (Configuration.config.getBoolean("checkUpdates")==true)
		{
			this.getServer().getScheduler()
			.runTaskLaterAsynchronously(this, new Runnable() {
				public void run() {
					updater check = new updater(plugin);
					check.checkUpdates();
				}
			}, 0L);	
		}
	}

	@Override
	public void onDisable() {

	}
}
