package controller;

import model.BaseDatos.UsuarioDB;
import model.entidades.Usuario;

/*La funcion de esta clase es gestionar la autenticacion de roles ya que vamos
* a generar distintos mediante un login, dando autorizacion y autenticacion a las
* credenciales que se den por entrada */
public class AuthController {

    UsuarioDB userdb = new UsuarioDB();
    public AuthController(){}

    public Usuario login(String correo , String password){
        //SE VA A MANEJAR CON SCANNER Y SOUT POR EL MOMENTO PERO DESPUES SE ARREGLA PARA GIU
        if (userdb.revisarCredenciales(correo, password)){
            System.out.println("Ingreso correcto");
            return userdb.buscarPorCorreo(correo);
        }else{
            System.err.println("DATOS INCORRECTOS");
        }
        return null;
    }

    public Usuario register(String nombre, String correo, String password, Double id, Double telefono, String rol){
        Usuario userRegister = new Usuario(nombre,correo,password,id,telefono,rol);
        if(userdb.insertar(userRegister)) {
            System.out.println("Registro exitoso");
            return userRegister;
        }
        return null;
    }
}
