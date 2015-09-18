package de.starwit.auth.userdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;


/**
 * 
 * @author ztarbug
 *
 */
public class ConfigData extends Properties {
	
	private static final long serialVersionUID = 1L;
	
	private static ConfigData instance;
	
	private List<String> attributeNames; 
	
	public static ConfigData getInstance() {
		if(instance == null) {
			instance = new ConfigData("conf/userDirectoryConnection.properties");
		}
		
		return instance;
	}
	
	private ConfigData(String filename) {
		
		InputStream input = null;
		
		try {
			input = new FileInputStream(filename);
			load(input);
		} catch (FileNotFoundException e) {
			// prop file not found
		} catch (IOException e) {
			// couldn't read from property file
		}
		
		attributeNames = new ArrayList<String>();
		String attributeNamesString = (String) get("attributes");
		StringTokenizer st = new StringTokenizer(attributeNamesString, ",");
		while (st.hasMoreElements()) {
			String attrName = (String) st.nextElement();
			attributeNames.add(attrName);
		}
	}

	public List<String> getAttributeNames() {
		return attributeNames;
	}

	public void setAttributeNames(List<String> attributeNames) {
		this.attributeNames = attributeNames;
	}

}
