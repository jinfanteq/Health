package controller;

import model.BaseDatos.UsuarioDB;
import model.entidades.Usuario;
import java.sql.SQLException;

public class AuthController {

    private UsuarioDB userdb;

    public AuthController() {}
    public AuthController(UsuarioDB userdb){
        this.userdb = userdb;
    }

    public Usuario login(String correo , String password){

        if (userdb.revisarCredenciales(correo, password)){
            System.out.println("Ingreso correcto");
            return userdb.buscarPorCorreo(correo);
        } else {
            System.err.println("DATOS INCORRECTOS");
            return null;
        }
    }

    public Usuario register(String nombre, String correo, String password, double id, double telefono, String rol){

        Usuario userRegister = new Usuario(nombre, correo, password, id, telefono, rol);

        if(userdb.insertar(userRegister)) {
            System.out.println("Registro exitoso");
            return userRegister;
        }

        return null;
    }

}
