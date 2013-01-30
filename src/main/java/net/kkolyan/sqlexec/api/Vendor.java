package net.kkolyan.sqlexec.api;

import net.kkolyan.sqlexec.InputDataException;
import net.kkolyan.sqlexec.impl.MySqlConnect;
import net.kkolyan.sqlexec.impl.NewConnectionData;

import java.sql.Driver;

/**
 * @author n.plekhanov
 */
public enum Vendor {
    MySQL,
    PostgreSQL;

    public boolean isUseDatabaseSupported() {
        switch (this) {
            case MySQL: return true;
            case PostgreSQL: return false;
        }
        throw notImpl();
    }

    public Class<? extends Driver> getDriverClass() {
        switch (this) {
            case MySQL: return com.mysql.jdbc.Driver.class;
            case PostgreSQL: return org.postgresql.Driver.class;
        }
        throw notImpl();
    }

    public static Vendor resolve(String url) throws InputDataException {
        if (!url.startsWith("jdbc:")) {
            throw new InputDataException("wrong primary schema. url: "+url);
        }
        String[] urlParts = url.split(":");
        if (urlParts.length < 2) {
            throw new InputDataException("wrong url: "+url);
        }
        String subSchema = urlParts[1];
        
        if (subSchema.equals("mysql")) return MySQL;
        if (subSchema.equals("postgresql")) return PostgreSQL;
        
        throw new InputDataException("db not supported: "+subSchema);
    }

    public int getDefaultPort() {
        switch (this) {
            case MySQL: return 3306;
            case PostgreSQL: return 5432;
        }
        throw notImpl();
    }

	public String getDefaultUsername() {
		switch (this) {
			case MySQL: return "root";
			case PostgreSQL: return "postgres";
		}
		throw notImpl();
	}

    public String generateUrl(String host, int port, String database) {
        if (database == null) {
            database = "";
        }
        if (port == -1) {
            port = getDefaultPort();
        }
        switch (this) {
            case MySQL: return String.format("jdbc:mysql://%s:%s/%s", host, port, database);
            case PostgreSQL: return String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
        }
        throw notImpl();
    }

    private RuntimeException notImpl() {
        return new RuntimeException("not implemented: "+this);
    }

	public Connect newConnect(NewConnectionData data) {
		switch (this) {
			case MySQL: return new MySqlConnect(data);
			case PostgreSQL: throw notImpl();
		}
		throw notImpl();
	}
}
