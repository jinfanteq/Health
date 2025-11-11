package model.entidades;

public class Usuario {
    private String nombre;
    private String correo;
    private String password;
    private Double id;
    private Double telefono;

    public Usuario(String nombre, String correo, String password, Double id, Double telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.id = id;
        this.telefono = telefono;
    }
    public Usuario(){}

    //GETTER Y SETTERS
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public Double getTelefono() {
        return telefono;
    }

    public void setTelefono(Double telefono) {
        this.telefono = telefono;
    }
}
