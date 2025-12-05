package model.BaseDatos;

import GIU.LoginGUI;
import model.entidades.Usuario;
import java.sql.*;
import java.util.Scanner;

public class UsuarioDB {

    private Connection conn;
    private LoginGUI loginGUI;

    public UsuarioDB() {
        conn = ConexionBD.getConnection();//conexion con base de datos
    }

    // ==========================================================
    // INSERTAR USUARIO + INSERTAR EN TABLA CORRESPONDIENTE AL ROL
    // ==========================================================
    public boolean insertar(Usuario u) { //CREATE
        String sqlUsuario = "INSERT INTO usuario(id, nombre, correo, contrasena, telefono, rol) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);  // Iniciar transacción

            PreparedStatement ps = conn.prepareStatement(sqlUsuario);
            ps.setDouble(1, u.getId());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getPassword());
            ps.setDouble(5, u.getTelefono());
            ps.setString(6, u.getRol());

            ps.executeUpdate();

            switch (u.getRol().toLowerCase()) {
                case "paciente":
                    insertarPaciente(u, u.getId());
                    break;

                case "medico":
                    insertarMedico(u, u.getId());
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

    private void insertarPaciente(Usuario u , Double id) throws SQLException {
        String sql = "INSERT INTO paciente(nombre , id, tipoSangre, altura, peso, edad, alergias) VALUES (? , ?, NULL, NULL, NULL, NULL, NULL)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, u.getNombre());
        ps.setDouble(2, id);
        ps.executeUpdate();
    }

    private void insertarMedico(Usuario u, Double id) throws SQLException {
        String sql = "INSERT INTO medico(id, especialidad) VALUES (?, ?, NULL)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, u.getNombre());
        ps.setDouble(2, id);
        ps.executeUpdate();
    }

    private void insertarAdmin(Double id, String nombre) throws SQLException {
        String sql = "INSERT INTO admin(id, nombre) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, id);
        ps.setString(2, nombre);
        ps.executeUpdate();
    }

    // READ
    public boolean revisarCredenciales(String correoIngresado , String contrasenaIngresada){
        String sql = "SELECT * FROM usuario WHERE correo = ? AND contrasena = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correoIngresado);
            ps.setString(2,contrasenaIngresada);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                return true;
            }

        }catch (Exception e) {
            System.err.println("Error buscar: " + e.getMessage());
        }
        return false;
    }

    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM usuario WHERE correo = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getDouble("id"),
                        rs.getDouble("telefono"),
                        rs.getString("rol")
                );
            }

        } catch (Exception e) {
            System.out.println("Error buscar: " + e.getMessage());
        }
        return null;
    }

    //UPDATE
    public Usuario actualizarDatos(Usuario u, String correo ,String  password , Double idNuevo , String nombreNuevo, String correoNuevo, String contrasenaNueva , Double telefonoNuevo){
        if(revisarCredenciales(correo , password)) {
            double idUsuario = u.getId();
            String sql = "SELECT * FROM usuario where id = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setDouble(1, idUsuario);
                ResultSet rs = ps.executeQuery();

                if (rs.next() ) {
                    String cambio =
                            "UPDATE usuario SET id = ? , nombre = ? , correo = ? , contrasena = ? , telefono = ?" +
                                    " WHERE id = ? ";

                    ps = conn.prepareStatement(cambio);
                    ps.setDouble(1 , idNuevo );
                    ps.setString(2, nombreNuevo);
                    ps.setString(3, correoNuevo);
                    ps.setString(4, contrasenaNueva);
                    ps.setDouble(5, telefonoNuevo);
                    ps.setDouble(6, idUsuario);

                    ps.executeUpdate();

                    switch (u.getRol().toLowerCase()){
                        //ACTUALIZAR PARA QUE LOS DATOS SE PIDAN POR CONSOLA EN EL LOGINGUI
                        case "medico":
                            Scanner scanner = new Scanner(System.in);
                            String especialidadNueva = scanner.nextLine();
                            actualizarMedico(u ,especialidadNueva);
                            break;

                        case "paciente":

                            break;
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private void actualizarMedico(Usuario u, String especialidadNueva) throws SQLException{
        String sql = "UPDATE medico SET especialidad = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1 , especialidadNueva);
        ps.setDouble(2, u.getId());
        ps.executeUpdate();
    }

    private void actualizarPaciente(Usuario u, String tipoSangreNueva , float alturaNueva, float pesoNuevo , int edadNueva , String alergias) throws SQLException{
        String sql = "UPDATE paciente SET tipoSangre = ? , altura = ?,peso = ? ,  edad = ? , alergias = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1 , tipoSangreNueva);
        ps.setFloat(2, alturaNueva);
        ps.setFloat(3, pesoNuevo);
        ps.setInt(4, edadNueva);
        ps.setString(5, alergias);
        ps.setDouble(6,u.getId());
        ps.executeUpdate();
    }

    public boolean eliminarUsuario(Usuario u) throws SQLException {
        if(u != null) {
            String sql = "DELETE FROM usuario WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1,u.getId());
            ps.executeUpdate();
        }

        return false;
    }



}
