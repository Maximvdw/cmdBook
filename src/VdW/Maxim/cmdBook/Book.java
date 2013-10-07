package VdW.Maxim.cmdBook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.BlockIterator;

import VdW.Maxim.cmdBook.cmdBook;

/* Name:		cmdBook
 * Version: 	1.4.0
 * Made by: 	Maxim Van de Wynckel
 * Build date:	28/01/2013						
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
	static String error_money = "&2[&fcmdBook&2] &cYou do not have {MONEY}$!";
	static String error_alreadycmd = "&cThis is already a cmdBook!";
	static String error_effect = "&cThe effect {EFFECT} is not valid. Use BLAZE_SHOOT BOW_FIRE CLICK1 CLICK2 DOOR_TOGGLE ENDER_SIGNAL EXTINGUISH GHAST_SHOOT GHAST_SHRIEK MOBSPAWNER_FLAMES POTION_BREAK RECORD_PLAY SMOKE STEP_SOUND ZOMBIE_CHEW_IRON_DOOR ZOMBIE_CHEW_WOODEN_DOOR ZOMBIE_DESTROY_DOOR";
	static String error_uses = "&cYou cannot use this book anymore!";
	static String error_wait = "&cPlease wait until {TIME} before using this book again!";
	static String error_normalbook = "&ccmdBook is amazed by the complexity of this book, but it is not a cmdBook...";
	// Confirm messages (Handy for other languages)
	static String confirm_bookcreated = "&aYour cmdBook has been created!";
	static String confirm_unsigned = "&aYour cmdBook has been unsigned!";
	static String confirm_commandsperformed = "&acmdBook Commands performed!";
	static String confirm_converted = "&aYour cmdBook has been converted to the latest version!";
	static String confirm_money = "&2[&fcmdBook&2] &a{MONEY}$ payed!";

	// Other messages (Handy for other languages)
	static String variable_inputquestion = "&2[&fcmdBook&2] &aInput: ";

	// Help messages (Handy for other languages)
	static String cmdbook_info = "&6----[ cmdBook Info ]----\n"
			+ "&aBook Title: &f";

	public void infocmdBook(Player player, ItemStack item) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// This function will list all commands inside a book
		String commandList = "";

		// In order to edit a book, the book has to
		// be unsigned, so it can be used for editing again
		// Get the item
		ItemStack is = item;
		if (is.getTypeId() == 387) {
			BookMeta book = (BookMeta) is.getItemMeta();
			// check if the valid yet
			Object[] pageContent = getBookContent(player, item);
			// Get contents
			Object pageContents[] = getBookContent(player, item);
			// Remove the [cmdbook]
			if (pageContents[0].toString().startsWith("[cmdbook]")) {
				pageContents[0] = pageContents[0].toString().substring(
						"[cmdbook]".length());
				// Now read every page an execute the commands
				int count = 1;
				String seperator = plugin.splitCmd;
				String bookContent = "";
				for (int i = 0; i < pageContents.length; i++) {
					ScriptEngineManager mgr = new ScriptEngineManager();
					ScriptEngine engine = mgr.getEngineByName("javascript");
					Pattern regex = Pattern.compile("calc\\((.*?)\\)");
					Matcher regexMatcher = regex.matcher(pageContent[i]
							.toString());
					while (regexMatcher.find()) {
						for (int x = 1; x <= regexMatcher.groupCount(); x++) {
							try {
								// matched text: regexMatcher.group(i)
								// match start: regexMatcher.start(i)
								// match end: regexMatcher.end(i)
								engine.getBindings(ScriptContext.ENGINE_SCOPE);
								commandList += "&4'calc("
										+ regexMatcher.group(x).toString()
										+ ")' is deprecated!\nUse /cb convert to fix this issue\n";
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}

					// Make one string of all those pages
					bookContent = pageContents[i].toString().replace("\n", "");
					// Check if bookcontent includes a || in a command
					bookContent = bookContent.replace(plugin.splitCmd
							+ plugin.splitCmd, "#TOKEN#");

					if (bookContent.toLowerCase().contains("@destroywhenused")) {
						bookContent = bookContent.replace("@destroywhenused",
								"");
						commandList += "&4Book will be destroyed when uses=0!\n";
					}
					if (bookContent.toLowerCase().contains("@runconsole")) {
						bookContent = bookContent.replace("@runconsole", "");
						commandList += "&4Commands will be run as console\n";
					}
					if (bookContent.toLowerCase().contains("@hidemessages")) {
						bookContent = bookContent.replace("@hidemessages", "");
						commandList += "&4cmdBook Messages will be hidden\n";
					}
			
					// Check permissions
					regex = Pattern.compile("\\@name\\[(.*?)\\]");
					regexMatcher = regex.matcher(bookContent);
					String name = "";
					while (regexMatcher.find()) {
						try {
							// matched text: regexMatcher.group(i)
							// match start: regexMatcher.start(i)
							// match end: regexMatcher.end(i)

							name = regexMatcher
									.group(0).replace("@name[", "")
									.replace("]", "");
							bookContent = bookContent
									.replace(regexMatcher.group(0), "");
							commandList += "&aBook name: &f" + name + "\n";
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					
					// Default Economy
					int economy_price_use = Configuration.config
							.getInt("economy.use_price");
					// Now check for price[] in book
					regex = Pattern.compile("\\@price\\[(.*?)\\]");
					regexMatcher = regex.matcher(bookContent);
					while (regexMatcher.find()) {
						try {
							// matched text: regexMatcher.group(i)
							// match start: regexMatcher.start(i)
							// match end: regexMatcher.end(i)

							economy_price_use = Integer.parseInt(regexMatcher
									.group(0).replace("@price[", "")
									.replace("]", ""));
							bookContent = bookContent.replace(
									regexMatcher.group(0), "");
							commandList += "&4You need to pay "
									+ economy_price_use
									+ "$ to use this book!\n";
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
					// Now check for uses[] in book
					int uses = -1;
					regex = Pattern.compile("\\@uses\\[(.*?)\\]");
					regexMatcher = regex.matcher(bookContent);
					while (regexMatcher.find()) {
						try {
							// matched text: regexMatcher.group(i)
							// match start: regexMatcher.start(i)
							// match end: regexMatcher.end(i)

							uses = Integer.parseInt(regexMatcher.group(0)
									.replace("@uses[", "").replace("]", ""));
							bookContent = bookContent.replace(
									regexMatcher.group(0), "");
							if (uses != 0) {
								commandList += "&4There are " + uses
										+ " uses left!\n";
							} else {
							}
						} catch (Exception ex) {
						}
					}

					// Get cooldown
					regex = Pattern.compile("\\@cooldown\\[(.*?)\\]");
					regexMatcher = regex.matcher(bookContent);
					int cooldown = 0;
					while (regexMatcher.find()) {
						try {
							// matched text: regexMatcher.group(i)
							// match start: regexMatcher.start(i)
							// match end: regexMatcher.end(i)
							plugin.logger.info(cmdFormat
									+ "Cooldown loaded: "
									+ regexMatcher.group(0)
											.replace("@cooldown[", "")
											.replace("]", ""));
							cooldown = Integer
									.parseInt(regexMatcher.group(0)
											.replace("@cooldown[", "")
											.replace("]", ""));
							commandList += "&4Cooldown set to " + cooldown
									+ " minutes\n";
							bookContent = bookContent.replace(
									regexMatcher.group(0), "");
							// Check last used
							regex = Pattern.compile("\\@lastused\\[(.*?)\\]");
							regexMatcher = regex.matcher(bookContent);
							DateFormat dateFormat = new SimpleDateFormat(
									"yyyy/MM/dd HH:mm:ss");
							Date lastused = new Date();
							Date now = new Date();
							while (regexMatcher.find()) {
								try {
									// matched text: regexMatcher.group(i)
									// match start: regexMatcher.start(i)
									// match end: regexMatcher.end(i)

									lastused = dateFormat.parse(regexMatcher
											.group(0).replace("@lastused[", "")
											.replace("]", ""));
									bookContent = bookContent.replace(
											regexMatcher.group(0), "");
									book = (BookMeta) item.getItemMeta();
									if ((lastused.getTime() + (cooldown * 60000)) <= now
											.getTime()) {

									} else {
										commandList += "&4Book locked till "
												+ dateFormat
														.format(((lastused
																.getTime() + (cooldown * 60000))))
														.toString() + "\n";
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
							
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}

					// Economy
					try {
						if (Configuration.config.getBoolean("economy.enabled") == false
								|| plugin.economyFound == false
								|| plugin.economyFound == false
								|| plugin.economyFound == false) {
							// No economy enabled
						} else {
							if (plugin.econ.getBalance(player.getName()) >= economy_price_use) {
								plugin.econ.withdrawPlayer(player.getName(),
										economy_price_use);
								// Send message to player
								player.sendMessage(chatColor
										.stringtodata(confirm_money.replaceAll(
												"\\{MONEY\\}",
												economy_price_use + "")));
							} else {
								// No money
								player.sendMessage(chatColor
										.stringtodata(error_money.replaceAll(
												"\\{MONEY\\}",
												economy_price_use + "")));
								return;
							}
						}
					} catch (Exception ex) {
						// Error
					}

					// Now read every letter and search for |
					for (int j = 1; j < bookContent.length() + 1; j++) {
						if (bookContent.charAt(j - 1) == seperator
								.toCharArray()[0] || bookContent.length() == j) {
							// Execute command
							String command = bookContent.substring((j - count),
									j).replace(plugin.splitCmd, "");
							command = command.replace("#TOKEN#",
									plugin.splitCmd);

							commandList += "&b"
									+ chatColor.stringtodelete(command) + "\n";
							count = 0;
						}
						count += 1; // Add int
					}
				}

				// Show commands in the book
				player.sendMessage(chatColor.stringtodata(cmdbook_info
						+ book.getTitle() + "\n" + commandList));
			} else {
				// The player in not holding a book
				player.sendMessage(chatColor.stringtodata(error_normalbook));
				return;
			}
		}else{
			// The player in not holding a book
			player.sendMessage(chatColor.stringtodata(error_nobook));
			return;
		}
	}

	public void createCmdBook(Player player, ItemStack item, Boolean silent) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// In order to edit a book, the book has to
		// be unsigned, so it can be used for editing again
		// Get the item
		ItemStack stack = item;
		if (stack.getTypeId() == 386) {
			// The book is not signed yet
			// Warn player
			player.sendMessage(chatColor.stringtodata(error_notsigned));
			return;
		} else if (stack.getTypeId() == 387) {
			// The player is holding a book
			// Check if it is a valid cmdBook
			Object pageContent[] = getBookContent(player, item);
			// Check if all variables are allowed
			ItemStack is = stack;
			BookMeta book = (BookMeta) is.getItemMeta();
			if (book.getAuthor().contains("cmdBook") == false) {
				if (pageContent[0].toString().toLowerCase()
						.startsWith("[cmdbook]")) {
					// Check if player has enough money
					if (Configuration.config.getBoolean("economy.enabled") == false
							|| plugin.economyFound == false
							|| plugin.economyFound == false
							|| silent == true) {
						// No economy enabled
						// Commandbook Created :)
						this.logger.info(cmdFormat + player.getName()
								+ " created a cmdBook!");
						if (silent == false){
							player.sendMessage(chatColor
									.stringtodata(confirm_bookcreated));	
						}
					} else {
						if (plugin.econ.getBalance(player.getName()) >= Configuration.config
								.getInt("economy.create_price")) {
							plugin.econ.withdrawPlayer(player.getName(),
									Configuration.config
											.getInt("economy.create_price"));
							// Send message to player
							player.sendMessage(chatColor.stringtodata(confirm_money
									.replaceAll(
											"\\{MONEY\\}",
											Configuration.config
													.getString("economy.create_price"))));

							// Commandbook Created :)
							this.logger.info(cmdFormat + player.getName()
									+ " created a cmdBook!");
							player.sendMessage(chatColor
									.stringtodata(confirm_bookcreated));
						} else {
							// No money
							player.sendMessage(chatColor.stringtodata(error_money
									.replaceAll(
											"\\{MONEY\\}",
											Configuration.config
													.getString("economy.create_price"))));
							return;
						}
					}
				} else {
					// This is not a valid cmdbook
					player.sendMessage(chatColor.stringtodata(error_noprefix));
					return;
				}
			} else {
				// Already a cmdbook
				player.sendMessage(chatColor.stringtodata(error_alreadycmd));
				return;
			}
			// Change data of book
			book.setAuthor(ChatColor.RED + "cmdBook");
			book.setTitle(ChatColor.UNDERLINE + book.getTitle());
			is.setItemMeta(book);
		} else {
			// The player in not holding a book
			player.sendMessage(chatColor.stringtodata(error_nobook));
			return;
		}
	}

	public void editBook(Player player, ItemStack item) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// This function will check if the player
		// holds a book, and then it will check
		// if it is a valid cmdBook

		// Get the item the player has in his hand
		ItemStack is = item;

		// Check if the player is holding a book
		if (is.getTypeId() == 387) {
			BookMeta book = (BookMeta) is.getItemMeta();
			// Player is holding a book
			// Now check if it is a cmdBook
			Object pageContent[] = getBookContent(player, item);

			// Now read author
			String authorPlugin = (ChatColor.RED + "cmdBook").toString(); // The
																			// cmdBook
			// author
			try {
				if (pageContent[0].toString().toLowerCase()
						.startsWith("[cmdbook]")
						& (book.getAuthor().equalsIgnoreCase(authorPlugin) || player
								.hasPermission("cmdbook.public.all"))) {
					// It is a cmdBook
					// Now check if the player has permission
					// to execute that
					if (player.hasPermission("cmdbook.edit")) {
						// Player has permisions
						// Unsign the book
						if (Configuration.config.getBoolean("economy.enabled") == false
								|| plugin.economyFound == false
								|| plugin.economyFound == false) {
							// No economy enabled
						} else {
							if (plugin.econ.getBalance(player.getName()) >= Configuration.config
									.getInt("economy.edit_price")) {
								plugin.econ.withdrawPlayer(player.getName(),
										Configuration.config
												.getInt("economy.edit_price"));
								// Send message to player
								player.sendMessage(chatColor.stringtodata(confirm_money
										.replaceAll(
												"\\{MONEY\\}",
												Configuration.config
														.getString("economy.edit_price"))));
							} else {
								// No money
								player.sendMessage(chatColor.stringtodata(error_money
										.replaceAll(
												"\\{MONEY\\}",
												Configuration.config
														.getString("economy.edit_price"))));
								return;
							}
						}
						is = player.getItemInHand();
						book = (BookMeta) is.getItemMeta();
						is.setType(Material.BOOK_AND_QUILL);
						book.setAuthor("");
						book.setTitle("");
						book.setDisplayName("");
						is.setItemMeta(book);
						// Book unsigned
						this.logger.info(cmdFormat + player.getName()
								+ " unsigned a cmdBook!");
						player.sendMessage(chatColor
								.stringtodata(confirm_unsigned));
					} else {
						// No permission
						player.sendMessage(chatColor
								.stringtodata(error_permission));
					}
				}
			} catch (Exception ex) {
				// The player in not holding a book
				player.sendMessage(chatColor.stringtodata(error_nobook));
				return;
			}
		} else {
			// The player in not holding a book
			player.sendMessage(chatColor.stringtodata(error_nobook));
			return;
		}
	}

	String answer = "";

	public void performCommands(Player player, ItemStack item) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// This function will execute the commands within a book
		// The player is holding

		// Get contents
		Object pageContents[] = getBookContent(player, item);
		// Remove the [cmdbook]
		pageContents[0] = pageContents[0].toString().substring(
				"[cmdbook]".length());
		// Now read every page an execute the commands
		int count = 1;
		answer = "";
		String seperator = plugin.splitCmd;
		String bookContent = "";
		boolean runConsole = false;
		boolean destroywhenused = false;
		boolean hideMessages = false;
		int cmd_counter = 0;
		int counter = 0;
		String[] cmd_list = null;
		cmd_list = new String[cmd_counter];
		for (int i = 0; i < pageContents.length; i++) {
			// Make one string of all those pages
			bookContent = pageContents[i].toString().replace("\n", "");

			// Now replace the shortcuts in Bookcontent
			try {
				if (player.hasPermission("cmdbook.use.runconsole")) {
					if (bookContent.toLowerCase().contains("@runconsole")) {
						runConsole = true;
					}
				}
				bookContent = bookContent.replace("@runconsole", "");
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.destroywhenused")) {
					if (bookContent.toLowerCase().contains("@destroywhenused")) {
						destroywhenused = true;
					}
				}
				bookContent = bookContent.replace("@destroywhenused", "");
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.hidemessages")) {
					if (bookContent.toLowerCase().contains("@hidemessages")) {
						hideMessages = true;
					}
				}
				bookContent = bookContent.replace("@hidemessages", "");
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.player")) {
					bookContent = bookContent.replace("$player",
							player.getName());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.targetplayer")) {
					bookContent = bookContent.replace("$targetplayer",
							getTarget(player).getName());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.health")) {
					bookContent = bookContent.replace("$health",
							"" + player.getHealth());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.lvl")) {
					bookContent = bookContent.replace("$lvl",
							"" + player.getLevel());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.targetlvl")) {
					bookContent = bookContent.replace("$targetlvl", ""
							+ getTarget(player).getLevel());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.hunger")) {
					bookContent = bookContent.replace("$hunger",
							"" + player.getFoodLevel());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.targethunger")) {
					bookContent = bookContent.replace("$targethunger", ""
							+ getTarget(player).getFoodLevel());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.killer")) {
					bookContent = bookContent.replace("$killer",
							"" + player.getKiller());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.targetkiller")) {
					bookContent = bookContent.replace("$targetkiller", ""
							+ player.getKiller());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.xpos")) {
					bookContent = bookContent.replace("$xpos", ""
							+ player.getLocation().getX());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.ypos")) {
					bookContent = bookContent.replace("$ypos", ""
							+ player.getLocation().getY());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.zpos")) {
					bookContent = bookContent.replace("$zpos", ""
							+ player.getLocation().getZ());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.targetxpos")) {
					bookContent = bookContent.replace("$targetxpos", ""
							+ getTarget(player).getLocation().getX());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.targetypos")) {
					bookContent = bookContent.replace("$targetypos", ""
							+ getTarget(player).getLocation().getY());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.targetzpos")) {
					bookContent = bookContent.replace("$targetzpos", ""
							+ getTarget(player).getLocation().getZ());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.losx")) {
					bookContent = bookContent.replace("$losx", ""
							+ player.getTargetBlock(null, 200).getX());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.losy")) {
					bookContent = bookContent.replace("$losy", ""
							+ player.getTargetBlock(null, 200).getY());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.losz")) {
					bookContent = bookContent.replace("$losz", ""
							+ player.getTargetBlock(null, 200).getZ());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.xp")) {
					bookContent = bookContent.replace("$xp",
							"" + player.getExp());
				}
			} catch (Exception ex) {
			}
			try {
				if (player.hasPermission("cmdbook.use.targetxp")) {
					bookContent = bookContent.replace("$targetxp", ""
							+ getTarget(player).getExp());
				}
			} catch (Exception ex) {
			}

			// Default Economy
			int economy_price_use = Configuration.config
					.getInt("economy.use_price");
			// Now check for price[] in book
			Pattern regex = Pattern.compile("\\@price\\[(.*?)\\]");
			Matcher regexMatcher = regex.matcher(bookContent);
			while (regexMatcher.find()) {
				try {
					// matched text: regexMatcher.group(i)
					// match start: regexMatcher.start(i)
					// match end: regexMatcher.end(i)
					plugin.logger.info(cmdFormat
							+ "Price book loaded: "
							+ regexMatcher.group(0).replace("@price[", "")
									.replace("]", ""));
					economy_price_use = Integer.parseInt(regexMatcher.group(0)
							.replace("@price[", "").replace("]", ""));
					bookContent = bookContent
							.replace(regexMatcher.group(0), "");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			// Check permissions
			regex = Pattern.compile("\\@name\\[(.*?)\\]");
			regexMatcher = regex.matcher(bookContent);
			String name = "";
			while (regexMatcher.find()) {
				try {
					// matched text: regexMatcher.group(i)
					// match start: regexMatcher.start(i)
					// match end: regexMatcher.end(i)

					name = regexMatcher
							.group(0).replace("@name[", "")
							.replace("]", "");
					bookContent = bookContent
							.replace(regexMatcher.group(0), "");
					if (!player.hasPermission("cmdbook.book." + name)){
						player.sendMessage(chatColor.stringtodata("&cNo permissions for book '" + name + "'"));
						return;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			
			// Now check for uses[] in book
			int uses = -1;
			regex = Pattern.compile("\\@uses\\[(.*?)\\]");
			regexMatcher = regex.matcher(bookContent);
			while (regexMatcher.find()) {
				try {
					// matched text: regexMatcher.group(i)
					// match start: regexMatcher.start(i)
					// match end: regexMatcher.end(i)
					plugin.logger.info(cmdFormat
							+ "Loaded uses of book: "
							+ regexMatcher.group(0).replace("@uses[", "")
									.replace("]", ""));
					uses = Integer.parseInt(regexMatcher.group(0)
							.replace("@uses[", "").replace("]", ""));
					bookContent = bookContent
							.replace(regexMatcher.group(0), "");
					BookMeta book = (BookMeta) item.getItemMeta();
					if (uses != 0) {
						int nextuse = Integer.parseInt(regexMatcher.group(0)
								.replace("@uses[", "").replace("]", "")) - 1;
						book.setPage(
								1,
								book.getPage(1).replace(regexMatcher.group(0),
										"@uses[" + nextuse + "]"));
						item.setItemMeta(book);
					} else {
						if (destroywhenused == true) {
							item = null;
							player.setItemInHand(null);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			if (uses > 0 || uses == -1) {
				// Get cooldown
				regex = Pattern.compile("\\@cooldown\\[(.*?)\\]");
				regexMatcher = regex.matcher(bookContent);
				int cooldown = 0;
				while (regexMatcher.find()) {
					try {
						// matched text: regexMatcher.group(i)
						// match start: regexMatcher.start(i)
						// match end: regexMatcher.end(i)
						plugin.logger.info(cmdFormat
								+ "Cooldown loaded: "
								+ regexMatcher.group(0)
										.replace("@cooldown[", "")
										.replace("]", ""));
						cooldown = Integer.parseInt(regexMatcher.group(0)
								.replace("@cooldown[", "").replace("]", ""));
						bookContent = bookContent.replace(
								regexMatcher.group(0), "");
						// Check last used
						regex = Pattern.compile("\\@lastused\\[(.*?)\\]");
						regexMatcher = regex.matcher(bookContent);
						DateFormat dateFormat = new SimpleDateFormat(
								"yyyy/MM/dd HH:mm:ss");
						Date lastused = new Date();
						Date now = new Date();
						while (regexMatcher.find()) {
							try {
								// matched text: regexMatcher.group(i)
								// match start: regexMatcher.start(i)
								// match end: regexMatcher.end(i)
								plugin.logger.info(cmdFormat
										+ "Loaded last use of book: "
										+ regexMatcher.group(0)
												.replace("@lastused[", "")
												.replace("]", ""));
								lastused = dateFormat.parse(regexMatcher
										.group(0).replace("@lastused[", "")
										.replace("]", ""));
								bookContent = bookContent.replace(
										regexMatcher.group(0), "");
								BookMeta book = (BookMeta) item.getItemMeta();
								if ((lastused.getTime() + (cooldown * 60000)) <= now
										.getTime()) {
									book.setPage(
											1,
											book.getPage(1)
													.replace(
															regexMatcher
																	.group(0),
															"@lastused["
																	+ dateFormat
																			.format(now)
																	+ "]"));
									item.setItemMeta(book);
								} else {
									player.sendMessage(chatColor.stringtodata(error_wait.replace(
											"{TIME}",
											dateFormat
													.format(((lastused
															.getTime() + (cooldown * 60000))))
													.toString())));
									return;
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				// Economy
				try {
					if (Configuration.config.getBoolean("economy.enabled") == false
							|| plugin.economyFound == false
							|| plugin.economyFound == false
							|| plugin.economyFound == false) {
						// No economy enabled
					} else {
						if (plugin.econ.getBalance(player.getName()) >= economy_price_use) {
							plugin.econ.withdrawPlayer(player.getName(),
									economy_price_use);
							// Send message to player
							player.sendMessage(chatColor
									.stringtodata(confirm_money.replaceAll(
											"\\{MONEY\\}", economy_price_use
													+ "")));
						} else {
							// No money
							player.sendMessage(chatColor
									.stringtodata(error_money.replaceAll(
											"\\{MONEY\\}", economy_price_use
													+ "")));
							return;
						}
					}
				} catch (Exception ex) {
					// Error
				}

				// Check if bookcontent includes a || in a command
				bookContent = bookContent.replace(plugin.splitCmd
						+ plugin.splitCmd, "#TOKEN#");

				// Do a quick check to see how many commands need to be stored
				for (int j = 1; j < bookContent.length() + 1; j++) {
					if (bookContent.charAt(j - 1) == seperator.toCharArray()[0]
							|| bookContent.length() == j) {
						cmd_counter += 1;
					}
				}
				plugin.logger.info(cmdFormat + "Found " + cmd_counter
						+ " commands in " + player.getName() + "'s cmdBook");

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
					if (bookContent.charAt(j - 1) == seperator.toCharArray()[0]
							|| bookContent.length() == j) {
						// Execute command
						try {
							// Check for calculations
							String command = bookContent.substring((j - count),
									j).replace(plugin.splitCmd, "");
							command = command.replace("#TOKEN#",
									plugin.splitCmd);
							cmd_list[counter] = command;
							counter += 1;
						} catch (Exception e) {
							// Error
						}
						count = 0;
					}

					// Check for @input
					if (bookContent.charAt(j - 1) == '@') {
						// Check if it is an input
						if (bookContent.substring(j - 1, j + 5)
								.equalsIgnoreCase("@input")) {
							// It is an input
							String questionUser = "";
							String inputStr_Replace = "@input";
							if (bookContent.charAt(j + 5) == '[') {
								try {
									questionUser = bookContent.substring(j + 6,
											bookContent.indexOf("]", j + 7));
									inputStr_Replace = "@input[" + questionUser
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
								map.put("data",
										chatColor.stringtodata(variable_inputquestion
												+ chatColor
														.stringtodata(questionUser)));
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
										Thread.currentThread();
										Thread.sleep(10);
									} catch (InterruptedException e) {
									}
								}

								if (answer == "/abort") {
									// Abort
									answer = "";
									break;
								}

								// Replace input
								try {
									plugin.logger.info(cmdFormat
											+ player.getName() + " input : "
											+ answer);
									bookContent = bookContent.substring(0,
											j - 1)
											+ answer
											+ bookContent.substring(j
													+ inputStr_Replace.length()
													- 1);
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
			} else {

			}
		}

		// Now execute all of those commands
		for (int i = 0; i < cmd_list.length - 1; i++) {
			// First check if the player is still online
			if (player.isOnline()) {
				try {
					String command = cmd_list[i];

					// Check for input
					for (int j = 1; j < command.length() + 1; j++) {
						if (command.charAt(j - 1) == '$') {
							// Check if it is an input
							if (command.substring(j - 1, j + 5)
									.equalsIgnoreCase("$input")) {
								// It is an input
								String questionUser = "";
								String inputStr_Replace = "$input";
								if (command.charAt(j + 5) == '[') {
									try {
										questionUser = command.substring(j + 6,
												command.indexOf("]", j + 7));
										inputStr_Replace = "$input["
												+ questionUser + "]";
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
									map.put("data",
											chatColor.stringtodata(variable_inputquestion
													+ chatColor
															.stringtodata(questionUser)));
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
															.getSessionData(
																	"data");
										}
									});
									conv.begin();

									while (answer == "") {
										try {
											Thread.currentThread();
											Thread.sleep(10);
										} catch (InterruptedException e) {
										}
									}

									if (answer == "/abort") {
										// Abort
										answer = "";
										break;
									}

									// Replace input
									try {
										plugin.logger.info(cmdFormat
												+ player.getName()
												+ " input : " + answer);
										command = command.substring(0, j - 1)
												+ answer
												+ command.substring(j
														+ inputStr_Replace
																.length() - 1);
										answer = ""; // Reset
									} catch (Exception ex) {
										// Error
										plugin.logger
												.severe(cmdFormat
														+ "Unable to replace the input!");
										player.sendMessage(chatColor
												.stringtodata(error_broken));
										return;
									}
								}
							}
						}
					}

					try {
						if (player.hasPermission("cmdbook.use.calculation")) {
							// Check for calculations
							ScriptEngineManager mgr = new ScriptEngineManager();
							ScriptEngine engine = mgr
									.getEngineByName("javascript");
							Pattern regex = Pattern
									.compile("\\$calc\\[(.*?)\\]");
							Matcher regexMatcher = regex.matcher(command);
							String strCalc = "";
							while (regexMatcher.find()) {
								for (int x = 1; x <= regexMatcher.groupCount(); x++) {
									try {
										// matched text: regexMatcher.group(i)
										// match start: regexMatcher.start(i)
										// match end: regexMatcher.end(i)
										engine.getBindings(ScriptContext.ENGINE_SCOPE);
										strCalc = regexMatcher.group(x)
												.toLowerCase().replace("[", "");
										strCalc = strCalc.replace("]", "");
										double d = Double.parseDouble(engine
												.eval(strCalc) + "");
										int integer = (int) d;
										command = command.replace("$calc["
												+ regexMatcher.group(x)
														.toString() + "]", ""
												+ integer);
									} catch (Exception ex) {
										player.sendMessage(chatColor
												.stringtodata("&cThe calculation '"
														+ strCalc
														+ "' could not be made!"));
									}
								}
							}
						}

						if (player.hasPermission("cmdbook.use.script")) {
							// Check for script
							ScriptEngineManager mgr = new ScriptEngineManager();
							ScriptEngine engine = mgr
									.getEngineByName("javascript");
							Pattern regex = Pattern
									.compile("\\$script\\[(.*?)\\]");
							Matcher regexMatcher = regex.matcher(command);
							String strCalc = "";
							while (regexMatcher.find()) {
								for (int x = 1; x <= regexMatcher.groupCount(); x++) {
									try {
										// matched text:
										// regexMatcher.group(i)
										// match start:
										// regexMatcher.start(i)
										// match end: regexMatcher.end(i)
										engine.getBindings(ScriptContext.ENGINE_SCOPE);
										strCalc = regexMatcher.group(x)
												.replace("[", "");
										strCalc = strCalc.replace("]", "");
										engine.eval(strCalc);
										String data = "" + engine.get("output");
										command = command.replace("$script["
												+ regexMatcher.group(x)
														.toString() + "]", ""
												+ data);
									} catch (Exception ex) {
										player.sendMessage(chatColor
												.stringtodata("&cThe script '"
														+ strCalc
														+ "' could not be parsed!"));
									}
								}
							}
						}

						// Now check if it is a 'DO' command

						// Check for $wait[xxx]
						if (command.toLowerCase().startsWith("$wait[")) {
							// Get time
							int timewait = 0;
							timewait = Integer.parseInt(command.substring(
									"$wait[".length(), command.indexOf("]")));
							if (player
									.hasPermission("cmdbook.use.hidemessages")
									&& hideMessages == true) {
								// Thats the point.. nothing :D
							} else {
								player.sendMessage(ChatColor.ITALIC
										+ "Sleeping " + timewait + " ms...");
							}
							this.logger.info(cmdFormat + player.getName()
									+ " sleeping " + timewait);
							Thread.currentThread();
							Thread.sleep(timewait);
						} else if (command.toLowerCase().startsWith("$effect[")) {
							if (player.hasPermission("cmdbook.use.effect")) {
								// Private message
								String sound = "";
								sound = command.substring("$effect[".length(),
										command.indexOf("]"));
								try {
									player.playEffect(player.getLocation(),
											Effect.valueOf(sound), 5);
									this.logger.info(cmdFormat
											+ player.getName()
											+ " played effect: " + sound);
								} catch (Exception ex) {
									player.sendMessage(chatColor
											.stringtodata(error_effect));
								}
							}
						} else if (command.toLowerCase().startsWith("$msg[")) {
							if (player.hasPermission("cmdbook.use.message")) {
								// Private message
								String message = "";
								message = command.substring("$msg[".length(),
										command.indexOf("]"));
								player.sendMessage(chatColor
										.stringtodata(message));
								this.logger.info(cmdFormat + player.getName()
										+ " message send: " + message);
							}
						} else if (command.toLowerCase().startsWith(
								"$workbench")) {
							if (player.hasPermission("cmdbook.use.workbench")) {
								player.openWorkbench(player.getLocation(), true);

							}
						} else if (command.toLowerCase().startsWith(
								"$enchtable")) {
							if (player.hasPermission("cmdbook.use.enchtable")) {
								player.openEnchanting(player.getLocation(),
										true);
							}
						} else if (command.toLowerCase().startsWith("$anvil")) {
							if (player.hasPermission("cmdbook.use.anvil")) {
								Inventory GUI = Bukkit.createInventory(player,
										InventoryType.ANVIL);
								player.openInventory(GUI);
							}
						} else if (command.toLowerCase().startsWith("$chat[")) {
							// Chat perform
							if (player.hasPermission("cmdbook.use.chat")) {
								String message = "";
								message = command.substring("$chat[".length(),
										command.indexOf("]"));
								player.chat(chatColor.stringtodata(message));
								this.logger.info(cmdFormat + player.getName()
										+ " send chat: " + message);
							}
						} else if (command.toLowerCase().startsWith(
								"$broadcast[")) {
							// broadcast a message
							if (player.hasPermission("cmdbook.use.broadcast")) {
								String message = "";
								message = command.substring(
										"$broadcast[".length(),
										command.indexOf("]"));
								Bukkit.broadcastMessage(chatColor
										.stringtodata(message));
								this.logger.info(cmdFormat + player.getName()
										+ " broadcast send: " + message);
							}
						} else {
							if (command.startsWith("/")) {
								if (player
										.hasPermission("cmdbook.use.runconsole")
										&& runConsole == true) {
									plugin.getServer().dispatchCommand(
											plugin.getServer()
													.getConsoleSender(),
											command.substring(1));
									this.logger
											.info(cmdFormat
													+ player.getName()
													+ " let the console perform command "
													+ command);
								} else {
									player.chat(command);
									this.logger.info(cmdFormat
											+ player.getName()
											+ " performed command " + command);
								}
							} else if (!command.startsWith("/")
									&& plugin.allowChat == true) {
								if (player
										.hasPermission("cmdbook.use.runconsole")
										&& runConsole == true) {
									plugin.getServer().dispatchCommand(
											plugin.getServer()
													.getConsoleSender(),
											command);
									this.logger
											.info(cmdFormat
													+ player.getName()
													+ " let the console perform command "
													+ command);
								} else {
									player.chat(command);
									this.logger.info(cmdFormat
											+ player.getName()
											+ " performed chat " + command);
								}
							} else {

							}
						}
					} catch (Exception e) {
						// Error
						if (command.startsWith("/")) {
							player.chat(command);
							this.logger.info(cmdFormat + player.getName()
									+ " performed command " + command);
						} else if (!command.startsWith("/")
								&& plugin.allowChat == true) {
							player.chat(command);
							this.logger.info(cmdFormat + player.getName()
									+ " performed chat " + command);
						}
						e.printStackTrace();
					}
				} catch (Exception ex) {
					// Error
					this.logger.info(cmdFormat + player.getName()
							+ " - Error while executing command!");
				}
			} else {
				// Player is not online
				this.logger.info(cmdFormat + player.getName()
						+ " - Player is not online, command has been stopped!");
				break;
			}
		}
		if (player.hasPermission("cmdbook.use.hidemessages")
				&& hideMessages == true) {
			// Thats the point.. nothing :D
		} else {
			// Complete
			player.sendMessage(chatColor
					.stringtodata(confirm_commandsperformed));
		}
	}

	public void convertBook(Player player) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// This function will check if the player
		// holds a book, and then it will check
		// if it is a valid cmdBook

		// Get the item the player has in his hand
		ItemStack is = player.getItemInHand();
		BookMeta book = (BookMeta) is.getItemMeta();

		// Check if the player is holding a book
		if (is.getTypeId() == 387) {
			// Player is holding a book
			// Now check if it is a cmdBook
			List<String> pageContent = book.getPages();

			// Now read author
			String authorPlugin = (ChatColor.RED + "cmdBook").toString(); // The
																			// cmdBook
			// author
			try {
				if (pageContent.get(0).toLowerCase().startsWith("[cmdbook]")
						& (book.getAuthor().equalsIgnoreCase(authorPlugin) || player
								.hasPermission("cmdbook.public.all"))) {
					// It is a cmdBook
					// Now check if the player has permission
					// to execute that
					if (player.hasPermission("cmdbook.convert")) {
						// Player has permisions
						// Unsign the book
						is = player.getItemInHand();
						book = (BookMeta) is.getItemMeta();
						// Check every page
						for (int i = 0; i < pageContent.toArray().length; i++) {
							ScriptEngineManager mgr = new ScriptEngineManager();
							ScriptEngine engine = mgr
									.getEngineByName("javascript");
							Pattern regex = Pattern.compile("calc\\((.*?)\\)");
							Matcher regexMatcher = regex.matcher(pageContent
									.get(i));
							while (regexMatcher.find()) {
								for (int x = 1; x <= regexMatcher.groupCount(); x++) {
									try {
										// matched text: regexMatcher.group(i)
										// match start: regexMatcher.start(i)
										// match end: regexMatcher.end(i)
										engine.getBindings(ScriptContext.ENGINE_SCOPE);
										book.setPage(
												i + 1,
												pageContent
														.get(i)
														.replace(
																"calc("
																		+ regexMatcher
																				.group(x)
																				.toString()
																		+ ")",
																"$calc["
																		+ regexMatcher
																				.group(x)
																		+ "]"));
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							}
						}
						is.setItemMeta(book);
						// Book unsigned
						this.logger.info(cmdFormat + player.getName()
								+ " converted the cmdBook!");
						player.sendMessage(chatColor
								.stringtodata(confirm_converted));
					} else {
						// No permission
						player.sendMessage(chatColor
								.stringtodata(error_permission));
					}
				}
			} catch (Exception ex) {
				// The player in not holding a book
				player.sendMessage(chatColor.stringtodata(error_nobook));
				return;
			}
		} else {
			// The player in not holding a book
			player.sendMessage(chatColor.stringtodata(error_nobook));
			return;
		}
	}

	private Player getTarget(Player player) {
 List<Entity> nearbyE = player.getNearbyEntities(20, 20, 20);
        ArrayList<Player> nearPlayers = new ArrayList<Player>();
        for (Entity e : nearbyE) {
            if (e instanceof Player) {
                nearPlayers.add((Player) e);
            }
        }
        Player target = null;
        BlockIterator bItr = new BlockIterator(player, 20);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        while (bItr.hasNext()) {
 
            block = bItr.next();
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            for (Player e : nearPlayers) {
                loc = e.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if ((bx - .75 <= ex && ex <= bx + 1.75) && (bz - .75 <= ez && ez <= bz + 1.75) && (by - 1 <= ey && ey <= by + 2.5)) {
                    target = e;
                    break;
 
                }
            }
 
        }
        return target;
	}

	public Object[] getBookContent(Player player, ItemStack item) {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------

		// This function will get the contents of a book
		// the player is holding

		Object pageContents[] = null;
		try {
			// Get the item the player has in his hand
			ItemStack is = item;

			// Now read each page content
			BookMeta book = (BookMeta) is.getItemMeta();
			pageContents = book.getPages().toArray();
		} catch (Exception ex) {
			// Something happend, return null
			logger.severe(cmdFormat + "Unable to get book content!");
			logger.severe(cmdFormat + "ERROR: " + ex.getMessage());
			return null;
		}

		// Return the contents
		return pageContents;
	}
}
