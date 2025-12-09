package model.BaseDatos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

import controller.AuthController;
import model.entidades.CitaMedica;
import model.entidades.Medico;
import model.entidades.Paciente;

import java.sql.*;

public class CitaMedicaBD {

    private Connection conn;
    private UsuarioDB userDb;
    private AuthController authController;


    public CitaMedicaBD() {
        conn = ConexionBD.getConnection();
        userDb = new UsuarioDB();
        authController = new AuthController(userDb);
    }

    // ==========================================================
    // CREATE
    // ==========================================================

    public int crearCita(Paciente paciente, Medico medico, LocalDate fecha, Time hora) {
        Random random = new Random();
        int idCita = random.nextInt(900000) + 100000;

        if (paciente != null && medico != null) {

            if (!paciente.agendarCita(medico, paciente, fecha, hora)) {
                System.out.println("El paciente ya tiene una cita en esa fecha/hora");
                return 0;
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
                System.out.println("SU CITA HA SIDO AGENDADA CON EXITO, EL IDENTIFICADOR ES: "  + idCita);
                return idCita;

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
        return 0;
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

    public boolean eliminarCita(Paciente paciente, int idCita){

        // 1. Revisar si la cita existe en BD
        if (!revisarCita(idCita)) {
            System.out.println("Cita inexistente");
            return false;
        }

        // 2. Traer el paciente desde BD
        Paciente pacientee = (Paciente) userDb.buscarPorCorreo(paciente.getCorreo());
        if (pacientee == null) {
            System.out.println("Paciente no encontrado");
            return false;
        }

        // 3. Obtener la cita para revisar fecha y hora
        CitaMedica cita = buscarPorId(idCita);
        if (cita == null) {
            System.out.println("No se pudo recuperar la cita");
            return false;
        }

        // 4. Verificar si ya ha pasado 1 hora desde la cita
        LocalDateTime fechaHoraCita = LocalDateTime.of(
                cita.getFecha(),
                cita.getHora().toLocalTime()
        );

        LocalDateTime limite = fechaHoraCita.plusHours(1);
        LocalDateTime ahora = LocalDateTime.now();

        if (ahora.isAfter(limite)) {
            System.out.println("La cita ya pasó hace más de una hora. Se eliminará automáticamente.");
        }

        // 5. Eliminar del paciente (lista en memoria)
        if(pacientee.cancelarCita(idCita)) {

            // 6. Eliminar de la base de datos
            String sql = "DELETE FROM citamedica WHERE idCita = ?";

            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, idCita);
                return ps.executeUpdate() > 0;

            } catch (SQLException e) {
                System.out.println("Error al eliminar cita: " + e.getMessage());
            }

            return true;
        }

        return false;
    }


}
