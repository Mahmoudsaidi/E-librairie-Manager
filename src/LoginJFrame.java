
import javax.swing.*;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginJFrame extends JFrame {

    public static JTextField usernameField;
    public JTextField nomField;
    public JTextField prenomField;
    public JTextField signupUsernameField;
    public static JPasswordField passwordField;
    public JPasswordField signupPasswordField;
    public JButton loginButton, signupButton;
    public JComboBox<String> roleComboBox;

    private static final int x = 300; // Example x coordinate 50
    private static final int y = 120; // Example y coordinate

    public LoginJFrame() {
        setAppIcon();
        initComponents(); 
    }
    private void setAppIcon() {
        // Load the icon image using getResource()
        ImageIcon icon = new ImageIcon(getClass().getResource("ProgIcon.png"));
        setIconImage(icon.getImage());
    }
     


    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login");
        setResizable(false);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel loginPanel = createLoginPanel();
        JPanel signupPanel = createSignupPanel();

        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Signup", signupPanel);

        add(tabbedPane);
        ImageIcon loginIcon = new ImageIcon(getClass().getResource("LoginIcon.jpg"));
        tabbedPane.setIconAt(0, loginIcon);

        ImageIcon signupIcon = new ImageIcon(getClass().getResource("signup.png"));
        tabbedPane.setIconAt(1, signupIcon);

        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(220, 220, 220));
        
        JLabel loginLabel = new JLabel("Login");
        loginLabel.setBounds(x, y - 100, 120, 40);
        loginLabel.setForeground(Color.BLUE); 
        Font labelFont = new Font("Arial", Font.BOLD, 22); 
        loginLabel.setFont(labelFont);
        loginPanel.add(loginLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(x, y, 100, 20);
        loginPanel.add(usernameLabel);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(x, y + 50, 100, 20);
        loginPanel.add(passwordLabel);

        usernameField = new JTextField();
        usernameField.setBounds(x + 100, y, 150, 20);
        loginPanel.add(usernameField);

        passwordField = new JPasswordField();
        passwordField.setBounds(x + 100, y + 50, 150, 20);
        loginPanel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(x + 100, y + 100, 100, 30);
        loginButton.setBackground(new Color(50, 150, 200));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginPanel.add(loginButton);

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("RightSidePanel.PNG"));
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(0, 0, 277, 480);
        loginPanel.add(imageLabel);
        

        return loginPanel;
    }

   
   
    
    private JPanel createSignupPanel() {
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(null);
        signupPanel.setBackground(new Color(220, 220, 220));
        
        JLabel SignupLabel = new JLabel("Signup");
        SignupLabel.setBounds(x, y - 100, 120, 40);
        SignupLabel.setForeground(Color.BLUE); 
        Font labelFont = new Font("Arial", Font.BOLD, 22); 
        SignupLabel.setFont(labelFont);
        signupPanel.add(SignupLabel);


        JLabel nomLabel = new JLabel("First Name:");
        nomLabel.setBounds(x, y, 100, 20);
        signupPanel.add(nomLabel);

        JLabel prenomLabel = new JLabel("Last nom:");
        prenomLabel.setBounds(x, y + 50, 100, 20);
        signupPanel.add(prenomLabel);

        JLabel signupUsernameLabel = new JLabel("Username:");
        signupUsernameLabel.setBounds(x, y + 100, 100, 20);
        signupPanel.add(signupUsernameLabel);

        JLabel signupPasswordLabel = new JLabel("Password:");
        signupPasswordLabel.setBounds(x, y + 150, 100, 20);
        signupPanel.add(signupPasswordLabel);

        nomField = new JTextField();
        nomField.setBounds(x + 100, y, 150, 20);
        signupPanel.add(nomField);

        prenomField = new JTextField();
        prenomField.setBounds(x + 100, y + 50, 150, 20);
        signupPanel.add(prenomField);

        signupUsernameField = new JTextField();
        signupUsernameField.setBounds(x + 100, y + 100, 150, 20);
        signupPanel.add(signupUsernameField);

        signupPasswordField = new JPasswordField();
        signupPasswordField.setBounds(x + 100, y + 150, 150, 20);
        signupPanel.add(signupPasswordField);

        roleComboBox = new JComboBox<>(new String[]{"Student", "Teacher", "Librarian"});
        
        roleComboBox.setMaximumRowCount(3);
        roleComboBox.setBounds(x + 100, y + 200, 150, 20);
        signupPanel.add(roleComboBox);

        signupButton = new JButton("Signup");
        signupButton.setBounds(x + 100, y + 250, 100, 30);
        signupButton.setBackground(new Color(50, 150, 200));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        signupPanel.add(signupButton);


        SignupService signupService = new SignupService();
        

        // Assuming signupButton is your JButton for signup
        signupButton.addActionListener(e -> {
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String signupUsername = signupUsernameField.getText().trim();
            String signupPassword = new String(signupPasswordField.getPassword());
            String role = roleComboBox.getSelectedItem().toString();

            // Call the signup method from SignupService
            signupService.signup(nom, prenom, signupUsername, signupPassword, role);
            });
           

            loginButton.addActionListener(e -> {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                
                LoginService.login(username, password);

            
            });
           

        ImageIcon imageIcon = new ImageIcon(getClass().getResource("SignupBackGound.jpg"));
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(0, 0, 277, 480);
        signupPanel.add(imageLabel);


        return signupPanel;
    }
    public JButton getLoginButton() {
        return loginButton;
    }

    public static String getUsername() {
        return usernameField.getText().trim();
    }
    public static String getpassword() {
        return String.valueOf(passwordField.getPassword());
    }
    public static int getUserIdFromDatabase(String username, String password) {
        int userId = -1; // Initialize to an invalid value as -1 or any value that represents "not found"
        String jdbcURL = "jdbc:sqlite:library.db"; // Replace with your database URL

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            String query = "SELECT id_utilisateur FROM Utilisateur WHERE login = ? AND pwd = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("id_utilisateur");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return userId;
    }

}