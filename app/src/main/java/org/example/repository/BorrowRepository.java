package org.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * BorrowRepository
 */
public class BorrowRepository {
	private Connection connection;

	public BorrowRepository(Connection connection) {
		this.connection = connection;
	}

	public void returnBook(long memberId, long bookId) throws SQLException {

		String sqlBorrow = "UPDATE borrows SET return_date = CURRENT_TIMESTAMP " +
				"WHERE member_id = ? AND book_id = ? AND return_date IS NULL";

		try (PreparedStatement stmtBorrow = connection.prepareStatement(sqlBorrow)) {

			// Update borrows
			stmtBorrow.setLong(1, memberId);
			stmtBorrow.setLong(2, bookId);
			int rows = stmtBorrow.executeUpdate();

			if (rows == 0) {
				throw new SQLException("No borrow record found for member with " + memberId
						+ " id and book with " + bookId + " id.");
			}
		}
	}

	public void addBorrow(long memberId, long bookId) throws SQLException {

		String sql = "INSERT INTO borrows (member_id, book_id) VALUES (?,?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, memberId);
			statement.setLong(2, bookId);

			int rows = statement.executeUpdate();

			if (rows == 0) {
				throw new SQLException("Failed to insert borrow record for member " + memberId
						+ " and book " + bookId);
			}
		}
	}
}
