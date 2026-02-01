package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());

            if (member.getMembershipTypeId() == null) {
                stmt.setNull(3, java.sql.Types.BIGINT);
            } else {
                stmt.setLong(3, member.getMembershipTypeId());
            }

            if (member.getMembershipStart() == null || member.getMembershipStart().isBlank()) {
                stmt.setNull(4, java.sql.Types.DATE);
            } else {
                stmt.setDate(4, java.sql.Date.valueOf(member.getMembershipStart()));
            }

            if (member.getMembershipEnd() == null || member.getMembershipEnd().isBlank()) {
                stmt.setNull(5, java.sql.Types.DATE);
            } else {
                stmt.setDate(5, java.sql.Date.valueOf(member.getMembershipEnd()));
            }


            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                member.setId(rs.getLong("id"));
            }
            return member;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create member", e);
        }
    }

    @Override
    public Member findById(Long id) {
        if (id == null) return null;

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
                        rs.getDate("membership_start") == null ? null : rs.getDate("membership_start").toString(),
                        rs.getDate("membership_end") == null ? null : rs.getDate("membership_end").toString()
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find member", e);
        }
    }

    @Override
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();

        String sql = """
                SELECT id, full_name, email, membership_type_id, membership_start, membership_end
                FROM members
                """;

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

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch members", e);
        }

        return members;
    }
}
