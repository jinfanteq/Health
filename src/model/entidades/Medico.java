package model.entidades;


/*Esta Clase sirve solo como plantilla y para instanciar y hacer operaciones
 * leves, no tiene la logica completa, esta se manejara en el Usuario Controller,
 * aca manejaremos datos y logica interno, nada de mostrar datos */

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

public class Medico extends Usuario {
    private String especialidad;

    public Medico(String nombre, String correo, String password, Double id, Double telefono, String especialidad) {
        super(nombre, correo, password, id, telefono , "Medico");
        this.especialidad = especialidad;
    }
    public Medico(){} //constructor vacio y arrina por defecto

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /*Metodos especificos que puede realizar sin consultar base de datos ni mostrar mensajes(trabajos
    * del CRUD y de la interfaz grafica*/
    public boolean confirmarCita(){
        return true;
    }


    public boolean cancelarCita(CitaMedica cita) {
        //Solo puede cancelar la cita con 24 horas horas maximo de antelacion, para no causar problemas
        return cita.getHora().after(Date.from(Instant.now().plusSeconds(86400)));
    }


}
