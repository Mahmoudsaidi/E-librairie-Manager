import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        initializeDatabase();
        setupLoginInterface();
    }
    
    private static void setupLoginInterface() {
        LoginJFrame loginFrame = new LoginJFrame();
        loginFrame.getLoginButton().addActionListener(e -> handleLoginAction(loginFrame));
    }

    private static void handleLoginAction(LoginJFrame loginFrame) {
        String username = LoginJFrame.getUsername();
        SwingUtilities.invokeLater(() -> {
            if (isStudentOrTeacher(username) && LoginService.login(username, LoginJFrame.getpassword())) {
                openUserInterface();
                loginFrame.dispose();
            } else if ("Librarian".equals(LoginService.getLoginRole(username)) && LoginService.login(username, LoginJFrame.getpassword())) {
                openLibrarianInterface();
                loginFrame.dispose();
            }
        });
    }

    private static boolean isStudentOrTeacher(String username) {
        String role = LoginService.getLoginRole(username);
        return "Student".equals(role) || "Teacher".equals(role);
    }

    private static void openUserInterface() {
        new UserJFrame().setVisible(true);
    }

    private static void openLibrarianInterface() {
        new LibrarianJFrame().setVisible(true);
    }

    private static void initializeDatabase() {
        try {
            SQLiteDBInitializer dbInitializer = new SQLiteDBInitializer();
            dbInitializer.createTables();
        } catch (Exception e) {

        }
    }
}
