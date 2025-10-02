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

	public void saveMember(Member member) throws SQLException {

		String sql = "INSERT INTO members (name) VALUES (?)";

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, member.getName());
			statement.executeUpdate();

		}
	}

	public List<Member> getAllMembers() throws SQLException {

		List<Member> members = new ArrayList<>();

		String sql = "SELECT id, name FROM members";

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery();) {

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");

				Member member = new Member(id, name);
				members.add(member);
			}
		}
		return members;
	}

	public void borrowBook(long memberId, long bookId) throws SQLException {
		String updateBookSql = "UPDATE books SET is_available = false WHERE id = ? AND is_available = true;";

		try (Connection connection = getConnection()) {
			connection.setAutoCommit(false);

			try {
				try (PreparedStatement updateStatement = connection.prepareStatement(updateBookSql)) {
					updateStatement.setLong(1, bookId);
					int rowsAffected = updateStatement.executeUpdate();

					if (rowsAffected != 1) {
						connection.rollback();
						throw new SQLException("Book with ID " + bookId
								+ " is not available or does not exist.");
					}
				}

				addBorrows(memberId, bookId, connection);

				connection.commit();

			} catch (SQLException e) {
				connection.rollback();
				throw e;
			} finally {
				connection.setAutoCommit(true);
			}
		}
		// TODO: This is not fully done, It works though
	}

	public void returnBook(long memberId, long bookId) throws SQLException {
		String sqlBook = "UPDATE books SET is_available = true WHERE id = ?";
		String sqlBorrow = "UPDATE borrows SET return_date = CURRENT_TIMESTAMP " +
				"WHERE member_id = ? AND book_id = ? AND return_date IS NULL";

		try (Connection connection = getConnection()) {
			connection.setAutoCommit(false);

			try (
					PreparedStatement stmtBook = connection.prepareStatement(sqlBook);
					PreparedStatement stmtBorrow = connection.prepareStatement(sqlBorrow)) {
				// Update books
				stmtBook.setLong(1, bookId);
				stmtBook.executeUpdate();

				// Update borrows
				stmtBorrow.setLong(1, memberId);
				stmtBorrow.setLong(2, bookId);
				stmtBorrow.executeUpdate();

				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				throw e;
			} finally {
				connection.setAutoCommit(true);
			}
		}
	}

	private void addBorrows(long memberId, long bookId, Connection connection) throws SQLException {

		String sql = "INSERT INTO borrows (member_id, book_id) VALUES (?,?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setLong(1, memberId);
			statement.setLong(2, bookId);

			statement.executeUpdate();
		}
	}

	private Member mapMemberWithId(long id, PreparedStatement statement) throws SQLException {

		statement.setLong(1, id);

		try (ResultSet resultSet = statement.executeQuery();) {

			if (!resultSet.next()) {
				throw new SQLException("Member with " + id + " does not exist!");
			}

			String name = resultSet.getString("name");

			Member returnedMember = new Member(id, name);

			return returnedMember;

		}
	}

	public Member getMemberById(long id) throws SQLException {

		String sql = "SELECT name FROM members WHERE id = ?";

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			Member returnedMember = mapMemberWithId(id, statement);

			return returnedMember;
		}
	}

	public Member deleteMember(long id) throws SQLException {

		Member deletedMember = getMemberById(id);

		String deleteSql = "DELETE FROM members WHERE id = ?";

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(deleteSql)) {

			statement.setLong(1, id);
			statement.executeUpdate();

			return deletedMember;
		}
	}

	private Connection getConnection() throws SQLException {

		String url = "jdbc:postgresql://localhost:5432/mylibrary";
		String name = "myuser";
		String password = "mypassword";
		Connection connection = DriverManager.getConnection(url, name, password);

		return connection;
	}
}
