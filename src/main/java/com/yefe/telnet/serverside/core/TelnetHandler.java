package com.yefe.telnet.serverside.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * This class manages socket connection, reads command line, keeps current directory.
 */
public class TelnetHandler implements Runnable {

	private static Logger logger = Logger.getLogger(TelnetHandler.class);

	public static String USER_DIR_KEY = "user.dir";

	private Socket socket;

	private OutputStream out;

	private InputStream in;

	private boolean exit;

	private String currentDir;

	private CommandExecutor commandExecutor;

	private TelnetConfiguration telnetConfiguration;

	private TelnetServer telnetServer;

	public TelnetHandler(Socket socket, CommandExecutor commandExecutor, TelnetConfiguration telnetConfiguration, TelnetServer telnetServer) {
		this.socket = socket;
		this.commandExecutor = commandExecutor;
		this.telnetConfiguration = telnetConfiguration;
		this.telnetServer = telnetServer;
		currentDir = System.getProperty(USER_DIR_KEY);
	}

	/**
	 * Initialize streams, write promt, read line, execute command, write again promt If commandline is null so client
	 * must be closed then break.
	 */
	public void run() {
		try {
			init();
			writePrompt();
			while (!exit) {
				String commandLine = readCommand();
				if (commandLine == null) {
					break;
				}
				commandExecutor.execute(commandLine, this);
				writePrompt();
			}
		} catch (IOException e) {
			logger.error("IO Exception occured : " + e.getMessage(), e);
		} finally {
			closeConnection();
		}
	}

	private void writePrompt() throws IOException {
		out.write((currentDir + telnetConfiguration.getPrompt()).getBytes());
	}

	private void init() throws IOException {
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}

	private String readCommand() throws IOException {
		String line = null;
		StringBuffer sb = new StringBuffer("");
		int ch;
		while ((ch = in.read()) != -1) {
			sb.append((char) ch);
			// If line is completed with line separator so this mean the command has completed
			// You can run it but firstly you have to remove LINE_SEPARATOR
			if (sb.toString().endsWith(IOUtils.LINE_SEPARATOR)) {
				sb.delete(sb.length() - IOUtils.LINE_SEPARATOR.length(), sb.length());
				line = sb.toString();
				break;
			}
		}
		return line;
	}

	/**
	 * Close quietly input stream, output stream and socket
	 */
	private void closeConnection() {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				logger.error("IOException while closing output stream: " + e.getMessage());
			}
		}

		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				logger.error("IOException while closing input stream : " + e.getMessage());
			}
		}

		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				logger.error("IOException while closing socket : " + e.getMessage());
			}
		}
		telnetServer.removeClientThreads(this);
	}

	/**
	 * Stops telnet handler
	 */
	public void stopTelnetHandler() {
		exit = true;
		closeConnection();
	}

	public OutputStream getOut() {
		return out;
	}

	public void setDir(String currentDir) {
		this.currentDir = currentDir;
	}

	public String getDir() {
		return currentDir;
	}

}