package model.BaseDatos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import model.entidades.CitaMedica;
import model.entidades.Medico;
import model.entidades.Paciente;

import java.sql.*;

public class CitaMedicaBD {

    private Connection conn;

    public CitaMedicaBD() {
        conn = ConexionBD.getConnection();
    }

    // ==========================================================
    // CREATE
    // ==========================================================

    public boolean crearCita(Paciente paciente, Medico medico, LocalDate fecha, Time hora) {
        Random random = new Random();
        int idCita = random.nextInt(900000) + 100000;

        if (paciente != null && medico != null) {

            if (!paciente.agendarCita(medico, paciente, fecha, hora)) {
                System.out.println("El paciente ya tiene una cita en esa fecha/hora");
                return false;
            }

            String sql = "INSERT INTO citamedica(idCita, idPaciente, idMedico, fecha, hora, motivoConsulta) VALUES (?,?,?,?,?,NULL)";

            try {
                conn.setAutoCommit(false);

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, idCita);
                ps.setDouble(2, paciente.getId());
                ps.setDouble(3, medico.getId());
                ps.setDate(4, Date.valueOf(fecha));
                ps.setTime(5, hora);

                ps.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true);
                return true;

            } catch (SQLException e) {
                try {
                    conn.rollback();
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.out.println("Error en rollback: " + ex.getMessage());
                }

                System.out.println("Error al crear cita: " + e.getMessage());
            }
        }
        return false;
    }

    // ==========================================================
    // READ – Verificar si existe
    // ==========================================================

    public boolean revisarCita(int idCita) {
        String sql = "SELECT idCita FROM citamedica WHERE idCita = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCita);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error revisar: " + e.getMessage());
        }
        return false;
    }

    // ==========================================================
    // READ – Obtener cita por ID
    // ==========================================================

    public CitaMedica buscarPorId(int idCita) {
        String sql = "SELECT * FROM citamedica WHERE idCita = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCita);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new CitaMedica(
                        rs.getInt("idCita"),
                        rs.getDouble("idPaciente"),
                        rs.getDouble("idMedico"),
                        rs.getDate("fecha"),
                        rs.getTime("hora"),
                        rs.getString("motivoConsulta")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error buscar: " + e.getMessage());
        }
        return null;
    }

    // ==========================================================
    // UPDATE – Cambiar fecha/hora de la cita
    // ==========================================================

    public boolean actualizarCita(int idCita, LocalDate nuevaFecha, LocalTime nuevaHora) {

        if (!revisarCita(idCita)) {
            System.out.println("La cita no existe");
            return false;
        }

        String sql = "UPDATE citamedica SET fecha = ?, hora = ? WHERE idCita = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(nuevaFecha));
            ps.setTime(2, Time.valueOf(nuevaHora));
            ps.setInt(3, idCita);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error actualizar: " + e.getMessage());
        }
        return false;
    }

    // ==========================================================
    // DELETE – Eliminar cita
    // ==========================================================

    public boolean eliminarCita(int idCita) {

        if (!revisarCita(idCita)) {
            System.out.println("Cita inexistente");
            return false;
        }

        String sql = "DELETE FROM citamedica WHERE idCita = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCita);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar cita: " + e.getMessage());
        }
        return false;
    }

}
