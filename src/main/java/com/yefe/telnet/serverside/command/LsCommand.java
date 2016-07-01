package com.yefe.telnet.serverside.command;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.apache.commons.io.IOUtils;

/**
 * This command lists all files and folders.
 */
public class LsCommand implements Command {

	public static final String DESCRIPTION = "Lists current directory";
	public static final String NAME = "ls";
	public static final String USAGE = "ls";

	public void execute(CommandContext context) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH.mm.ss");

		String dir = context.getTelnetHandler().getDir();

		if (context.getParameters() != null && context.getParameters().size() > 0) {
			dir = dir + IOUtils.DIR_SEPARATOR + context.getParameters().get(0);
		}

		File[] listFiles = new File(dir).listFiles();
		if (listFiles != null && listFiles.length != 0) {
			for (File file : listFiles) {
				String name = file.getName();
				boolean isDirectory = file.isDirectory();
				long lastModified = file.lastModified();

				StringBuffer sb = new StringBuffer();
				sb.append(sdf.format(new Date(lastModified))).append(" ");
				sb.append(isDirectory ? "<DIR>" : "     ").append(" ");
				sb.append(name).append(IOUtils.LINE_SEPARATOR);

				context.write(sb.toString());
			}
		} else {
			context.write(dir + " folder is empty !");
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
