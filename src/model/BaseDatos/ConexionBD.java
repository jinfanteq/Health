package model.BaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;

//EN ESTA CLASE SOLO SE CONECTA LA BASE DE DATOS CON EL CODIGO

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/health";
    private static final String USER = "root";
    private static final String PASSWORD = "ServerInfante2007.";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Error en la conexión: " + e.getMessage());
            return null;
        }
    }
}

