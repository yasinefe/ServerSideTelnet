package com.yefe.telnet.serverside.test.util;

import com.yefe.telnet.serverside.core.TelnetConfiguration;

public class TestUtil {

	public static TelnetConfiguration createTelnetConfiguration() {
		TelnetConfiguration telnetConfiguration = new TelnetConfiguration();
		telnetConfiguration.setPort(1980);
		telnetConfiguration.setPrompt(":");
		return telnetConfiguration;
	}

}
