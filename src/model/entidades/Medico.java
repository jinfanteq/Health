package model.entidades;

public class Medico extends Usuario {
    private String especialidad;

    public Medico(String nombre, String correo, String password, Double id, Double telefono, String especialidad) {
        super(nombre, correo, password, id, telefono);
        this.especialidad = especialidad;
    }
    public Medico(){} //constructor vacio y arrina por defecto

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /*metodos especificos que puede realizar un doctor, aparte de los clasicos que serian
    iniciar sesion y cerrarla */


}
