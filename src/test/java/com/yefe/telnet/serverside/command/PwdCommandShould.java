package com.yefe.telnet.serverside.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.yefe.telnet.serverside.command.CommandContext;
import com.yefe.telnet.serverside.command.PwdCommand;
import com.yefe.telnet.serverside.core.TelnetHandler;

public class PwdCommandShould {

	private PwdCommand pwdCommand;
	private CommandContext commandContext;
	private TelnetHandler telnetHandler;
	private ByteArrayOutputStream baos;

	@Before
	public void setup() throws IOException {
		pwdCommand = new PwdCommand();
		telnetHandler = Mockito.mock(TelnetHandler.class);

		Mockito.when(telnetHandler.getDir()).thenReturn(System.getProperty(TelnetHandler.USER_DIR_KEY));
		baos = new ByteArrayOutputStream();
		Mockito.when(telnetHandler.getOut()).thenReturn(baos);

		commandContext = new CommandContext(PwdCommand.NAME, telnetHandler);
	}

	@Test
	public void returnEmptyWhenNameIsCalled() {
		// When
		String actual = pwdCommand.name();

		// Then
		Assert.assertEquals(PwdCommand.NAME, actual);
	}

	@Test
	public void returnEmptyWhenUsageIsCalled() {
		// When
		String actual = pwdCommand.usage();

		// Then
		Assert.assertEquals(PwdCommand.USAGE, actual);
	}

	@Test
	public void returnEmptyWhenDescriptionIsCalled() {
		// When
		String actual = pwdCommand.description();

		// Then
		Assert.assertEquals(PwdCommand.DESCRIPTION, actual);
	}

	@Test
	public void writeEmptyMessageWhenFolderIsEmpty() throws IOException {
		// When
		pwdCommand.execute(commandContext);

		// Then
		Assert.assertEquals(System.getProperty(TelnetHandler.USER_DIR_KEY), baos.toString());
	}

}
