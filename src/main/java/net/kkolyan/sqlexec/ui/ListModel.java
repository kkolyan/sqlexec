package net.kkolyan.sqlexec.ui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.Collections;
import java.util.List;

public abstract class ListModel<T> extends AbstractTableModel {
	private List<T> list = Collections.emptyList();

	public void setList(List<T> list) {
		this.list = list;
	}
	
	public List<T> getList() {
		return list;
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public abstract String getColumnName(int column);

	@Override
	public abstract Class<?> getColumnClass(int columnIndex);
}
