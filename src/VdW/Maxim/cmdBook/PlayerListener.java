package VdW.Maxim.cmdBook;

/* Name:		cmdBook
 * Version: 	1.2.0
 * Made by: 	Maxim Van de Wynckel
 * Build date:	03/01/2012						
 */

import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import VdW.Maxim.cmdBook.Metrics.IncrementalPlotter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import VdW.Maxim.cmdBook.Metrics.Metrics;

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
	static String confirm_opening = "&aThis book is protected!";

	// Get arguments from main class
	public PlayerListener(cmdBook cmdBook) {
		plugin = cmdBook;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// Check if it is an admin
		if (event.getPlayer().hasPermission("cmdbook.update"))
		{
			if (updater.updateAvailable == true)
			{
				if (updater.updated == true)
				{
					event.getPlayer().sendMessage(chatColor.stringtodata(warning_updated));
				}else
				{
					event.getPlayer().sendMessage(chatColor.stringtodata(warning_update_available));
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		// Get the item the player has in his hand
		ItemStack is = player.getItemInHand();
		BookMeta book = (BookMeta) is.getItemMeta();

		// Check if the player is holding a book
		if (is.getTypeId() == 387
				& (event.getAction() == Action.LEFT_CLICK_AIR || event
						.getAction() == Action.LEFT_CLICK_BLOCK)) {
			// Player is holding a book
			// Now check if it is a cmdBook
			Book check = new Book(plugin);
			Object pageContent[] = check.getBookContent(player);

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
									execute.performCommands(player);

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
			Object pageContent[] = check.getBookContent(player);

			// Now read author
			String authorPlugin = (ChatColor.RED + "cmdBook").toString(); // The
																			// cmdBook
			if (pageContent[0].toString().toLowerCase().startsWith("[cmdbook]")
					& book.getAuthor().equalsIgnoreCase(authorPlugin)) {
				if (Configuration.config.getBoolean("executeOnRead")==true)
				{
					if (player.hasPermission("cmdbook.use")) {
						// Player has permisions
						plugin.getServer().getScheduler()
								.runTaskLaterAsynchronously(plugin, new Runnable() {
									public void run() {
										Book execute = new Book(plugin);
										execute.performCommands(player);

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
				else
				{
					player.sendMessage(chatColor.stringtodata(error_noread));	
				}
				player.closeInventory();
			} else {
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
			}
		}
	}
}
