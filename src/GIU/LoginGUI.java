package GIU;

import controller.UsuarioController;
import model.BaseDatos.UsuarioDB;
import model.entidades.Paciente;
import model.entidades.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class LoginGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().mostrarPantallaInicial());
    }

    private final UsuarioDB usuarioDB;
    private final controller.AuthController controladorLogin; // asume que existe
    private final UsuarioController userController;

    public LoginGUI() {
        usuarioDB = new UsuarioDB();
        controladorLogin = new controller.AuthController(usuarioDB);
        userController = new UsuarioController(usuarioDB);
    }

    // -------------------- Pantalla inicial --------------------
    public void mostrarPantallaInicial() {
        JFrame frame = new JFrame("SaludDirecta");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 260);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Bienvenido a SaludDirecta", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(35, 35, 35));
        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setBackground(Color.WHITE);
        center.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton loginBtn = createPrimaryButton("Iniciar Sesión", new Color(75, 175, 80));
        JButton registroBtn = createPrimaryButton("Registrarse", new Color(100, 149, 237));

        loginBtn.addActionListener(e -> {
            frame.dispose();
            mostrarLogin();
        });

        registroBtn.addActionListener(e -> {
            frame.dispose();
            mostrarRegistro();
        });

        center.add(loginBtn);
        center.add(registroBtn);

        root.add(center, BorderLayout.CENTER);
        frame.setContentPane(root);
        frame.setVisible(true);
    }

    // -------------------- Login --------------------
    private void mostrarLogin() {
        JFrame frame = new JFrame("Login - SaludDirecta");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(40, 40, 40));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        gbc.gridx = 0;
        panel.add(new JLabel("Correo:"), gbc);
        gbc.gridx = 1;
        JTextField correoText = new JTextField(20);
        panel.add(correoText, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        JPasswordField passText = new JPasswordField(20);
        panel.add(passText, gbc);

        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 2;
        JButton loginButton = createPrimaryButton("Iniciar Sesión", new Color(75, 175, 80));
        panel.add(loginButton, gbc);

        gbc.gridy++;
        JLabel mensaje = new JLabel("", SwingConstants.CENTER);
        mensaje.setForeground(Color.RED);
        panel.add(mensaje, gbc);

        loginButton.addActionListener(e -> {
            String correo = correoText.getText().trim();
            String contraseña = new String(passText.getPassword()).trim();

            if (correo.isEmpty() || contraseña.isEmpty()) {
                mensaje.setText("Completa correo y contraseña.");
                return;
            }

            Usuario usuarioLogin = controladorLogin.login(correo, contraseña);
            if (usuarioLogin != null) {
                mensaje.setText("¡Login exitoso!");
                mensaje.setForeground(new Color(75, 175, 80));
                Usuario usuario = usuarioDB.buscarPorCorreo(correo);
                System.out.println("Usuario logueado: " + (usuario != null ? usuario.getNombre() + ", rol: " + usuario.getRol() : "desconocido"));
                // Aquí podrías abrir la siguiente pantalla según rol
                frame.dispose();
            } else {
                mensaje.setText("Correo o contraseña incorrectos.");
                mensaje.setForeground(Color.RED);
            }
        });

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    // -------------------- Registro (UI + flujo BD) --------------------
    private void mostrarRegistro() {
        JFrame frame = new JFrame("Registro - SaludDirecta");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 720);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        JLabel title = new JLabel("Crear nuevo usuario", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(40, 40, 40));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panel.add(title, gbc);
        gbc.gridwidth = 1;

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField nombreText = new JTextField();
        addRow(panel, lblNombre, nombreText, gbc, row++);

        JLabel lblCorreo = new JLabel("Correo:");
        JTextField correoText = new JTextField();
        addRow(panel, lblCorreo, correoText, gbc, row++);

        JLabel lblPass = new JLabel("Contraseña:");
        JPasswordField passText = new JPasswordField();
        addRow(panel, lblPass, passText, gbc, row++);

        JLabel lblId = new JLabel("ID:");
        JTextField idText = new JTextField();
        addRow(panel, lblId, idText, gbc, row++);

        JLabel lblTelefono = new JLabel("Teléfono:");
        JTextField telText = new JTextField();
        addRow(panel, lblTelefono, telText, gbc, row++);

        JLabel lblTipoSangre = new JLabel("Tipo sangre:");
        JTextField tipoSangreText = new JTextField();
        addRow(panel, lblTipoSangre, tipoSangreText, gbc, row++);

        JLabel lblAltura = new JLabel("Altura (cm):");
        JTextField alturaText = new JTextField();
        addRow(panel, lblAltura, alturaText, gbc, row++);

        JLabel lblPeso = new JLabel("Peso (kg):");
        JTextField pesoText = new JTextField();
        addRow(panel, lblPeso, pesoText, gbc, row++);

        JLabel lblEdad = new JLabel("Edad:");
        JTextField edadText = new JTextField();
        addRow(panel, lblEdad, edadText, gbc, row++);

        JLabel lblSexo = new JLabel("Sexo:");
        JTextField sexoText = new JTextField();
        addRow(panel, lblSexo, sexoText, gbc, row++);

        JLabel lblAlergias = new JLabel("Alergias:");
        JTextField alergiasText = new JTextField();
        addRow(panel, lblAlergias, alergiasText, gbc, row++);

        // botón crear
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 8, 4, 8);
        JButton crearBtn = createPrimaryButton("Crear Usuario", new Color(80, 140, 255));
        crearBtn.setPreferredSize(new Dimension(200, 36));
        panel.add(crearBtn, gbc);

        gbc.gridy++;
        JLabel mensaje = new JLabel("", SwingConstants.CENTER);
        mensaje.setForeground(Color.RED);
        panel.add(mensaje, gbc);

        // Acción del botón: flujo BD (insertar usuario -> insertar paciente)
        crearBtn.addActionListener(e -> {
            try {
                // Validaciones básicas
                String nombre = nombreText.getText().trim();
                String correo = correoText.getText().trim();
                String contraseña = new String(passText.getPassword()).trim();
                if (nombre.isEmpty() || correo.isEmpty() || contraseña.isEmpty()) {
                    mensaje.setText("Nombre, correo y contraseña son obligatorios.");
                    return;
                }

                long id;
                long telefono;
                try {
                    id = Long.parseLong(idText.getText().trim());
                    telefono = Long.parseLong(telText.getText().trim());
                } catch (NumberFormatException nfe) {
                    mensaje.setText("ID y Teléfono deben ser números enteros.");
                    return;
                }

                // Campos opcionales: si están vacíos, pasar 0 para evitar NPE al unboxear
                float altura = parseFloatOrDefault(alturaText.getText().trim(), 0f);
                float peso = parseFloatOrDefault(pesoText.getText().trim(), 0f);
                int edad = parseIntOrDefault(edadText.getText().trim(), 0);

                String tipoSangre = tipoSangreText.getText().trim();
                String sexo = sexoText.getText().trim();
                String alergias = alergiasText.getText().trim();

                // Construir usuario y persistir en la tabla 'usuario'
                Usuario nuevo = new Usuario(nombre, correo, contraseña, (double) id, (double) telefono, "paciente");

                boolean usuarioCreado = usuarioDB.insertar(nuevo);
                if (!usuarioCreado) {
                    mensaje.setText("Error creando usuario (tabla usuario).");
                    return;
                }

                // Insertar paciente: llamamos al controller que delega a UsuarioDB
                try {
                    Paciente p = userController.ponerDatosEspecificosPaciente(nuevo,
                            tipoSangre,
                            altura,
                            peso,
                            edad,
                            alergias,
                            sexo
                    );

                    if (p != null) {
                        mensaje.setText("Usuario y paciente creados correctamente.");
                        mensaje.setForeground(new Color(60, 160, 70));
                        // opcional: cerrar ventana o limpiar campos
                        // frame.dispose();
                    } else {
                        mensaje.setText("Usuario creado, pero fallo al crear datos de paciente.");
                        mensaje.setForeground(Color.RED);
                    }
                } catch (SQLException sqlEx) {
                    // Si falla insertar paciente, podrías intentar eliminar el usuario para dejar consistencia
                    mensaje.setText("Error al insertar datos de paciente: " + sqlEx.getMessage());
                    mensaje.setForeground(Color.RED);
                    sqlEx.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                mensaje.setText("Ocurrió un error. Revisa la consola.");
                mensaje.setForeground(Color.RED);
            }
        });

        // Añadir escape (al cerrar volver a pantalla inicial)
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // opcional: mostrarPantallaInicial();
            }
        });

        frame.setContentPane(new JScrollPane(panel));
        frame.setVisible(true);
    }

    // ---------- Helpers UI ----------
    private static void addRow(JPanel panel, JComponent left, JComponent right, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.25;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(left, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        panel.add(right, gbc);
    }

    private static JButton createPrimaryButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return b;
    }

    private static float parseFloatOrDefault(String s, float def) {
        if (s == null || s.isEmpty()) return def;
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException ex) {
            return def;
        }
    }

    private static int parseIntOrDefault(String s, int def) {
        if (s == null || s.isEmpty()) return def;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return def;
        }
    }
}
