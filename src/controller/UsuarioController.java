package controller;

import model.BaseDatos.UsuarioDB;
import model.entidades.Medico;
import model.entidades.Paciente;
import model.entidades.Usuario;

import java.sql.SQLException;

public class UsuarioController {

    private UsuarioDB userdb;

    public UsuarioController() {
    }

    public UsuarioController(UsuarioDB userdb) {
        this.userdb = userdb;
    }


    public Usuario update(
            Usuario u,
            String correoActual,
            String passwordActual,
            double idNuevo,
            String nombreNuevo,
            String correoNuevo,
            String contrasenaNueva,
            double telefonoNuevo
    ) throws SQLException {

        if (u == null) {
            return null;
        }

        // 1. Actualizar datos generales
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

        if (actualizado == null) {
            System.out.println("Error al actualizar usuario.");
            return null;
        }

        System.out.println("Usuario actualizado exitosamente.");
        return actualizado;
    }

    public Medico ponerDatosEspecificosMedico(Usuario u, String especialidad) throws SQLException {
        if (u == null) {
            System.out.println("Usuario no " +
                    "encontrado");
            return null;
        }
        return userdb.insertarMedico(u, especialidad);
    }

    public Usuario delete(Usuario u, int codigoConfirmacion) throws SQLException {

        final int CODIGO_VALIDO = 1234;

        if (u == null) {
            return null;
        }

        if (codigoConfirmacion != CODIGO_VALIDO) {
            System.out.println("Código de confirmación incorrecto.");
            return null;
        }

        boolean eliminado = userdb.eliminarUsuario(u);

        if (eliminado) {
            System.out.println("Usuario eliminado exitosamente.");
            return u;
        } else {
            System.out.println("No se eliminó el usuario (no existe en BD).");
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


    public Usuario buscarPorCorreo(String correo) {
        if (userdb.revisarPorCorreo(correo)) return userdb.buscarPorCorreo(correo);
        else return null;
    }
}

