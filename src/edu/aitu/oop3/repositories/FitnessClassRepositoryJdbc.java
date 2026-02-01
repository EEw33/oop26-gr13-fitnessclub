package edu.aitu.oop3.repositories;

import edu.aitu.oop3.entities.FitnessClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FitnessClassRepositoryJdbc implements FitnessClassRepository {

    private final Connection connection;

    public FitnessClassRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public FitnessClass create(FitnessClass entity) {
        throw new UnsupportedOperationException("Not required for milestone");
    }

    @Override
    public FitnessClass findById(Integer id) {
        if (id == null) return null;

        String sql = "SELECT id, name, capacity FROM fitness_classes WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;

                return new FitnessClass(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("capacity")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find class", e);
        }
    }

    @Override
    public List<FitnessClass> findAll() {
        List<FitnessClass> classes = new ArrayList<>();
        String sql = "SELECT id, name, capacity FROM fitness_classes";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                classes.add(new FitnessClass(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("capacity")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch classes", e);
        }

        return classes;
    }
}
