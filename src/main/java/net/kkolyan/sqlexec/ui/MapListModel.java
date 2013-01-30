package net.kkolyan.sqlexec.ui;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MapListModel extends ListModel<Map<String,Object>> {
	private List<String> keys = Collections.emptyList();

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	@Override
	public void setList(List<Map<String, Object>> list) {
		super.setList(list);
	}

	@Override
	public int getColumnCount() {
		return keys.size();
	}

	@Override
	public String getColumnName(int column) {
		return keys.get(column);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Map<String,Object> row = getList().get(rowIndex);
		String columnName = keys.get(columnIndex);
		return row.get(columnName);
	}
}
