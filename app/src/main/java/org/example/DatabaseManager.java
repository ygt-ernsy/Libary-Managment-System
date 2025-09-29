package org.example;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DatabaseManager
 */
public class DatabaseManager {

	public void saveBook(Book book) throws SQLException {
		/*
		 * The ? are placeholders for the actual values
		 * Using placeholders prevents SQL injection and allows you to reuse the
		 * statement with different values.
		 */
		String sql = "INSERT INTO books (title, author, is_available) VALUES (?, ?, ?)";

		try ( // Establish connection to sql database
				Connection connection = getConnection();

				// PreparedStatement allows you to safely insert dynamic values in place of the
				// ? placeholders
				PreparedStatement statement = connection.prepareStatement(sql);) {

			statement.setString(1, book.getTitle());
			statement.setString(2, book.getAuthor());
			statement.setBoolean(3, book.isAvaible());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void saveMember(Member member) throws SQLException {

		String sql = "INSERT INTO members (name) VALUE (?)";

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, member.getName());

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public List<Book> getAllBooks() throws SQLException {

		List<Book> books = new ArrayList<>();

		// Retrieves all columns needed to construct a Book
		String sql = "SELECT id, title, author, is_available FROM books";

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery();) {

			/*
			 * executeQuery() is used for SELECT statements.
			 * Returns a ResultSet, which is essentially a table of results from the
			 * database
			 */

			// Loop over resultset
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String title = resultSet.getString("title");
				String author = resultSet.getString("author");
				boolean isAvailable = resultSet.getBoolean("is_available");

				books.add(new Book(id, title, author, isAvailable));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return books;
	}

	public List<Member> getAllMembers() {

		List<Member> members = new ArrayList<>();

		String sql = "SELECT id, name FROM members";

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery();) {

			// TODO: Do this

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private Connection getConnection() throws SQLException {

		try {
			String url = "jdbc:postgresql://localhost:5432/mylibrary";
			String name = "myuser";
			String password = "mypassword";
			Connection connection = DriverManager.getConnection(url, name, password);

			return connection;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
