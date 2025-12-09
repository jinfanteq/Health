package model.entidades;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;


/*Esta Clase sirve solo como plantilla y para instanciar y hacer operaciones
* leves, no tiene la logica completa, esta se manejara en el Usuario Controller,
* aca manejaremos datos y logica interno, nada de mostrar datos */

public class Paciente extends Usuario {
    private String tipoSangre;
    private String genero;
    private float altura;
    private float peso;
    private ArrayList<String> alergias = new ArrayList<>();
    private ArrayList<CitaMedica> citas; // relación con las citas del paciente

    //Contructores, local con herencia y constructor vacio
    public Paciente(String nombre, String correo, String password, double id, double telefono,
                    String tipoSangre, String genero, float peso, float altura,
                    ArrayList<String> alergias, ArrayList<CitaMedica> citas) {
        super(nombre, correo, password, id, telefono, "Paciente");
        this.tipoSangre = tipoSangre;
        this.genero = genero;
        this.altura = altura;
        this.peso = peso;
        this.alergias = alergias != null ? alergias : new ArrayList<>();
        this.citas = citas != null ? citas : new ArrayList<>();
    }

   //Getters y Setters

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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

    public ArrayList<CitaMedica> getCitas() {
        return citas;
    }

    public void setCitas(ArrayList<CitaMedica> citas) {
        this.citas = citas;
    }

    //Metodos propios

    //verifica si si se puede agendar una cita
    public boolean agendarCita(Medico medico , Paciente paciente, LocalDate fecha , Time hora) {

        // 1. Revisar que el paciente no tenga ya una cita en esa fecha/hora
        for (CitaMedica c : citas) {
            if (c.getFecha().equals(fecha) && c.getHora().equals(hora)) {
                return false; // El paciente ya tiene cita
            }
        }

        // 2. Revisar que el médico no tenga una cita en esa fecha/hora
        if (medico.getCitaMedicasAtender() != null) {
            for (CitaMedica c : medico.getCitaMedicasAtender()) {
                if (c.getFecha().equals(fecha) && c.getHora().equals(hora)) {
                    return false; // El médico ya tiene ocupada esa hora
                }
            }
        }

        // 3. Crear la cita si todo está libre
        CitaMedica cita = new CitaMedica(paciente, medico, fecha, hora);

        // 4. Guardar la cita en ambas listas
        this.citas.add(cita);
        medico.getCitaMedicasAtender().add(cita);

        return true;
    }


    public boolean cancelarCita(int citaId){
        return citas.removeIf(c -> c.getIdCita() == citaId);
    }
    /*Estos dos metodos solo revisan que la cita medica exista o no se repita, para poder
    * o crearla o eliminarla, estos metodos seran llamados posteriormente por usuarioController
    * para poder hacer la autenticacion de datos restantes y */
}
