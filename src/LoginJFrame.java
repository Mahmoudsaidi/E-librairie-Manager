import javax.swing.*;
import java.awt.*;

public class LoginJFrame extends JFrame {

    public static JTextField usernameField;
    public JTextField nomField;
    public JTextField prenomField;
    public JTextField signupUsernameField;
    public static JPasswordField passwordField;
    public JPasswordField signupPasswordField;
    public JButton loginButton, signupButton;
    public JComboBox<String> roleComboBox;

    private static final int x = 300;
    private static final int y = 120;

    public LoginJFrame() {
        setAppIcon();
        initComponents();
    }

    private void setAppIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("ProgIcon.png"));
        setIconImage(icon.getImage());
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login");
        setResizable(false);
        //creating a tabed panel
        JTabbedPane tabbedPane = new JTabbedPane();
        //creatio every sub panel 
        JPanel loginPanel = createLoginPanel();
        JPanel signupPanel = createSignupPanel();
        //adding those as a add in the main tabbed panel with there icons and titels
        tabbedPane.addTab("Login", loginPanel);
        tabbedPane.addTab("Signup", signupPanel);
        add(tabbedPane);
        ImageIcon loginIcon = new ImageIcon(getClass().getResource("LoginIcon.jpg"));
        tabbedPane.setIconAt(0, loginIcon);

        ImageIcon signupIcon = new ImageIcon(getClass().getResource("signup.png"));
        tabbedPane.setIconAt(1, signupIcon);
        //Frame Config
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JPanel createSignupPanel() {
        //creating a Panel for the signup

        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(null);
        signupPanel.setBackground(new Color(220, 220, 220));
        //Just a text
        JLabel SignupLabel = new JLabel("Signup");
        SignupLabel.setBounds(x, y - 100, 120, 40);
        SignupLabel.setForeground(Color.BLUE);
        Font labelFont = new Font("Arial", Font.BOLD, 22);
        SignupLabel.setFont(labelFont);
        signupPanel.add(SignupLabel);

        //text for first name
        JLabel nomLabel = new JLabel("First Name:");
        nomLabel.setBounds(x, y, 100, 20);
        signupPanel.add(nomLabel);
        //text for Last name
        JLabel prenomLabel = new JLabel("Last nom:");
        prenomLabel.setBounds(x, y + 50, 100, 20);
        signupPanel.add(prenomLabel);
        //text for Username
        JLabel signupUsernameLabel = new JLabel("Username:");
        signupUsernameLabel.setBounds(x, y + 100, 100, 20);
        signupPanel.add(signupUsernameLabel);
        //text for Password
        JLabel signupPasswordLabel = new JLabel("Password:");
        signupPasswordLabel.setBounds(x, y + 150, 100, 20);
        signupPanel.add(signupPasswordLabel);
        //text field for First name
        nomField = new JTextField();
        nomField.setBounds(x + 100, y, 150, 20);
        signupPanel.add(nomField);
        //text field for Last name
        prenomField = new JTextField();
        prenomField.setBounds(x + 100, y + 50, 150, 20);
        signupPanel.add(prenomField);
        //text field for Username
        signupUsernameField = new JTextField();
        signupUsernameField.setBounds(x + 100, y + 100, 150, 20);
        signupPanel.add(signupUsernameField);
        //textt field for Password with the ********* 'masked'
        signupPasswordField = new JPasswordField();
        signupPasswordField.setBounds(x + 100, y + 150, 150, 20);
        signupPanel.add(signupPasswordField);
        //multchoix "ComboBox" for role
        roleComboBox = new JComboBox<>(new String[] { "Student", "Teacher", "Librarian" });
        roleComboBox.setMaximumRowCount(3);
        roleComboBox.setBounds(x + 100, y + 200, 150, 20);
        signupPanel.add(roleComboBox);
        //a sign up button
        signupButton = new JButton("Signup");
        signupButton.setBounds(x + 100, y + 250, 100, 30);
        signupButton.setBackground(new Color(50, 150, 200));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        signupPanel.add(signupButton);
        // Creation de obj SignupSzevice 
        SignupService signupService = new SignupService();
        //Signup button listener
        signupButton.addActionListener(e -> {
            // getting informations into local variables ("from the ui")
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String signupUsername = signupUsernameField.getText().trim();
            String signupPassword = new String(signupPasswordField.getPassword());
            String role = roleComboBox.getSelectedItem().toString();

            // the signup method from SignupService class
            signupService.signup(nom, prenom, signupUsername, signupPassword, role);
        });
        //Login button listener
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            // the login method from loginService class
            LoginService.login(username, password);

        });
        //backgound Icon Implementation
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("SignupBackGound.jpg"));
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(0, 0, 277, 480);
        signupPanel.add(imageLabel);

        return signupPanel;
    }
    // creatig the login Panel with tther lables and button
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

    /*             Helpers             */
    /**********************************/
    public JButton getLoginButton() {
        return loginButton;
    }

    public static String getUsername() {
        return usernameField.getText().trim();
    }

    public static String getpassword() {
        return String.valueOf(passwordField.getPassword());
    }

}