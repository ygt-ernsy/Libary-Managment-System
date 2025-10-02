package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseUtils
 */
public class DatabaseUtils {

	public Connection getConnection() throws SQLException {

		String url = "jdbc:postgresql://localhost:5432/mylibrary";
		String name = "myuser";
		String password = "mypassword";
		Connection connection = DriverManager.getConnection(url, name, password);

		return connection;
	}
}
