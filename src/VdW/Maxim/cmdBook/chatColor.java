package VdW.Maxim.cmdBook;

/* Name:		cmdBook
 * Version: 	1.1.0
 * Made by: 	Maxim Van de Wynckel
 * Build date:	2/09/2012						
 */

import org.bukkit.ChatColor;

public class chatColor {
	// This class will replace all color symbols into ChatColor
	public static String stringtodata(String str){
		// Start replacing all symbols
		String output = str;
		output=output.replaceAll("&0", "" + ChatColor.BLACK.toString());
		output=output.replaceAll("&1", "" + ChatColor.BLUE.toString());
		output=output.replaceAll("&2", "" + ChatColor.DARK_GREEN.toString());
		output=output.replaceAll("&3", "" + ChatColor.DARK_AQUA.toString());
		output=output.replaceAll("&4", "" + ChatColor.DARK_RED.toString());
		output=output.replaceAll("&5", "" + ChatColor.DARK_PURPLE.toString());
		output=output.replaceAll("&6", "" + ChatColor.GOLD.toString());
		output=output.replaceAll("&7", "" + ChatColor.GRAY.toString());
		output=output.replaceAll("&8", "" + ChatColor.DARK_GRAY.toString());
		output=output.replaceAll("&9", "" + ChatColor.MAGIC.toString());
		output=output.replaceAll("&a", "" + ChatColor.GREEN.toString());
		output=output.replaceAll("&b", "" + ChatColor.AQUA.toString());
		output=output.replaceAll("&c", "" + ChatColor.RED.toString());
		output=output.replaceAll("&d", "" + ChatColor.LIGHT_PURPLE.toString());
		output=output.replaceAll("&e", "" + ChatColor.YELLOW.toString());
		output=output.replaceAll("&f", "" + ChatColor.WHITE.toString());
		output=output.replaceAll("&i", "" + ChatColor.ITALIC.toString());
		// Output the result
		return output;
	}
	public static String stringtodelete(String str){
		// Start replacing all symbols
		String output = str;
		output=output.replaceAll("&0",  "");
		output=output.replaceAll("&1",  "");
		output=output.replaceAll("&2",  "");
		output=output.replaceAll("&3",  "");
		output=output.replaceAll("&4", "");
		output=output.replaceAll("&5",  "");
		output=output.replaceAll("&6",  "");
		output=output.replaceAll("&7",  "");
		output=output.replaceAll("&8",  "");
		output=output.replaceAll("&9",  "");
		output=output.replaceAll("&a",  "");
		output=output.replaceAll("&b",  "");
		output=output.replaceAll("&c", "");
		output=output.replaceAll("&d",  "");
		output=output.replaceAll("&e",  "");
		output=output.replaceAll("&f",  "");
		// Output the result
		return output;
	}
}
