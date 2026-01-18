package edu.aitu.oop3.repositories;


import edu.aitu.oop3.entities.ClassBooking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingRepositoryJdbc implements BookingRepository {
    private final Connection connection;

    public BookingRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean createIfNotExists(int memberId, int classId) {
        String sql = """
            INSERT INTO class_bookings (member_id, class_id)
            VALUES (?, ?)
            ON CONFLICT (member_id, class_id) DO NOTHING
            """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);
            stmt.setInt(2, classId);
            return stmt.executeUpdate() == 1; // true если вставилось, false если уже было
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create booking: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ClassBooking> findByMemberId(int memberId) {
        String sql = """
            SELECT id, member_id, class_id
            FROM class_bookings
            WHERE member_id = ?
            ORDER BY id DESC
            """;

        List<ClassBooking> bookings = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, memberId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(new ClassBooking(
                            rs.getInt("id"),
                            rs.getInt("member_id"),
                            rs.getInt("class_id")
                    ));
                }
            }
            return bookings;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read bookings: " + e.getMessage(), e);
        }
    }

    @Override
    public int countByClassId(int classId) {
        String sql = "SELECT COUNT(*) AS cnt FROM class_bookings WHERE class_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, classId);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count bookings: " + e.getMessage(), e);
        }
    }
}
