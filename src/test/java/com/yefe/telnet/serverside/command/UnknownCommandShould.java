package com.yefe.telnet.serverside.command;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.yefe.telnet.serverside.command.BaseCommand;
import com.yefe.telnet.serverside.command.CommandContext;
import com.yefe.telnet.serverside.command.UnknownCommand;

public class UnknownCommandShould {

	private UnknownCommand unknownCommand;
	private CommandContext commandContext;

	@Before
	public void setup() {
		unknownCommand = new UnknownCommand();
		commandContext = Mockito.mock(CommandContext.class);
	}

	@Test
	public void returnEmptyWhenNameIsCalled() {
		// When
		String actual = unknownCommand.name();

		// Then
		Assert.assertEquals(BaseCommand.EMPTY, actual);
	}

	@Test
	public void returnEmptyWhenUsageIsCalled() {
		// When
		String actual = unknownCommand.usage();

		// Then
		Assert.assertEquals(BaseCommand.EMPTY, actual);
	}

	@Test
	public void returnEmptyWhenDescriptionIsCalled() {
		// When
		String actual = unknownCommand.description();

		// Then
		Assert.assertEquals(BaseCommand.EMPTY, actual);
	}

	@Test
	public void doNothingWhenExecuteIsCalled() throws IOException {
		// When
		unknownCommand.execute(commandContext);

		// Then
		Mockito.verify(commandContext).write(UnknownCommand.MESSAGE);
	}

}
