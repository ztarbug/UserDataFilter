package de.starwit.auth.userdata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;


/**
 * 
 * @author ztarbug
 *
 */
public class ConfigData extends Properties {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * the single instance
	 */
	private static ConfigData instance;
	
	/**
	 * attributes to be extracted from user directory
	 */
	private List<String> attributeNames;
	
	/**
	 * default config file name...
	 */
	private static final String configFileName = "userDirectoryConnection.properties";
	
	private Logger log = Logger.getLogger(ConfigData.class);
	
	public static ConfigData getInstance() {
		if(instance == null) {
			instance = new ConfigData();
		}
		
		return instance;
	}
	
	private ConfigData() {
		
		String configFilePath = computeConfigFilePath();
		
		InputStream input = null;
			
		try {
			input = new FileInputStream(configFilePath);
			load(input);
		} catch (FileNotFoundException e) {
			log.error("Could not find property file - Filter will not work! " + e.getMessage());
		} catch (IOException e) {
			log.error("couldn't read from property file - Filter will not work!");
		}
		
		attributeNames = new ArrayList<String>();
		String attributeNamesString = (String) get("attributes");
		StringTokenizer st = new StringTokenizer(attributeNamesString, ",");
		while (st.hasMoreElements()) {
			String attrName = (String) st.nextElement();
			attributeNames.add(attrName);
		}
	}
	
	private String computeConfigFilePath() {
		String configFilePath = System.getProperty("starwit.userdata.configfile");
		if (configFilePath == null || "".equals(configFilePath)) {
			log.warn("no path to config file provided, try to load default filename " + configFileName + " from execution path - this should be configured explicitly!");
			configFilePath = configFileName;
		}
		
		return configFilePath;
	}

	public List<String> getAttributeNames() {
		return attributeNames;
	}

	public void setAttributeNames(List<String> attributeNames) {
		this.attributeNames = attributeNames;
	}

}
