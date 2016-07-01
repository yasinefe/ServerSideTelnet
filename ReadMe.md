# COMMENTS

I will give some details about this application.

- If you want create a deployable tar.gx package which contains this project then you should use 'mvn package' command.
- If you want to measure unit test coverage you should use 'mvn cobertura:cobertura' command
- Deployable tar.gz file contains these directories:
	- bin contains start sh and bat files.
	- conf contains log4j file.
	- lib contains jar files, dependencies and telnet server jar.
	- log files is empty in the first time then if you start the application log files will write in this folder.
	
- You can find cobertura coverage report in the RAR file in target folder.
- You can find tar.gz deployable file in target folder
- You can find the jar file of the project in target folder

# USAGE

- You can extract tar.gz file and start with files in bin directory.
- Or you can import this project in eclipse and start TelnetMain class.
- After that you should use telnet. telnet localhost 1981

# ASSUMPTIONS

I did some assumptions

- I used apache common io jar.
- I used log4j for logging.
- I should have carried port and prompt parameter into a property or xml file but this is a test and I did not.
- I did not add any authorization mechanism
- I did not implement Telnet negotiation(ECHO, WILL, WONT, DO, DONT etc) so you can not use back space, back, forward, or special characters
	- If I implement Telnet negotiation then I could have managed this characters easily.

# NEW FEATURES CAN BE ADDED

Following feature can be added but I did not.

- Authorization and authentication
- Role and User management. Which user can use whic command
- Configuration from a file or somewhere else
- Command history
- Walking on history with up and down
- Backspace, delete, back, forward
- User and/or role based logging mechanism
- Client session management, kill session, list active session commands can be added
- Help command can be added
