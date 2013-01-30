package net.kkolyan.sqlexec.api;

import java.sql.SQLException;
import java.util.List;

public interface TableSpace {
	List<Table> getTables() throws SQLException;
	String getName();
	Connect getConnect();
}
