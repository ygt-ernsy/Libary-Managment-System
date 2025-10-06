package org.example.repository;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.example.DatabaseUtils;
import org.example.model.Member;

/**
 * MemberRepository
 */
public class MemberRepository {
	private DatabaseUtils databaseUtils;

	public MemberRepository() {
		this(null);
	}

	public MemberRepository(DatabaseUtils databaseUtils) {
		this.databaseUtils = databaseUtils;
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

		try (Connection connection = databaseUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			Member returnedMember = mapMemberWithId(id, statement);

			return returnedMember;
		}
	}

	public Member deleteMember(long id) throws SQLException {

		Member deletedMember = getMemberById(id);

		String deleteSql = "DELETE FROM members WHERE id = ?";

		try (Connection connection = databaseUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(deleteSql)) {

			statement.setLong(1, id);
			statement.executeUpdate();

			return deletedMember;
		}
	}

	public void saveMember(Member member) throws SQLException {

		String sql = "INSERT INTO members (name) VALUES (?)";

		try (Connection connection = databaseUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, member.getName());
			statement.executeUpdate();

		}
	}

	public List<Member> getAllMembers() throws SQLException {

		List<Member> members = new ArrayList<>();

		String sql = "SELECT id, name FROM members";

		try (Connection connection = databaseUtils.getConnection();
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
}
