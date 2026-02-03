package co.istad.jdbc.project;

import co.istad.jdbc.project.config.DBConnection;
import co.istad.jdbc.project.model.Pet;
import co.istad.jdbc.project.model.Appointment;
import co.istad.jdbc.project.service.PetService;
import co.istad.jdbc.project.service.AppointmentService;
import co.istad.jdbc.project.service.impl.PetServiceImpl;
import co.istad.jdbc.project.service.impl.AppointmentServiceImpl;
import co.istad.jdbc.project.dao.AppointmentDao;
import co.istad.jdbc.project.dao.impl.AppointmentDaoImpl;
import co.istad.jdbc.project.util.InputUtil;
import co.istad.jdbc.project.util.ViewUtil;

import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        DBConnection.init();

        PetService petService = new PetServiceImpl();
        AppointmentDao appointmentDao = new AppointmentDaoImpl();
        AppointmentService appointmentService = new AppointmentServiceImpl(appointmentDao, petService);

        while (true) {
            ViewUtil.printTopMenu();
            int choice = InputUtil.getInteger("Enter option");
            switch (choice) {
                case 1 -> managePets(petService);
                case 2 -> manageAppointments(petService, appointmentService);
                case 0 -> System.exit(0);
                default -> { ViewUtil.printHeader("Invalid option!"); pause(); }
            }
        }
    }

    private static void managePets(PetService petService) {
        boolean exit = false;
        while (!exit) {
            ViewUtil.printMenu();
            int option = InputUtil.getInteger("Enter option");
            switch (option) {
                case 1 -> listPets(petService);
                case 2 -> searchPets(petService);
                case 3 -> createPet(petService);
                case 4 -> updatePet(petService);
                case 5 -> deletePet(petService);
                case 0 -> exit = true;
                default -> { ViewUtil.printHeader("Invalid option!"); pause(); }
            }
        }
    }

    private static void listPets(PetService petService) {
        List<Pet> pets = petService.findAll();
        if (pets.isEmpty()) { ViewUtil.printHeader("No pets found!"); pause(); return; }

        int pageSize = 5, currentPage = 1;
        boolean paginateExit = false;
        while (!paginateExit) {
            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, pets.size());
            ViewUtil.printPetList(pets.subList(start, end));
            System.out.println("Page " + currentPage + " / " + ((pets.size() + pageSize - 1) / pageSize));
            System.out.println("[P] Previous  [N] Next  [Q] Back to Menu");
            String input = InputUtil.getText("Choose option").trim().toUpperCase();
            switch (input) {
                case "P" -> { if (currentPage > 1) currentPage--; else System.out.println("Already at first page!"); }
                case "N" -> { if (currentPage * pageSize < pets.size()) currentPage++; else System.out.println("Already at last page!"); }
                case "Q" -> paginateExit = true;
                default -> System.out.println("Invalid input!");
            }
        }
    }

    private static void searchPets(PetService petService) {
        ViewUtil.printHeader("Search pets");
        String keyword = InputUtil.getText("Enter keyword");
        List<Pet> pets = petService.searchPets(keyword);
        if (pets.isEmpty()) ViewUtil.printHeader("No pets found!");
        else ViewUtil.printPetList(pets);
        pause();
    }

    private static void createPet(PetService petService) {
        ViewUtil.printHeader("Add new pet");
        String name = InputUtil.getValidatedName("Enter name");
        String type = InputUtil.getValidatedType("Enter type");
        String breed = InputUtil.getValidatedBreed("Enter breed");
        String phone = InputUtil.getValidatedPhone("Enter phone number");

        Pet pet = new Pet(name, type, breed, phone, LocalDate.now(), false);
        petService.save(pet);
        ViewUtil.printHeader("Pet saved successfully!");
        pause();
    }

    private static void updatePet(PetService petService) {
        ViewUtil.printHeader("Update pet by ID");
        int petId = InputUtil.getInteger("Enter pet ID");
        Pet existing = petService.getPetById(petId);
        if (existing == null) { System.out.println("Pet not found!"); return; }

        String name = InputUtil.getOptionalValidatedName("Enter name", existing.getName());
        String type = InputUtil.getOptionalValidatedType("Enter type", existing.getType());
        String breed = InputUtil.getOptionalValidatedBreed("Enter breed", existing.getBreed());
        String phone = InputUtil.getOptionalValidatedPhone("Enter phone", existing.getOwnerPhone());

        Pet pet = new Pet();
        pet.setName(name); pet.setType(type); pet.setBreed(breed); pet.setOwnerPhone(phone);
        petService.updateById(petId, pet);
        ViewUtil.printHeader("Pet updated successfully!");
        pause();
    }

    private static void deletePet(PetService petService) {
        ViewUtil.printHeader("Delete pet by ID");
        String input = InputUtil.getText("Enter pet ID (or Q to quit)").trim();
        if (input.equalsIgnoreCase("Q")) return;

        int petId = Integer.parseInt(input);
        Pet pet = petService.getPetById(petId);
        if (pet == null) { System.out.println("Pet not found!"); return; }

        String confirm = InputUtil.getText("Are you sure? [Y/n]").trim();
        if (confirm.equalsIgnoreCase("Y")) petService.deleteById(petId);
        ViewUtil.printHeader("Pet deleted successfully!");
        pause();
    }

    private static void manageAppointments(PetService petService, AppointmentService appointmentService) {
        boolean exit = false;
        while (!exit) {
            ViewUtil.printAppointmentMenu();
            int option = InputUtil.getInteger("Enter option");
            switch (option) {
                case 1 -> listAppointments(appointmentService, petService);
                case 2 -> createAppointment(petService, appointmentService);
                case 3 -> updateAppointment(appointmentService);
                case 4 -> deleteAppointment(appointmentService);
                case 0 -> exit = true;
                default -> { ViewUtil.printHeader("Invalid option!"); pause(); }
            }
        }
    }

    private static void listAppointments(AppointmentService appointmentService, PetService petService) {
        List<Appointment> list = appointmentService.findAll();
        if (list.isEmpty()) { ViewUtil.printHeader("No appointments found!"); pause(); return; }

        int pageSize = 5, currentPage = 1;
        int totalPages = (list.size() + pageSize - 1) / pageSize;
        boolean paginateExit = false;

        while (!paginateExit) {
            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, list.size());
            ViewUtil.printAppointmentList(list.subList(start, end), petService);
            System.out.println("Page " + currentPage + " / " + totalPages);
            System.out.println("[P] Previous  [N] Next  [Q] Back to Menu");
            String input = InputUtil.getText("Choose option").trim().toUpperCase();
            switch (input) {
                case "P" -> { if (currentPage > 1) currentPage--; else System.out.println("Already at first page!"); }
                case "N" -> { if (currentPage < totalPages) currentPage++; else System.out.println("Already at last page!"); }
                case "Q" -> paginateExit = true;
                default -> System.out.println("Invalid input!");
            }
        }
    }

    private static void createAppointment(PetService petService, AppointmentService appointmentService) {
        int petId = InputUtil.getInteger("Enter pet ID");
        LocalDate checkIn = InputUtil.getDate("Enter check-in date");
        LocalDate checkOut = InputUtil.getDate("Enter check-out date");
        double pricePerDay = InputUtil.getDouble("Enter price per day");

        String paymentStatus = InputUtil.getPaymentStatus("Enter payment status", null);
        String paymentMethod = InputUtil.getPaymentMethod("Enter payment method", paymentStatus, "");
        String status = InputUtil.getAppointmentStatus("Enter status", null);

        Appointment appt = new Appointment();
        appt.setPetId(petId); appt.setCheckIn(checkIn); appt.setCheckOut(checkOut);
        appt.setPricePerDay(pricePerDay); appt.setPaymentStatus(paymentStatus);
        appt.setPaymentMethod(paymentMethod); appt.setStatus(status);

        appointmentService.createAppointment(appt);
        ViewUtil.printHeader("Appointment created successfully!");
        pause();
    }

    private static void updateAppointment(AppointmentService appointmentService) {

    }

    private static void deleteAppointment(AppointmentService appointmentService) {

    }

    private static void pause() {
        System.out.println("Press Enter to continue or type 'Q' to quit");
        String input = InputUtil.getText("").trim();
    }
}
