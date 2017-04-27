# RESTApiExample

# Active URLs

1. Get organization metro areas
	- GET `/{organization}/metros`
	- Example: `https://api.meetl.ink:8443/comeet/meetl.ink/metros`

2. Search Rooms
	- GET `/{organization}/roomlists/roomlist/rooms`
	- Parameters:
	- `roomlist=Bldg_CambMaOneStorySt@meetl.ink`
	- `start=2017-04-15T09:00:00-0400`
	- `end=2017-04-15T10:05:00-0400`
	- Example: `https://api.meetl.ink:8443/comeet/meetl.ink/roomlists/Bldg_CambMALoeb@meetl.ink/rooms`

3. Reserve a room
	- POST `/{organization}/rooms/roomrecipient/reserve`
	- Parameters:
	- `roomrecipient=CambMa1Story305@meetl.ink`
	- `start=2017-04-15T09:00:00-0400`
	- `end=2017-04-15T10:05:00-0400`
	- `subject=Title`
	- `body=Description`
	- `required=CambMa1Story305@meetl.ink,jablack@meetl.ink`
	- `optional=jillblack@meetl.ink`
	- Example URL: `https://api.meetl.ink:8443/comeet/meetl.ink/rooms/jablack@meetl.ink/reserve`

4. Get all the Meetings of a user
	- GET `/{organization}/meetings`
	- Parameters:
	- `start=2017-04-15T07:00:00-0400`
	- `end=2017-04-15T20:00:00-0400`
	- Example: `https://api.meetl.ink:8443/comeet/meetl.ink/meetings`

	Note: If the start parameter is empty, the next seven days are retrieved. If the start parameter is empty, the end parameter is ignored. 

5. Get Data about a specific meeting
	- GET `/{organization}/meeting/data`
	- Parameters:
	- `id (i.e.) JISFIJD23492834FSJSND`
	- Example: `https://api.meetl.ink:8443/comeet/meetl.ink/meeting/data`



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

# Test with Postman

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/356cf5ecffcaa423fe97)
