package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Member;
import edu.aitu.oop3.repositories.MemberRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRepositoryJdbc implements MemberRepository {
    private final Connection connection;

    public MemberRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Member create(Member member) {
        String sql = """
            INSERT INTO members (full_name, email, membership_type_id, membership_start, membership_end)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, member.getName()); // в объекте name, в БД full_name
            stmt.setString(2, member.getEmail());

            if (member.getMembershipTypeId() == null) {
                stmt.setNull(3, Types.BIGINT);
            } else {
                stmt.setLong(3, member.getMembershipTypeId());
            }

            stmt.setString(4, member.getMembershipStart());
            stmt.setString(5, member.getMembershipEnd());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    member.setId(rs.getLong("id"));
                    return member;
                }
                throw new RuntimeException("Failed to create member: no id returned.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create member: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = """
            SELECT id, full_name, email, membership_type_id, membership_start, membership_end
            FROM members
            ORDER BY id
            """;

        List<Member> members = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object obj = rs.getObject("membership_type_id");
                Long mt = (obj == null) ? null : ((Number) obj).longValue();

                members.add(new Member(
                        rs.getLong("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        mt,
                        rs.getString("membership_start"),
                        rs.getString("membership_end")
                ));
            }
            return members;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to read members: " + e.getMessage(), e);
        }
    }

    @Override
    public Member findById(long id) {
        String sql = """
            SELECT id, full_name, email, membership_type_id, membership_start, membership_end
            FROM members
            WHERE id = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;

                Object obj = rs.getObject("membership_type_id");
                Long mt = (obj == null) ? null : ((Number) obj).longValue();

                return new Member(
                        rs.getLong("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        mt,
                        rs.getString("membership_start"),
                        rs.getString("membership_end")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find member: " + e.getMessage(), e);
        }
    }
}
