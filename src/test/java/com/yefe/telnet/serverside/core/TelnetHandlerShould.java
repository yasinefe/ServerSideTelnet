package com.yefe.telnet.serverside.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.yefe.telnet.serverside.core.CommandExecutor;
import com.yefe.telnet.serverside.core.TelnetConfiguration;
import com.yefe.telnet.serverside.core.TelnetHandler;
import com.yefe.telnet.serverside.core.TelnetServer;
import com.yefe.telnet.serverside.test.util.TestUtil;

public class TelnetHandlerShould {

	private TelnetConfiguration configuration;
	private TelnetHandler telnetHandler;
	private Socket socket;
	private CommandExecutor commandExecutor;
	private InputStream inputStream;
	private OutputStream outputStream;
	private TelnetServer telnetServer;

	@Before
	public void setup() throws IOException {
		configuration = TestUtil.createTelnetConfiguration();
		socket = Mockito.mock(Socket.class);
		commandExecutor = Mockito.mock(CommandExecutor.class);
		telnetServer = Mockito.mock(TelnetServer.class);
		outputStream = Mockito.mock(OutputStream.class);
		Mockito.when(socket.getOutputStream()).thenReturn(outputStream);

		inputStream = Mockito.mock(InputStream.class);
		Mockito.when(inputStream.read()).thenReturn(-1);
		Mockito.when(socket.getInputStream()).thenReturn(inputStream);

		telnetHandler = new TelnetHandler(socket, commandExecutor, configuration, telnetServer);
	}

	@Test
	public void setSystemUserDirAsCurrentDirWhenConstructed() throws UnknownHostException, IOException {
		// Then
		Assert.assertEquals(System.getProperty(TelnetHandler.USER_DIR_KEY), telnetHandler.getDir());
	}

	@Test
	public void readLineAndExecuteEmptyCommand() throws UnknownHostException, IOException {
		// Given
		String str = IOUtils.LINE_SEPARATOR + (char) -1;
		inputStream = new ByteArrayInputStream(str.getBytes());
		Mockito.when(socket.getInputStream()).thenReturn(inputStream);

		// When
		telnetHandler.run();

		// Then
		Mockito.verify(commandExecutor).execute("", telnetHandler);
		Mockito.verify(outputStream, Mockito.times(2)).write((System.getProperty(TelnetHandler.USER_DIR_KEY) + configuration.getPrompt()).getBytes());
		Mockito.verify(outputStream).close();
		Mockito.verify(socket).close();
		Mockito.verify(telnetServer).removeClientThreads(telnetHandler);
	}

	@Test
	public void readLineAndExecuteCommand() throws UnknownHostException, IOException {
		// Given
		String command = "ls";
		String str = command + IOUtils.LINE_SEPARATOR + (char) -1;
		inputStream = new ByteArrayInputStream(str.getBytes());
		Mockito.when(socket.getInputStream()).thenReturn(inputStream);

		// When
		telnetHandler.run();

		// Then
		Mockito.verify(commandExecutor).execute(command, telnetHandler);
		Mockito.verify(outputStream, Mockito.times(2)).write((System.getProperty(TelnetHandler.USER_DIR_KEY) + configuration.getPrompt()).getBytes());
		Mockito.verify(outputStream).close();
		Mockito.verify(socket).close();
		Mockito.verify(telnetServer).removeClientThreads(telnetHandler);
	}

	@Test
	public void closeQuietlyIfIOExceptionOccuredWhileClosingInputStream() throws UnknownHostException, IOException {
		// Given
		Mockito.doThrow(new IOException("Input Stream IOException")).when(inputStream).close();

		// When
		telnetHandler.run();

		// Then
		Mockito.verify(outputStream, Mockito.times(1)).write((System.getProperty(TelnetHandler.USER_DIR_KEY) + configuration.getPrompt()).getBytes());
		Mockito.verify(inputStream).close();
		Mockito.verify(outputStream).close();
		Mockito.verify(socket).close();
		Mockito.verify(telnetServer).removeClientThreads(telnetHandler);
	}

	@Test
	public void closeQuietlyIfIOExceptionOccuredWhileClosingOutputStream() throws UnknownHostException, IOException {
		// Given
		Mockito.doThrow(new IOException("Output Stream IOException")).when(outputStream).close();

		// When
		telnetHandler.run();

		// Then
		Mockito.verify(outputStream, Mockito.times(1)).write((System.getProperty(TelnetHandler.USER_DIR_KEY) + configuration.getPrompt()).getBytes());
		Mockito.verify(inputStream).close();
		Mockito.verify(outputStream).close();
		Mockito.verify(socket).close();
		Mockito.verify(telnetServer).removeClientThreads(telnetHandler);
	}

	@Test
	public void closeQuietlyIfIOExceptionOccuredWhileClosingSocket() throws UnknownHostException, IOException {
		// Given
		Mockito.doThrow(new IOException("Socket IOException")).when(socket).close();

		// When
		telnetHandler.run();

		// Then
		Mockito.verify(outputStream, Mockito.times(1)).write((System.getProperty(TelnetHandler.USER_DIR_KEY) + configuration.getPrompt()).getBytes());
		Mockito.verify(inputStream).close();
		Mockito.verify(outputStream).close();
		Mockito.verify(socket).close();
		Mockito.verify(telnetServer).removeClientThreads(telnetHandler);
	}

	@Test
	public void closeQuietlyAndReturnIfIOExceptionOccuredWhileWritingSocket() throws UnknownHostException, IOException {
		// Given
		Mockito.doThrow(new IOException("Socket IOException")).when(outputStream)
				.write((System.getProperty(TelnetHandler.USER_DIR_KEY) + configuration.getPrompt()).getBytes());

		// When
		telnetHandler.run();

		// Then
		Mockito.verify(outputStream, Mockito.times(1)).write((System.getProperty(TelnetHandler.USER_DIR_KEY) + configuration.getPrompt()).getBytes());
		Mockito.verify(inputStream).close();
		Mockito.verify(outputStream).close();
		Mockito.verify(socket).close();
		Mockito.verify(telnetServer).removeClientThreads(telnetHandler);
	}
}
