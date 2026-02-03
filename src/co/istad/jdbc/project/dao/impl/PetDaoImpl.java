
package co.istad.jdbc.project.dao.impl;

import co.istad.jdbc.project.config.DBConnection;
import co.istad.jdbc.project.dao.PetDao;
import co.istad.jdbc.project.model.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PetDaoImpl implements PetDao {
    private final Connection conn;

    public PetDaoImpl() {
        conn = DBConnection.getInstance();
    }

    @Override
    public List<Pet> findAll() throws SQLException {
        // Only fetch pets that are not deleted
        final String SQL = "SELECT * FROM pets_info WHERE is_deleted = FALSE";

        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            ResultSet rs = pstmt.executeQuery();

            List<Pet> petLists = new ArrayList<>();
            while (rs.next()) {
                Pet pet = new Pet();
                pet.setPetId(rs.getInt("pet_id"));
                pet.setName(rs.getString("name"));
                pet.setType(rs.getString("type"));
                pet.setBreed(rs.getString("breed"));
                pet.setOwnerPhone(rs.getString("owner_contact"));
                pet.setCreatedAt(rs.getTimestamp("create_at").toLocalDateTime().toLocalDate());
                pet.setDeleted(rs.getBoolean("is_deleted"));
                petLists.add(pet);
            }

            return petLists;
        }
    }


    public int save(Pet pet) throws SQLException {
        final String SQL = """
                INSERT INTO pets_info( name, type, breed, owner_contact)
                VALUES (?, ?, ?, ?);
                """;

        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, pet.getName());
        pstmt.setString(2, pet.getType());
        pstmt.setString(3, pet.getBreed());
        pstmt.setString(4, pet.getOwnerPhone());
        return pstmt.executeUpdate();
    }
    @Override
    public Optional<Pet> findByCode(Integer petId) throws SQLException {
        final String SQL = "SELECT * FROM pets_info WHERE pet_id = ? AND is_deleted = FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, petId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Pet pet = new Pet();
                pet.setPetId(rs.getInt("pet_id"));
                pet.setName(rs.getString("name"));
                pet.setType(rs.getString("type"));
                pet.setBreed(rs.getString("breed"));
                pet.setOwnerPhone(rs.getString("owner_contact"));
                pet.setDeleted(rs.getBoolean("is_deleted"));
                return Optional.of(pet);
            }
        }
        return Optional.empty();
    }


    @Override
    public int updateById(Integer petId, Pet pet) throws SQLException {
        final String SQL = """
                UPDATE pets_info
                SET name = ?,type  = ?, breed = ?,owner_contact = ?
                WHERE pet_id = ?
                """;
        PreparedStatement pstmt = conn.prepareStatement(SQL);
        pstmt.setString(1, pet.getName());
        pstmt.setString(2, pet.getType());
        pstmt.setString(3, pet.getBreed());
        pstmt.setString(4, pet.getOwnerPhone());
        pstmt.setInt(5, petId);

        int affectedRow = pstmt.executeUpdate();
        System.out.println("Affected row: " + affectedRow);

        return affectedRow;

    }


    @Override
    public int deleteById(Integer petId) throws SQLException {
        return softDeleteById(petId);
    }

    @Override
    public int softDeleteById(Integer petId) throws SQLException {
        final String SQL = "UPDATE pets_info SET is_deleted = TRUE WHERE pet_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            pstmt.setInt(1, petId);
            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                System.out.println("No pet found with ID " + petId);
            }
            return affected;
        }
    }

    @Override
    public boolean existsById(Integer petId) throws SQLException {
        String sql = "SELECT 1 FROM pets_info WHERE pet_id = ? AND is_deleted = FALSE";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, petId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }


    @Override
    public List<Pet> searchPets(String keyword) {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets_info " +
                "WHERE is_deleted = FALSE " +
                "AND (name ILIKE ? OR type ILIKE ? OR breed ILIKE ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pet pet = new Pet();
                pet.setPetId(rs.getInt("pet_id"));
                pet.setName(rs.getString("name"));
                pet.setType(rs.getString("type"));
                pet.setBreed(rs.getString("breed"));
                pet.setOwnerPhone(rs.getString("owner_contact"));
                pet.setCreatedAt(rs.getTimestamp("create_at").toLocalDateTime().toLocalDate());
                pet.setDeleted(rs.getBoolean("is_deleted"));
                pets.add(pet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pets;
    }

    @Override
    public List<Pet> findAll(int limit, int offset) throws SQLException {
        String sql = """
        SELECT * FROM pets_info
        WHERE is_deleted = FALSE
        ORDER BY pet_id
        LIMIT ? OFFSET ?
    """;

        List<Pet> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Pet pet = new Pet();
                pet.setPetId(rs.getInt("pet_id"));
                pet.setName(rs.getString("name"));
                pet.setType(rs.getString("type"));
                pet.setBreed(rs.getString("breed"));
                pet.setOwnerPhone(rs.getString("owner_contact"));
                pet.setCreatedAt(rs.getTimestamp("create_at").toLocalDateTime().toLocalDate());
                pet.setDeleted(rs.getBoolean("is_deleted"));
                list.add(pet);
            }
        }
        return list;
    }


    @Override
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM pets_info WHERE is_deleted = FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
}






