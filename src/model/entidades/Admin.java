package model.entidades;

public class Admin extends Usuario {

    //constructores Local Y Vacio
    public Admin(String nombre, String correo, String password, double id, double telefono, String rol){
        super(nombre, correo,password,id,telefono,"Administrador");
    }
    public Admin(){}

    /*NO VAMOS A TENER METODOS PROPIOS, ESTOS ESTARAN EN LA CLASE CONTROLLER, PUES SE REQUIERE ACCEDER
    * A LA BASE DE DATOS PARA HACER LAS COMPROBACIONES PERTINENTES,Y ESTO SE HARA MEDIANTE EL CRUD EN
    * LOS CONTROLADORES, ESTA CLASE VA A SERVIR MAS COMO UNA PLANTILLA PARA INSTANCIAR EL OBJETO, PUES ESTE
    * TIENE ROLES ESPECIALES */

}
