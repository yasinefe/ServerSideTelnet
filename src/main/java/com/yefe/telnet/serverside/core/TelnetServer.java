package com.yefe.telnet.serverside.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * This class creates a Server Socket and wait for the client attemps, If a client wants to connect this server socket
 * then create new TelnetHandler which is associated with the client socket
 */
public class TelnetServer extends Thread {

	private static Logger logger = Logger.getLogger(TelnetServer.class);

	private boolean exit = false;
	private ServerSocket serverSocket;

	private List<TelnetHandler> telnetHandlers = new ArrayList<TelnetHandler>();

	private TelnetConfiguration telnetConfiguration;
	private CommandExecutor commandExecutor;

	public TelnetServer(TelnetConfiguration telnetConfiguration, CommandExecutor commandExecutor) {
		super("TelnetServerThread");
		this.telnetConfiguration = telnetConfiguration;
		this.commandExecutor = commandExecutor;
	}

	public void initialize() throws IOException {
		serverSocket = new ServerSocket(telnetConfiguration.getPort());
		if (logger.isInfoEnabled()) {
			logger.info("Server socket is created on port : " + telnetConfiguration.getPort());
		}
	}

	@Override
	public void run() {
		while (!exit) {
			try {
				// Accept client and create new TelnetHandler
				Socket clientSocket = serverSocket.accept();
				createTelnetHandler(clientSocket);
			} catch (IOException e) {
				logger.error("Exception caught while listening client connections:" + e.getMessage());
			}
		}
	}

	/**
	 * Creates new Telnet Handler for given clientSocket
	 * 
	 * @param clientSocket
	 */
	private void createTelnetHandler(Socket clientSocket) {
		TelnetHandler telnetHandler = new TelnetHandler(clientSocket, commandExecutor, telnetConfiguration, this);

		Thread thread = new Thread(telnetHandler);
		thread.setName("TelnetHandler_" + clientSocket.getRemoteSocketAddress());
		thread.start();
		telnetHandlers.add(telnetHandler);
	}

	public void removeClientThreads(TelnetHandler telnetHandler) {
		telnetHandlers.remove(telnetHandler);
	}

	/**
	 * Stops all telnet handlers and close the server socket
	 * 
	 * @throws IOException
	 */
	public void stopServer() throws IOException {
		exit = true;
		TelnetHandler[] telnetHandlersArr = telnetHandlers.toArray(new TelnetHandler[telnetHandlers.size()]);
		for (TelnetHandler telnetHandler : telnetHandlersArr) {
			telnetHandler.stopTelnetHandler();
		}
		if (serverSocket != null) {
			serverSocket.close();
		}
	}

	/**
	 * @return Telnet handler list
	 */
	public List<TelnetHandler> getTelnetHandlers() {
		return telnetHandlers;
	}
}
