package VdW.Maxim.cmdBook;

import java.util.logging.Logger;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

import VdW.Maxim.cmdBook.cmdBook;

/* Name:		cmdBook
 * Version: 	1.1.0
 * Made by: 	Maxim Van de Wynckel
 * Build date:	2/09/2012						
 */

public class Book {
	// Create global variables
	public final Logger logger = Logger.getLogger("Minecraft"); // Console
	public static cmdBook plugin; // Plugin will now refer to cmdBook

	// Get arguments from main class
	public Book(cmdBook cmdBook) {
		plugin = cmdBook;
	}

	// Error messages (Handy for other languages)
	static String error_notsigned = "&cPlease sign the book first!";
	static String error_nobook = "&cPlease put the written book in your hand!";
	static String error_permission = "&cYou do not have permission!";
	static String error_noprefix = "&cYou have to start your book with &4[cmdbook]&c!";

	// Confirm messages (Handy for other languages)
	static String confirm_bookcreated = "&aYour cmdBook has been created!";
	static String confirm_unsigned = "&aYour cmdBook has been unsigned!";

	// Other messages (Handy for other languages)
	static String variable_inputquestion = "&f[cmdBook] &aInput: ";

	// Help messages (Handy for other languages)
	static String cmdbook_info = "&6----[ cmdBook Info ]----\n"
			+ "&aBook Title: &f";

	public void infocmdBook(Player player) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		@SuppressWarnings("unused")
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// This function will list all commands inside a book
		String commandList = "";

		// Get contents
		String pageContents[] = getBookContent(player);
		// Remove the [cmdbook]
		pageContents[0] = pageContents[0].substring("[cmdbook]".length());
		// Now read every page an execute the commands
		int count = 1;
		char seperator = '|';
		String bookContent = "";
		for (int i = 0; i < pageContents.length; i++) {
			// Make one string of all those pages
			bookContent = pageContents[i].replace("\n", "");
			// Check if bookcontent includes a || in a command
			bookContent = bookContent.replace("||","#TOKEN#");
			// Now read every letter and search for |
			for (int j = 1; j < bookContent.length() + 1; j++) {
				if (bookContent.charAt(j - 1) == seperator
						|| bookContent.length() == j) {
					// Execute command
					String command = bookContent.substring((j - count), j).replace("|", "");
					command = command.replace("#TOKEN#", "|");
					
					commandList += "&b"
							+ command + "\n";
					count = 0;
				}
				count += 1; // Add int
			}
		}

