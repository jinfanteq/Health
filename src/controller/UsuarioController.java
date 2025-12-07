package controller;

import model.BaseDatos.UsuarioDB;
import model.entidades.Paciente;
import model.entidades.Usuario;

import java.sql.SQLException;

public class UsuarioController {

    private UsuarioDB userdb;

    public UsuarioController(){}

    public UsuarioController(UsuarioDB userdb){this.userdb = userdb;}


    public Usuario update(Usuario u, String correoActual, String passwordActual, double idNuevo, String nombreNuevo, String correoNuevo, String contrasenaNueva, double telefonoNuevo) {
        if (u == null) {
            return null;
        }

        Usuario actualizado = userdb.actualizarDatos(
                u,
                correoActual,
                passwordActual,
                idNuevo,
                nombreNuevo,
                correoNuevo,
                contrasenaNueva,
                telefonoNuevo
        );

        if (actualizado != null) {
            System.out.println("Usuario actualizado exitosamente.");
            return actualizado;
        } else {
            System.out.println("Error al actualizar usuario.");
            return null;
        }
    }

    public Paciente ponerDatosEspecificosPaciente(
            Usuario u,
            String tipoSangre,
            Float altura,
            Float peso,
            Integer edad,
            String alergias,
            String sexo
    ) throws SQLException {
        if (u == null) {
            System.out.println("Usuario no encontrado");
            return null;
        }
        return userdb.insertarPaciente(u, u.getId(), tipoSangre, altura, peso, edad, alergias, sexo);
    }

}
