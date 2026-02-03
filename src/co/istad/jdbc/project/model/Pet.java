package co.istad.jdbc.project.model;

import java.time.LocalDate;


public class Pet {
    private Integer petId;
    private String name;
    private String type;
    private String breed;
    private String ownerPhone;
    private LocalDate createdAt;
    private Boolean isDeleted;

    public Pet(){};
    public Pet(Integer petId, String name, String type, String breed, String ownerPhone, LocalDate createdAt, Boolean isDeleted) {
        this.petId = petId;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.ownerPhone = ownerPhone;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    public Pet(String name, String type, String breed, String ownerPhone, LocalDate createdAt, Boolean isDeleted) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.ownerPhone = ownerPhone;
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petId=" + petId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", breed='" + breed + '\'' +
                ", ownerPhone='" + ownerPhone + '\'' +
                ", createdAt=" + createdAt +
                ", isDeleted=" + isDeleted +
                '}';
    }


    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}

