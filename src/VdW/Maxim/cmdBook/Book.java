package VdW.Maxim.cmdBook;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;

import VdW.Maxim.cmdBook.cmdBook;

/* Name:		cmdBook
 * Version: 	1.1.1
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
	static String error_broken = "&cYour cmdBook is broken!";
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

		// In order to edit a book, the book has to
		// be unsigned, so it can be used for editing again
		// Get the item
		CraftItemStack stack = (CraftItemStack) player.getItemInHand();
		if (stack.getTypeId() == 387) {
			// check if the valid yet
			net.minecraft.server.ItemStack nmsStack = stack.getHandle();
			NBTTagCompound tag = nmsStack.getTag();
			String authorPlugin = (ChatColor.RED + "cmdBook").toString();
			String[] pageContent = getBookContent(player);
			if (pageContent[0].toLowerCase().startsWith("[cmdbook]")
					& tag.get("author").toString()
							.equalsIgnoreCase(authorPlugin)) {

			}
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
				bookContent = bookContent.replace("||", "#TOKEN#");
				// Now read every letter and search for |
				for (int j = 1; j < bookContent.length() + 1; j++) {
					if (bookContent.charAt(j - 1) == seperator
							|| bookContent.length() == j) {
						// Execute command
						String command = bookContent.substring((j - count), j)
								.replace("|", "");
						command = command.replace("#TOKEN#", "|");

						commandList += "&b" + command + "\n";
						count = 0;
					}
					count += 1; // Add int
				}
			}

			// Show commands in the book
			CraftItemStack inHand = (CraftItemStack) player.getItemInHand();
			nmsStack = inHand.getHandle();
			tag = nmsStack.getTag();
			player.sendMessage(chatColor.stringtodata(cmdbook_info
					+ tag.getString("title") + "\n" + commandList));
		}else{
			// The player in not holding a book
			player.sendMessage(chatColor.stringtodata(error_nobook));
			return;
		}
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
		}else{
			// The player in not holding a book
			player.sendMessage(chatColor.stringtodata(error_nobook));
			return;
		}
	}

	String answer = "";

	@SuppressWarnings("static-access")
	public void performCommands(Player player) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// This function will execute the commands within a book
		// The player is holding

		// Get contents
		String pageContents[] = getBookContent(player);
		// Remove the [cmdbook]
		pageContents[0] = pageContents[0].substring("[cmdbook]".length());
		// Now read every page an execute the commands
		int count = 1;
		answer = "";
		char seperator = '|';
		String bookContent = "";
		int cmd_counter = 0;
		int counter = 0;
		String[] cmd_list = null;
		cmd_list = new String[cmd_counter];
		for (int i = 0; i < pageContents.length; i++) {
			// Make one string of all those pages
			bookContent = pageContents[i].replace("\n", "");

			// Now replace the shortcuts in Bookcontent
			try {
				if (player.hasPermission("cmdbook.variable.player")) {
					bookContent = bookContent.replace("$player",
							player.getName());
				} else {
					// No permissions
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.variable.targetplayer")) {
					bookContent = bookContent.replace("$targetplayer",
							getTarget(player).getName());
				} else {
					// No permissions
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.variable.health")) {
					bookContent = bookContent.replace("$health",
							"" + player.getHealth());
				} else {
					// No permissions
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.variable.xp")) {
					bookContent = bookContent.replace("$xp",
							"" + player.getExp());
				} else {
					// No permissions
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.variable.targetxp")) {
					bookContent = bookContent.replace("$targetxp", ""
							+ getTarget(player).getExp());
				} else {
					// No permissions
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.variable.lvl")) {
					bookContent = bookContent.replace("$lvl",
							"" + player.getLevel());
				} else {
					// No permissions
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.variable.targetlvl")) {
					bookContent = bookContent.replace("$targetlvl", ""
							+ getTarget(player).getLevel());
				} else {
					// No permissions
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.variable.hunger")) {
					bookContent = bookContent.replace("$hunger", ""
							+ player.getFoodLevel());
				} else {
					// No permissions
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.variable.targethunger")) {
					bookContent = bookContent.replace("$targethunger", ""
							+ getTarget(player).getFoodLevel());
				} else {
					// No permissions
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			} catch (Exception ex) {
			}

			// Check if bookcontent includes a || in a command
			bookContent = bookContent.replace("||", "#TOKEN#");

			// Do a quick check to see how many commands need to be stored
			for (int j = 1; j < bookContent.length() + 1; j++) {
				if (bookContent.charAt(j - 1) == seperator
						|| bookContent.length() == j) {
					cmd_counter += 1;
				}
			}

			String[] tmpArray = new String[cmd_list.length];
			for (int j = 0; j < cmd_list.length; j++) {
				tmpArray[j] = cmd_list[j];
			}
			cmd_list = new String[cmd_counter + 1];
			for (int j = 0; j < tmpArray.length; j++) {
				cmd_list[j] = tmpArray[j];
			}

			// Now read every letter and search for |
			for (int j = 1; j < bookContent.length() + 1; j++) {
				if (bookContent.charAt(j - 1) == seperator
						|| bookContent.length() == j) {
					// Execute command
					try {
						// Check for calculations
						String command = bookContent.substring((j - count), j)
								.replace("|", "");
						command = command.replace("#TOKEN#", "|");
						cmd_list[counter] = command;
						counter += 1;
					} catch (Exception e) {
						// Error
					}
					count = 0;
				}
				if (bookContent.charAt(j - 1) == '$') {
					// Check if it is an input
					if (bookContent.substring(j - 1, j + 5).equalsIgnoreCase(
							"$input")) {
						// It is an input
						String questionUser = "";
						String inputStr_Replace = "$input";
						if (bookContent.charAt(j + 5) == '[') {
							try {
								questionUser = bookContent.substring(j + 6,
										bookContent.indexOf("]", j + 7));
								inputStr_Replace = "$input[" + questionUser
										+ "]";
							} catch (Exception ex) {
								// Error
							}
						}
						ConversationFactory factory;

						// Constructor or whatever init method
						factory = new ConversationFactory(plugin);

						// Where plugin = your JavaPlugin class instance
						if (player instanceof Player) {
							final Map<Object, Object> map = new HashMap<Object, Object>();
							map.put("data", chatColor
									.stringtodata(variable_inputquestion
											+ questionUser));
							Conversation conv = factory
									.withFirstPrompt(new inputPlayer() {
									}).withInitialSessionData(map)
									.withLocalEcho(false)
									.buildConversation((Player) player);
							conv.addConversationAbandonedListener(new ConversationAbandonedListener() {

								@Override
								public void conversationAbandoned(
										ConversationAbandonedEvent event) {
									answer = ""
											+ event.getContext()
													.getSessionData("data");
								}
							});
							conv.begin();

							while (answer == "") {
								try {
									Thread.currentThread().sleep(10);
								} catch (InterruptedException e) {
								}
							}

							if (answer == "/abort") {
								// Abort
								break;
							}

							// Replace input
							try {
								plugin.logger.info(cmdFormat + player.getName()
										+ " input : " + answer);
								bookContent = bookContent.substring(0, j - 1)
										+ answer
										+ bookContent
												.substring(j
														+ inputStr_Replace
																.length() - 1);
								answer = ""; // Reset
							} catch (Exception ex) {
								// Error
								plugin.logger.severe(cmdFormat
										+ "Unable to replace the input!");
								player.sendMessage(chatColor
										.stringtodata(error_broken));
								return;
							}
						}
					}
				}
				count += 1; // Add int
			}
		}

		// Now execute all of those commands
		for (int i = 0; i < cmd_list.length; i++) {
			try {
				String command = cmd_list[i];
				try {
					// Check for calculations
					ScriptEngineManager mgr = new ScriptEngineManager();
					ScriptEngine engine = mgr.getEngineByName("JavaScript");
					Pattern regex = Pattern.compile("calc(.*)");
					Matcher regexMatcher = regex.matcher(command);
					while (regexMatcher.find()) {
						for (int x = 1; x <= regexMatcher.groupCount(); x++) {
							// matched text: regexMatcher.group(i)
							// match start: regexMatcher.start(i)
							// match end: regexMatcher.end(i)
							String strCalc = regexMatcher.group(x)
									.toLowerCase().replace("(", "");
							strCalc = strCalc.replace(")", "");
							double d = Double.parseDouble(engine.eval(strCalc)
									+ "");
							int integer = (int) d;
							command = command.replace("calc"
									+ regexMatcher.group(x).toString(), ""
									+ integer);
						}
					}
					
					// Now check if it is a 'DO' command
					
					// Check for $wait[xxx]
					if (command.toLowerCase().startsWith("$wait[")){
						// Get time
						int timewait = 0;
						timewait = Integer.parseInt(command.substring("$wait[".length(),command.indexOf("]")));
						player.sendMessage(ChatColor.ITALIC + "Sleeping " + timewait + " ms...");
						this.logger.info(cmdFormat + player.getName()
								+ " sleeping " + timewait);
						Thread.currentThread().sleep(timewait);
					}else{
						player.chat(command);
						this.logger.info(cmdFormat + player.getName()
								+ " performed command " + command);
					}
				} catch (Exception e) {
					// Error
					player.chat(command);
					this.logger.info(cmdFormat + player.getName()
							+ " performed command " + command);
				}
			} catch (Exception ex) {
				// Error
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
