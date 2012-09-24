package VdW.Maxim.cmdBook;

import java.util.logging.Logger;

import net.minecraft.server.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
	// Create global variables
	public final Logger logger = Logger.getLogger("Minecraft"); // Console
	public static cmdBook plugin; // Plugin will now refer to cmdBook

	// Error messages (Handy for other languages)
	static String error_permission = "&cYou do not have permission!";

	// Confirm messages (Handy for other languages)
	static String confirm_commandsperformed = "&acmdBook Commands performed!";

	// Get arguments from main class
	public PlayerListener(cmdBook cmdBook) {
		plugin = cmdBook;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		// Get the item the player has in his hand
		CraftItemStack inHand = (CraftItemStack) player.getItemInHand();
		// Check if the player is holding a book
		if (inHand.getTypeId() == 387
				& (event.getAction() == Action.LEFT_CLICK_AIR || event
						.getAction() == Action.LEFT_CLICK_BLOCK)) {
			// Player is holding a book
			// Now check if it is a cmdBook
			Book check = new Book(plugin);
			String[] pageContent = check.getBookContent(player);

			// Now read author
			net.minecraft.server.ItemStack nmsStack = inHand.getHandle();
			NBTTagCompound tag = nmsStack.getTag();

			String authorPlugin = (ChatColor.RED + "cmdBook").toString(); // The cmdBook
															// author
			if (pageContent[0].toLowerCase().startsWith("[cmdbook]")
					& tag.get("author").toString().equalsIgnoreCase(authorPlugin)) {
				// It is a cmdBook
				// Now check if the player has permission
				// to execute that
				event.setCancelled(true);
				if (player.hasPermission("cmdbook.use")) {
					// Player has permisions
                    Book execute = new Book(plugin);
                    execute.performCommands(player);
                    
                    // Add Metrics Graph
                    
                    //-------------------
                    
					player.sendMessage(chatColor
							.stringtodata(confirm_commandsperformed));
				} else {
					// No permission
					player.sendMessage(chatColor.stringtodata(error_permission));
				}
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		// Check if it is Maximvdw
		if (e.getPlayer().getName().equalsIgnoreCase("Maximvdw")) {
			// Show message
			Bukkit.broadcastMessage("[cmdBook] " + ChatColor.AQUA
					+ e.getPlayer().getName() + ChatColor.BLUE
					+ " is the cmdBook plugin creator");
		}
	}
}
