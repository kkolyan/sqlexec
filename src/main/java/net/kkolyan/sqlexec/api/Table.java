package net.kkolyan.sqlexec.api;

import java.sql.SQLException;
import java.util.List;

/**
 * @author n.plekhanov
 */
public interface Table {
    String getName();
    List<Column> getColumns() throws SQLException;
    TableSpace getTableSpace();
}
