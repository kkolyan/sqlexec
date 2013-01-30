package net.kkolyan.sqlexec;

import org.postgresql.PGConnection;
import org.postgresql.core.BaseConnection;
import org.postgresql.jdbc4.Jdbc4Connection;
import org.postgresql.jdbc4.Jdbc4DatabaseMetaData;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import java.sql.*;

/**
 * @author n.plekhanov
 */
public class Exps {
    static Connection conn;
    public static void main(String[] args) throws SQLException {
//        conn = DriverManager.getConnection("jdbc:mysql://kkolyan.net/top", "kkolyan", "123123");
        conn = DriverManager.getConnection("jdbc:postgresql://handmag.ru/", "postgres", "trafictrafic");

        try {
			{
//				Jdbc4Connection pgConn = (Jdbc4Connection) conn;
//				Jdbc4DatabaseMetaData md = (Jdbc4DatabaseMetaData) pgConn.getMetaData();
			}
            echo(conn.createStatement().executeQuery("SELECT datname FROM pg_database"));
            echo(conn.createStatement().executeQuery("SELECT * FROM malma.public.client limit 10"));
            echo(conn.getMetaData().getCatalogs());
            echo(conn.getMetaData().getSchemas());
            conn.createStatement();
        } catch (SQLException e) {
            throw e;
        } finally {
            conn.close();
        }
    }

    private static void echo(ResultSet rs) throws SQLException {
        ColumnMapRowMapper mapper = new ColumnMapRowMapper();
        Object o = Utils.getData(rs, mapper );
        System.out.println(o);
    }

}
