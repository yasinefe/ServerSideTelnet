package com.yefe.telnet.serverside.command;

import java.io.IOException;

/**
 * This is used to show unknown command message to the user.
 */
public class UnknownCommand extends BaseCommand {

	public static final String MESSAGE = "Unknown command !";

	@Override
	public void execute(CommandContext context) throws IOException {
		context.write(MESSAGE);
	}

}
