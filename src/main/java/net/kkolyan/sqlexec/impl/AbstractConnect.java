package net.kkolyan.sqlexec.impl;

import net.kkolyan.sqlexec.api.Connect;
import net.kkolyan.sqlexec.api.Result;
import net.kkolyan.sqlexec.api.TableSpace;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author n.plekhanov
 */
abstract class AbstractConnect implements Connect {

	protected abstract DataSource getDataSource(TableSpace tableSpace);

	protected void execute(String sql, TableSpace tableSpace) throws SQLException {
		getDataSource(tableSpace).getConnection().createStatement().execute(sql);
	}

	@Override
	public <T> Result<T> execute(String sql, TableSpace tableSpace, RowMapper<T> mapper) throws SQLException {
		ResultSet rs = getDataSource(tableSpace).getConnection().createStatement().executeQuery(sql);
		List<T> list = new ArrayList<T>();
		while (rs.next()) {
			list.add(mapper.mapRow(rs, rs.getRow()));
		}
		List<String> columnNames;
		{
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			columnNames = new ArrayList<String>(columnCount);
			for (int i = 0; i < columnCount; i ++) {
				columnNames.add(metaData.getColumnName(i+1));
			}
		}
		return new ResultImpl<T>(columnNames, list);
	}
}
