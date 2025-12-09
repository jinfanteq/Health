package model.BaseDatos;

import GIU.LoginGUI;
import model.entidades.CitaMedica;
import model.entidades.Medico;
import model.entidades.Paciente;
import model.entidades.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class UsuarioDB {

    private Connection conn;
    private LoginGUI loginGUI;

    public UsuarioDB() {
        conn = ConexionBD.getConnection();
    }

    // ==========================================================
    // INSERTAR USUARIO + TABLA POR ROL
    // ==========================================================
    public boolean insertar(Usuario u) {
        String sqlUsuario = "INSERT INTO usuario(id, nombre, correo, contrasena, telefono, rol) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(sqlUsuario);
            ps.setDouble(1, u.getId());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getPassword());
            ps.setDouble(5, u.getTelefono());
            ps.setString(6, u.getRol());
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

            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }
    public Paciente insertarPaciente(
            Usuario u,
            Double id,
            String tipoSangre,
            Float altura,
            Float peso,
            Integer edad,
            String alergias,
            String sexo
    ) throws SQLException {

        // Verificar que el usuario existe; si no, insertarlo.
        String checkSql = "SELECT id FROM usuario WHERE id = ?";
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setDouble(1, id);
            try (ResultSet rsCheck = checkPs.executeQuery()) {
                if (!rsCheck.next()) {
                    insertar(u);
                }
            }
        }

        // Insertar paciente: asegurar orden correcto de columnas y parámetros
        String sql = "INSERT INTO paciente (id, tipoSangre, altura, peso, edad, alergias, nombre, sexo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, id);
            ps.setString(2, tipoSangre);
            ps.setFloat(3, altura != null ? altura : 0f);
            ps.setFloat(4, peso != null ? peso : 0f);
            ps.setInt(5, edad != null ? edad : 0);
            ps.setString(6, alergias != null ? alergias : "");
            ps.setString(7, u.getNombre()); // nombre
            ps.setString(8, sexo != null ? sexo : "");

            int rows = ps.executeUpdate();
            if (rows <= 0) {
                return null;
            }
        }

        // Construir y devolver el objeto Paciente con los datos que acabamos de insertar.
        ArrayList<String> listaAlergias = new ArrayList<>();
        if (alergias != null && !alergias.trim().isEmpty()) listaAlergias.add(alergias.trim());
        ArrayList<CitaMedica> citas = new ArrayList<>();

        // Asegúrate de que Paciente tenga un constructor compatible con este orden:
        // (nombre, correo, password, id, telefono, tipoSangre, sexo, peso, altura, alergiasList, citas)
        return new Paciente(
                u.getNombre(),
                u.getCorreo(),
                u.getPassword(),
                u.getId(),
                u.getTelefono(),
                tipoSangre,
                sexo,
                peso != null ? peso : 0f,
                altura != null ? altura : 0f,
                listaAlergias,
                citas
        );
    }


    public Medico insertarMedico(Usuario u, String especialidad) throws SQLException {
        // Verificar que el usuario existe; si no, insertarlo.
        String checkSql = "SELECT id FROM usuario WHERE id = ?";
        try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setDouble(1, u.getId());
            try (ResultSet rsCheck = checkPs.executeQuery()) {
                if (!rsCheck.next()) {
                    insertar(u);
                }
            }
        }
        String sql = "INSERT INTO medico(id, especialidad) VALUES (?, ?, ?)"; //falta añadir el nombre
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, u.getId());
            ps.setString(2, especialidad);
            ps.setString(3, u.getNombre());
            int rows = ps.executeUpdate();
            if (rows <= 0) {
                return null;
            }
            ArrayList<CitaMedica> citasMedico = new ArrayList<>();
            return new Medico(u.getNombre(), u.getCorreo(), u.getPassword(), u.getId(), u.getTelefono(), especialidad,citasMedico);
        }
    }



    // ==========================================================
    // READ
    // ==========================================================
    public boolean revisarCredenciales(String correoIngresado , String contrasenaIngresada){
        String sql = "SELECT * FROM usuario WHERE correo = ? AND contrasena = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correoIngresado);
            ps.setString(2, contrasenaIngresada);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            System.err.println("Error buscar: " + e.getMessage());
        }
        return false;
    }

    public boolean revisarPorCorreo(String correoIngresado ){
        String sql = "SELECT * FROM usuario WHERE correo = ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, correoIngresado);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
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

    // ==========================================================
    // UPDATE
    // ==========================================================
    public Usuario actualizarDatos(
            Usuario u, String correo, String password,
            double idNuevo, String nombreNuevo, String correoNuevo,
            String contrasenaNueva, double telefonoNuevo
    ) {
        if(!revisarCredenciales(correo, password)) {
            System.out.println("Credenciales incorrectas");
            return null;
        }

        double idUsuario = u.getId();
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                String cambio =
                        "UPDATE usuario SET id = ?, nombre = ?, correo = ?, contrasena = ?, telefono = ? WHERE id = ?";

                ps = conn.prepareStatement(cambio);
                ps.setDouble(1, idNuevo);
                ps.setString(2, nombreNuevo);
                ps.setString(3, correoNuevo);
                ps.setString(4, contrasenaNueva);
                ps.setDouble(5, telefonoNuevo);
                ps.setDouble(6, idUsuario);
                ps.executeUpdate();

                switch (u.getRol().toLowerCase()) {
                    case "medico":
                        Scanner scanner = new Scanner(System.in);
                        String especialidadNueva = scanner.nextLine();
                        actualizarMedico(u, especialidadNueva);
                        break;

                    case "paciente":
                        Scanner scanner1 = new Scanner(System.in);
                        String tipoSangre = scanner1.nextLine();
                        float altura = scanner1.nextFloat();
                        float peso = scanner1.nextFloat();
                        int edad = scanner1.nextInt();
                        scanner1.nextLine();
                        String alergias = scanner1.nextLine();
                        actualizarPaciente(u, tipoSangre, altura, peso, edad, alergias);
                        break;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return u;
    }

    private void actualizarMedico(Usuario u, String especialidadNueva) throws SQLException {
        String sql = "UPDATE medico SET especialidad = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, especialidadNueva);
        ps.setDouble(2, u.getId());
        ps.executeUpdate();
    }

    private void actualizarPaciente(Usuario u, String tipoSangreNueva, float altura, float peso, int edad, String alergias) throws SQLException {
        String sql = "UPDATE paciente SET tipoSangre = ?, altura = ?, peso = ?, edad = ?, alergias = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, tipoSangreNueva);
        ps.setFloat(2, altura);
        ps.setFloat(3, peso);
        ps.setInt(4, edad);
        ps.setString(5, alergias);
        ps.setDouble(6, u.getId());
        ps.executeUpdate();
    }

    // ==========================================================
    // DELETE
    // ==========================================================
    public boolean eliminarUsuario(Usuario u) throws SQLException {
        if (u == null) return false;

        String sql = "DELETE FROM usuario WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setDouble(1, u.getId());

        return ps.executeUpdate() > 0;
    }
}
