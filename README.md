# UserDataFilter
Implements a Servlet filter to query LDAP/AD and put various user data on HTTP session. Please note, 
this module assumes that you have an authentication mechanism in place and there is a valid UserPrincipal 
on a users session. Based on that principal name configured directory server is queried.

Just add this module to your pom.xml

		<dependency>
			<groupId>de.starwit.auth</groupId>
			<artifactId>UserData</artifactId>
			<version>0.1</version>
			<scope>compile</scope>
		</dependency>

Configuration in web.xml

	<filter>
		<filter-name>UserDataFilter</filter-name>
		<filter-class>de.starwit.auth.userdata.UserDataFilter</filter-class>
	</filter>

	<filter-mapping>
		<filter-name>UserDataFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

In you app servers base folder you need to have a file called userDirectoryConnection.properties within 
a folder called "conf".
Sample props file for accessing an apacheds server. An example schema can be found a https://github.com/ztarbug/apacheds-embedded/blob/master/starwit.ldif
# network url of directory
directory.url=ldap://localhost:10389
# credentials to access directory
directory.username=uid=admin,ou=system
directory.password=secret
# where to look for users
directory.basepath=dc=starwit,dc=de
# filter to search for user objects in basepath
directory.searchFilter=(&(objectclass=person)(uid=###USER_NAME###))

# attributes per user to be read from directory
attributes=cn,sn,uid
