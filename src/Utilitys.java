import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;



public class Utilitys {


    public static int getBookIdFromDatabase(String bookTitle) {
        int bookId = 0;
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            String query = "SELECT id_livre FROM Livre WHERE titre = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, bookTitle);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                bookId = resultSet.getInt("id_livre");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return bookId;
    }

    public static int getUserIdFromDatabase(String username, String password) {
        int userId = -1;
        String jdbcURL = "jdbc:sqlite:library.db";

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

    public static String getTitle(int bookId) {
        String title = null;
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            if (connection != null) {
                String query = "SELECT titre FROM Livre WHERE id_livre = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, bookId);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    title = resultSet.getString("titre");
                }

                resultSet.close();
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions here
        }

        return title;
    }

    public static void updateComboList(int userId, JComboBox<String> rentedBooksComboBox) {
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            String query = "SELECT Livre.titre, Emprunt.date_emprunt, Emprunt.statut " +
                    "FROM Emprunt " +
                    "INNER JOIN Livre ON Emprunt.id_livre = Livre.id_livre " +
                    "WHERE Emprunt.id_utilisateur = ? AND Emprunt.statut = 'en cours'";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("titre");
                if (!comboContainsItem(rentedBooksComboBox, title)) {
                    rentedBooksComboBox.addItem(title);
                }
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean comboContainsItem(JComboBox<String> comboBox, String item) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(item)) {
                return true;
            }
        }
        return false;
    }

}
