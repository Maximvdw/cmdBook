package be.maximvdw.cmdBook;

/* Name:		cmdBook
 * Version: 	1.4.0
 * Made by: 	Maxim Van de Wynckel
 * Build date:	28/01/2012						
 */

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import be.maximvdw.cmdBook.metrics.IncrementalPlotter;
import be.maximvdw.cmdBook.metrics.Metrics;

public class PlayerListener implements Listener {
	// Create global variables
	public final Logger logger = Logger.getLogger("Minecraft"); // Console
	public static cmdBook plugin; // Plugin will now refer to cmdBook
	IncrementalPlotter ip = new IncrementalPlotter("Total cmdBook usages");

	// Warning messages
	static String warning_update_available = "&2[&fcmdBook&2] &cAn update for cmdBook is available!";
	static String warning_updated = "&2[&fcmdBook&2] &acmdBook has an update available for install!";

	// Error messages (Handy for other languages)
	static String error_permission = "&cYou do not have permission!";
	static String error_noread = "&cYou cannot read a &4cmdBook&c!";

	// Confirm messages (Handy for other languages)
	static String confirm_opening = "&2This book is protected!";

	// Get arguments from main class
	public PlayerListener(cmdBook cmdBook) {
		plugin = cmdBook;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		// Get the item the player has in his hand
		ItemStack is = player.getItemInHand();

		// Check if it is a pressureplate event
		// Get block position
		Action action = event.getAction();
		Block action_block = event.getClickedBlock();
		if (action_block != null) {
			try {
				switch (action) {
				case LEFT_CLICK_BLOCK:
					if (action_block.getType() == Material.STONE_BUTTON || action_block.getType() == Material.WOOD_BUTTON)
					{
						int x = action_block.getX();
						int y = action_block.getY();
						int z = action_block.getZ();
						// Now check if there is a chest below it
						for (int xrad= -plugin.chestRadius;xrad<=plugin.chestRadius;xrad++)
						{
							for (int yrad = -plugin.chestRadius;yrad<=plugin.chestRadius;yrad++)
							{
								for (int zrad = -plugin.chestRadius;zrad<=plugin.chestRadius;zrad++)
								{
									Location chest_location = new Location(player.getWorld(),
											x-xrad, y-yrad, z-zrad);
									Block chest = chest_location.getBlock();
									// Check if it is indeed a chest
									if (chest.getType() == Material.CHEST) {
										// Continue searching the chest
										Chest chest_block = (Chest) chest.getState();
										Inventory chest_inv = chest_block.getInventory();
										for (final ItemStack item : chest_inv.getContents()) {
											// Check if item is a book
											if (item != null) {
												if (item.getTypeId() == 387) {
													// Check if it is a cmdbook
													// Player is holding a book
													// Now check if it is a cmdBook
													Book check = new Book(plugin);
													Object pageContent[] = check.getBookContent(player,item);
													BookMeta book = (BookMeta) item.getItemMeta();

													// Now read author
													String authorPlugin = (ChatColor.RED + "cmdBook")
															.toString(); // The
																			// cmdBook
													// author
													if (pageContent[0].toString().toLowerCase()
															.startsWith("[cmdbook]")
															& book.getAuthor().equalsIgnoreCase(
																	authorPlugin)) {
														// It is a cmdBook
														// Now check if the player has permission
														// to execute that
														if (player.hasPermission("cmdbook.use")) {
															// Player has permisions
															plugin.getServer()
																	.getScheduler()
																	.runTaskLaterAsynchronously(plugin,
																			new Runnable() {
																				public void run() {
																					Book execute = new Book(
																							plugin);
																					execute.performCommands(player,item);

																					// Add Metrics Graph
																					try {
																						Metrics metrics = new Metrics(
																								plugin);

																						// Plot the total
																						// amount of
																						// protections
																						ip.increment();

																						metrics.addCustomData(ip);
																						metrics.start();

																					} catch (IOException e) {
																						// Error
																					}
																					// -------------------

																				}
																			}, 0L);
														} else {
															// No permission
															player.closeInventory();
															player.sendMessage(chatColor
																	.stringtodata(error_permission));
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					break;
				case RIGHT_CLICK_BLOCK:
					if (action_block.getType() == Material.STONE_BUTTON || action_block.getType() == Material.WOOD_BUTTON)
					{
						int x = action_block.getX();
						int y = action_block.getY();
						int z = action_block.getZ();
						// Now check if there is a chest below it
						for (int xrad= -plugin.chestRadius;xrad<=plugin.chestRadius;xrad++)
						{
							for (int yrad = -plugin.chestRadius;yrad<=plugin.chestRadius;yrad++)
							{
								for (int zrad = -plugin.chestRadius;zrad<=plugin.chestRadius;zrad++)
								{
									Location chest_location = new Location(player.getWorld(),
											x-xrad, y-yrad, z-zrad);
									Block chest = chest_location.getBlock();
									// Check if it is indeed a chest
									if (chest.getType() == Material.CHEST) {
										// Continue searching the chest
										Chest chest_block = (Chest) chest.getState();
										Inventory chest_inv = chest_block.getInventory();
										for (final ItemStack item : chest_inv.getContents()) {
											// Check if item is a book
											if (item != null) {
												if (item.getTypeId() == 387) {
													// Check if it is a cmdbook
													// Player is holding a book
													// Now check if it is a cmdBook
													Book check = new Book(plugin);
													Object pageContent[] = check.getBookContent(player,item);
													BookMeta book = (BookMeta) item.getItemMeta();

													// Now read author
													String authorPlugin = (ChatColor.RED + "cmdBook")
															.toString(); // The
																			// cmdBook
													// author
													if (pageContent[0].toString().toLowerCase()
															.startsWith("[cmdbook]")
															& book.getAuthor().equalsIgnoreCase(
																	authorPlugin)) {
														// It is a cmdBook
														// Now check if the player has permission
														// to execute that
														if (player.hasPermission("cmdbook.use")) {
															// Player has permisions
															plugin.getServer()
																	.getScheduler()
																	.runTaskLaterAsynchronously(plugin,
																			new Runnable() {
																				public void run() {
																					Book execute = new Book(
																							plugin);
																					execute.performCommands(player,item);

																					// Add Metrics Graph
																					try {
																						Metrics metrics = new Metrics(
																								plugin);

																						// Plot the total
																						// amount of
																						// protections
																						ip.increment();

																						metrics.addCustomData(ip);
																						metrics.start();

																					} catch (IOException e) {
																						// Error
																					}
																					// -------------------

																				}
																			}, 0L);
														} else {
															// No permission
															player.closeInventory();
															player.sendMessage(chatColor
																	.stringtodata(error_permission));
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					break;
				case PHYSICAL:
					int x = action_block.getX();
					int y = action_block.getY();
					int z = action_block.getZ();
					// Now check if there is a chest below it
					for (int xrad= -plugin.chestRadius;xrad<=plugin.chestRadius;xrad++)
					{
						for (int yrad = -plugin.chestRadius;yrad<=plugin.chestRadius;yrad++)
						{
							for (int zrad = -plugin.chestRadius;zrad<=plugin.chestRadius;zrad++)
							{
								Location chest_location = new Location(player.getWorld(),
										x-xrad, y-yrad, z-zrad);
								Block chest = chest_location.getBlock();
								// Check if it is indeed a chest
								if (chest.getType() == Material.CHEST) {
									// Continue searching the chest
									Chest chest_block = (Chest) chest.getState();
									Inventory chest_inv = chest_block.getInventory();
									for (final ItemStack item : chest_inv.getContents()) {
										// Check if item is a book
										if (item != null) {
											if (item.getTypeId() == 387) {
												// Check if it is a cmdbook
												// Player is holding a book
												// Now check if it is a cmdBook
												Book check = new Book(plugin);
												Object pageContent[] = check.getBookContent(player,item);
												BookMeta book = (BookMeta) item.getItemMeta();

												// Now read author
												String authorPlugin = (ChatColor.RED + "cmdBook")
														.toString(); // The
																		// cmdBook
												// author
												if (pageContent[0].toString().toLowerCase()
														.startsWith("[cmdbook]")
														& book.getAuthor().equalsIgnoreCase(
																authorPlugin)) {
													// It is a cmdBook
													// Now check if the player has permission
													// to execute that
													if (player.hasPermission("cmdbook.use")) {
														// Player has permisions
														plugin.getServer()
																.getScheduler()
																.runTaskLaterAsynchronously(plugin,
																		new Runnable() {
																			public void run() {
																				Book execute = new Book(
																						plugin);
																				execute.performCommands(player,item);

																				// Add Metrics Graph
																				try {
																					Metrics metrics = new Metrics(
																							plugin);

																					// Plot the total
																					// amount of
																					// protections
																					ip.increment();

																					metrics.addCustomData(ip);
																					metrics.start();

																				} catch (IOException e) {
																					// Error
																				}
																				// -------------------

																			}
																		}, 0L);
													} else {
														// No permission
														player.closeInventory();
														player.sendMessage(chatColor
																.stringtodata(error_permission));
													}
												}
											}
										}
									}
								}
							}
						}
					}
				default:
					break;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// Check if the player is holding a book
		if (is.getTypeId() == 387
				& (event.getAction() == Action.LEFT_CLICK_AIR || event
						.getAction() == Action.LEFT_CLICK_BLOCK)) {
			// Player is holding a book
			// Now check if it is a cmdBook
			Book check = new Book(plugin);
			Object pageContent[] = check.getBookContent(player, is);
			BookMeta book = (BookMeta) is.getItemMeta();

			// Now read author
			String authorPlugin = (ChatColor.RED + "cmdBook").toString(); // The
																			// cmdBook
			// author
			if (pageContent[0].toString().toLowerCase().startsWith("[cmdbook]")
					& book.getAuthor().equalsIgnoreCase(authorPlugin)) {
				// It is a cmdBook
				// Now check if the player has permission
				// to execute that
				event.setCancelled(true);
				if (player.hasPermission("cmdbook.use")) {
					// Player has permisions
					plugin.getServer().getScheduler()
							.runTaskLaterAsynchronously(plugin, new Runnable() {
								public void run() {
									Book execute = new Book(plugin);
									execute.performCommands(player,
											player.getItemInHand());

									// Add Metrics Graph
									try {
										Metrics metrics = new Metrics(plugin);

										// Plot the total amount of protections
										ip.increment();

										metrics.addCustomData(ip);
										metrics.start();

									} catch (IOException e) {
										// Error
									}
									// -------------------

								}
							}, 0L);
				} else {
					// No permission
					player.closeInventory();
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			}
		} else if (is.getTypeId() == 387
				& (event.getAction() == Action.RIGHT_CLICK_AIR || event
						.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			// Player is holding a book
			// Now check if it is a cmdBook
			Book check = new Book(plugin);
			Object pageContent[] = check.getBookContent(player, is);

			// Now read author
			String authorPlugin = (ChatColor.RED + "cmdBook").toString(); // The
																			// cmdBook
			BookMeta book = (BookMeta) is.getItemMeta();
			if (pageContent[0].toString().toLowerCase().startsWith("[cmdbook]")
					& book.getAuthor().equalsIgnoreCase(authorPlugin)) {
				if (Configuration.config.getBoolean("executeOnRead") == true) {
					if (player.hasPermission("cmdbook.use")) {
						// Player has permisions
						plugin.getServer()
								.getScheduler()
								.runTaskLaterAsynchronously(plugin,
										new Runnable() {
											public void run() {
												Book execute = new Book(plugin);
												execute.performCommands(player,
														player.getItemInHand());

												// Add Metrics Graph
												try {
													Metrics metrics = new Metrics(
															plugin);

													// Plot the total amount of
													// protections
													ip.increment();

													metrics.addCustomData(ip);
													metrics.start();

												} catch (IOException e) {
													// Error
												}
												// -------------------

											}
										}, 0L);
					} else {
						// No permission
						player.closeInventory();
						player.sendMessage(chatColor
								.stringtodata(error_permission));
					}
				} else {
					player.sendMessage(chatColor.stringtodata(error_noread));
				}
				player.closeInventory();
			} else {
				try{
					if (book.getDisplayName().startsWith(ChatColor.BLUE + "")) {
						if (book.getAuthor() == player.getName()
								&& book.getDisplayName().startsWith(
										ChatColor.BLUE + "")
								|| player.hasPermission("cmdbook.readall")) {
							player.sendMessage(chatColor
									.stringtodata(confirm_opening));
						} else if (book.getAuthor() != player.getName()) {
							player.sendMessage(chatColor
									.stringtodata(error_permission));
							player.closeInventory();
						}
					} else {
						// nothing
					}	
				}catch (Exception e)
				{
					// ERROR
				}
			}
		}
	}
}
