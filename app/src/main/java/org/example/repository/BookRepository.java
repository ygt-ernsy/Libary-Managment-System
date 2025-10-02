package org.example.repository;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * BookRepository
 */
public class BookRepository {
	DatabaseUtils databaseUtils;

	public BookRepository() {
		this(null);
	}

	public BookRepository(DatabaseUtils databaseUtils) {
		this.databaseUtils = databaseUtils;
	}

	public void saveBook(Book book) throws SQLException {
		/*
		 * The ? are placeholders for the actual values
		 * Using placeholders prevents SQL injection and allows you to reuse the
		 * statement with different values.
		 */
		String sql = "INSERT INTO books (title, author, is_available) VALUES (?, ?, ?)";

		try ( // Establish connection to sql database
				Connection connection = databaseUtils.getConnection();

				// PreparedStatement allows you to safely insert dynamic values in place of the
				// ? placeholders
				PreparedStatement statement = connection.prepareStatement(sql);) {

			statement.setString(1, book.getTitle());
			statement.setString(2, book.getAuthor());
			statement.setBoolean(3, book.isAvailable());

			statement.executeUpdate();
		}

	}

	public Book getBookById(long id) throws SQLException {

		String sql = "SELECT title, author, is_available FROM books WHERE id = ?";

		try (Connection connection = databaseUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			return mapBookWithId(id, statement);

		}
	}

	private Book mapBookWithId(long id, PreparedStatement statement) throws SQLException {

		statement.setLong(1, id);

		try (ResultSet resultSet = statement.executeQuery();) {

			if (!resultSet.next()) {
				throw new SQLException("Book with " + id + " does not exist!");
			}

			String title = resultSet.getString("title");
			String author = resultSet.getString("author");
			boolean isAvailable = resultSet.getBoolean("is_available");

			return new Book(id, title, author, isAvailable);
		}
	}

	public List<Book> getAllBooks() throws SQLException {

		java.util.List<Book> books = new ArrayList<>();

		// Retrieves all columns needed to construct a Book
		String sql = "SELECT id, title, author, is_available FROM books";

		try (Connection connection = databaseUtils.getConnection();
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

				Book book = new Book(id, title, author, isAvailable);
				books.add(book);
			}
		}
		return books;
	}

	public void updateBookAuthor(long id, String author) throws SQLException {

		String updateSql = "UPDATE books SET author = ? WHERE id = ?";

		try (Connection connection = databaseUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(updateSql);) {

			statement.setString(1, author);
			statement.setLong(2, id);

			statement.executeUpdate();

		}
	}

	public void updateBookTitle(long id, String title) throws SQLException {

		String updateSql = "UPDATE books SET title = ? WHERE id = ?";

		try (Connection connection = databaseUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(updateSql);) {

			statement.setString(1, title);
			statement.setLong(2, id);

			statement.executeUpdate();

		}
	}

	public Book deleteBook(long id) throws SQLException {

		Book deletedBook = getBookById(id);

		String deleteSql = "DELETE FROM books WHERE id = ?";

		try (Connection connection = databaseUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(deleteSql)) {

			statement.setLong(1, id);
			statement.executeUpdate();

			return deletedBook;
		}
	}

	public void markBookUnavailable(long id) throws SQLException {

		String updateSql = "UPDATE books SET is_available = false WHERE id = ?";

		try (Connection connection = databaseUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(updateSql);) {

			statement.setLong(1, id);

			statement.executeUpdate();
		}
	}

	public void markBookAvailable(long id) throws SQLException {

		String updateSql = "UPDATE books SET is_available = true WHERE id = ?";

		try (Connection connection = databaseUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(updateSql);) {

			statement.setLong(1, id);

			statement.executeUpdate();

		}
	}

	public boolean isBookAvailable(long id) throws SQLException {

		String checkSql = "SELECT is_available FROM books WHERE id = ?";

		try (Connection connection = databaseUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(checkSql);) {

			statement.setLong(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {

				if (!resultSet.next()) {
					throw new SQLException("Book with " + id + " id does not exist!");
				}

				boolean isAvailable = resultSet.getBoolean("is_available");

				return isAvailable;
			}
		}
	}
}
