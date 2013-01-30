package net.kkolyan.sqlexec.api;

import java.util.List;

public interface Result<T> {
	List<String> getColumnNames();
	List<T> getList();
}
