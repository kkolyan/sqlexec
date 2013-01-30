package net.kkolyan.sqlexec.api;

/**
 * @author n.plekhanov
 */
public interface Column {
    Table getTable();

    String getName();

    int getDataType();

    String getTypeName();

    int getSize();

    boolean isNullable();

    int getOrdinalPosition();

    boolean isAutoincrement();
}
