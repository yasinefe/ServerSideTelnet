package com.yefe.telnet.serverside.command;

import java.io.IOException;

public class BaseCommand implements Command {

	public static final String EMPTY = "";

	public void execute(CommandContext context) throws IOException {
	}

	public String name() {
		return EMPTY;
	}

	public String usage() {
		return EMPTY;
	}

	public String description() {
		return EMPTY;
	}

}
