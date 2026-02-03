
package co.istad.jdbc.project.service.impl;

import co.istad.jdbc.project.dao.PetDao;
import co.istad.jdbc.project.dao.impl.PetDaoImpl;
import co.istad.jdbc.project.model.Pet;
import co.istad.jdbc.project.service.PetService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PetServiceImpl implements PetService {

    private final PetDao petDao;

    public PetServiceImpl() {
        petDao = new PetDaoImpl();
    }

    @Override
    public List<Pet> findAll() {
        try {
            return petDao.findAll();
        }catch (SQLException e){
           throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void save(Pet pet) {
        try {
            if (pet.getPetId() != null) {
                Optional<Pet> existing = petDao.findByCode(pet.getPetId());
                if (existing.isPresent() && !existing.get().getDeleted()) {
                    throw new RuntimeException("Pet ID already exists! Delete the old pet first.");
                }
            }

            int affectedRow = petDao.save(pet);
            if (affectedRow < 1)
                throw new RuntimeException("Save operation failed");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void updateById(Integer petId, Pet pet) {
        try {
            Pet existingPet = getPetById(petId);
            if (existingPet.getDeleted()) {
                throw new RuntimeException("Cannot update. This pet has been deleted!");
            }
            if (!pet.getName().isBlank()) existingPet.setName(pet.getName());
            if (pet.getType() != null && !pet.getType().isBlank()) existingPet.setType(pet.getType());
            if (pet.getBreed() != null && !pet.getBreed().isBlank()) existingPet.setBreed(pet.getBreed());
            if (pet.getOwnerPhone() != null && !pet.getOwnerPhone().isBlank()) existingPet.setOwnerPhone(pet.getOwnerPhone());

            int affectedRow = petDao.updateById(petId, existingPet);
            if (affectedRow < 1) throw new RuntimeException("Update operation failed");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public Pet getPetById(Integer petId) {
        try {
            return petDao.findByCode(petId)
                    .orElseThrow(() -> new RuntimeException("Pet ID is invalid..!"));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer petId) {
        try {
            // Check if pet exists
            Pet existingPet = getPetById(petId);
            if (existingPet.getDeleted()) {
                throw new RuntimeException("Pet is already deleted!");
            }

            int affectedRow = petDao.softDeleteById(petId);
            if (affectedRow < 1) {
                throw new RuntimeException("Delete operation failed");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public List<Pet> searchPets(String keyword) {
        return petDao.searchPets(keyword);
    }

    @Override
    public List<Pet> findAll(int page, int size) {
        try {
            int offset = (page - 1) * size;
            return petDao.findAll(size, offset);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int countAll() {
        try {
            return petDao.countAll();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Pet getPetByIdIncludeDeleted(Integer petId) {
        return null;
    }
}



