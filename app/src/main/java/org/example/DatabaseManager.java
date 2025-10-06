package org.example;

import java.util.ArrayList;
import java.util.List;
import org.example.model.Member;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DatabaseManager
 */
public class DatabaseManager {

	private Connection getConnection() throws SQLException {

		String url = "jdbc:postgresql://localhost:5432/mylibrary";
		String name = "myuser";
		String password = "mypassword";
		Connection connection = DriverManager.getConnection(url, name, password);

		return connection;
	}
}
