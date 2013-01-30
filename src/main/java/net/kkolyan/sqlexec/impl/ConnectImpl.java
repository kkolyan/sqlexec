package net.kkolyan.sqlexec.impl;

import net.kkolyan.sqlexec.Utils;
import net.kkolyan.sqlexec.api.Result;
import net.kkolyan.sqlexec.api.TableSpace;
import net.kkolyan.sqlexec.api.Vendor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author n.plekhanov
 */
public class ConnectImpl extends AbstractConnect {
	private int id;
    private ManagerImpl manager;
    private String url;
	private String user;
    private Vendor vendor;

    public ConnectImpl(int id, ManagerImpl manager, String url, String user, Vendor vendor) {
		this.id = id;
		this.manager = manager;
		this.url = url;
		this.user = user;
        this.vendor = vendor;
	}

	@Override
	public String getName() {
		return user;
	}

	@Override
	public void close() throws SQLException {
		manager.closeConnect(this);
	}

    @Override
    public DataSource getDataSource(TableSpace tableSpace) {
        return manager.getDataSource(this);
    }

    public int getId() {
		return id;
	}

	@Override
	public <T> Result<T> execute(String sql, TableSpace tableSpace, RowMapper<T> mapper) throws SQLException {
		ResultSet rs = createStatement().executeQuery(sql);
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

	private Statement createStatement() throws SQLException {
		Statement statement = getDataSource(null).getConnection().createStatement();
//		statement.setFetchSize(100);
		return statement;
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
        return vendor;
    }
}
