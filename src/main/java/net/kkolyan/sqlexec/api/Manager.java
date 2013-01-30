package net.kkolyan.sqlexec.api;

import net.kkolyan.sqlexec.InputDataException;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author n.plekhanov
 */
public interface Manager {
    List<Connect> getConnects();
	Connect openConnect(String url, String user, String password) throws InputDataException;
}
