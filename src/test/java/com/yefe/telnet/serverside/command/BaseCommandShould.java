package com.yefe.telnet.serverside.command;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.yefe.telnet.serverside.command.BaseCommand;

public class BaseCommandShould {

	private BaseCommand baseCommand;

	@Before
	public void setUp() {
		baseCommand = new BaseCommand();
	}

	@Test
	public void returnEmptyWhenNameIsCalled() {
		// When
		String actual = baseCommand.name();

		// Then
		Assert.assertEquals(BaseCommand.EMPTY, actual);
	}

	@Test
	public void returnEmptyWhenUsageIsCalled() {
		// When
		String actual = baseCommand.usage();

		// Then
		Assert.assertEquals(BaseCommand.EMPTY, actual);
	}

	@Test
	public void returnEmptyWhenDescriptionIsCalled() {
		// When
		String actual = baseCommand.description();

		// Then
		Assert.assertEquals(BaseCommand.EMPTY, actual);
	}

	@Test
	public void doNothingWhenExecuteIsCalled() throws IOException {
		baseCommand.execute(null);
	}

}
