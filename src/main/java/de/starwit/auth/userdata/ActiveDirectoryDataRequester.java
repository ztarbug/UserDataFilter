package de.starwit.auth.userdata;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

/**
 *  
 * @author VWTDZWZ
 *
 */
public class ActiveDirectoryDataRequester {
	
	private DirContext ctx;
	
	private final ConfigData configData = ConfigData.getInstance();
	
	Logger log = Logger.getLogger(ActiveDirectoryDataRequester.class);

	public ActiveDirectoryDataRequester(DirContext context) {
		ctx = context;
	}

	public Map<String, String> getUserData(String loggedInUser) {
		
        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setReturningObjFlag(true);
        
        String base = configData.getProperty("directory.basepath"); 
        String filter = configData.getProperty("directory.searchFilter");
        //filter contains placeholder - insert user name
        filter = filter.replaceAll("###USER_NAME###", loggedInUser);
        
        try {
        	NamingEnumeration<SearchResult> results = ctx.search(base, filter, sc);
			return extractUserData(results);
		} catch (NamingException e) {
			log.error("try to query user directory, returned empty data set. Filter used: " + filter);
		}
        
		return new HashMap<String, String>(); 
	}
	
	private Map<String, String> extractUserData(NamingEnumeration<SearchResult> searchResults) {
		
		Map<String, String> result = new HashMap<String, String>();
		
		while (searchResults.hasMoreElements()){
			SearchResult sr = (SearchResult) searchResults.nextElement();
			Attributes attrs = sr.getAttributes();
			
			for (String attrName : ConfigData.getInstance().getAttributeNames()) {
				Attribute attr = attrs.get(attrName);
				String value = "";
				if(attr != null) {
					try {
						value = (String) attr.get(0);
					} catch (NamingException e) {
						log.info("Accessing attribute " + attrName + " threw Naming Exception.");
					}
				} else {
					log.info("Accessing attribute " + attrName + " resulted in null - check name in property file");
				}
				result.put(attrName, value);
			}
		}
	
		return result;
	}

}
