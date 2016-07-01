package com.yefe.telnet.serverside.command;

import java.io.IOException;

/**
 * This command shows current directory
 */
public class PwdCommand implements Command {

	public static final String DESCRIPTION = "Shows current directory";
	public static final String NAME = "pwd";
	public static final String USAGE = "pwd";

	public void execute(CommandContext context) throws IOException {
		String dir = context.getTelnetHandler().getDir();
		context.write(dir);
	}

	public String name() {
		return NAME;
	}

	public String usage() {
		return USAGE;
	}

	public String description() {
		return DESCRIPTION;
	}

}
