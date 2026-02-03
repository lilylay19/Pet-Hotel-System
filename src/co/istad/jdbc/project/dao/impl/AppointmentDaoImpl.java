
package co.istad.jdbc.project.dao.impl;

import co.istad.jdbc.project.config.DBConnection;
import co.istad.jdbc.project.dao.AppointmentDao;
import co.istad.jdbc.project.model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentDaoImpl implements AppointmentDao {

    private final Connection conn;

    public AppointmentDaoImpl() {
        conn = DBConnection.getInstance();
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appt = new Appointment();
        appt.setApptId(rs.getInt("appt_id"));
        appt.setPetId(rs.getInt("pet_id"));
        appt.setCheckIn(rs.getDate("check_in").toLocalDate());
        appt.setCheckOut(rs.getDate("check_out").toLocalDate());
        appt.setPricePerDay(rs.getDouble("price_per_day"));
        appt.setDiscount(rs.getDouble("discount"));
        appt.setTotalPrice(rs.getDouble("total_price"));
        appt.setPaymentStatus(rs.getString("payment_status"));
        appt.setPaymentMethod(rs.getString("payment_method"));
        appt.setStatus(rs.getString("status"));
        appt.setDeleted(rs.getBoolean("is_deleted"));
        return appt;
    }

    @Override
    public Optional<Appointment> findById(Integer apptId) throws SQLException {
        final String SQL = "SELECT * FROM appointments WHERE appt_id = ? AND is_deleted = FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, apptId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapResultSetToAppointment(rs));
        }
        return Optional.empty();
    }

    @Override
    public int create(Appointment appointment) throws SQLException {
        final String SQL = """
            INSERT INTO appointments (pet_id, check_in, check_out, price_per_day, discount, total_price, payment_status, payment_method, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, appointment.getPetId());
            stmt.setDate(2, Date.valueOf(appointment.getCheckIn()));
            stmt.setDate(3, Date.valueOf(appointment.getCheckOut()));
            stmt.setDouble(4, appointment.getPricePerDay());
            stmt.setDouble(5, appointment.getDiscount());
            stmt.setDouble(6, appointment.getTotalPrice());
            stmt.setString(7, appointment.getPaymentStatus());
            stmt.setString(8, appointment.getPaymentMethod());
            stmt.setString(9, appointment.getStatus());
            return stmt.executeUpdate();
        }
    }

    @Override
    public int updateById(Integer apptId, Appointment appointment) throws SQLException {
        final String SQL = """
            UPDATE appointments
            SET pet_id=?, check_in=?, check_out=?, price_per_day=?, discount=?, total_price=?, payment_status=?, payment_method=?, status=?
            WHERE appt_id=?
        """;
        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, appointment.getPetId());
            stmt.setDate(2, Date.valueOf(appointment.getCheckIn()));
            stmt.setDate(3, Date.valueOf(appointment.getCheckOut()));
            stmt.setDouble(4, appointment.getPricePerDay());
            stmt.setDouble(5, appointment.getDiscount());
            stmt.setDouble(6, appointment.getTotalPrice());
            stmt.setString(7, appointment.getPaymentStatus());
            stmt.setString(8, appointment.getPaymentMethod());
            stmt.setString(9, appointment.getStatus());
            stmt.setInt(10, apptId);
            return stmt.executeUpdate();
        }
    }

    @Override
    public int softDeleteById(Integer apptId) throws SQLException {
        final String SQL = "UPDATE appointments SET is_deleted = TRUE WHERE appt_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, apptId);
            return stmt.executeUpdate();
        }
    }

    @Override
    public List<Appointment> findAll() throws SQLException {
        final String SQL = "SELECT * FROM appointments WHERE is_deleted = FALSE";
        List<Appointment> list = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToAppointment(rs));
            }
        }
        return list;
    }

    @Override
    public boolean existsById(Integer apptId) throws SQLException {
        final String SQL = "SELECT 1 FROM appointments WHERE appt_id=? AND is_deleted=FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setInt(1, apptId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}



