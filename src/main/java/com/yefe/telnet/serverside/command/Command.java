package com.yefe.telnet.serverside.command;

import java.io.IOException;

/**
 * This interface separate the API logic and command implementation. I used name, usage, description becuase in future
 * we will have to write a HELP command and then we can use this methods to give help to the user <br/>
 * <br/>
 * NOT: Implemantor classes should have default constructor.
 */
public interface Command {

	/**
	 * When the command is called this method will be executed.
	 * 
	 * @param context
	 */
	void execute(CommandContext context) throws IOException;

	/**
	 * @return name of the command
	 */
	String name();

	/**
	 * @return usage of the command
	 */
	String usage();

	/**
	 * @return short description of the command
	 */
	String description();

}
