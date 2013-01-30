package net.kkolyan.sqlexec.impl;

import net.kkolyan.sqlexec.InputDataException;
import net.kkolyan.sqlexec.api.Connect;
import net.kkolyan.sqlexec.api.Manager;
import net.kkolyan.sqlexec.api.Vendor;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author n.plekhanov
 */
public class ManagerImpl implements Manager {
    private Map<Integer, DataSource> dataSourceMap = new HashMap<Integer, DataSource>();
    private List<Connect> connects = new ArrayList<Connect>();
    private Properties drivers = new Properties();
	private static final AtomicInteger idGenerator = new AtomicInteger(0);

    public ManagerImpl() throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("drivers.ini");
        drivers.load(inputStream);
        inputStream.close();
    }

    public DataSource getDataSource(ConnectImpl connect) {
        DataSource dataSource = dataSourceMap.get(connect.getId());
        if (dataSource == null) {
			throw new IllegalArgumentException("dataSource not found for connect "+connect);
        }
        return dataSource;
    }

	@Override
	public Connect openConnect(String url, String user, String password) throws InputDataException {
        Vendor vendor = Vendor.resolve(url);
		DataSource dataSource = newDataSource(url, user, password, vendor);
		int connectID = idGenerator.incrementAndGet();
		dataSourceMap.put(connectID, dataSource);
		Connect connect = new ConnectImpl(connectID, this, url, user, null);
		connects.add(connect);
		return connect;
	}

	public void closeConnect(ConnectImpl connect) throws SQLException {
		DataSource dataSource = dataSourceMap.get(connect.getId());

		dataSource.getConnection().close();

		dataSourceMap.remove(connect.getId());
		connects.remove(connect);
	}

    @Override
    public List<Connect> getConnects() {
        return Collections.unmodifiableList(connects);
    }

    private DataSource newDataSource(String url, String user, String password, Vendor vendor) throws InputDataException {
        SingleConnectionDataSource dataSource;
        {
            dataSource = new SingleConnectionDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(user);
            dataSource.setPassword(password);
            dataSource.setDriverClassName(vendor.getDriverClass().getName());
        }
        return dataSource;
    }

}
