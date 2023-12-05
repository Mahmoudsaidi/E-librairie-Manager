import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        initializeDatabase();
        LoginJFrame login = new LoginJFrame();
        login.getLoginButton().addActionListener(e -> {
             String username = LoginJFrame.getUsername();
            SwingUtilities.invokeLater(() -> {
                if (isStudentOrTeacher(username)) {
                    new UserJFrame().setVisible(true);
                    login.dispose(); 
                } else if ("Librarian".equals(LoginService.getLoginRole(username))) {
                    new LibrarianJFrame().setVisible(true);
                    login.dispose(); 
                }
            });
        });
    }
    
    
    private static boolean isStudentOrTeacher(String username) {
        String role = LoginService.getLoginRole(username);
        return "Student".equals(role) || "Teacher".equals(role);
    }
    

    private static void initializeDatabase() {
        try {
            SQLiteDBInitializer dbInitializer = new SQLiteDBInitializer();
            dbInitializer.createTables();
        } catch (Exception e) {
    
        }
    }
    
}
