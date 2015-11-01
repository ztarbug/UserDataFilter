# UserDataFilter
Implements a Servlet filter to query LDAP/AD and put various user data on HTTP session. Please note, 
this module assumes that you have an authentication mechanism in place and there is a valid UserPrincipal 
on a users session. Based on that principal name configured directory server is queried.

## What does it do
Module consists of two main elements. The servlet filter gets alias of currently logged in user and retrieves
from configured user directory additional data. Those date are then pushed to user's session.
Second component is a servlet (context path *GetUserData*) that delivers retrieved user data as a JSON object.
Your application's frontend technology can then use that data to display a user's details.

## How to configure

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

User needs to come from somewhere, so this module expects a configuration file that contains all necessary
data to access and search users in an LDAP directory. Location of that configuration file has to be provided
as a JVM parameter like so ```-Dstarwit.userdata.configfile=conf/userDirectoryConnection.properties```

Sample props file for accessing an apacheds server. An example schema can be found a https://github.com/ztarbug/apacheds-embedded/blob/master/starwit.ldif
```properties
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
```