		// Show commands in the book
		CraftItemStack inHand = (CraftItemStack) player.getItemInHand();
		net.minecraft.server.ItemStack nmsStack = inHand.getHandle();
		NBTTagCompound tag = nmsStack.getTag();
		player.sendMessage(chatColor.stringtodata(cmdbook_info
				+ tag.getString("title") + "\n" + commandList));
	}

	public void createCmdBook(Player player) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// In order to edit a book, the book has to
		// be unsigned, so it can be used for editing again
		// Get the item
		CraftItemStack stack = (CraftItemStack) player.getItemInHand();
		if (stack.getTypeId() == 386) {
			// The book is not signed yet
			// Warn player
			player.sendMessage(chatColor.stringtodata(error_notsigned));
			return;
		} else if (stack.getTypeId() == 387) {
			// The player is holding a book
			// Check if it is a valid cmdBook
			String[] pageContent = getBookContent(player);
			if (pageContent[0].toLowerCase().startsWith("[cmdbook]")) {
				// Commandbook Created :)
				this.logger.info(cmdFormat + player.getName()
						+ " created a cmdBook!");
				player.sendMessage(chatColor.stringtodata(confirm_bookcreated));
			} else {
				// This is not a valid cmdbook
				player.sendMessage(chatColor.stringtodata(error_noprefix));
				return;
			}
			// Change data of book
			CraftItemStack book = (CraftItemStack) player.getItemInHand();
			book.getHandle().getTag()
					.setString("author", ChatColor.RED + "cmdBook");
		} else {
			// The player in not holding a book
			player.sendMessage(chatColor.stringtodata(error_nobook));
			return;
		}
	}

	public static String inputData = "";

	public void editBook(Player player) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// This function will check if the player
		// holds a book, and then it will check
		// if it is a valid cmdBook

		// Get the item the player has in his hand
		CraftItemStack inHand = (CraftItemStack) player.getItemInHand();
		// Check if the player is holding a book
		if (inHand.getTypeId() == 387) {
			// Player is holding a book
			// Now check if it is a cmdBook
			Book check = new Book(plugin);
			String[] pageContent = check.getBookContent(player);

			// Now read author
			net.minecraft.server.ItemStack nmsStack = inHand.getHandle();
			NBTTagCompound tag = nmsStack.getTag();

			String authorPlugin = (ChatColor.RED + "cmdBook").toString(); // The
																			// cmdBook
			// author
			if (pageContent[0].toLowerCase().startsWith("[cmdbook]")
					& tag.get("author").toString()
							.equalsIgnoreCase(authorPlugin)) {
				// It is a cmdBook
				// Now check if the player has permission
				// to execute that
				if (player.hasPermission("cmdbook.edit")) {
					// Player has permisions
					// Unsign the book
					ItemStack is = player.getItemInHand();
					CraftItemStack cis = (CraftItemStack) is;
					NBTTagCompound tc = cis.getHandle().getTag();
					is.setType(Material.BOOK_AND_QUILL);
					tc.remove("author");
					tc.remove("title");
					// Book unsigned
					this.logger.info(cmdFormat + player.getName()
							+ " unsigned a cmdBook!");
					player.sendMessage(chatColor.stringtodata(confirm_unsigned));
				} else {
					// No permission
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			}
		}
	}

	public void performCommands(Player player) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// This function will execute the commands within a book
		// The player is holding

		// Get contents
		inputData = "";
		String pageContents[] = getBookContent(player);
		// Remove the [cmdbook]
		pageContents[0] = pageContents[0].substring("[cmdbook]".length());
		// Now read every page an execute the commands
		int count = 1;
		char seperator = '|';
		String bookContent = "";
		for (int i = 0; i < pageContents.length; i++) {
			// Make one string of all those pages
			bookContent = pageContents[i].replace("\n", "");
			
			// Now replace the shortcuts in Bookcontent
			try {
				bookContent = bookContent.replace("$player", player.getName());
			} catch (Exception ex) {
			}
			try {
				bookContent = bookContent.replace("$targetplayer",
						getTarget(player).getName());
			} catch (Exception ex) {
			}
			try {
				bookContent = bookContent.replace("$health",
						"" + player.getHealth());
			} catch (Exception ex) {
			}
			// Check if bookcontent includes a || in a command
			bookContent = bookContent.replace("||","#TOKEN#");

			// Now read every letter and search for |
			for (int j = 1; j < bookContent.length() + 1; j++) {
				if (bookContent.charAt(j - 1) == seperator
						|| bookContent.length() == j) {
					// Execute command
					String command = bookContent.substring((j - count), j).replace("|", "");
					command = command.replace("#TOKEN#", "|");
					this.logger.info(cmdFormat
							+ player.getName()
							+ " performed command "
							+ command);
					player.chat(command);
					count = 0;
				}
				count += 1; // Add int
			}
		}
	}

	private Player getTarget(Player player) {
		for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
			Entity passenger = onlinePlayer.getPassenger();

			if (passenger instanceof Player
					&& passenger.getEntityId() == player.getEntityId()) {
				return onlinePlayer;
			}
		}

		return null;
	}

	public String[] getBookContent(Player player) {
		// This function will get the contents of a book
		// the player is holding

		String pageContents[] = null;
		try {
			// Get the item the player has in his hand
			CraftItemStack stack = (CraftItemStack) player.getItemInHand();
			net.minecraft.server.ItemStack nmsStack = stack.getHandle();

			// Now read each page content
			NBTTagCompound tag = nmsStack.getTag();
			NBTTagList pagesTag = tag.getList("pages");
			pageContents = new String[pagesTag.size()]; // Dynamically change
														// the size
			for (int i = 0; i < pagesTag.size(); i++) {
				pageContents[i] = ((NBTTagString) pagesTag.get(i)).toString();
			}
		} catch (Exception ex) {
			// Something happend, return null
			return null;
		}

		// Return the contents
		return pageContents;
	}
}
