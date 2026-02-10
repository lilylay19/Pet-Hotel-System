package co.istad.jdbc.project.util;
import co.istad.jdbc.project.service.PetService;

import co.istad.jdbc.project.model.Pet;
import co.istad.jdbc.project.model.Appointment;
import org.nocrala.tools.texttablefmt.BorderStyle;

import org.nocrala.tools.texttablefmt.Table;

import java.util.List;

public class ViewUtil {
    public static void print(String text, boolean isNewLine) {
        if (isNewLine)
            System.out.println(text);
        else
            System.out.print(text);
    }

    public static void printHeader(String text) {
        Table table = new Table(1, BorderStyle.UNICODE_ROUND_BOX_WIDE);
        table.addCell(text);
        print(table.render(), true);
    }

    public static void printTopMenu() {

        Table table = new Table(1, BorderStyle.UNICODE_DOUBLE_BOX);
        int width = 25;
        table.addCell(String.format("%" + ((width + "Menu".length()) / 2) + "s", "Menu"));
        table.addCell("1. Pets Management");
        table.addCell("2. Appointments Management");
        table.addCell("0. Exit");
        print(table.render(), true);
    }

    public static void printMenu() {
        Table table = new Table(1, BorderStyle.UNICODE_BOX);
        table.addCell("Pet Management Menu");
        table.addCell("1. List all info  ");
        table.addCell("2. Search pet ");
        table.addCell("3. Add new pet info");
        table.addCell("4. Update pet info");
        table.addCell("5. Delete pet info ");
        table.addCell("0. Back to Main Menu");
        print(table.render(), true);
    }

    public static void printPetList(List<Pet> petLists) {
        Table table = new Table(6, BorderStyle.UNICODE_HEAVY_BOX_WIDE);

        table.addCell("ID");
        table.addCell("NAME");
        table.addCell("TYPE");
        table.addCell("BREED");
        table.addCell("OWNER_CONTACT");
        table.addCell("CREATE_AT");

        for (Pet pet : petLists) {
            table.addCell(pet.getPetId().toString());
            table.addCell(pet.getName());
            table.addCell(pet.getType());
            table.addCell(pet.getBreed());
            table.addCell(pet.getOwnerPhone());
            table.addCell(pet.getCreatedAt().toString());
        }

        print(table.render(), true);
    }

    public static void printAppointmentMenu() {
        Table table = new Table(1, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);
        table.addCell("Appointment Menu");
        table.addCell("1. List all appts");
        table.addCell("2. Add new pet appt");
        table.addCell("3. Update pet appt");
        table.addCell("4. Delete pet appt");
        table.addCell("0. Back to Main Menu");
        print(table.render(), true);
    }


    public static void printAppointmentList(List<Appointment> list, PetService petService) {
        Table table = new Table(9, BorderStyle.UNICODE_HEAVY_BOX_WIDE);

        table.addCell("ID");
        table.addCell("Pet ID");
        table.addCell("Pet Name");
        table.addCell("Check In");
        table.addCell("Check Out");
        table.addCell("Price/Day");
        table.addCell("Discount");
        table.addCell("Total Price");
        table.addCell("Status");

        for (Appointment a : list) {
            table.addCell(a.getApptId().toString());
            table.addCell(a.getPetId().toString());

            // Fetch pet name
            String petName = "";
            try {
                Pet pet = petService.getPetById(a.getPetId());
                if (pet != null) petName = pet.getName();
            } catch (RuntimeException e) {
                petName = "Unknown";
            }
            table.addCell(petName);

            table.addCell(a.getCheckIn().toString());
            table.addCell(a.getCheckOut().toString());
            table.addCell(String.valueOf(a.getPricePerDay()));
            table.addCell(String.valueOf(a.getDiscount()));
            table.addCell(String.valueOf(a.getTotalPrice()));
            table.addCell(a.getStatus());
        }

        print(table.render(), true);
    }
}