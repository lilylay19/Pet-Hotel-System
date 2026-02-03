package co.istad.jdbc.project.service.impl;

import co.istad.jdbc.project.TelegramBot;
import co.istad.jdbc.project.dao.AppointmentDao;
import co.istad.jdbc.project.model.Appointment;
import co.istad.jdbc.project.service.AppointmentService;
import co.istad.jdbc.project.service.PetService;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;

public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDao appointmentDao;
    private final PetService petService;


    public AppointmentServiceImpl(AppointmentDao appointmentDao, PetService petService) {
        this.appointmentDao = appointmentDao;
        this.petService = petService;
    }

    private double calculateDiscount(LocalDate checkIn, LocalDate checkOut) {
        long days = ChronoUnit.DAYS.between(checkIn, checkOut) + 1;

        if (days <= 1) return 0.0;
        else if (days > 3 && days <= 7) return 5.0;
        else if (days > 7) return 10.0;
        else return 0.0;
    }

    private double calculateTotalPrice(LocalDate checkIn, LocalDate checkOut, double pricePerDay) {
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        double discountPercent = calculateDiscount(checkIn, checkOut);
        double total = pricePerDay * days;
        return total - (total * discountPercent / 100);
    }

    @Override
    public Appointment getAppointmentById(Integer apptId) {
        try {
            return appointmentDao.findById(apptId)
                    .orElseThrow(() -> new RuntimeException("Appointment ID invalid or deleted!"));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void createAppointment(Appointment appointment) {
        try {
            petService.getPetById(appointment.getPetId());
        } catch (RuntimeException e) {
            throw new RuntimeException("Pet ID does not exist!");
        }

        if (appointment.getPricePerDay() != 12) {
            throw new RuntimeException("Price per day must be 12!");
        }

        double discount = calculateDiscount(appointment.getCheckIn(), appointment.getCheckOut());
        appointment.setDiscount(discount);

        double totalPrice = calculateTotalPrice(
                appointment.getCheckIn(),
                appointment.getCheckOut(),
                appointment.getPricePerDay()
        );
        appointment.setTotalPrice(totalPrice);

        String paymentStatus = appointment.getPaymentStatus().toLowerCase();
        if (!paymentStatus.equals("paid") && !paymentStatus.equals("unpaid")) {
            throw new RuntimeException("Payment status must be 'paid' or 'unpaid'");
        }
        appointment.setPaymentStatus(paymentStatus);

        String paymentMethod = appointment.getPaymentMethod();
        if (paymentStatus.equals("unpaid")) {
            appointment.setPaymentMethod(""); // force empty
        } else if (paymentStatus.equals("paid") && (paymentMethod == null || paymentMethod.isBlank())) {
            throw new RuntimeException("Payment method required if status is paid!");
        }

        String status = appointment.getStatus().toLowerCase();
        if (!status.equals("accept") && !status.equals("pending") && !status.equals("cancel")) {
            throw new RuntimeException("Status must be 'accept', 'pending', or 'cancel'");
        }
        appointment.setStatus(status);

        try {
            appointmentDao.create(appointment);

            String message = "New Appointment Booked!\n" +
                    "Pet ID: " + appointment.getPetId() + "\n" +
                    "Check-in: " + appointment.getCheckIn() + "\n" +
                    "Check-out: " + appointment.getCheckOut() + "\n" +
                    "Total Price: $" + appointment.getTotalPrice() + "\n" +
                    "Status: " + appointment.getStatus();
            TelegramBot.sendMessage(message);

            System.out.println(">> Appointment created & notification sent!");

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateAppointment(Integer apptId, Appointment appointment) {
        Appointment existing = getAppointmentById(apptId);
        if (existing.getDeleted()) throw new RuntimeException("Cannot update a deleted appointment!");

        if (appointment.getPetId() != null) {
            try {
                petService.getPetById(appointment.getPetId());
                existing.setPetId(appointment.getPetId());
            } catch (RuntimeException e) {
                throw new RuntimeException("Pet ID does not exist!");
            }
        }

        if (appointment.getCheckIn() != null) existing.setCheckIn(appointment.getCheckIn());
        if (appointment.getCheckOut() != null) existing.setCheckOut(appointment.getCheckOut());

        if (appointment.getPricePerDay() != 0) {
            if (appointment.getPricePerDay() != 12)
                throw new RuntimeException("Price per day must be 12!");
            existing.setPricePerDay(appointment.getPricePerDay());
        }

        if (appointment.getPaymentStatus() != null) {
            String paymentStatus = appointment.getPaymentStatus().toLowerCase();
            if (!paymentStatus.equals("paid") && !paymentStatus.equals("unpaid")) {
                throw new RuntimeException("Payment status must be 'paid' or 'unpaid'");
            }
            existing.setPaymentStatus(paymentStatus);

            String paymentMethod = appointment.getPaymentMethod();
            if (paymentStatus.equals("unpaid")) {
                existing.setPaymentMethod("");
            } else if (paymentStatus.equals("paid")) {
                if (paymentMethod == null || paymentMethod.isBlank())
                    throw new RuntimeException("Payment method required if status is paid!");
                existing.setPaymentMethod(paymentMethod);
            }
        }

        if (appointment.getStatus() != null) {
            String status = appointment.getStatus().toLowerCase();
            if (!status.equals("accept") && !status.equals("pending") && !status.equals("cancel"))
                throw new RuntimeException("Status must be 'accept', 'pending', or 'cancel'");
            existing.setStatus(status);
        }

        double discount = calculateDiscount(existing.getCheckIn(), existing.getCheckOut());
        existing.setDiscount(discount);
        double totalPrice = calculateTotalPrice(existing.getCheckIn(), existing.getCheckOut(),
                existing.getPricePerDay());
        existing.setTotalPrice(totalPrice);

        try {
            appointmentDao.updateById(apptId, existing);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void softDeleteAppointment(Integer apptId) {
        try {
            if (!appointmentDao.existsById(apptId))
                throw new RuntimeException("Appointment ID invalid or already deleted!");
            appointmentDao.softDeleteById(apptId);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Appointment> findAll() {
        try {
            return appointmentDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
