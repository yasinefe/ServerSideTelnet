package com.yefe.telnet.serverside.command;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.yefe.telnet.serverside.command.CdCommand;
import com.yefe.telnet.serverside.command.CommandContext;
import com.yefe.telnet.serverside.core.TelnetHandler;

public class CdCommandShould {

	private CdCommand cdCommand;
	private CommandContext commandContext;
	private TelnetHandler telnetHandler;
	private String folderName;
	private ByteArrayOutputStream baos;
	private String workingDirectory;

	@Before
	public void setup() throws IOException {
		folderName = "temp";
		workingDirectory = System.getProperty(TelnetHandler.USER_DIR_KEY) + IOUtils.DIR_SEPARATOR + folderName;
		cdCommand = new CdCommand();
		telnetHandler = Mockito.mock(TelnetHandler.class);

		Mockito.when(telnetHandler.getDir()).thenReturn(System.getProperty(TelnetHandler.USER_DIR_KEY));
		baos = new ByteArrayOutputStream();
		Mockito.when(telnetHandler.getOut()).thenReturn(baos);

		commandContext = new CommandContext(CdCommand.NAME + " " + folderName, telnetHandler);
		FileUtils.deleteQuietly(new File(System.getProperty(TelnetHandler.USER_DIR_KEY) + IOUtils.DIR_SEPARATOR + folderName));

		FileUtils.forceMkdir(new File(System.getProperty(TelnetHandler.USER_DIR_KEY) + IOUtils.DIR_SEPARATOR + folderName));
	}

	@Test
	public void returnEmptyWhenNameIsCalled() {
		// When
		String actual = cdCommand.name();

		// Then
		Assert.assertEquals(CdCommand.NAME, actual);
	}

	@Test
	public void returnEmptyWhenUsageIsCalled() {
		// When
		String actual = cdCommand.usage();

		// Then
		Assert.assertEquals(CdCommand.USAGE, actual);
	}

	@Test
	public void returnEmptyWhenDescriptionIsCalled() {
		// When
		String actual = cdCommand.description();

		// Then
		Assert.assertEquals(CdCommand.DESCRIPTION, actual);
	}

	@Test
	public void writeMessageWhenFolderIsNotFound() throws IOException {
		// Given
		commandContext = new CommandContext(CdCommand.NAME + " sample", telnetHandler);

		// When
		cdCommand.execute(commandContext);

		// Then
		Assert.assertEquals("Folder not found", baos.toString());
	}

	@Test
	public void writeMessageWhenGivenParameterIsFile() throws IOException {
		// Given
		FileUtils.write(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "sample"), "dummy message");
		commandContext = new CommandContext(CdCommand.NAME + " " + folderName + IOUtils.DIR_SEPARATOR + "sample", telnetHandler);

		// When
		cdCommand.execute(commandContext);

		// Then
		Assert.assertEquals("Given parameter must be folder", baos.toString());

		// Clear
		FileUtils.deleteQuietly(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "sample"));
	}

	@Test
	public void writeUsageWhenThereIsNoGivenFolder() throws IOException {
		// Given
		commandContext = new CommandContext(CdCommand.NAME, telnetHandler);

		// When
		cdCommand.execute(commandContext);

		// Then
		Assert.assertEquals("Usage : " + CdCommand.USAGE, baos.toString());
	}

	public void changeCurrentDirectoryAsParentWhenDoublePointIsCalled() throws IOException {
		// Given
		commandContext = new CommandContext(CdCommand.NAME + " .." + IOUtils.DIR_SEPARATOR + "sample", telnetHandler);
		File file = new File(workingDirectory + IOUtils.DIR_SEPARATOR + "..");
		String expected = file.getCanonicalPath();

		// When
		cdCommand.execute(commandContext);

		// Then
		Mockito.verify(telnetHandler).setDir(expected);
	}

	@After
	public void tearDown() throws IOException {
		FileUtils.deleteQuietly(new File(System.getProperty(TelnetHandler.USER_DIR_KEY) + IOUtils.DIR_SEPARATOR + folderName));
	}
}
