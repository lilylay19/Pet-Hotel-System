package co.istad.jdbc.project.service;

import co.istad.jdbc.project.model.Pet;

import java.util.List;

public interface PetService {
    List <Pet> findAll();
    void save (Pet pet);
    void updateById(Integer petId, Pet pet);
    Pet getPetById(Integer petId);
    void deleteById(Integer petId);
    List<Pet> searchPets(String keyword);
    List<Pet> findAll(int page, int size);
    int countAll();
    Pet getPetByIdIncludeDeleted(Integer petId);
}
