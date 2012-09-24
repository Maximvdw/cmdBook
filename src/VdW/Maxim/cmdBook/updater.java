package VdW.Maxim.cmdBook;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.plugin.PluginDescriptionFile;

/* Name:		Updater Class
 * Version: 	1.0.0
 * Made by: 	Maxim Van de Wynckel
 * Build date:	11/09/2012						
 */

public class updater {
	// CONFIGURATION //
	static String devName = "cmdBook";
	static String devFileSplit = "cmd-book";
	// ------------- //

	// Create global variables
	public static boolean updateAvailable = false;
	public final Logger logger = Logger.getLogger("Minecraft"); // Console
	public static cmdBook plugin; // Plugin will now refer to cmdBook

	// Get arguments from main class
	public updater(cmdBook cmdBook) {
		plugin = cmdBook;
	}

	public void checkUpdates() {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = plugin.getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------
		
		try {
			URL url = new URL("http://dev.bukkit.org/server-mods/" + devName);
			InputStream is = url.openConnection().getInputStream();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			// Get version as string with .=-
			String versionStr = pdfFile.getVersion().replace(".", "-");
			
			String line = null;
			String regExp = ".*<a href=\"(.*/server-mods/" + devName
					+ "/files/.*-" + devFileSplit + "-.*)\">.*";
			Pattern p = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);

			while ((line = reader.readLine()) != null) {
				Matcher m = p.matcher(line);
				if (m.matches()) {
					if(!m.group(1).toString().contains(versionStr)){
						// Outdated
						this.logger.warning(cmdFormat + "The plugin is outdated!");
						updateAvailable=true;
						return;
					}else{
						this.logger.info(cmdFormat + "The plugin is Up-to date");
						return;
					}
				}
			}
			reader.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
