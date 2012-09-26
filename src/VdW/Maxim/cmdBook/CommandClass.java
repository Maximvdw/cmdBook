package VdW.Maxim.cmdBook;

/* Name:		cmdBook
 * Version: 	1.1.1
 * Made by: 	Maxim Van de Wynckel
 * Build date:	2/09/2012						
 */

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClass implements CommandExecutor {
	// Create global variables
	public final Logger logger = Logger.getLogger("Minecraft"); // Console
	public static cmdBook plugin; // Plugin will now refer to cmdBook

	// Get arguments from main class
	public CommandClass(cmdBook cmdBook) {
		plugin = cmdBook;
	}

	// Error messages (Handy for other languages)
	static String error_permission = "&cYou do not have permission!";
	static String error_console = "This function is only available ingame!";
	static String error_unknowncommand = "&cUnknown command!\n&cType &4/cb help&c for the available commands.";

	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		// This function will be triggered whenever a player
		// performs a command

		// Convert the sender to player
		// Set Default player to NULL
		Player player = null;
		// Check if it is a real player
		if (sender instanceof Player) {
			// Add playerID to player
			player = (Player) sender;
		}
		// Helpful vars
		String argument = null;

		// Check if the player entered a valid command (Only mcrss or rss are
		// allowed)
		// This class will only check for the first row of Arguments
		if (cmdLabel.equalsIgnoreCase("cmdbook")
				|| cmdLabel.equalsIgnoreCase("cb")) {
			// Check if any arguments are given
			if (args.length == 0) {
				// No arguments given
				// Show HELP
				help help = new help(plugin);
				help.cmdbook_help(player);
			} else {
				// Arguments given
				// Check arguments
				argument = args[0];
				if (argument.equalsIgnoreCase("help")
						|| argument.equalsIgnoreCase("?")) {
					// Show HELP
					help help = new help(plugin);
					help.cmdbook_help(player);
				} else if (argument.equalsIgnoreCase("create")) {
					// Create a cmdBook
					// Check if it is a player and has permissions
					if (player == null) {
						// Console cannot create a book
						this.logger.warning(chatColor.stringtodata(error_console));
					} else {
						// Check if player has permissions
						if (player.hasPermission("cmdbook.create")) {
							// Start CreateBook
							Book creator = new Book(plugin);
							creator.createCmdBook(player);
						} else {
							// No permission
							player.sendMessage(chatColor
									.stringtodata(error_permission));
						}
					}
				} else if (argument.equalsIgnoreCase("about")) {
					// Show more information about this plugin
					help help = new help(plugin);
					help.cmdbook_about(player);
				} else if (argument.equalsIgnoreCase("edit")) {
					// Unsign a cmdBook
					if (player == null) {
						// Console cannot create a book
						this.logger.warning(chatColor.stringtodata(error_console));
					} else {
						// Check if player has permissions
						if (player.hasPermission("cmdbook.edit")) {
							// Start CreateBook
							Book creator = new Book(plugin);
							creator.editBook(player);
						} else {
							// No permission
							player.sendMessage(chatColor
									.stringtodata(error_permission));
						}
					}
				} else if (argument.equalsIgnoreCase("info")) {
					// Get commands inside a book
					if (player == null) {
						// Console cannot create a book
						this.logger.warning(chatColor.stringtodata(error_console));
					} else {
						// Check if player has permissions
						if (player.hasPermission("cmdbook.info")) {
							// Start CreateBook
							Book creator = new Book(plugin);
							creator.infocmdBook(player);
						} else {
							// No permission
							player.sendMessage(chatColor
									.stringtodata(error_permission));
						}
					}
				} else {
					// Unknown command
					player.sendMessage(chatColor
							.stringtodata(error_unknowncommand));
				}
			}
		}

		// Return positive
		return true;
	}
}
