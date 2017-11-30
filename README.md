##Community Builder Back-End
### 1 Development Setup Guide
#### 1.1 Required software
- Eclipse Mars.2 EE (or newer) - http://www.eclipse.org/downloads/eclipse-packages/
- Tomcat 8 - http://tomcat.apache.org/download-80.cgi
- MySQL 5.6.14 Community Server (or newer) - http://dev.mysql.com/downloads/
- AspectJ 1.8 and AspectJ Tools in Eclipse - Available in the eclipse marketplace

Make sure you have these installed before you continue

#### 1.2 Add Tomcat to Eclipse
- In Eclipse:
- On windows: Window > Preferences > Server > Runtime Environments
- On a mac: Eclipse > Preferences > Server > Runtime Environments
- add > Apache Tomcat v8
- Select the downloaded version of Tomcat on your machine
- Finish, ok.

#### 1.3 Importing the project into eclipse
- File > Import > Projects from GIT > CLONE URI
- URI = https://github.com/jochem261/daringduckBackend.git
- Press next, enter github username & password, select branch: master
- Press next again, wait for eclipse to download the project
- Press next, finish

- Some java files have missing libraries. Import the missing libraries from tomcat.
- Select the project. Project > properties > build path OR right-click the project > Build Path > Configure Build Path
- Select 'libraries' and press 'add library' then select Server > Apache Tomcat v8
- The errors should now be gone.

#### 1.4 Deploying the schema to MySQL
- Open a tool to manage SQL (if not installed, look for one. Or use the command line tool provided by MySQL)
- Create a database named: daringduck
- Run the script: '/src/main/resources/daringduck.sql' in the 'daringduck' database

**Connecting MySQL with Hibernate**
- Change the host, username and password in '/src/main/resources/META-INF/persistence.xml'
- PLEASE MAKE SURE YOU UNTRACK THIS FILE IN GIT, OTHERWISE THE PASSWORD WILL BE CHANGED FOR EVERYONE

#### 1.5 Running the project
- Select the project and press run
- Eclipse will prompt you to select a way of running the project
- Select: Run On Server -> Select Tomcat v8.0 Server -> Next -> Finish
- The project will now run in Eclipse

### 2 Deploying Project to Server
#### 2.1 Required software
- Oracle JDK8
- Tomcat 8
- Mysql-server 5 or newer
- maven2

#### 2.2 Other requirements
- Create the database schema in a database on the server '/src/main/resources/daringduck.sql'
- Set the correct settings in '/src/main/resources/META-INF/persistence.xml'

#### 2.3 Build and Package the project
- Go into the root folder of a project and run 'mvn package'
- A .war package will be created in the targets/ directory
- Copy the .war file to the webapps directory in tomcat.
- The server can now be reached on: http://[ip-adres]/[name-of-war-file-without-file-extension]/
