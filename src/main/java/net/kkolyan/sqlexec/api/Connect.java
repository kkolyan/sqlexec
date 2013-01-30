package net.kkolyan.sqlexec.api;

import org.springframework.jdbc.core.RowMapper;

import java.sql.SQLException;
import java.util.List;

/**
 * @author n.plekhanov
 */
public interface Connect {
	void close() throws SQLException;

	String getName();
	<T> Result<T> execute(String sql, TableSpace tableSpace, RowMapper<T> mapper) throws SQLException;

	List<TableSpace> getTableSpaces() throws SQLException;

    Vendor getVendor();

}
