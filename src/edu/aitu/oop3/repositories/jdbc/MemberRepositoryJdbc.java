package edu.aitu.oop3.repositories.jdbc;

import edu.aitu.oop3.db.DatabaseConnection;
import edu.aitu.oop3.entities.Member;
import edu.aitu.oop3.repositories.MemberRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class MemberRepositoryJdbc implements MemberRepository {

    private final DatabaseConnection db;

    public MemberRepositoryJdbc(DatabaseConnection db) {
        this.db = db;
    }

    @Override
    public void update(Member member) {
        String sql = """
                UPDATE members
                SET name = ?, email = ?, membership_start = ?, membership_end = ?
                WHERE id = ?
                """;

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getMembershipStart()); // String date
            ps.setString(4, member.getMembershipEnd());   // String date
            ps.setLong(5, member.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update member: " + e.getMessage(), e);
        }
    }

    @Override
    public Member create(Member entity) {
        return null;
    }

    // keep your existing methods below (findById, findAll, save, etc)
    @Override
    public Member findById(Long id) { /* your code */ return Optional.empty(); }

    @Override
    public List<Member> findAll() { /* your code */ return new ArrayList<>(); }

    @Override
    public void save(Member member) { /* your code */ }
}
