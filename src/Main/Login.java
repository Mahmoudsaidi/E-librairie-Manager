package Main;
import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private JTextField usernameField, nomField, prenomField, signupUsernameField;
    private JPasswordField passwordField, signupPasswordField;
    private JButton loginButton, signupButton;
    private JComboBox<String> roleComboBox;

    private static final int x = 300; // Example x coordinate 50
    private static final int y = 120; // Example y coordinate

    public Login() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login/Signup");
        setResizable(false);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel loginPanel = createLoginPanel();
        JPanel signupPanel = createSignupPanel();

        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Signup", signupPanel);

        add(tabbedPane);

        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(240, 240, 240));

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

        ImageIcon imageIcon = new ImageIcon("RightSidePanel.PNG");
        Image image = imageIcon.getImage(); // Extract Image object from ImageIcon
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        loginPanel.add(imageLabel, BorderLayout.CENTER);
        

        return loginPanel;
    }

    private JPanel createSignupPanel() {
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(null);
        signupPanel.setBackground(new Color(220, 220, 220));

        JLabel nomLabel = new JLabel("Nom:");
        nomLabel.setBounds(x, y, 100, 20);
        signupPanel.add(nomLabel);

        JLabel prenomLabel = new JLabel("Prenom:");
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

        roleComboBox = new JComboBox<>(new String[]{"Étudiant", "Enseignant", "Bibliothécaire"});
        roleComboBox.setMaximumRowCount(3);
        roleComboBox.setBounds(x + 100, y + 200, 150, 20);
        signupPanel.add(roleComboBox);

        signupButton = new JButton("Signup");
        signupButton.setBounds(x + 100, y + 250, 100, 30);
        signupButton.setBackground(new Color(50, 150, 200));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        signupPanel.add(signupButton);


        ImageIcon imageIcon = new ImageIcon("signupBackGround.png");

        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(400, 0, 400, 500); // Adjust the bounds according to your image size and positioning
        signupPanel.add(imageLabel);

        return signupPanel;
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new Login();
        });
    }
}