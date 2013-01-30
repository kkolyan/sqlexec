package net.kkolyan.sqlexec.impl;

import net.kkolyan.sqlexec.Utils;
import net.kkolyan.sqlexec.api.Column;
import net.kkolyan.sqlexec.api.Table;
import net.kkolyan.sqlexec.api.TableSpace;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author n.plekhanov
 */
public class TableImpl implements Table {
    private String name;
	private String catalog;
	private String schema;
	private TableSpaceImpl tableSpace;

    public TableImpl(ResultSet rs, TableSpaceImpl tableSpace) throws SQLException {
        name = rs.getString("TABLE_NAME");
        catalog = rs.getString("TABLE_CAT");
		schema = rs.getString("TABLE_SCHEM");
		this.tableSpace = tableSpace;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Column> getColumns() throws SQLException {
        DataSource dataSource = tableSpace.getConnect().getDataSource(tableSpace);
        ResultSet rs = dataSource.getConnection().getMetaData().getColumns(catalog, schema, name, null);

        return Utils.getData(rs, new RowMapper<Column>() {
            @Override
            public Column mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ColumnImpl(rs, TableImpl.this);
            }
        });
    }

	@Override
	public TableSpaceImpl getTableSpace() {
		return tableSpace;
	}
}
