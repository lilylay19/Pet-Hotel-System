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
                case "N" -> { if (currentPage < ((pets.size() + pageSize - 1) / pageSize)) currentPage++; else System.out.println("Already at last page!"); }
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
        int petId;
        Pet existing;
        while (true) {
            petId = InputUtil.getInteger("Enter Pet ID: ");
            try {
                existing = petService.getPetById(petId);
                break;
            } catch (RuntimeException e) {
                System.out.println("Pet ID not found!");
            }
        }

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


        int petId;
        Pet pet;
        while (true) {
            petId = InputUtil.getInteger("Enter Pet ID: ");
            try {
                pet = petService.getPetById(petId);
                break;
            } catch (RuntimeException e) {
                System.out.println("Pet ID not found!");
            }
        }

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
                case 3 -> updateAppointment(petService, appointmentService);
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
        ViewUtil.printHeader("Add New Appointment");

        Pet pet;
        int petId;

        while (true) {
            petId = InputUtil.getInteger("Enter Pet ID");
            try {
                pet = petService.getPetById(petId);
                break; // valid pet
            } catch (RuntimeException e) {
                System.out.println("Pet ID not found!");
            }
        }

        LocalDate checkIn = InputUtil.getDate("Enter check-in date (yyyy-MM-dd)");
        LocalDate checkOut = InputUtil.getDate("Enter check-out date (yyyy-MM-dd)");

        String paymentStatus = InputUtil.getOptionalPaymentStatus("Enter payment status (paid/unpaid)", null);
        String paymentMethod = InputUtil.getOptionalPaymentMethod("Enter payment method (cash/atm)", paymentStatus, null);
        String status = InputUtil.getOptionalAppointmentStatus("Enter status (accept/pending/cancel)", null);

        Appointment appointment = new Appointment();
        appointment.setPetId(petId);
        appointment.setCheckIn(checkIn);
        appointment.setCheckOut(checkOut);
        appointment.setPaymentStatus(paymentStatus);
        appointment.setPaymentMethod(paymentMethod);
        appointment.setStatus(status);

        appointmentService.createAppointment(appointment);

        ViewUtil.printHeader("✅ Appointment created successfully!");
        pause();
    }



    private static void updateAppointment(PetService petService, AppointmentService appointmentService) {
        ViewUtil.printHeader("Update Appointment");

        Appointment existing;
        int apptId;
        while (true) {
            apptId = InputUtil.getInteger("Enter Appointment ID to update");
            try {
                existing = appointmentService.getAppointmentById(apptId);
                break;
            } catch (RuntimeException e) {
                System.out.println("Appointment ID not found");
            }
        }

        System.out.println("Leave empty to keep current value");

        LocalDate checkIn = InputUtil.getOptionalDate("Enter new check-in date", existing.getCheckIn());
        LocalDate checkOut = InputUtil.getOptionalDate("Enter new check-out date", existing.getCheckOut());
        String paymentStatus = InputUtil.getOptionalPaymentStatus("Enter new payment status (paid/unpaid)", existing.getPaymentStatus());
        String paymentMethod = InputUtil.getOptionalPaymentMethod("Enter new payment method (cash/atm)", paymentStatus, existing.getPaymentMethod());
        String status = InputUtil.getOptionalAppointmentStatus("Enter new status (accept/pending/cancel)", existing.getStatus());

        Appointment updated = new Appointment();
        updated.setCheckIn(checkIn);
        updated.setCheckOut(checkOut);
        updated.setPaymentStatus(paymentStatus);
        updated.setPaymentMethod(paymentMethod);
        updated.setStatus(status);

        try {
            appointmentService.updateAppointment(apptId, updated);
            ViewUtil.printHeader("✅ Appointment updated successfully!");
        } catch (RuntimeException e) {
            System.out.println("Failed to update appointment: " + e.getMessage());
        }

        pause();
    }


    private static void deleteAppointment(AppointmentService appointmentService) {
        ViewUtil.printHeader("Delete Appointment");

        int apptId;
        Appointment existing;

        while (true) {
            String input = InputUtil.getText("Enter Appointment ID to delete (or Q to quit)").trim();
            if (input.equalsIgnoreCase("Q")) return;

            try {
                apptId = Integer.parseInt(input);
                existing = appointmentService.getAppointmentById(apptId);
                break; // valid ID found, exit loop
            } catch (NumberFormatException e) {
                System.out.println("Invalid number");
            } catch (RuntimeException e) {
                System.out.println("Appointment ID does not exist!");
            }
        }

        String confirm = InputUtil.getText("Are you sure you want to delete? [Y/n]").trim();
        if (confirm.equalsIgnoreCase("Y")) {
            appointmentService.softDeleteAppointment(apptId);
            ViewUtil.printHeader("Appointment deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }

        pause();
    }

    private static void pause() {
        System.out.println("Press Enter to continue or type 'Q' to quit");
        InputUtil.getText("");
    }
}
