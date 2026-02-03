package co.istad.jdbc.project.service;

import co.istad.jdbc.project.dao.AppointmentDao;
import co.istad.jdbc.project.model.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment getAppointmentById(Integer apptId);
    void createAppointment(Appointment appointment);
    void updateAppointment(Integer apptId, Appointment appointment);
    void softDeleteAppointment(Integer apptId);
    List<Appointment> findAll();
}

