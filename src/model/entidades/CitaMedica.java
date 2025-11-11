package model.entidades;

import java.time.LocalDateTime;

public class CitaMedica {
    private Paciente pacienteSolicitante;
    private Medico medicoQueAtiende;
    private LocalDateTime horaYFecha;
    private double idPaciente;
    private double idMedico;

    public CitaMedica(Paciente pacienteSolicitante, Medico medicoQueAtiende, LocalDateTime horaYFecha) {
        this.pacienteSolicitante = pacienteSolicitante;
        this.medicoQueAtiende = medicoQueAtiende;
        this.horaYFecha = horaYFecha;
        this.idPaciente = pacienteSolicitante.getId();
        this.idMedico = medicoQueAtiende.getId();
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

    public LocalDateTime getHoraYFecha() {
        return horaYFecha;
    }

    public void setHoraYFecha(LocalDateTime horaYFecha) {
        this.horaYFecha = horaYFecha;
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
