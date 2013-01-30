package net.kkolyan.sqlexec.impl;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import net.kkolyan.sqlexec.Utils;
import net.kkolyan.sqlexec.api.Result;
import net.kkolyan.sqlexec.api.TableSpace;
import net.kkolyan.sqlexec.api.Vendor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author n.plekhanov
 */
public class MySqlConnect extends AbstractConnect {
    private SingleConnectionDataSource dataSource;

	private String name;

	@Override
	public String getName() {
		return name;
	}

	public MySqlConnect(NewConnectionData data) {
		SingleConnectionDataSource ds;
		{
			ds = new SingleConnectionDataSource();
			ds.setUrl(Vendor.MySQL.generateUrl(
					data.getHost(),
					data.getPort(),
					data.getDatabase()
			));
			ds.setUsername(data.getUsername());
			ds.setPassword(String.valueOf(data.getPassword()));
			ds.setDriverClassName(data.getVendor().getDriverClass().getName());
		}
		dataSource = ds;
		name = data.getConnectionName();
		name = name.trim();
		if (name.isEmpty()) {
			name = "Unnamed MySQL connection";
		}
    }

	@Override
	public <T> Result<T> execute(String sql, TableSpace tableSpace, RowMapper<T> mapper) throws SQLException {
		if (tableSpace != null) {
			execute("use "+tableSpace.getName(), tableSpace);
		}
		return super.execute(sql, tableSpace, mapper);
	}

	@Override
	protected DataSource getDataSource(TableSpace tableSpace) {
        return dataSource;
    }

    @Override
    public void close() throws SQLException {
		dataSource.destroy();
    }

	@Override
    public List<TableSpace> getTableSpaces() throws SQLException {
		DatabaseMetaData metaData = getDataSource(null).getConnection().getMetaData();

		List<String> catalogs = Utils.getData(metaData.getCatalogs(), new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("TABLE_CAT");
			}
		});

		List<TableSpace> tableSpaces = new ArrayList<TableSpace>();

		for (String catalog: catalogs) {
			List<String> schemas = Utils.getData(metaData.getSchemas(), new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString("TABLE_SCHEM");
				}
			});

			if (schemas.isEmpty()) {
				tableSpaces.add(new TableSpaceImpl(this, catalog, null));
			} else {
				for (String schema: schemas) {
					tableSpaces.add(new TableSpaceImpl(this, catalog, schema));
				}
			}
		}

		return tableSpaces;
    }

    @Override
    public Vendor getVendor() {
        return Vendor.MySQL;
    }
}
