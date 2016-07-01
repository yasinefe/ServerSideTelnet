package com.yefe.telnet.serverside.core;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yefe.telnet.serverside.core.TelnetConfiguration;
import com.yefe.telnet.serverside.core.TelnetManager;
import com.yefe.telnet.serverside.test.util.TestUtil;

public class TelnetManagerShould {

	private TelnetConfiguration configuration;
	private TelnetManager telnetManager;

	@Before
	public void setup() {
		configuration = TestUtil.createTelnetConfiguration();
		telnetManager = new TelnetManager(configuration);
	}

	@Test
	public void createATelnetServerAndCommandExecutorWhenStartIsCalled() throws UnknownHostException, IOException {
		// When
		telnetManager.start();

		// Then
		Assert.assertNotNull(telnetManager.getCommandExecutor());
		Assert.assertNotNull(telnetManager.getTelnetServer());
	}

	@Test
	public void callTelnetServerStopWhenStopIsCalled() throws UnknownHostException, IOException {
		// When
		telnetManager.start();
		telnetManager.stop();

		// Then
		try {
			new Socket("localhost", configuration.getPort());
			Assert.fail("Exception should be thrown");
		} catch (Exception e) {
		}
	}

	@After
	public void tearDown() throws IOException {
		telnetManager.stop();
	}
}
