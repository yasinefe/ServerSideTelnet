package com.yefe.telnet.serverside.core;

import java.util.HashMap;
import java.util.Map;

import com.yefe.telnet.serverside.command.Command;

/**
 * This class keeps configuration and registered commands to the telnet server.
 */
public class TelnetConfiguration {

	private int port = 9090;

	private String prompt = ">";

	private Map<String, Class<? extends Command>> registeredCommand = new HashMap<String, Class<? extends Command>>();

	public String getPrompt() {
		return prompt;
	}

	public int getPort() {
		return port;
	}

	/**
	 * Instantiate a new command to get command name. Keep it in registered command map.
	 * 
	 * @param commandClass
	 */
	public void register(Class<? extends Command> commandClass) {
		Command command = CommandExecutor.instantiateCommand(commandClass);
		registeredCommand.put(command.name(), commandClass);
	}

	public Map<String, Class<? extends Command>> getRegisteredCommand() {
		return registeredCommand;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

}
