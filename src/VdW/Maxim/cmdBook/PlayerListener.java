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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
	// Create global variables
	public final Logger logger = Logger.getLogger("Minecraft"); // Console
	public static cmdBook plugin; // Plugin will now refer to cmdBook

	// Error messages (Handy for other languages)
	static String error_permission = "&cYou do not have permission!";
	static String error_noread = "&cYou cannot read a &4cmdBook&c!";

	// Confirm messages (Handy for other languages)
	static String confirm_commandsperformed = "&acmdBook Commands performed!";
	static String confirm_opening = "&aThis book is protected!";

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
		BookMeta book = (BookMeta) is.getItemMeta();
		
		// Check if the player is holding a book
		if (is.getTypeId() == 387
				& (event.getAction() == Action.LEFT_CLICK_AIR || event
						.getAction() == Action.LEFT_CLICK_BLOCK)) {
			// Player is holding a book
			// Now check if it is a cmdBook
			Book check = new Book(plugin);
			String[] pageContent = check.getBookContent(player);

			// Now read author
			String authorPlugin = (ChatColor.RED + "cmdBook").toString(); // The
																			// cmdBook
			// author
			if (pageContent[0].toLowerCase().startsWith("[cmdbook]")
					& book.getAuthor()
							.equalsIgnoreCase(authorPlugin)) {
				// It is a cmdBook
				// Now check if the player has permission
				// to execute that
				event.setCancelled(true);
				if (player.hasPermission("cmdbook.use")) {
					// Player has permisions
					plugin.getServer().getScheduler()
					.scheduleAsyncDelayedTask(plugin, new Runnable() {
						public void run() {
							Book execute = new Book(plugin);
							execute.performCommands(player);

							// Add Metrics Graph
							try {
							    Metrics metrics = new Metrics(plugin);

							    // Plot the total amount of protections
							    metrics.addCustomData(new Metrics.Plotter("Total cmdBook usages") {

							        @Override
							        public int getValue() {
							            return 1;
							        }

							    });
							    metrics.start();
							} catch (IOException e) {
							    // Error
							}
							// -------------------

							player.sendMessage(chatColor
									.stringtodata(confirm_commandsperformed));
						}
					}, 0L);
				} else {
					// No permission
					player.closeInventory();
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			}
		}else if (is.getTypeId() == 387
				& (event.getAction() == Action.RIGHT_CLICK_AIR || event
				.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			// Player is holding a book
						// Now check if it is a cmdBook
						Book check = new Book(plugin);
						String[] pageContent = check.getBookContent(player);

						// Now read author
						String authorPlugin = (ChatColor.RED + "cmdBook").toString(); // The
																						// cmdBook
			if (pageContent[0].toLowerCase().startsWith("[cmdbook]")
					& book.getAuthor()
							.equalsIgnoreCase(authorPlugin)) {
				player.sendMessage(chatColor.stringtodata(error_noread));
				player.closeInventory();
			}else{
				if (book.getAuthor() == player.getName() && book.getDisplayName().startsWith(ChatColor.BLUE + "") || player.hasPermission("cmdbook.readall"))
				{
					player.sendMessage(chatColor.stringtodata(confirm_opening));
				}else if (book.getAuthor() != player.getName()){
					player.sendMessage(chatColor.stringtodata(error_permission));
					player.closeInventory();
				}
			}
		}
	}
}
