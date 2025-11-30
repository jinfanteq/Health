import controller.AuthController;
import model.entidades.Usuario;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        AuthController controladorLogin = new AuthController();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese su correo: ");
        String correo = scanner.nextLine();
        System.out.print("Ingrese su password: ");
        String password = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("ID: ");
        Double id = scanner.nextDouble();
        System.out.print("Telefono: ");
        Double tel = scanner.nextDouble();
        System.out.println("Rol (1. Paciente , 2. medico , 3. admin ) : ");
        scanner = new Scanner(System.in);
        String rol = scanner.nextLine();


        controladorLogin.register(nombre,correo,password,id,tel,rol);
    }
}