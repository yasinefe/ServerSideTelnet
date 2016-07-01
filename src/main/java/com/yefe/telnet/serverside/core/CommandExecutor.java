package com.yefe.telnet.serverside.core;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.yefe.telnet.serverside.command.Command;
import com.yefe.telnet.serverside.command.CommandContext;
import com.yefe.telnet.serverside.command.DummyCommand;
import com.yefe.telnet.serverside.command.UnknownCommand;

/**
 * This class finds the command according to given command line, executes the command
 */
public class CommandExecutor {

	private static Logger logger = Logger.getLogger(CommandExecutor.class);

	private TelnetConfiguration telnetConfiguration;

	public CommandExecutor(TelnetConfiguration telnetConfiguration) {
		this.telnetConfiguration = telnetConfiguration;
	}

	/**
	 * 1. Check incoming command request is null or empty. <br>
	 * 2. Parse the incoming command request. <br>
	 * 3. Find the command. <br>
	 * 4. Execute the command. <br>
	 * 
	 * @param commandStr
	 *            {@link String}
	 * @throws IOException
	 */
	public void execute(String commandLine, TelnetHandler telnetHandler) throws IOException {
		if (logger.isInfoEnabled()) {
			logger.info("Command line " + commandLine + " will be executed");
		}
		Command command = null;
		CommandContext commandContext;
		if (isDummyCommand(commandLine)) {
			commandContext = new CommandContext("", telnetHandler);
			command = dummy();
			if (logger.isDebugEnabled()) {
				logger.debug("Dummy command will be executed");
			}
		} else {
			commandContext = new CommandContext(commandLine, telnetHandler);
			command = findCommand(commandContext);
			if (logger.isDebugEnabled()) {
				logger.debug("Command " + command.name() + " will be executed");
			}
		}
		execute(command, commandContext);
	}

	/**
	 * Search registered command and if it is available return it otherwise return unknown command
	 * 
	 * @param commandContext
	 * @return Command
	 */
	private Command findCommand(CommandContext commandContext) {
		if (telnetConfiguration.getRegisteredCommand().containsKey(commandContext.getCommandName())) {
			if (logger.isDebugEnabled()) {
				logger.debug("Command was found for : " + commandContext.getCommandName());
			}
			return instantiateCommand(telnetConfiguration.getRegisteredCommand().get(commandContext.getCommandName()));
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Unknown command will be executed");
			}
			return unknown();
		}
	}

	private void execute(Command command, CommandContext commandContext) throws IOException {
		command.execute(commandContext);
		commandContext.write(IOUtils.LINE_SEPARATOR);
	}

	private boolean isDummyCommand(String commandStr) {
		return commandStr == null || commandStr.trim().equals("");
	}

	private Command dummy() {
		return instantiateCommand(DummyCommand.class);
	}

	private Command unknown() {
		return instantiateCommand(UnknownCommand.class);
	}

	/**
	 * Instantiates command object with given command class
	 * 
	 * @param commandClass
	 * @return Command
	 */
	public static Command instantiateCommand(Class<? extends Command> commandClass) {
		try {
			return commandClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
