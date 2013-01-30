package net.kkolyan.sqlexec.impl;

import net.kkolyan.sqlexec.api.Column;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author n.plekhanov
 */
public class ColumnImpl implements Column {
    private TableImpl table;
    private String name;
    private int dataType;
    private String typeName;
    private int size;
    private boolean nullable;
    private int ordinalPosition;
    private boolean autoincrement;

    public ColumnImpl(ResultSet rs, TableImpl table) throws SQLException {
        name = rs.getString("COLUMN_NAME");
        dataType = rs.getInt("DATA_TYPE");
        typeName = rs.getString("TYPE_NAME");
        size = rs.getInt("COLUMN_SIZE");
        nullable = rs.getBoolean("IS_NULLABLE");
        ordinalPosition = rs.getInt("ORDINAL_POSITION");
        autoincrement = rs.getBoolean("IS_AUTOINCREMENT");
        this.table = table;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDataType() {
        return dataType;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    @Override
    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    @Override
    public boolean isAutoincrement() {
        return autoincrement;
    }

    @Override
    public TableImpl getTable() {
        return table;
    }
}
