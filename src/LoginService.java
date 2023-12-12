import java.sql.*;

public class LoginService {
    private static final String JDBC_URL = "jdbc:sqlite:library.db";
    //Login tester function true/false
    public static boolean login(String username, String password) {
        boolean isValid = false;
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            //scaning the data base for login and password
            String query = "SELECT * FROM Utilisateur WHERE login = ? AND pwd = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            isValid = resultSet.next(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    public static String getLoginRole(String username) {
        //this function gettes the login role using the username
        String role = "";
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            String query = "SELECT role FROM Utilisateur WHERE login = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                role = resultSet.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }
}
