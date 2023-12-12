import javax.swing.SwingUtilities;

public class App {
    //Main Function that initialize the database using the SQLiteDBInitializer class Then setup Login Interface
    public static void main(String[] args) {
        initializeDatabase();
        setupLoginInterface();
    }
    // creatin a Login Jframe
    private static void setupLoginInterface() {
        LoginJFrame loginFrame = new LoginJFrame();
        // Init the Login frame/window
        loginFrame.getLoginButton().addActionListener(e -> handleLoginAction(loginFrame));
    }
    //check the user User(user) or lib(Admin) using Method Helpers then call the open function based of the user 
    private static void handleLoginAction(LoginJFrame loginFrame) {
        //Get Username
        String username = LoginJFrame.getUsername();
        SwingUtilities.invokeLater(() -> {
            if (isStudentOrTeacher(username) && LoginService.login(username, LoginJFrame.getpassword())) {
                //this is a user
                openUserInterface();
                loginFrame.dispose();
            } else if ("Librarian".equals(LoginService.getLoginRole(username)) && LoginService.login(username, LoginJFrame.getpassword())) {
                //this is a admin
                openLibrarianInterface();
                loginFrame.dispose();
            }
        });
    }
    //Helper Function tto see if its a user nott admin
    private static boolean isStudentOrTeacher(String username) {
        String role = LoginService.getLoginRole(username);
        return "Student".equals(role) || "Teacher".equals(role);
    }
    //Open tthe Userinterface
    private static void openUserInterface() {
        new UserJFrame().setVisible(true);
    }
    //open the librarianInterface
    private static void openLibrarianInterface() {
        new LibrarianJFrame().setVisible(true);
    }
    //Initt Data base
    private static void initializeDatabase() {
        try {
            SQLiteDBInitializer dbInitializer = new SQLiteDBInitializer();
            dbInitializer.createTables();
        } catch (Exception e) {

        }
    }
}
