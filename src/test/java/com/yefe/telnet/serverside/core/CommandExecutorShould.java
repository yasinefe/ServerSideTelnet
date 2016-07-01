package com.yefe.telnet.serverside.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.yefe.telnet.serverside.command.LsCommand;
import com.yefe.telnet.serverside.command.UnknownCommand;
import com.yefe.telnet.serverside.core.CommandExecutor;
import com.yefe.telnet.serverside.core.TelnetConfiguration;
import com.yefe.telnet.serverside.core.TelnetHandler;
import com.yefe.telnet.serverside.test.util.TestUtil;

public class CommandExecutorShould {

	private TelnetConfiguration telnetConfiguration;
	private CommandExecutor commandExecutor;
	private TelnetHandler telnetHandler;
	private ByteArrayOutputStream baos;

	@Before
	public void setup() {
		telnetConfiguration = TestUtil.createTelnetConfiguration();
		telnetConfiguration.register(LsCommand.class);
		telnetHandler = Mockito.mock(TelnetHandler.class);
		commandExecutor = new CommandExecutor(telnetConfiguration);
		baos = new ByteArrayOutputStream();
		Mockito.when(telnetHandler.getDir()).thenReturn(System.getProperty(TelnetHandler.USER_DIR_KEY));
		Mockito.when(telnetHandler.getOut()).thenReturn(baos);
	}

	@Test
	public void executedDummyCommandWhenGivenCommandLineIsNull() throws UnknownHostException, IOException {
		// When
		commandExecutor.execute(null, telnetHandler);

		// Then
		Assert.assertEquals("" + IOUtils.LINE_SEPARATOR, new String(baos.toByteArray()));
	}

	@Test
	public void executedDummyCommandWhenGivenCommandLineIsEmpty() throws UnknownHostException, IOException {
		// When
		commandExecutor.execute("", telnetHandler);

		// Then
		Assert.assertEquals("" + IOUtils.LINE_SEPARATOR, new String(baos.toByteArray()));
	}

	@Test
	public void executedUnknownCommandWhenGivenCommandLineIsWrong() throws UnknownHostException, IOException {
		// When
		commandExecutor.execute("asd", telnetHandler);

		// Then
		Assert.assertEquals(UnknownCommand.MESSAGE + IOUtils.LINE_SEPARATOR, new String(baos.toByteArray()));
	}

	@Test
	public void executedCommandWhenGivenCommandLineIsCorrect() throws UnknownHostException, IOException {
		// When
		commandExecutor.execute("ls", telnetHandler);

		// Then
		String str = new String(baos.toByteArray());
		Assert.assertTrue(str.contains("pom.xml"));
		Assert.assertTrue(str.contains("src"));
	}
}
