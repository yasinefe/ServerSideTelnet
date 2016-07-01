package com.yefe.telnet.serverside.core;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.yefe.telnet.serverside.command.Command;
import com.yefe.telnet.serverside.command.LsCommand;
import com.yefe.telnet.serverside.command.MkdirCommand;
import com.yefe.telnet.serverside.core.TelnetConfiguration;

public class TelnetConfigurationShould {

	private TelnetConfiguration telnetConfiguration;

	@Before
	public void setup() {
		telnetConfiguration = new TelnetConfiguration();
	}

	@Test
	public void returnPortWhenGetPortIsCalled() {
		// Given
		int expected = 1000;
		telnetConfiguration.setPort(expected);

		// When
		int actual = telnetConfiguration.getPort();

		// Then
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void returnPromptWhenGetPromtIsCalled() {
		// Given
		String expected = ":)";
		telnetConfiguration.setPrompt(expected);

		// When
		String actual = telnetConfiguration.getPrompt();

		// Then
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void returnRegisteredClassMapWhenGetRegisteredCommandIsCalled() {
		// Given
		telnetConfiguration.register(LsCommand.class);
		telnetConfiguration.register(MkdirCommand.class);

		// When
		Map<String, Class<? extends Command>> registeredCommand = telnetConfiguration.getRegisteredCommand();

		// Then
		Assert.assertTrue(registeredCommand.containsKey(LsCommand.NAME));
		Assert.assertTrue(registeredCommand.containsKey(MkdirCommand.NAME));
	}

}
