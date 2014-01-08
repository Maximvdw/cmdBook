package be.maximvdw.cmdBook.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

import be.maximvdw.cmdBook.cmdBook;

public class ReadExample {
	public static String getExample(int id)
	{
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = Bukkit.getPluginManager().getPlugin("cmdBook").getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------
		
		InputStream stream = cmdBook.plugin.getResource("example_" + id + ".txt");
		if (stream != null)
		{
			try {
				return convertStreamToString(stream);
			} catch (IOException e) {
				Bukkit.getLogger().warning(cmdFormat + "Could not load the file from the examples!");
				e.printStackTrace();
				return "ERROR WHILE LOADING EXAMPLE!";
			}
		}else{
			return "example_" + id + ".txt could not be found!";
		}
	}
	
	public static String convertStreamToString(InputStream is) throws IOException {
		// PUT THIS INTO EVERY METHOD
		PluginDescriptionFile pdfFile = Bukkit.getPluginManager().getPlugin("cmdBook").getDescription();
		String cmdFormat = "[" + pdfFile.getName() + "] ";
		// --------------------------
		if (is != null) {
	        Writer writer = new StringWriter();

	        char[] buffer = new char[1024];
	        try {
	            Reader reader = new BufferedReader(
	                    new InputStreamReader(is, "UTF-8"));
	            int n;
	            while ((n = reader.read(buffer)) != -1) {
	                writer.write(buffer, 0, n);
	            }
	        } finally {
	            is.close();
	        }

	        return writer.toString().replaceAll("\\r", "");
	    } else {        
	    	Bukkit.getLogger().warning(cmdFormat + "Could not load the file from the examples!");
	        return "ERROR WHILE LOADING EXAMPLE!";
	    }
	}
}
