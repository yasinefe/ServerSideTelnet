package com.yefe.telnet.serverside.core;

import java.io.IOException;

/**
 * This class hides usage of TelnetServer and its dependencies. Usage should be easy for the user.
 */
public class TelnetManager {

	private TelnetServer telnetServer;

	private CommandExecutor commandExecutor;

	private TelnetConfiguration telnetConfiguration;

	public TelnetManager(TelnetConfiguration telnetConfiguration) {
		this.telnetConfiguration = telnetConfiguration;
	}

	/**
	 * Creates dependencies, telnet server and initialize it.
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		commandExecutor = new CommandExecutor(telnetConfiguration);

		telnetServer = new TelnetServer(telnetConfiguration, commandExecutor);
		telnetServer.initialize();
		telnetServer.start();
	}

	/**
	 * Stops the telnet server
	 * 
	 * @throws IOException
	 */
	public void stop() throws IOException {
		telnetServer.stopServer();
	}

	public TelnetServer getTelnetServer() {
		return telnetServer;
	}

	public CommandExecutor getCommandExecutor() {
		return commandExecutor;
	}

}
