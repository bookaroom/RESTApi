# RESTApiExample


1. $ ssh ec2-user@ec2-52-35-139-201.us-west-2.compute.amazonaws.com -i /PATH/mykeypair.pem
	- Replace PATH. A copy of mykeypair.pem is in the RESTApiExample's directory.

2. Copy .WAR into the /usr/share/tomcat6/webapps/ directory. An FTP application such as FileZilla can be used. 
	- Connect to Amazon EC2 file directory using Filezilla: http://stackoverflow.com/questions/16744863/connect-to-amazon-ec2-file-directory-using-filezilla-and-sftp

3. Restart the server if necessary. Sometimes, changes take a few mins. or longer to show up.
	- $ sudo service tomcat6 stop
	- $ sudo service tomcat6 start


# Setting up the project

1. Download Eclipse Java Neon EE IDE
2. Install maven on your local machine
2. Load the project as a Maven project

#Debugging the program

To do local testing, you need to make sure you have Apache Tomcat installed on the machine and create an Apache Tomcat run configuration. 
