package com.yefe.telnet.serverside.command;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

/**
 * This class is used to change current directory
 */
public class CdCommand implements Command {

	public static final String DESCRIPTION = "Changes current directory with given parameter";
	public static final String USAGE = "cd <folder>";
	public static final String NAME = "cd";

	public void execute(CommandContext context) throws IOException {
		String dir = context.getTelnetHandler().getDir();

		if (context.getParameters() != null && context.getParameters().size() > 0) {
			String prm = context.getParameters().get(0);
			if (prm.startsWith(String.valueOf(IOUtils.DIR_SEPARATOR)) || prm.contains(":")) {
				dir = prm;
			} else {
				dir = dir + IOUtils.DIR_SEPARATOR + prm;
			}
			File file = new File(dir);
			if (file.exists()) {
				if (file.isDirectory()) {
					context.getTelnetHandler().setDir(file.getCanonicalPath());
				} else {
					context.write("Given parameter must be folder");
				}
			} else {
				context.write("Folder not found");
			}
		} else {
			context.write("Usage : " + usage());
		}
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
