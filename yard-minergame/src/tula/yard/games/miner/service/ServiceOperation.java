package tula.yard.games.miner.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Properties;

import org.eclipse.swt.graphics.Point;

public class ServiceOperation {
	
	public final static int BIT_MASK_16 = 65535; 
	public final static int BIT_MASK_8 = 255;
	public final static int BIT_MASK_4 = 15;	
	public final static int BIT_MASK_3 = 7;		
	public final static int BIT_MASK_2 = 3;
			
	public static int intFromString(String string, int def) {
		try {
			return Integer.valueOf(string).intValue();
		} catch (Exception e) {
			return def;
		}
		

	}

	public static Properties loadPropertiesFromFile(String fileName,
			Properties defProperties) {
		File file = new File(fileName);
		Properties properties = new Properties();
		try {
			InputStream is = new FileInputStream(file);
			properties.load(is);
		} catch (Exception e) {
			return defProperties;
		}
		
				
		return properties;
	}

	public static void savePropertiesToFile(String dir, String fileName,
			Properties properties) {
		File file = new File(dir);
		try {
			file.mkdirs();
			file = new File(dir + fileName);
			FileOutputStream fos = new FileOutputStream(file);
			properties.store(fos, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	public static Point getMiddlePosition(int parentX, int parentY,
			int parentWidth, int parentHeight, int width, int height) {

		return new Point(parentX + (parentWidth >> 1) - (width >> 1),
				parentY + (parentHeight >> 1) - (height >> 1) );
	}

	public static int getNsetIntProperty(Properties properties, String key, int def) {
		
		String value = properties.getProperty(key);
		if (value == null) {
			properties.put(key, String.valueOf(def));
			return def;
		}
		return intFromString(value, def);
		
	}
	
	public static String loadTextFromStream(InputStream inputStream, Charset charset) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset));
		StringBuilder builder = new StringBuilder();
		String readLine; 
		while ((readLine = br.readLine()) != null) {
				builder.append(readLine);
		}
		return builder.toString();
		

	}
	
}
