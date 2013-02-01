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
	public help(cmdBook cmdBook) {
		plugin = cmdBook;
	}

	// Default language text
	static String error_permissions = "&cYou do not have permission!";
	static String error_console = "This function is only available ingame!";

	// Default help text
	static String cmdbook_help_str = "&6----[ cmdBook ]----\n"
			+ "&a/cb create&f - Create a cmdBook with the book you are holding\n"
			+ "&a/cb edit&f - Edit the commands in the book\n"
			+ "&a/cb info&f - List all commands in the book\n"
			+ "&a/cb private&f - Make your normal book private\n"
			+ "&a/cb public&f - Make your book public\n"
			+ "&a/cb variables&f - List all variables\n"
			+ "&a/cb about&f - Show more information about the plugin\n"
			+ "&c/cb convert&f - Convert deprecated variables\n"
			+ "&c/cb reload&f - Reload configuration";
	static String cmdbook_about_str = "&6----------[ cmdBook About ]----------\n"
			+ "&6Name: cmdBook - Execute multiple commands\n" + "&6Version: ";
	static String cmdbook_about_str2 = "\n&"
			+ "6Author: Maxim Van de Wynckel (Maximvdw)\n"
			+ "&6Site: dev.bukkit.org/server-mods/cmdBook\n"
			+ "&6------------------------------------";
	static String cmdbook_variables_page1_str = "&6----------[ cmdBook Variables ]----------\n"
			+ "&1Page 1 of 4\n"
			+ "&e$player &f- Gives you the players name\n"
			+ "&e$targetplayer &f- Gives your targets player name\n"
			+ "&e$health &f- Gives you the players health as integer\n"
			+ "&e$xp &f- Gives you the players xp as integer\n"
			+ "&e$targetxp &f- Gives your targets xp as integer\n"
			+ "&e$lvl &f- Gives you the players level as integer\n"
			+ "&e$targetlvl &f- Gives your targets level as integer\n"
			+ "&e$hunger &f- Gives your hunger as integer\n"
			+ "&e$targethunger &f- Gives your targets hunger as integer";
	static String cmdbook_variables_page2_str = "&6----------[ cmdBook Variables ]----------\n"
			+ "&1Page 2 of 4\n"
			+ "&e$killer &f- Gives you the players last killer\n"
			+ "&e$targetkiller &f- Gives your targets last killer\n"
			+ "&e$xpos &f- Gives you the players x position\n"
			+ "&e$targetxpos &f- Gives your targets player x position\n"
			+ "&e$ypos &f- Gives you the players y position\n"
			+ "&e$targetypos &f- Gives your targets player y position\n"
			+ "&e$zpos &f- Gives your targets player z position\n"
			+ "&e$targetzpos &f- Gives your targets player z position\n"
			+ "&e$losx &f- Get the x position of the block you are looking at";
	static String cmdbook_variables_page3_str = "&6----------[ cmdBook Variables ]----------\n"
			+ "&1Page 3 of 4\n"
			+ "&e$losy &f- Get the y position of the block you are looking at\n"
			+ "&e$losz &f- Get the z position of the block you are looking at\n"
			+ "&e$script[...] &f- Perform javascript\n"
			+ "&e$input &f- Input the user\n"
			+ "&e$input[Question] &f- Input the user (custom text)\n"
			+ "&e@input &f- Input the user at start\n"
			+ "&e@input[Question] &f- Input the user at start (custom text)\n"
			+ "&e$calc[...] &f- Make a simple calculation";
	static String cmdbook_variables_page4_str = "&6----------[ cmdBook Variables ]----------\n"
			+ "&1Page 4 of 4\n"
			+ "&e$msg[...] &f- Send a private message to the player\n"
			+ "&e$broadcast[...] &f- Broadcast a message to all players\n"
			+ "&e$chat[...] &f- Send a chat message as the player\n"
			+ "&e@runconsole &f- Run all commands as console\n"
			+ "&e@pay[...] &f- Override default usage cost\n"
			+ "&e@uses[...] &f- Decreases to 0, unable to use when 0\n"
			+ "&e@destroywhenused &f- Delete book when uses=0\n"
			+ "&e@hidemessages &f- Hide all cmdBook messages";

	public void cmdbook_help(Player player) {
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
		String HelpTxt_console = chatColor.stringtoconsole(id);

		// Send to sender
		if (player == null) {
			// Command is executed by Console
			logger.info(cmdFormat + "Console peformed 'Help'" + "\n"
					+ HelpTxt_console);
		} else {
			// Command is executed by player
			// Check if he has permissions
			if (player.hasPermission(Permission)) {
				// Send help text
				player.sendMessage(HelpTxt);
			} else {
				// Error
				player.sendMessage(chatColor.stringtodata(error_permissions));
			}
		}
	}

	public void cmdbook_variables_page4(Player player) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// CHANGE HELP ID HERE
		String id = cmdbook_variables_page4_str;
		String Permission = "cmdbook.help";

		Logger logger = Logger.getLogger("Minecraft");
		// Generate help text
		String HelpTxt = chatColor.stringtodata(id);
		String HelpTxt_console = chatColor.stringtoconsole(id);

		// Send to sender
		if (player == null) {
			// Command is executed by Console
			logger.info(cmdFormat + "Console peformed 'Help'" + "\n"
					+ HelpTxt_console);
		} else {
			// Command is executed by player
			// Check if he has permissions
			if (player.hasPermission(Permission)) {
				// Send help text
				player.sendMessage(HelpTxt);
			} else {
				// Error
				player.sendMessage(chatColor.stringtodata(error_permissions));
			}
		}
	}

	public void cmdbook_variables_page3(Player player) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// CHANGE HELP ID HERE
		String id = cmdbook_variables_page3_str;
		String Permission = "cmdbook.help";

		Logger logger = Logger.getLogger("Minecraft");
		// Generate help text
		String HelpTxt = chatColor.stringtodata(id);
		String HelpTxt_console = chatColor.stringtoconsole(id);

		// Send to sender
		if (player == null) {
			// Command is executed by Console
			logger.info(cmdFormat + "Console peformed 'Help'" + "\n"
					+ HelpTxt_console);
		} else {
			// Command is executed by player
			// Check if he has permissions
			if (player.hasPermission(Permission)) {
				// Send help text
				player.sendMessage(HelpTxt);
			} else {
				// Error
				player.sendMessage(chatColor.stringtodata(error_permissions));
			}
		}
	}

	public void cmdbook_variables_page2(Player player) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// CHANGE HELP ID HERE
		String id = cmdbook_variables_page2_str;
		String Permission = "cmdbook.help";

		Logger logger = Logger.getLogger("Minecraft");
		// Generate help text
		String HelpTxt = chatColor.stringtodata(id);
		String HelpTxt_console = chatColor.stringtoconsole(id);

		// Send to sender
		if (player == null) {
			// Command is executed by Console
			logger.info(cmdFormat + "Console peformed 'Help'" + "\n"
					+ HelpTxt_console);
		} else {
			// Command is executed by player
			// Check if he has permissions
			if (player.hasPermission(Permission)) {
				// Send help text
				player.sendMessage(HelpTxt);
			} else {
				// Error
				player.sendMessage(chatColor.stringtodata(error_permissions));
			}
		}
	}

	public void cmdbook_variables_page1(Player player) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// CHANGE HELP ID HERE
		String id = cmdbook_variables_page1_str;
		String Permission = "cmdbook.help";

		Logger logger = Logger.getLogger("Minecraft");
		// Generate help text
		String HelpTxt = chatColor.stringtodata(id);
		String HelpTxt_console = chatColor.stringtoconsole(id);

		// Send to sender
		if (player == null) {
			// Command is executed by Console
			logger.info(cmdFormat + "Console peformed 'Help'" + "\n"
					+ HelpTxt_console);
		} else {
			// Command is executed by player
			// Check if he has permissions
			if (player.hasPermission(Permission)) {
				// Send help text
				player.sendMessage(HelpTxt);
			} else {
				// Error
				player.sendMessage(chatColor.stringtodata(error_permissions));
			}
		}
	}

	public void cmdbook_about(Player player) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// CHANGE HELP ID HERE
		String id = cmdbook_about_str;
		String Permission = "cmdbook.about";

		Logger logger = Logger.getLogger("Minecraft");
		// Generate help text
		String HelpTxt = chatColor.stringtodata(id + pdfFile.getVersion()
				+ cmdbook_about_str2);
		String HelpTxt_console = chatColor.stringtoconsole(id
				+ pdfFile.getVersion() + cmdbook_about_str2);

		// Send to sender
		if (player == null) {
			// Command is executed by Console
			logger.info(cmdFormat + "Console peformed 'About'" + "\n"
					+ HelpTxt_console);
		} else {
			// Command is executed by player
			// Check if he has permissions
			if (player.hasPermission(Permission)) {
				// Send help text
				player.sendMessage(HelpTxt);
			} else {
				// Error
				player.sendMessage(chatColor.stringtodata(error_permissions));
			}
		}
	}
}
