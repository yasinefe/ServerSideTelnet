package com.yefe.telnet.serverside.main;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.yefe.telnet.serverside.command.CdCommand;
import com.yefe.telnet.serverside.command.LsCommand;
import com.yefe.telnet.serverside.command.MkdirCommand;
import com.yefe.telnet.serverside.command.PwdCommand;
import com.yefe.telnet.serverside.core.TelnetConfiguration;
import com.yefe.telnet.serverside.core.TelnetManager;

/**
 * This class configures and runs the telnet server.
 */
public class TelnetMain {

	private static Logger logger = Logger.getLogger(TelnetMain.class);

	public static void main(String[] args) {
		TelnetConfiguration telnetConfiguration = new TelnetConfiguration();

		// It should be configurable but this is sample
		telnetConfiguration.setPort(1981);
		telnetConfiguration.setPrompt(">");

		// Register all commands
		telnetConfiguration.register(LsCommand.class);
		telnetConfiguration.register(CdCommand.class);
		telnetConfiguration.register(PwdCommand.class);
		telnetConfiguration.register(MkdirCommand.class);

		// Create telnet manager with telnet configuration and start it
		TelnetManager telnetManager = new TelnetManager(telnetConfiguration);
		try {
			telnetManager.start();
		} catch (IOException e) {
			logger.error("Error occured during server socket initialization : " + e.getMessage());
		}
	}
}
