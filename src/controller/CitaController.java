package controller;

import model.BaseDatos.CitaMedicaBD;
import model.entidades.CitaMedica;
import model.entidades.Medico;
import model.entidades.Paciente;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CitaController {

    private CitaMedicaBD medicaBD;
    private ArrayList<CitaMedica> citaMedicas;

    public CitaController(CitaMedicaBD medicaBD,Paciente paciente){
        this.medicaBD = medicaBD;
        this.citaMedicas = paciente.getCitas();

    }

    public CitaMedica agendarCita(Paciente paciente, Medico medico, LocalDate fecha, LocalTime hora){
        int idCita = medicaBD.crearCita(paciente,medico,fecha,Time.valueOf(hora));
           if( idCita != 0) {
               CitaMedica cita = medicaBD.buscarPorId(idCita);
               citaMedicas.add(cita);
               return cita;
           }
           else
               System.out.println("ERROR");
           return null;
    }
    public CitaMedica actualizarFecha(int idCita, LocalDate fecha , LocalTime hora){
        if(medicaBD.revisarCita(idCita)){ //si la cita existe
            CitaMedica citaAntigua = medicaBD.buscarPorId(idCita); //guardamos su valor antes de cambiarlo
           if(medicaBD.actualizarCita(idCita,fecha,hora)){ //actualiza los datos de la Cita
               CitaMedica nuevaCita = medicaBD.buscarPorId(idCita);

               citaMedicas.remove(citaMedicas.equals(citaAntigua));
               citaMedicas.add(nuevaCita);
               return nuevaCita; //Lo busca y devuelve la cita con los datos nuevos
           }
        }
        return null;
    }
    public CitaMedica buscarPorId(int idCita){
        if(medicaBD.revisarCita(idCita)) return medicaBD.buscarPorId(idCita);
        return null;
    }
    public boolean cancelarCita(int idCita, Paciente paciente){
        boolean citaExiste = medicaBD.revisarCita(idCita);
        if(!citaExiste) return false;
        else
            if (paciente.cancelarCita(idCita)){
                medicaBD.eliminarCita(paciente, idCita);
                return true;
            }
            return false;
    }
}

