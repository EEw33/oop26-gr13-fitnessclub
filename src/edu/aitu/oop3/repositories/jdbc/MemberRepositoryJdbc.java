package edu.aitu.oop3.repositories.jdbc;

import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.entities.Member;
import edu.aitu.oop3.repositories.MemberRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRepositoryJdbc implements MemberRepository {

    private final DatabaseConnection db;

    public MemberRepositoryJdbc(DatabaseConnection db) {
        this.db = db;
    }

    @Override
    public Member create(Member entity) {
        String sql = """
            INSERT INTO members (name, email, membership_type_id, membership_start, membership_end)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id
            """;

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entity.getName());
            ps.setString(2, entity.getEmail());

            if (entity.getMembershipTypeId() == null) ps.setNull(3, Types.BIGINT);
            else ps.setLong(3, entity.getMembershipTypeId());

            ps.setString(4, entity.getMembershipStart());
            ps.setString(5, entity.getMembershipEnd());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    entity.setId(rs.getLong("id"));
                    return entity;
                }
                throw new RuntimeException("Create member failed: no id returned");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create member: " + e.getMessage(), e);
        }
    }

    @Override
    public Member findById(Long id) {
        if (id == null) return null;

        String sql = """
            SELECT id, name, email, membership_type_id, membership_start, membership_end
            FROM members
            WHERE id = ?
            """;

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Member m = new Member();
                m.setId(rs.getLong("id"));
                m.setName(rs.getString("name"));
                m.setEmail(rs.getString("email"));

                long mt = rs.getLong("membership_type_id");
                if (rs.wasNull()) m.setMembershipTypeId(null);
                else m.setMembershipTypeId(mt);

                m.setMembershipStart(rs.getString("membership_start"));
                m.setMembershipEnd(rs.getString("membership_end"));
                return m;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find member: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = """
            SELECT id, name, email, membership_type_id, membership_start, membership_end
            FROM members
            ORDER BY id
            """;

        List<Member> members = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Member m = new Member();
                m.setId(rs.getLong("id"));
                m.setName(rs.getString("name"));
                m.setEmail(rs.getString("email"));

                long mt = rs.getLong("membership_type_id");
                if (rs.wasNull()) m.setMembershipTypeId(null);
                else m.setMembershipTypeId(mt);

                m.setMembershipStart(rs.getString("membership_start"));
                m.setMembershipEnd(rs.getString("membership_end"));

                members.add(m);
            }

            return members;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to list members: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Member member) {
        String sql = """
            UPDATE members
            SET name = ?, email = ?, membership_type_id = ?, membership_start = ?, membership_end = ?
            WHERE id = ?
            """;

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());

            if (member.getMembershipTypeId() == null) ps.setNull(3, Types.BIGINT);
            else ps.setLong(3, member.getMembershipTypeId());

            ps.setString(4, member.getMembershipStart());
            ps.setString(5, member.getMembershipEnd());
            ps.setLong(6, member.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update member: " + e.getMessage(), e);
        }
    }

    @Override
    public void save(Member member) {
        // simple upsert behavior: if id==0 -> create, else update
        if (member.getId() == 0) create(member);
        else update(member);
    }
}
