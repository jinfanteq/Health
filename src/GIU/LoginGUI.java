package GIU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import controller.AuthController;
import model.BaseDatos.UsuarioDB;
import model.entidades.Usuario;

public class LoginGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().mostrarPantallaInicial());
    }

    private UsuarioDB usuarioDB;
    private AuthController controladorLogin;

    public LoginGUI() {
        usuarioDB = new UsuarioDB();          // instancia única
        controladorLogin = new AuthController(usuarioDB);  // se comparte
    }

    // Login o Registro

    public void mostrarPantallaInicial() {
        JFrame frame = new JFrame("SaludDirecta");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(60, 63, 65));
        panel.setLayout(null);
        frame.setContentPane(panel);

        JLabel titulo = new JLabel("Bienvenido a SaludDirecta");
        titulo.setBounds(70, 20, 300, 30);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(100, 100, 80, 30);
        loginBtn.setBackground(new Color(75, 175, 80));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        panel.add(loginBtn);

        JButton registroBtn = new JButton("Registrar");
        registroBtn.setBounds(200, 100, 100, 30);
        registroBtn.setBackground(new Color(100, 149, 237)); // azul
        registroBtn.setForeground(Color.WHITE);
        registroBtn.setFocusPainted(false);
        panel.add(registroBtn);

        loginBtn.addActionListener(e -> {
            frame.dispose();
            mostrarLogin();
        });

        registroBtn.addActionListener(e -> {
            frame.dispose();
            mostrarRegistro();
        });

        frame.setVisible(true);
    }

    // Login

    private void mostrarLogin() {
        JFrame frame = new JFrame("Login - Health App");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(60, 63, 65));
        panel.setLayout(null);
        frame.setContentPane(panel);

        JLabel titulo = new JLabel("Login");
        titulo.setBounds(160, 10, 100, 30);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo);

        JLabel correoLabel = new JLabel("Correo:");
        correoLabel.setBounds(50, 60, 80, 25);
        correoLabel.setForeground(Color.WHITE);
        panel.add(correoLabel);

        JTextField correoText = new JTextField(20);
        correoText.setBounds(150, 60, 180, 25);
        panel.add(correoText);

        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setBounds(50, 100, 100, 25);
        passLabel.setForeground(Color.WHITE);
        panel.add(passLabel);

        JPasswordField passText = new JPasswordField(20);
        passText.setBounds(150, 100, 180, 25);
        panel.add(passText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 30);
        loginButton.setBackground(new Color(75, 175, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        panel.add(loginButton);

        JLabel mensaje = new JLabel("", SwingConstants.CENTER);
        mensaje.setBounds(50, 190, 300, 25);
        mensaje.setForeground(Color.RED);
        panel.add(mensaje);

        loginButton.addActionListener(e -> {
            String correo = correoText.getText().trim();
            String contraseña = new String(passText.getPassword()).trim();
            Usuario usuarioLogin = controladorLogin.login(correo,contraseña);

            if (usuarioLogin != null) {
                mensaje.setText("¡Login exitoso!");
                mensaje.setForeground(new Color(75, 175, 80));

                Usuario usuario = usuarioDB.buscarPorCorreo(correo);
                System.out.println("Usuario logueado: " + usuario.getNombre() + ", rol: " + usuario.getRol());

                frame.dispose();
            } else {
                mensaje.setText("Correo o contraseña incorrectos.");
                mensaje.setForeground(Color.RED);
            }
        });

        frame.setVisible(true);
    }

    // Registro

    private void mostrarRegistro() {
        JFrame frame = new JFrame("Registro - SaludDirecta");
        frame.setSize(450, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(60, 63, 65));
        panel.setLayout(null);
        frame.setContentPane(panel);

        JLabel titulo = new JLabel("Crear nuevo usuario");
        titulo.setBounds(120, 10, 250, 30);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(Color.WHITE);
        panel.add(titulo);

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(50, 50, 100, 25);
        nombreLabel.setForeground(Color.WHITE);
        panel.add(nombreLabel);
        JTextField nombreText = new JTextField();
        nombreText.setBounds(150, 50, 200, 25);
        panel.add(nombreText);

        JLabel correoLabel = new JLabel("Correo:");
        correoLabel.setBounds(50, 90, 100, 25);
        correoLabel.setForeground(Color.WHITE);
        panel.add(correoLabel);
        JTextField correoText = new JTextField();
        correoText.setBounds(150, 90, 200, 25);
        panel.add(correoText);

        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setBounds(50, 130, 100, 25);
        passLabel.setForeground(Color.WHITE);
        panel.add(passLabel);
        JPasswordField passText = new JPasswordField();
        passText.setBounds(150, 130, 200, 25);
        panel.add(passText);

        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(50, 170, 100, 25);
        idLabel.setForeground(Color.WHITE);
        panel.add(idLabel);
        JTextField idText = new JTextField();
        idText.setBounds(150, 170, 200, 25);
        panel.add(idText);

        JLabel telLabel = new JLabel("Teléfono:");
        telLabel.setBounds(50, 210, 100, 25);
        telLabel.setForeground(Color.WHITE);
        panel.add(telLabel);
        JTextField telText = new JTextField();
        telText.setBounds(150, 210, 200, 25);
        panel.add(telText);

        JLabel rolLabel = new JLabel("Rol:");
        rolLabel.setBounds(50, 250, 100, 25);
        rolLabel.setForeground(Color.WHITE);
        panel.add(rolLabel);
        String[] roles = {"paciente", "medico", "admin"};
        JComboBox<String> rolCombo = new JComboBox<>(roles);
        rolCombo.setBounds(150, 250, 200, 25);
        panel.add(rolCombo);

        JButton crearBtn = new JButton("Crear Usuario");
        crearBtn.setBounds(150, 300, 150, 30);
        crearBtn.setBackground(new Color(100, 149, 237));
        crearBtn.setForeground(Color.WHITE);
        crearBtn.setFocusPainted(false);
        panel.add(crearBtn);

        JLabel mensaje = new JLabel("", SwingConstants.CENTER);
        mensaje.setBounds(50, 350, 350, 25);
        mensaje.setForeground(Color.RED);
        panel.add(mensaje);

        crearBtn.addActionListener(e -> {
            try {
                String nombre = nombreText.getText().trim();
                String correo = correoText.getText().trim();
                String contraseña = new String(passText.getPassword()).trim();
                Double id = Double.parseDouble(idText.getText().trim());
                Double telefono = Double.parseDouble(telText.getText().trim());
                String rol = rolCombo.getSelectedItem().toString();

                Usuario nuevo = controladorLogin.register(nombre,correo,contraseña,id,telefono,rol);

                if (nuevo != null) {
                    mensaje.setText("Usuario creado exitosamente!");
                    mensaje.setForeground(new Color(75, 175, 80));
                } else {
                    mensaje.setText("Error al crear usuario. Verifica los datos.");
                    mensaje.setForeground(Color.RED);
                }
            } catch (Exception ex) {
                mensaje.setText("Por favor, completa todos los campos correctamente.");
                mensaje.setForeground(Color.RED);
            }
        });

        frame.setVisible(true);
    }
}