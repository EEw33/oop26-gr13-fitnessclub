package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.FitnessClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FitnessClassRepositoryJdbc implements FitnessClassRepository {
    private final Connection connection;

    public FitnessClassRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<FitnessClass> findAll() {
        String sql = "SELECT id, title, capacity FROM classes ORDER BY id";
        List<FitnessClass> classes = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                classes.add(new FitnessClass(
                        rs.getInt("id"),
                        rs.getString("title"),     // <-- было name, стало title
                        rs.getInt("capacity")
                ));
            }
            return classes;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to read classes: " + e.getMessage(), e);
        }
    }

    @Override
    public FitnessClass findById(int id) {
        String sql = "SELECT id, title, capacity FROM classes WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;

                return new FitnessClass(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("capacity")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find class: " + e.getMessage(), e);
        }
    }
}
