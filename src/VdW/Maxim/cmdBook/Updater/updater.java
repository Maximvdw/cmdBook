package VdW.Maxim.cmdBook.Updater;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.plugin.PluginDescriptionFile;

import VdW.Maxim.cmdBook.cmdBook;

/* Name:		cmdBook
 * Version: 	1.4.0
 * Made by: 	Maxim Van de Wynckel
 * Build date:	21/06/2013						
 */

public class updater {
	// CONFIGURATION //
	static String devName = "cmdBook";
	static String devFileSplit = "cmd-book";
	// ------------- //

	// Create global variables
	public static boolean updateAvailable = false;
	public static boolean updated = false;
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
			URL url = new URL("http://dev.bukkit.org/bukkit-mods/" + devName);
			InputStream is = url.openConnection().getInputStream();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			// Get version as string with .=-
			String versionStr = pdfFile.getVersion().replace(".", "-");
			String line = null;
			String regExp = "<a href=\"/bukkit-plugins/" + devName + "/files/"
					+ "^(0{0,2}[1-9]|0?[1-9][0-9]|[1-9][0-9][0-9])$-" + devFileSplit + "-(.?-.?-.?)";
			Pattern p = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
			while ((line = reader.readLine()) != null) {
				Matcher m = p.matcher(line);
				if (m.find()) {
					if (!m.group(1).toString().contains(versionStr)) {
						// Outdated
						this.logger.warning(cmdFormat
								+ "The plugin is outdated!");
						updateAvailable = true;
						// Get url
						/*String url_downloadPage = "http://dev.bukkit.org/"
								+ m.group(1).toString();

						url = new URL(url_downloadPage);
						is = url.openConnection().getInputStream();

						reader = new BufferedReader(new InputStreamReader(is));

						line = null;
						regExp = ".*<a href=\"(.*cmdBook.jar)\">.*";
						p = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);

						while ((line = reader.readLine()) != null) {
							m = p.matcher(line);
							if (m.matches()) {
								// URL Found
								String downloadURL = m.group(1).toString();
								if (Configuration.config
										.getBoolean("autoUpdate") == true) {
									try {
										/*
										 * Get a connection to the URL and start
										 * up a buffered reader.
										 *
										long startTime = System
												.currentTimeMillis();
										File theDir = new File(
												System.getProperty("user.dir")
														+ "/plugins/update/");

										// if the directory does not exist,
										// create
										// it
										if (!theDir.exists()) {
											theDir.mkdir();
										}

										System.out.println(cmdFormat
												+ "Connecting to Bukkit...");

										url = new URL(downloadURL);
										url.openConnection();
										InputStream reader_dl = url
												.openStream();

										/*
										 * Setup a buffered file writer to write
										 * out what we read from the website.
										 *
										FileOutputStream writer = new FileOutputStream(
												System.getProperty("user.dir")
														+ "/plugins/update/cmdBook.jar");
										byte[] buffer = new byte[153600];
										int totalBytesRead = 0;
										int bytesRead = 0;

										System.out.println(cmdFormat
												+ "Starting download ...");

										while ((bytesRead = reader_dl
												.read(buffer)) > 0) {
											writer.write(buffer, 0, bytesRead);
											buffer = new byte[153600];
											totalBytesRead += bytesRead;
										}

										long endTime = System
												.currentTimeMillis();

										System.out
												.println(cmdFormat
														+ "Done. "
														+ (new Integer(
																totalBytesRead)
																.toString())
														+ " bytes read ("
														+ (new Long(endTime
																- startTime)
																.toString())
														+ " millseconds).");
										writer.close();
										reader.close();
										updated = true;
									} catch (MalformedURLException e) {

									} catch (IOException e) {

									}
								}
								reader.close();
								break;
							}
						}*/
						break;
					} else {
						this.logger
								.info(cmdFormat + "The plugin is Up-to date");
						reader.close();
						break;
					}
				}
			}
			reader.close();

		} catch (Exception ex) {
			this.logger
			.warning(cmdFormat + "Unable to check for updates!");
			ex.printStackTrace();
		}
	}
}
