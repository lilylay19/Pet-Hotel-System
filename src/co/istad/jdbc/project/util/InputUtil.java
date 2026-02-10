package co.istad.jdbc.project.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {

    private static final Scanner scanner = new Scanner(System.in);

    public static String getText(String label) {
        System.out.print(label + " -> ");
        return scanner.nextLine();
    }

    public static Integer getInteger(String label) {
        while (true) {
            System.out.print(label + " -> ");
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Try again.");
            }
        }
    }

    public static LocalDate getDate(String label) {
        while (true) {
            System.out.print(label + " -> ");
            try {
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date! Use yyyy-MM-dd.");
            }
        }
    }

    public static LocalDate getOptionalDate(String label, LocalDate existing) {
        while (true) {
            System.out.print(label + (existing != null ? " [" + existing + "]" : "") + " -> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return existing; // keep current
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date! Use yyyy-MM-dd.");
            }
        }
    }

    public static String getValidatedName(String label) {
        while (true) {
            String input = getText(label).trim();
            if (input.isBlank()) System.out.println("Must input name!");
            else if (!input.matches("[a-zA-Z ]+")) System.out.println("Letters only!");
            else return input;
        }
    }

    public static String getOptionalValidatedName(String label, String existing) {
        while (true) {
            String input = getText(label + " [" + existing + "]").trim();
            if (input.isBlank()) return existing;
            else if (!input.matches("[a-zA-Z ]+")) System.out.println("Letters only!");
            else return input;
        }
    }

    public static String getValidatedType(String label) { return getValidatedName(label); }
    public static String getOptionalValidatedType(String label, String existing) { return getOptionalValidatedName(label, existing); }

    public static String getValidatedBreed(String label) { return getValidatedName(label); }
    public static String getOptionalValidatedBreed(String label, String existing) { return getOptionalValidatedName(label, existing); }

    public static String getValidatedPhone(String label) {
        while (true) {
            String input = getText(label).trim();
            if (input.isBlank()) System.out.println("Must input phone!");
            else if (!input.matches("\\d{8,9}")) System.out.println("Must be 8 or 9 digits!");
            else return input;
        }
    }

    public static String getOptionalValidatedPhone(String label, String existing) {
        while (true) {
            String input = getText(label + " [" + existing + "]").trim();
            if (input.isBlank()) return existing;
            else if (!input.matches("\\d{8,9}")) System.out.println("Must be 8 or 9 digits!");
            else return input;
        }
    }

    public static String getOptionalPaymentStatus(String label, String existing) {
        while (true) {
            String input = getText(label + " [" + existing + "]").trim().toLowerCase();
            if (input.isEmpty()) return existing;
            else if (!input.equals("paid") && !input.equals("unpaid")) System.out.println("Must input 'paid' or 'unpaid'");
            else return input;
        }
    }

    public static String getOptionalPaymentMethod(String label, String paymentStatus, String existing) {
        if ("unpaid".equals(paymentStatus)) return "";
        while (true) {
            String input = getText(label + " [" + existing + "]").trim().toLowerCase();
            if (input.isEmpty()) return existing;
            else if (!input.equals("cash") && !input.equals("atm")) System.out.println("Must input 'cash' or 'atm'");
            else return input;
        }
    }

    public static String getOptionalAppointmentStatus(String label, String existing) {
        while (true) {
            String input = getText(label + " [" + existing + "]").trim().toLowerCase();
            if (input.isEmpty()) return existing;
            else if (!input.equals("accept") && !input.equals("pending") && !input.equals("cancel"))
                System.out.println("Must input accept/pending/cancel");
            else return input;
        }
    }

    public static String getPaymentStatus(String label) {
        while (true) {
            String input = getText(label).trim().toLowerCase();
            if (input.equals("paid") || input.equals("unpaid")) return input;
            System.out.println("Must input 'paid' or 'unpaid'");
        }
    }

    public static String getPaymentMethod(String label, String paymentStatus) {
        if ("unpaid".equals(paymentStatus)) return "";
        while (true) {
            String input = getText(label).trim().toLowerCase();
            if (input.equals("cash") || input.equals("atm")) return input;
            System.out.println("Must input 'cash' or 'atm'");
        }
    }

    public static String getAppointmentStatus(String label) {
        while (true) {
            String input = getText(label).trim().toLowerCase();
            if (input.equals("accept") || input.equals("pending") || input.equals("cancel")) return input;
            System.out.println("Must input accept/pending/cancel");
        }
    }

}
