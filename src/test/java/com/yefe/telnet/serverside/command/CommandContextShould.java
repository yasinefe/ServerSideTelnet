package com.yefe.telnet.serverside.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.yefe.telnet.serverside.command.CommandContext;
import com.yefe.telnet.serverside.core.TelnetHandler;

public class CommandContextShould {

	private TelnetHandler telnetHandler;
	private String commandStr;

	@Before
	public void setup() {
		telnetHandler = Mockito.mock(TelnetHandler.class);
	}

	@Test
	public void parseCommandWhenCommandHasParameters() {
		// Given
		commandStr = "ls prm1 prm2";

		// Then
		CommandContext commandContext = new CommandContext(commandStr, telnetHandler);

		// When
		Assert.assertEquals("ls", commandContext.getCommandName());
		Assert.assertEquals("prm1", commandContext.getParameters().get(0));
		Assert.assertEquals("prm2", commandContext.getParameters().get(1));
	}

	@Test
	public void parseCommandWhenCommandHasNoParameters() {
		// Given
		commandStr = "ls";

		// When
		CommandContext commandContext = new CommandContext(commandStr, telnetHandler);

		// Then
		Assert.assertEquals(commandStr, commandContext.getCommandName());
		Assert.assertNull(commandContext.getParameters());
	}

	@Test
	public void writeOutWhenWriteIsCalled() throws IOException {
		// Given
		String str = "line";
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Mockito.when(telnetHandler.getOut()).thenReturn(baos);
		CommandContext commandContext = new CommandContext("ls", telnetHandler);

		// When
		commandContext.write(str);

		// Then
		Assert.assertArrayEquals(str.getBytes(), baos.toByteArray());
	}
}
