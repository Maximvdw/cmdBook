package VdW.Maxim.cmdBook;

/* Name:		cmdBook
 * Version: 	1.4.0
 * Made by: 	Maxim Van de Wynckel
 * Build date:	21/08/2013						
 */

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import VdW.Maxim.cmdBook.Metrics.Metrics;
import VdW.Maxim.cmdBook.Updater.updater;

// Vault
import net.milkbowl.vault.economy.Economy;

public class cmdBook extends JavaPlugin {
	// Create global variables
	public final Logger logger = Logger.getLogger("Minecraft"); // Console
	public static cmdBook plugin; // Plugin will now refer to cmdBook
	private CommandClass commandlistener; // Wait for commands in a different
											// class
	public static int config_version = 6; // Configuration version
	public static int cb_buildversion = 152; // The craftbukkit version, this app was build for
	public String splitCmd = "|"; // The default split command char
	public boolean allowChat = true; // Allow chat to be executed in cmdBooks
	public final PlayerListener pl = new PlayerListener(this);
	public boolean economyFound = false;
	public int chestRadius = 3;

	// Vault variables
    public Economy econ = null;

	
	@Override
	public void onEnable() {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = this.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		plugin = this;
		// This function will be started when the plugin is Enabled
		// Load everything here

		// Show plugin information in console
		this.logger.info(cmdFormat + "Made by: Maxim Van de Wynckel");

		// Check for version errors
		int cb_version = Integer.parseInt(Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf("-")).replaceAll("\\.", ""));
		if (cb_version!=cb_buildversion){
			this.logger.severe(cmdFormat + "cmdBook was not made for this craftbook version!");
		}
		// Show craftbook version
		this.logger.info(cmdFormat + "Using craftbukkit: " + Bukkit.getBukkitVersion().substring(0, Bukkit.getBukkitVersion().indexOf("-")));
		
		// Check for vault hook
        if (!setupEconomy()) {
        	economyFound = false;
        	this.logger.info(cmdFormat + "No economy system found! Disabling feature...");
        }else{
        	economyFound = true;
        	this.logger.info(cmdFormat + "Economy system found!");
        }
		
		// Start Player listener
		this.logger.info(cmdFormat + "Starting player listener...");
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(pl, this);
		this.logger.info(cmdFormat + "Player listener loaded!");

		// Now start Command Listener - This will wait for commands
		this.logger.info(cmdFormat + "Starting command listener...");
		commandlistener = new CommandClass(this);
		// List all commands that have to be heard by the Command Listener
		try{
			getCommand("cb").setExecutor(commandlistener);
		}catch (Exception ex){
			// Error while enabling the commands
			// Do not show this cuz its a compatibility bug
			this.logger.info(cmdFormat + "/cb command already used!");
		}
		try{
			getCommand("cbook").setExecutor(commandlistener);	
		}catch (Exception ex){
			// Error while enabling the commands
			// Do not show this cuz its a compatibility bug
			this.logger.info(cmdFormat + "/cbook command already used!");
		}
		try{
			getCommand("cmdbook").setExecutor(commandlistener);	
		}catch (Exception ex){
			// Error while enabling the commands
			// Do not show this cuz its a compatibility bug
			this.logger.info(cmdFormat + "/cmdbook command already used!");
		}
		
		this.logger.info(cmdFormat + "Command listeners loaded!");

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
			if (Configuration.config.getInt("config")!=config_version)
			{
				this.logger.info(cmdFormat + "Updating configuration...");
				configClass.update();
			}
			this.splitCmd = Configuration.config.getString("cmd_split"); // Get split character
			this.chestRadius =  Configuration.config.getInt("chestRadius"); // Get chest Radius
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
		if (Configuration.config.getBoolean("checkUpdates")==true || Configuration.config.getBoolean("autoUpdate")==true)
		{
			this.logger.info(cmdFormat + "Checking for updates on dev.bukkit.org ...");
			this.getServer().getScheduler()
			.runTaskLaterAsynchronously(this, new Runnable() {
				public void run() {
					updater check = new updater(plugin);
					check.checkUpdates();
				}
			}, 0L);	
		}else{
			this.logger.warning(cmdFormat + "Automatic updates is turned off!");
		}
	}

	@Override
	public void onDisable() {

	}
	
	// Economy
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}
