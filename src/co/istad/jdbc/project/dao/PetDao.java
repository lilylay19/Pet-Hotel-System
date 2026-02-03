package co.istad.jdbc.project.dao;

import co.istad.jdbc.project.model.Pet;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PetDao {
    List<Pet> findAll() throws SQLException;
    int save(Pet pet) throws SQLException;
    Optional<Pet> findByCode(Integer petId) throws SQLException;
    int updateById(Integer petId, Pet pet) throws SQLException;
    int deleteById(Integer petId) throws SQLException;
    int softDeleteById(Integer petId) throws SQLException;
    boolean existsById(Integer petId) throws SQLException;
    List<Pet> searchPets(String keyword);
    List<Pet> findAll(int limit, int offset) throws SQLException;
    int countAll() throws SQLException;


}
