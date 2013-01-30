package net.kkolyan.sqlexec.ui;

import net.kkolyan.sqlexec.api.TableSpace;

public class TableSpaceModel extends ListModel<TableSpace> {

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public String getColumnName(int column) {
		return "name";
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return getList().get(rowIndex).getName();
	}
}
