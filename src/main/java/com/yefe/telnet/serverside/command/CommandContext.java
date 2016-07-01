package com.yefe.telnet.serverside.command;

import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.List;

import com.yefe.telnet.serverside.core.TelnetHandler;

/**
 * This context contains telnet handler, socket output stream and command details.
 */
public class CommandContext {

	private static final String SPACE = " ";

	private String commandStr;

	private OutputStream out;

	private TelnetHandler telnetHandler;

	private String commandName;

	private List<String> parameters;

	public CommandContext(String commandStr, TelnetHandler telnetHandler) {
		this.commandStr = commandStr;
		this.telnetHandler = telnetHandler;
		out = telnetHandler.getOut();
		parseCommand();
	}

	/**
	 * Parse the command line, first parameter must be command name, and the others must be additional parameters
	 */
	private void parseCommand() {
		String[] tokens = commandStr.split(SPACE);
		commandName = tokens[0];
		if (tokens.length > 1) {
			parameters = new ArrayList<String>();
			for (int i = 1; i < tokens.length; i++) {
				parameters.add(tokens[i]);
			}
		}
	}

	public void write(String line) throws IOException {
		out.write(line.getBytes());
	}

	public TelnetHandler getTelnetHandler() {
		return telnetHandler;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public String getCommandName() {
		return commandName;
	}

}
