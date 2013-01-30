package net.kkolyan.sqlexec.ui;

import net.kkolyan.sqlexec.api.TableSpace;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import java.util.Collections;
import java.util.List;

public class TableSpaceComboBoxModel extends AbstractListModel implements ComboBoxModel{
	private List<TableSpace> list = Collections.emptyList();
	private String selectedName;

	public List<TableSpace> getList() {
		return list;
	}

	public void setList(List<TableSpace> list) {
		this.list = list;
	}

	//=======================================

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public Object getElementAt(int index) {
		return list.get(index).getName();
	}

	@Override
	public void setSelectedItem(Object anItem) {
		selectedName = (String) anItem;
	}

	@Override
	public Object getSelectedItem() {
		return selectedName;
	}
}
