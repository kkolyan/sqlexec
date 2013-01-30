package net.kkolyan.sqlexec.impl;

import net.kkolyan.sqlexec.api.Result;

import java.util.List;

public class ResultImpl<T> implements Result<T> {
	private List<String> columnNames;
	private List<T> list;

	public ResultImpl(List<String> columnNames, List<T> list) {
		this.columnNames = columnNames;
		this.list = list;
	}

	@Override
	public List<String> getColumnNames() {
		return columnNames;
	}

	@Override
	public List<T> getList() {
		return list;
	}
}
