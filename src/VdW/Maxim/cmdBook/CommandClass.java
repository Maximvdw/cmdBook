package VdW.Maxim.cmdBook;

/* Name:		cmdBook
 * Version: 	1.1.1
 * Made by: 	Maxim Van de Wynckel
 * Build date:	2/09/2012						
 */

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.PluginDescriptionFile;

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
	static String error_nobook = "&cYou can only use this command on a Written book!";
	static String error_noauthor = "&cOnly the author of this book can protect it!";
	static String error_noauthor2 = "&cOnly the author of this book can make it public!";
	static String error_nopage = "&cUnable to find that page!";

	// Confirm messages
	static String confirm_private = "&aYour book has been made private!";
	static String confirm_unprivate = "&2Your book has been made public!";
	static String confirm_reloaded = "&2[&fcmdBook&2] &aReload complete!";

	public boolean onCommand(CommandSender sender, Command cmd,
			String cmdLabel, String[] args) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

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
		if (cmdLabel.equalsIgnoreCase("cb")) {
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
				} else if (argument.equalsIgnoreCase("variables")) {
					if (args.length == 1) {
						// Show HELP page 1
						help help = new help(plugin);
						help.cmdbook_variables_page1(player);
					}else{
						argument = args[1];
						if (argument.equalsIgnoreCase("1"))
						{
							// Show HELP page 1
							help help = new help(plugin);
							help.cmdbook_variables_page1(player);
						}
						else if (argument.equalsIgnoreCase("2"))
						{
							// Show HELP page 2
							help help = new help(plugin);
							help.cmdbook_variables_page2(player);
						}
						else if (argument.equalsIgnoreCase("3"))
						{
							// Show HELP page 3
							help help = new help(plugin);
							help.cmdbook_variables_page3(player);
						}
						else if (argument.equalsIgnoreCase("4"))
						{
							// Show HELP page 4
							help help = new help(plugin);
							help.cmdbook_variables_page4(player);
						}else{
							player.sendMessage(chatColor
									.stringtodata(error_nopage));
						}
					}
				} else if (argument.equalsIgnoreCase("convert")) {
					try {
						if (player == null) {
							// Console cannot create a book
							this.logger.warning(chatColor
									.stringtodata(error_console));
						} else {
							Book converter = new Book(plugin);
							converter.convertBook(player);
						}
					} catch (Exception ex) {

					}
				} else if (argument.equalsIgnoreCase("public")) {
					// Make the book unprivate
					try {
						if (player == null) {
							// Console cannot create a book
							this.logger.warning(chatColor
									.stringtodata(error_console));
						} else {
							ItemStack is = player.getItemInHand();

							// Check if the player is holding a book
							if (is.getTypeId() == 387) {
								BookMeta book = (BookMeta) is.getItemMeta();
								if (book.getAuthor() == player.getName()
										|| player
												.hasPermission("cmdbook.public.all")) {
									if (player
											.hasPermission("cmdbook.public.own")) {
										book.setDisplayName(ChatColor.RESET
												+ book.getTitle());
										is.setItemMeta(book);
										player.sendMessage(chatColor
												.stringtodata(confirm_unprivate));
									}
								} else {
									// Player is not the author
									player.sendMessage(chatColor
											.stringtodata(error_noauthor2));
								}
							} else {
								// No book
								player.sendMessage(chatColor
										.stringtodata(error_nobook));
							}
						}
					} catch (Exception ex) {

					}
				} else if (argument.equalsIgnoreCase("private")) {
					// Make the book private
					try {
						if (player == null) {
							// Console cannot create a book
							this.logger.warning(chatColor
									.stringtodata(error_console));
						} else {
							ItemStack is = player.getItemInHand();

							// Check if the player is holding a book
							if (is.getTypeId() == 387) {
								BookMeta book = (BookMeta) is.getItemMeta();
								if ((book.getAuthor() == player.getName())
										|| player
												.hasPermission("cmdbook.private.all")) {
									if (player
											.hasPermission("cmdbook.private.own")) {
										book.setDisplayName(ChatColor.BLUE
												+ book.getTitle());
										is.setItemMeta(book);
										player.sendMessage(chatColor
												.stringtodata(confirm_private));
									}
								} else {
									// Player is not the author
									player.sendMessage(chatColor
											.stringtodata(error_noauthor));
								}
							} else {
								// No book
								player.sendMessage(chatColor
										.stringtodata(error_nobook));
							}
						}
					} catch (Exception ex) {

					}
				} else if (argument.equalsIgnoreCase("reload")) {
					// Reload configuration
					if (player == null) {
						Configuration cfg = new Configuration(plugin);
						cfg.loadYamls();
						plugin.splitCmd = Configuration.config
								.getString("cmd_split"); // Get split character
						this.logger.info(cmdFormat + "Using split character '"
								+ plugin.splitCmd + "'");
						if (plugin.splitCmd == "") {
							this.logger
									.severe(cmdFormat
											+ "No split character found! Using default '|'");
							plugin.splitCmd = "|";
						}
						logger.info(chatColor.stringtoconsole(confirm_reloaded));
					} else {
						if (player.hasPermission("cmdbook.reload")
								|| (player.isOp() && Configuration.config.getBoolean("opPermissions") == true)) {
							Configuration cfg = new Configuration(plugin);
							cfg.loadYamls();
							player.sendMessage(chatColor
									.stringtodata(confirm_reloaded));
						} else {
							// No permission
							player.sendMessage(chatColor
									.stringtodata(error_permission));
						}
					}
				} else if (argument.equalsIgnoreCase("create")) {
					// Create a cmdBook
					// Check if it is a player and has permissions
					if (player == null) {
						// Console cannot create a book
						this.logger.warning(chatColor
								.stringtodata(error_console));
					} else {
						// Check if player has permissions
						if (player.hasPermission("cmdbook.create") 
								|| (player.isOp() && Configuration.config.getBoolean("opPermissions") == true)) {
							// Start CreateBook
							Book creator = new Book(plugin);
							creator.createCmdBook(player,player.getItemInHand());
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
						this.logger.warning(chatColor
								.stringtodata(error_console));
					} else {
						// Check if player has permissions
						if (player.hasPermission("cmdbook.edit")) {
							// Start CreateBook
							Book creator = new Book(plugin);
							creator.editBook(player,player.getItemInHand());
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
						this.logger.warning(chatColor
								.stringtodata(error_console));
					} else {
						// Check if player has permissions
						if (player.hasPermission("cmdbook.info")) {
							// Start CreateBook
							Book creator = new Book(plugin);
							creator.infocmdBook(player,player.getItemInHand());
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
