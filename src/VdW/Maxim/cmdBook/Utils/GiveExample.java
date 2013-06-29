package VdW.Maxim.cmdBook.Utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import VdW.Maxim.cmdBook.Book;
import VdW.Maxim.cmdBook.cmdBook;

public class GiveExample {
	public static void example(int id, Player player){
		try{
			ItemStack is = new ItemStack(Material.WRITTEN_BOOK);
			BookMeta bookM = (BookMeta)is.getItemMeta();
			String data = ReadExample.getExample(id);
			bookM.addPage(data);
			bookM.setAuthor(player.getName());
			bookM.setTitle("Example " + id);
			
		    is.setItemMeta(bookM);
			// Start CreateBook
			Book creator = new Book(cmdBook.plugin);
			creator.createCmdBook(player,is,true);
		    player.getInventory().addItem(is);
		}catch(Exception ex){
			// ERROR
		}
	}
}
