echo "Copying pantry.war to Tomcat webapps...."
cp target/pantry.war /home/beehyv/apache-tomcat-8.5.9/webapps/
echo "Finished copying"
echo "Starting Tomcat....."
sh /home/beehyv/apache-tomcat-8.5.9/bin/startup.sh
echo "Waiting for Tomcat to start....."
sleep 60s
