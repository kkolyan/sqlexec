package net.kkolyan.sqlexec.ui;

import net.kkolyan.sqlexec.api.Connect;
import net.kkolyan.sqlexec.api.Manager;
import net.kkolyan.sqlexec.api.Vendor;
import net.kkolyan.sqlexec.impl.ConnectImpl;
import net.kkolyan.sqlexec.impl.MySqlConnect;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.net.PasswordAuthentication;

public class Model {
    private Manager manager;

    public Connect newConnect(Vendor vendor, String host, int port, String database, PasswordAuthentication auth) {
        String url = vendor.generateUrl(host, port, database);
        SingleConnectionDataSource dataSource;
        {
            dataSource = new SingleConnectionDataSource();
            dataSource.setDriverClassName(vendor.getDriverClass().getName());
            dataSource.setUsername(auth.getUserName());
            dataSource.setPassword(new String(auth.getPassword()));
            dataSource.setUrl(url);
        }
        switch (vendor) {
//            case MySQL: return new MySqlConnect(url, dataSource);
        }
        throw new Error("db not supported: "+vendor);
    }
}
