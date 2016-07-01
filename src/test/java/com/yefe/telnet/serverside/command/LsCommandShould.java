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
import com.yefe.telnet.serverside.command.LsCommand;
import com.yefe.telnet.serverside.core.TelnetHandler;

public class LsCommandShould {

	private LsCommand lsCommand;
	private CommandContext commandContext;
	private TelnetHandler telnetHandler;
	private String folderName;
	private ByteArrayOutputStream baos;
	private String workingDirectory;

	@Before
	public void setup() throws IOException {
		folderName = "temp";
		workingDirectory = System.getProperty(TelnetHandler.USER_DIR_KEY) + IOUtils.DIR_SEPARATOR + folderName;
		lsCommand = new LsCommand();
		telnetHandler = Mockito.mock(TelnetHandler.class);

		Mockito.when(telnetHandler.getDir()).thenReturn(System.getProperty(TelnetHandler.USER_DIR_KEY));
		baos = new ByteArrayOutputStream();
		Mockito.when(telnetHandler.getOut()).thenReturn(baos);

		commandContext = new CommandContext(LsCommand.NAME + " " + folderName, telnetHandler);
		FileUtils.deleteQuietly(new File(System.getProperty(TelnetHandler.USER_DIR_KEY) + IOUtils.DIR_SEPARATOR + folderName));

		FileUtils.forceMkdir(new File(System.getProperty(TelnetHandler.USER_DIR_KEY) + IOUtils.DIR_SEPARATOR + folderName));
	}

	@Test
	public void returnEmptyWhenNameIsCalled() {
		// When
		String actual = lsCommand.name();

		// Then
		Assert.assertEquals(LsCommand.NAME, actual);
	}

	@Test
	public void returnEmptyWhenUsageIsCalled() {
		// When
		String actual = lsCommand.usage();

		// Then
		Assert.assertEquals(LsCommand.USAGE, actual);
	}

	@Test
	public void returnEmptyWhenDescriptionIsCalled() {
		// When
		String actual = lsCommand.description();

		// Then
		Assert.assertEquals(LsCommand.DESCRIPTION, actual);
	}

	@Test
	public void writeEmptyMessageWhenFolderIsEmpty() throws IOException {
		// When
		lsCommand.execute(commandContext);

		// Then
		Assert.assertEquals(workingDirectory + " folder is empty !", baos.toString());
	}

	@Test
	public void listAllOfFilesAndFolders() throws IOException {
		// Given
		FileUtils.write(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "File1.txt"), "dummy message");
		FileUtils.write(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "File2.txt"), "dummy message");
		FileUtils.forceMkdir(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "dir1"));
		FileUtils.forceMkdir(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "dir2"));

		// When
		lsCommand.execute(commandContext);

		// Then
		// Should be like this :
		// 14/06/2013 07.50.19 <DIR> dir1
		// 14/06/2013 07.50.19 <DIR> dir2
		// 14/06/2013 07.50.19 File1.txt
		// 14/06/2013 07.50.19 File2.txt
		Assert.assertTrue(baos.toString().contains("<DIR> dir1"));
		Assert.assertTrue(baos.toString().contains("<DIR> dir2"));
		Assert.assertTrue(baos.toString().contains("File1.txt"));
		Assert.assertTrue(baos.toString().contains("File2.txt"));
	}

	@Test
	public void listAllOfFilesAndFoldersOfCurrentDirectorWhenAFolderNotGiven() throws IOException {
		workingDirectory = System.getProperty(TelnetHandler.USER_DIR_KEY);

		// Given
		FileUtils.write(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "File1.txt"), "dummy message");
		FileUtils.write(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "File2.txt"), "dummy message");
		FileUtils.forceMkdir(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "dir1"));
		FileUtils.forceMkdir(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "dir2"));

		commandContext = new CommandContext(LsCommand.NAME, telnetHandler);

		// When
		lsCommand.execute(commandContext);

		// Then
		// Should be like this :
		// 14/06/2013 07.50.19 <DIR> dir1
		// 14/06/2013 07.50.19 <DIR> dir2
		// 14/06/2013 07.50.19 File1.txt
		// 14/06/2013 07.50.19 File2.txt
		Assert.assertTrue(baos.toString().contains("<DIR> dir1"));
		Assert.assertTrue(baos.toString().contains("<DIR> dir2"));
		Assert.assertTrue(baos.toString().contains("File1.txt"));
		Assert.assertTrue(baos.toString().contains("File2.txt"));

		FileUtils.deleteQuietly(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "File1.txt"));
		FileUtils.deleteQuietly(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "File2.txt"));
		FileUtils.deleteQuietly(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "dir1"));
		FileUtils.deleteQuietly(new File(workingDirectory + IOUtils.DIR_SEPARATOR + "dir2"));
	}

	@After
	public void tearDown() throws IOException {
		FileUtils.deleteQuietly(new File(System.getProperty(TelnetHandler.USER_DIR_KEY) + IOUtils.DIR_SEPARATOR + folderName));
	}
}
