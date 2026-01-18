package edu.aitu.oop3.repositories;

import edu.aitu.oop3.db.IDB;
import edu.aitu.oop3.entities.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberRepositoryJdbc implements MemberRepository {

    private final IDB db;

    public MemberRepositoryJdbc(IDB db) {
        this.db = db;
    }

    @Override
    public Member create(Member member) {
        String sql = "insert into members (name, email) values (?, ?) returning id";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, member.getName());
            st.setString(2, member.getEmail());

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    member.setId(rs.getLong("id"));
                }
            }
            return member;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create member: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select id, name, email from members order by id";
        List<Member> members = new ArrayList<>();

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                members.add(new Member(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
            return members;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to list members: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Member> findById(long id) {
        String sql = "select id, name, email from members where id = ?";

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setLong(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                return Optional.of(new Member(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find member: " + e.getMessage(), e);
        }
    }
}
