package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class StorageFactory {
 static StorageFactory _instance = new StorageFactory();
 Properties props = null;
 String path = "appProps.ini";

 public StorageFactory() {
		props = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(path);
			props.load(in); //loads last properties
			in.close();
		} catch (FileNotFoundException e1) {
			System.err.println("No Properties files were found. Creating one");
			File propsFile = new File(path);
			if(!propsFile.exists()) {
				try {
					propsFile.createNewFile();
				} catch (IOException e) {e.printStackTrace();}
			}
			
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}
 
 	static StorageFactory getInstance() {
 		return _instance;
 	}
 	
 	public void addProperty(String key, String value) {
 		props.put(key, value);
 	}
 	
 	public void saveProperties() {
 		try {
			FileOutputStream out = new FileOutputStream(path);
			props.store(out, "");
			out.close();
			System.out.println( path + " was Successfully Saved");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
 		
 	}

	public String getProperty(String key) {
		return props.getProperty(key);
	}

}
