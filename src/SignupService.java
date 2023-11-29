import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class SignupService {
    public void signup(String nom, String prenom, String signupUsername, String signupPassword, String role) {
             // Check if any of the input fields are empty
        if (isEmptyOrNull(nom) || isEmptyOrNull(prenom) || isEmptyOrNull(signupUsername) || isEmptyOrNull(signupPassword) || isEmptyOrNull(role)) {
            System.out.println("Please fill in all fields.");
            return;
        }

                // Database connection details
            String JDBC_URL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            // Prepare SQL statement for inserting user information into the Utilisateur table
            String sql = "INSERT INTO Utilisateur (nom, prenom, login, pwd, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            // Set parameter values in the PreparedStatement
            pstmt.setString(1, nom);
            pstmt.setString(2, prenom);
            pstmt.setString(3, signupUsername);
            pstmt.setString(4, signupPassword);
            pstmt.setString(5, role);

            // Execute the PreparedStatement to insert user information into the database
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "User registered successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to register user.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // Helper method to check if a string is null or empty
    private boolean isEmptyOrNull(String str) {
        return str == null || str.trim().isEmpty();
    }
}