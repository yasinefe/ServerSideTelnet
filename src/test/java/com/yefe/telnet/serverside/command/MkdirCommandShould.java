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

import com.yefe.telnet.serverside.command.CommandContext;
import com.yefe.telnet.serverside.command.MkdirCommand;
import com.yefe.telnet.serverside.core.TelnetHandler;

public class MkdirCommandShould {

	private MkdirCommand mkdirCommand;
	private CommandContext commandContext;
	private TelnetHandler telnetHandler;
	private String folderName;
	private ByteArrayOutputStream baos;
	private String workingDirectory;

	@Before
	public void setup() throws IOException {
		folderName = "temp";
		workingDirectory = System.getProperty(TelnetHandler.USER_DIR_KEY) + IOUtils.DIR_SEPARATOR + folderName;
		mkdirCommand = new MkdirCommand();
		telnetHandler = Mockito.mock(TelnetHandler.class);

		Mockito.when(telnetHandler.getDir()).thenReturn(System.getProperty(TelnetHandler.USER_DIR_KEY));
		baos = new ByteArrayOutputStream();
		Mockito.when(telnetHandler.getOut()).thenReturn(baos);

		commandContext = new CommandContext(MkdirCommand.NAME + " " + folderName, telnetHandler);
		FileUtils.deleteQuietly(new File(System.getProperty(TelnetHandler.USER_DIR_KEY) + IOUtils.DIR_SEPARATOR + folderName));
	}

	@Test
	public void returnEmptyWhenNameIsCalled() {
		// When
		String actual = mkdirCommand.name();

		// Then
		Assert.assertEquals(MkdirCommand.NAME, actual);
	}

	@Test
	public void returnEmptyWhenUsageIsCalled() {
		// When
		String actual = mkdirCommand.usage();

		// Then
		Assert.assertEquals(MkdirCommand.USAGE, actual);
	}

	@Test
	public void returnEmptyWhenDescriptionIsCalled() {
		// When
		String actual = mkdirCommand.description();

		// Then
		Assert.assertEquals(MkdirCommand.DESCRIPTION, actual);
	}

	@Test
	public void writeUsageWhenFolderNameIsEmpty() throws IOException {
		// Given
		commandContext = new CommandContext(MkdirCommand.NAME, telnetHandler);

		// When
		mkdirCommand.execute(commandContext);

		// Then
		Assert.assertEquals("Usage : " + MkdirCommand.USAGE, baos.toString());
	}

	@Test
	public void writeAlreadyExistWhenFolderIsExist() throws IOException {
		File file = new File(workingDirectory);
		FileUtils.forceMkdir(file);
		// When
		mkdirCommand.execute(commandContext);

		// Then
		Assert.assertEquals("Directory already exist", baos.toString());
	}

	@Test
	public void createFolder() throws IOException {
		// When
		mkdirCommand.execute(commandContext);

		// Then
		Assert.assertTrue(new File(workingDirectory).exists());
		Assert.assertEquals(folderName + " directory created successfully", baos.toString());
	}

	@After
	public void tearDown() throws IOException {
		FileUtils.deleteQuietly(new File(System.getProperty(TelnetHandler.USER_DIR_KEY) + IOUtils.DIR_SEPARATOR + folderName));
	}
}
