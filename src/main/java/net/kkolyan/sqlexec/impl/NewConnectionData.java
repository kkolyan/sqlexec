package net.kkolyan.sqlexec.impl;

import net.kkolyan.sqlexec.api.Vendor;

public interface NewConnectionData {
	String getConnectionName();
	String getDatabase();

	String getHost();

	char[] getPassword();

	int getPort();

	String getUsername();

	Vendor getVendor();

	interface Handler {
		void handle(NewConnectionData data) throws Exception;
	}
}
