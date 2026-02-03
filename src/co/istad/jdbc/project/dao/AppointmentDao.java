package co.istad.jdbc.project.dao;
import co.istad.jdbc.project.model.Appointment;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AppointmentDao {
    Optional<Appointment> findById(Integer apptId) throws SQLException;
    int create(Appointment appointment) throws SQLException;
    int updateById(Integer apptId, Appointment appointment) throws SQLException;
    int softDeleteById(Integer apptId) throws SQLException;
    List<Appointment> findAll() throws SQLException;
    boolean existsById(Integer apptId) throws SQLException;
}
