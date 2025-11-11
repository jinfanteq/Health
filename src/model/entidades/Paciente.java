package model.entidades;

import java.util.ArrayList;

public class Paciente extends Usuario {
    private String tipoSangre;
    private String genero;
    private float altura;
    private float peso;
    private ArrayList<String> alergias = new ArrayList<>();

    public Paciente(String nombre, String correo, String password, Double id, Double telefono, String tipoSangre, float altura, float peso, ArrayList<String> alergias, String genero) {
        super(nombre, correo, password, id, telefono);
        this.tipoSangre = tipoSangre;
        this.altura = altura;
        this.peso = peso;
        this.alergias = alergias;
        this.genero = genero;
    }
    public Paciente(){}

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public ArrayList<String> getAlergias() {
        return alergias;
    }

    public void setAlergias(ArrayList<String> alergias) {
        this.alergias = alergias;
    }
}
