package com.yefe.telnet.serverside.command;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * Creates directory with given parameter
 */
public class MkdirCommand implements Command {

	public static final String DESCRIPTION = "Creates directory with given parameter";
	public static final String USAGE = "mkdir <folder_name>";
	public static final String NAME = "mkdir";

	public void execute(CommandContext context) throws IOException {
		String dir = context.getTelnetHandler().getDir();

		if (context.getParameters() != null && context.getParameters().size() > 0) {
			String prm = context.getParameters().get(0);
			dir = dir + IOUtils.DIR_SEPARATOR + prm;
			File file = new File(dir);
			if (file.exists() && file.isDirectory()) {
				context.write("Directory already exist");
			} else {
				FileUtils.forceMkdir(file);
				context.write(prm + " directory created successfully");
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
