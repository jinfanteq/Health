package model.entidades;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

public class CitaMedica {
    private Paciente pacienteSolicitante;
    private Medico medicoQueAtiende;
    private LocalDate fecha;
    private Time hora;
    private double idPaciente;
    private double idMedico;
    private int idCita;

    Random random = new Random();

    public CitaMedica(Paciente pacienteSolicitante, Medico medicoQueAtiende, LocalDate fecha , Time hora) {
        this.pacienteSolicitante = pacienteSolicitante;
        this.medicoQueAtiende = medicoQueAtiende;
        this.fecha = fecha;
        this.hora = hora;
        this.idPaciente = pacienteSolicitante.getId();
        this.idMedico = medicoQueAtiende.getId();
    }
    public CitaMedica(){}

    public CitaMedica(int idCita, Object idPaciente, Object idMedico, Date fecha, Time hora, String motivoConsulta) {
        this.idCita = idCita;
        this.idMedico = (double) idMedico;
        this.idPaciente = (double) idPaciente;
        this.fecha = fecha.toLocalDate();
        this.hora = hora;
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public Paciente getPacienteSolicitante() {
        return pacienteSolicitante;
    }

    public void setPacienteSolicitante(Paciente pacienteSolicitante) {
        this.pacienteSolicitante = pacienteSolicitante;
    }

    public Medico getMedicoQueAtiende() {
        return medicoQueAtiende;
    }

    public void setMedicoQueAtiende(Medico medicoQueAtiende) {
        this.medicoQueAtiende = medicoQueAtiende;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public double getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(double idPaciente) {
        this.idPaciente = idPaciente;
    }

    public double getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(double idMedico) {
        this.idMedico = idMedico;
    }

}
