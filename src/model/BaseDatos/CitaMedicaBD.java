package model.BaseDatos;

import model.entidades.CitaMedica;
import java.sql.*;

public class CitaMedicaBD {
    private Connection conn;

    public CitaMedicaBD(){
        conn = ConexionBD.getConnection();
    }
}
