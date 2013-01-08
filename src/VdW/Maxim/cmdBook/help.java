package VdW.Maxim.cmdBook;

/* Name:		cmdBook
 * Version: 	1.1.1
 * Made by: 	Maxim Van de Wynckel
 * Build date:	24/09/2012						
 */

import java.util.logging.Logger;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

public class help extends JavaPlugin {
	// Create global variables
	public final Logger logger = Logger.getLogger("Minecraft"); // Console
	public static cmdBook plugin; // Plugin will now refer to cmdBook
	
	// Get arguments from main class
	public help(cmdBook cmdBook){
		plugin = cmdBook;
	}
	
	// Default language text
	static String error_permissions = "&cYou do not have permission!";
	static String error_console = "This function is only available ingame!";
	
	// Default help text
	static String cmdbook_help_str = "&6----[ cmdBook ]----\n" +
			"&a/cb create&f - Create a cmdBook with the book you are holding\n" +
			"&a/cb edit&f - Edit the commands in the book\n" + 
			"&a/cb info&f - List all commands in the book\n" +
			"&a/cb private&f - Make your normal book private\n" +
			"&a/cb about&f - Show more information about the plugin";
	static String cmdbook_about_str = "&6----------[ cmdBook About ]----------\n" +
			"&6Name: cmdBook - Execute multiple commands\n" +
			"&6Version: ";
	static String cmdbook_about_str2 = "\n&" +
			"6Author: Maxim Van de Wynckel (Maximvdw)\n" +
			"&6Site: dev.bukkit.org/server-mods/cmdBook\n" + 
			"&6------------------------------------";
	
	public void cmdbook_help(Player player){
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------
		
		// CHANGE HELP ID HERE
		String id = cmdbook_help_str;
		String Permission = "cmdbook.help";
		
		Logger logger = Logger.getLogger("Minecraft");
		// Generate help text
		String HelpTxt = chatColor.stringtodata(id);
		String HelpTxt_console = chatColor.stringtodelete(id);
		
		// Send to sender
		if(player==null){
			// Command is executed by Console
			logger.info(cmdFormat + "Console peformed 'Help'" + "\n" + HelpTxt_console);
		}else{
			// Command is executed by player
			// Check if he has permissions
			if(player.hasPermission(Permission)){
				// Send help text
				player.sendMessage(HelpTxt);
			}else{
				// Error
				player.sendMessage(chatColor.stringtodata(error_permissions));
			}
		}
	}
    public void cmdbook_about(Player player){
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------
		
    	// CHANGE HELP ID HERE
		String id = cmdbook_about_str;
		String Permission = "cmdbook.about";
		
		Logger logger = Logger.getLogger("Minecraft");
		// Generate help text
		String HelpTxt = chatColor.stringtodata(id + pdfFile.getVersion() + cmdbook_about_str2);
		String HelpTxt_console = chatColor.stringtodelete(id + pdfFile.getVersion() + cmdbook_about_str2);
		
		// Send to sender
		if(player==null){
			// Command is executed by Console
			logger.info(cmdFormat + "Console peformed 'About'" + "\n" + HelpTxt_console);
		}else{
			// Command is executed by player
			// Check if he has permissions
			if(player.hasPermission(Permission)){
				// Send help text
				player.sendMessage(HelpTxt);
			}else{
				// Error
				player.sendMessage(chatColor.stringtodata(error_permissions));
			}
		}
    }
}
