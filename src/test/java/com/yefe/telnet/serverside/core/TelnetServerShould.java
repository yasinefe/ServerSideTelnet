package com.yefe.telnet.serverside.core;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.yefe.telnet.serverside.core.CommandExecutor;
import com.yefe.telnet.serverside.core.TelnetConfiguration;
import com.yefe.telnet.serverside.core.TelnetServer;
import com.yefe.telnet.serverside.test.util.LockObject;
import com.yefe.telnet.serverside.test.util.TestUtil;

public class TelnetServerShould {

	private TelnetServer telnetServer;
	private TelnetConfiguration configuration;
	private CommandExecutor commandExecutor;

	@Before
	public void setup() {
		configuration = TestUtil.createTelnetConfiguration();
		commandExecutor = Mockito.mock(CommandExecutor.class);
		telnetServer = new TelnetServer(configuration, commandExecutor);
	}

	@Test
	public void createAServerSocketWhenInitializeIsCalled() throws UnknownHostException, IOException {
		// When
		telnetServer.initialize();

		// Then
		Socket socket = new Socket("localhost", configuration.getPort());
		Assert.assertTrue(socket.isConnected());
	}

	@Test
	public void stopAndCloseServerSocketWhenStopServerIsCalled() throws UnknownHostException, IOException {
		// When
		telnetServer.initialize();
		telnetServer.stopServer();

		// Then
		try {
			new Socket("localhost", configuration.getPort());
			Assert.fail("Exception should be thrown");
		} catch (Exception e) {
		}
	}

	@Test
	public void acceptClientWhenThreadIsStarted() throws UnknownHostException, IOException, InterruptedException {
		// When
		telnetServer.initialize();
		telnetServer.start();

		int count = 10;
		final LockObject lockObject = LockObject.createLocked(count);
		for (int i = 0; i < count; i++) {
			new Thread(new Runnable() {
				public void run() {
					try {
						new Socket("localhost", configuration.getPort());
						lockObject.release();
					} catch (UnknownHostException e) {
						Assert.fail(e.getMessage());
					} catch (IOException e) {
						Assert.fail(e.getMessage());
					}
				}
			}).start();
		}
		lockObject.waitFor();

		// Then
		Assert.assertEquals(10, telnetServer.getTelnetHandlers().size());
	}

	@Test
	public void stopTelnetServerWhenStopIsCalled() throws UnknownHostException, IOException, InterruptedException {
		// When
		telnetServer.initialize();
		telnetServer.start();

		int count = 10;
		final LockObject lockObject = LockObject.createLocked(count);
		for (int i = 0; i < count; i++) {
			new Thread(new Runnable() {
				public void run() {
					try {
						new Socket("localhost", configuration.getPort());
						lockObject.release();
					} catch (UnknownHostException e) {
						Assert.fail(e.getMessage());
					} catch (IOException e) {
						Assert.fail(e.getMessage());
					}
				}
			}).start();
		}
		lockObject.waitFor();
		Assert.assertEquals(10, telnetServer.getTelnetHandlers().size());

		telnetServer.stopServer();

		// Then
		Assert.assertEquals(0, telnetServer.getTelnetHandlers().size());
	}

	@After
	public void tearDown() throws IOException {
		telnetServer.stopServer();
	}
}
