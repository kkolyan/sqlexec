package net.kkolyan.sqlexec.ui;

import net.kkolyan.sqlexec.api.Table;

public class TablesModel extends ListModel<Table> {

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public String getColumnName(int column) {
		return "name";
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return getList().get(rowIndex).getName();
	}
}
