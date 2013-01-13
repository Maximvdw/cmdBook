package VdW.Maxim.cmdBook;

/* Name:		cmdBook
 * Version: 	1.3.0
 * Made by: 	Maxim Van de Wynckel
 * Build date:	10/01/2013						
 */

import org.bukkit.ChatColor;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;

public class chatColor {
	public static String stringtoconsole(String str){
		// Start replacing all symbols
				String output = str;
				output=output.replaceAll("&0", "" + Ansi.ansi().fg(Ansi.Color.BLACK).boldOff().toString());
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
				output=output.replaceAll(ChatColor.BLACK.toString(), Ansi.ansi().fg(Ansi.Color.BLACK).boldOff().toString());
		        output=output.replaceAll(ChatColor.DARK_BLUE.toString(), Ansi.ansi().fg(Ansi.Color.BLUE).boldOff().toString());
		        output=output.replaceAll(ChatColor.DARK_GREEN.toString(), Ansi.ansi().fg(Ansi.Color.GREEN).boldOff().toString());
		        output=output.replaceAll(ChatColor.DARK_AQUA.toString(), Ansi.ansi().fg(Ansi.Color.CYAN).boldOff().toString());
		        output=output.replaceAll(ChatColor.DARK_RED.toString(), Ansi.ansi().fg(Ansi.Color.RED).boldOff().toString());
		        output=output.replaceAll(ChatColor.DARK_PURPLE.toString(), Ansi.ansi().fg(Ansi.Color.MAGENTA).boldOff().toString());
		        output=output.replaceAll(ChatColor.GOLD.toString(), Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff().toString());
		        output=output.replaceAll(ChatColor.GRAY.toString(), Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString());
		        output=output.replaceAll(ChatColor.DARK_GRAY.toString(), Ansi.ansi().fg(Ansi.Color.BLACK).bold().toString());
		        output=output.replaceAll(ChatColor.BLUE.toString(), Ansi.ansi().fg(Ansi.Color.BLUE).bold().toString());
		        output=output.replaceAll(ChatColor.GREEN.toString(), Ansi.ansi().fg(Ansi.Color.GREEN).bold().toString());
		        output=output.replaceAll(ChatColor.AQUA.toString(), Ansi.ansi().fg(Ansi.Color.CYAN).bold().toString());
		        output=output.replaceAll(ChatColor.RED.toString(), Ansi.ansi().fg(Ansi.Color.RED).bold().toString());
		        output=output.replaceAll(ChatColor.LIGHT_PURPLE.toString(), Ansi.ansi().fg(Ansi.Color.MAGENTA).bold().toString());
		        output=output.replaceAll(ChatColor.YELLOW.toString(), Ansi.ansi().fg(Ansi.Color.YELLOW).bold().toString());
		        output=output.replaceAll(ChatColor.WHITE.toString(), Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString());
		        output=output.replaceAll(ChatColor.MAGIC.toString(), Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
		        output=output.replaceAll(ChatColor.BOLD.toString(), Ansi.ansi().a(Attribute.UNDERLINE_DOUBLE).toString());
		        output=output.replaceAll(ChatColor.STRIKETHROUGH.toString(), Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
		        output=output.replaceAll(ChatColor.UNDERLINE.toString(), Ansi.ansi().a(Attribute.UNDERLINE).toString());
		        output=output.replaceAll(ChatColor.ITALIC.toString(), Ansi.ansi().a(Attribute.ITALIC).toString());
		        output=output.replaceAll(ChatColor.RESET.toString(), Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.DEFAULT).toString());
		        
		        output += Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.DEFAULT).toString(); // Else whole the console is colored
				// Output the result
				return output;
	}
	
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
