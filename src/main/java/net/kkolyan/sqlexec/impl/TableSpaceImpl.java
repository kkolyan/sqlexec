package net.kkolyan.sqlexec.impl;

import net.kkolyan.sqlexec.Utils;
import net.kkolyan.sqlexec.api.Table;
import net.kkolyan.sqlexec.api.TableSpace;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TableSpaceImpl implements TableSpace {
	private String schema;
	private String catalog;
	private AbstractConnect connect;
	private String name;

	public TableSpaceImpl(AbstractConnect connect, String catalog, String schema) {
		this.catalog = catalog;
		this.connect = connect;
		this.schema = schema;

		if (schema == null || schema.isEmpty()) {
			name = catalog;
		} else if (catalog == null || catalog.isEmpty()) {
			name = schema;
		} else {
			name = catalog + "." + schema;
		}
	}

	@Override
	public List<Table> getTables() throws SQLException {
        DataSource dataSource = connect.getDataSource(this);
        ResultSet rs = dataSource.getConnection().getMetaData().getTables(catalog, schema, null, new String[] {"TABLE"});

        return Utils.getData(rs, new RowMapper<Table>() {
            @Override
            public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new TableImpl(rs, TableSpaceImpl.this);
            }
        });
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public AbstractConnect getConnect() {
		return connect;
	}
}
