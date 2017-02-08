Pantry is a backend system for Casabots food robots that can be mainly used to store and retrieve various kinds of relevant data over the web.

Steps To Setup Development Environment For Pantry:

1. Install Java 8
2. Install Maven 3
3. Install MongoDB version 3.4.0
4. Install MySQL server
4. Install Tomcat 8
5. Start MongoDB service
	sudo service mongod start
6. Start Tomcat server
            cd [tomcat-directory]/bin
	./startup.sh
7. Clone and build 'pantry'
	git clone https://gitlab.com/amitbose/pantry.git (you need authorization to be able to clone the
    project)
	cd ~/pantry
	mvn install
    pantry.war file will be generated in target folder.
    copy pantry.war to [tomcat-directory]/webapps.
    Now, you will be able to use pantry's API.
    Provide both your databases' details in pantry-db.properties
    Base url for pantry: localhost:8080/pantry/
