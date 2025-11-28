package model.BaseDatos;

import model.entidades.Usuario;
import java.sql.*;

//CRUD PARA PODER USAR LA BASE DE DATOS DEL USUARIO

public class UsuarioDB {

    private Connection conn;

    public UsuarioDB() {
        conn = ConexionBD.getConnection();//conexion con base de datos
    }

    // ==========================================================
    // INSERTAR USUARIO + INSERTAR EN TABLA CORRESPONDIENTE AL ROL
    // ==========================================================
    public boolean insertar(Usuario u) {
        String sqlUsuario = "INSERT INTO usuario(id, nombre, correo, contrasena, telefono, rol) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);  // Iniciar transacción

            // ---------------------------------
            // 1. Insertar en tabla Usuario
            // ---------------------------------
            PreparedStatement ps = conn.prepareStatement(sqlUsuario);
            ps.setDouble(1, u.getId());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getPassword());
            ps.setDouble(5, u.getTelefono());
            ps.setString(6, u.getRol());
            ps.executeUpdate();

            // ---------------------------------
            // 2. Insertar en tabla hija según rol
            // ---------------------------------
            switch (u.getRol().toLowerCase()) {

                case "paciente":
                    insertarPaciente(u.getId());
                    break;

                case "medico":
                    insertarMedico(u.getId());
                    break;

                case "admin":
                    insertarAdmin(u.getId(), u.getNombre());
                    break;

                default:
                    conn.rollback();
                    System.out.println("Rol no válido: " + u.getRol());
                    return false;
            }

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
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    // ==========================================================
    // MÉTODOS PRIVADOS PARA INSERTAR EN TABLAS HIJAS
    // ==========================================================

    private void insertarPaciente(Double id) throws SQLException {

        String sql = "INSERT INTO paciente(id, tipoSangre, altura, peso, edad, alergias) VALUES (?, NULL, NULL, NULL, NULL, NULL)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, id);
        ps.executeUpdate();
    }

    private void insertarMedico(Double id) throws SQLException {
        String sql = "INSERT INTO medico(id, especialidad) VALUES (?, NULL)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, id);
        ps.executeUpdate();
    }

    private void insertarAdmin(Double id, String nombre) throws SQLException {
        String sql = "INSERT INTO admin(id, nombre) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, id);
        ps.setString(2, nombre);
        ps.executeUpdate();


        /*LOS VALORES SE PONEN NULOS PORQUE DESDE ACA NO SE PUEDEN AÑADIR, SERAN AÑADIDOS DESDE  EL LOGIN CON
        /UNA ACTUALIZACION */
    }
}
