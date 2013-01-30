package net.kkolyan.sqlexec.ui;

import net.kkolyan.sqlexec.api.Column;

public class ColumnsModel extends ListModel<Column> {

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
			case 0: return "name";
			case 1: return "type";
			case 2: return "autoinc";
			case 3: return "nullable";
		}
		System.out.println(column);
		throw new ArrayIndexOutOfBoundsException(column);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0: return String.class;
			case 1: return String.class;
			case 2: return Boolean.class;
			case 3: return Boolean.class;
		}
		System.out.println(columnIndex);
		throw new ArrayIndexOutOfBoundsException(columnIndex);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Column o = getList().get(rowIndex);
		switch (columnIndex) {
			case 0: return o.getName();
			case 1: return o.getTypeName()+"("+o.getSize()+")";
			case 2: return o.isAutoincrement();
			case 3: return o.isNullable();
		}
		System.out.println(columnIndex);
		throw new ArrayIndexOutOfBoundsException(columnIndex);
	}
}
