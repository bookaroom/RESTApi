# RESTApiExample

# Active URLs

1. Get all the rooms within an organization
	- GET `/organization/rooms`

2. Reserve a room
	- POST /organization/rooms/reserve
	- Parameters: /organization/rooms/reserve\
	`start=2017-04-15T09:00:00-0400`\
	`end=2017-04-15T10:05:00-0400`\
	`subject=testSubject`\
	`body=testBody`\
	`recipients=CambMa1Story305@meetl.ink,jablack@meetl.ink`
	- Example: `curl -X POST -H "Content-Type: application/x-www-form-urlencoded" http://ec2-34-208-175-1.us-west-2.compute.amazonaws.com:8080/organization/rooms/reserve -d "start=2017-04-16T09:00:00-0400&end=2017-04-16T10:05:00-0400&subject=testSubject&body=testBody&recipients=CambMa1Story305@meetl.ink,jablack@meetl.ink"`

3. Get all the Meetings of a user
	- GET `/organization/user/meetings`
	- Parameters: /organization/user/meetings\
	`start=2017-04-15T07:00:00-0400`\
	`end=2017-04-15T20:00:00-0400`
	- Example: `/organization/user/meetings?start=2017-03-25T22:00:00-0400&end=2017-03-30T12:00:00-0400`
	
4. Get organization metro areas
	- GET `/{organization}/metros`
	- Example: `/google.com/metros
 
5. Get all the Meetings of a user
	- GET `/{organization}/roomlists/{roomlist}/rooms"`
	- Parameters: start, end
	`start=2017-04-15T07:00:00-0400`\
	`end=2017-04-15T20:00:00-0400`
	- Example: `/google.com/roomlists/abc@test.com/rooms?start=2017-03-25T22:00:00-0400&end=2017-03-30T12:00:00-0400`
 	
	Note: If the start parameter is empty, the next seven days are retrieved. If the start parameter is empty, the end parameter is 		ignored. 


# Deployment Instructions

1. `$ ssh ec2-user@ec2-52-35-139-201.us-west-2.compute.amazonaws.com -i /PATH/MyKeyPairTwo.pem`
	- Replace PATH. MyKeyPairTwo.pem is pinned in the #General channel on slack.

2. Copy .WAR into the /usr/share/tomcat6/webapps/ directory. An FTP application such as FileZilla can be used. 
	- Connect to Amazon EC2 file directory using Filezilla: http://stackoverflow.com/questions/16744863/connect-to-amazon-ec2-file-directory-using-filezilla-and-sftp

3. Restart the server if necessary. Sometimes, changes take a few mins. or longer to show up.
	- `$ sudo service tomcat6 stop`
	- `$ sudo service tomcat6 start`


# Setting up the project

1. Download Eclipse Java Neon EE IDE
2. Install maven on your local machine
3. Load the project as a Maven project

# Debugging the program

To do local testing, you need to make sure you have Apache Tomcat installed on the machine and create an Apache Tomcat run configuration. 
