import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReturnJFrame extends JFrame {

    public ReturnJFrame(int bookId) {
        initComponents(bookId);
        setTitle("Return Book");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents(int bookId) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        try {
            String jdbcURL = "jdbc:sqlite:library.db";
            Connection connection = DriverManager.getConnection(jdbcURL);

            String query = "SELECT * FROM Livre WHERE id_livre = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String bookTitle = resultSet.getString("titre");
                String author = resultSet.getString("auteur");
                String genre = resultSet.getString("genre");
                String availability = resultSet.getString("disponibilite");

                JLabel titleLabel = new JLabel("Returning Book: " + bookTitle);
                titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
                mainPanel.add(titleLabel, BorderLayout.NORTH);

                JLabel authorLabel = new JLabel("Author: " + author);
                JLabel genreLabel = new JLabel("Genre: " + genre);
                JLabel availabilityLabel = new JLabel("Availability: " + availability);

                JPanel infoPanel = new JPanel(new GridLayout(4, 1));
                infoPanel.add(authorLabel);
                infoPanel.add(genreLabel);
                infoPanel.add(availabilityLabel);

                mainPanel.add(infoPanel, BorderLayout.CENTER);

                // Display current date and time
                JLabel dateLabel = new JLabel("Date of Return: " + getCurrentDateTime());
                dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(dateLabel, BorderLayout.SOUTH);

                confirmReturn(bookId, getCurrentDateTime(), connection);

            } else {
                JOptionPane.showMessageDialog(null, "Book not found.");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        add(mainPanel);
        setResizable(false);
    }


    private void confirmReturn(int bookId, String returnTime, Connection connection) {
        try {
           
            String updateQuery = "UPDATE Emprunt SET date_retour = ?, statut = ? WHERE id_livre = ? AND statut = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, returnTime);
            updateStatement.setString(2, "termine");
            updateStatement.setInt(3, bookId);
            updateStatement.setString(4, "en cours");
            updateStatement.executeUpdate();

        
            String livreUpdateQuery = "UPDATE Livre SET disponibilite = ? WHERE id_livre = ?";
            PreparedStatement livreUpdateStatement = connection.prepareStatement(livreUpdateQuery);
            livreUpdateStatement.setString(1, "Available");
            livreUpdateStatement.setInt(2, bookId);
            livreUpdateStatement.executeUpdate();
            updateStatement.close();
            livreUpdateStatement.close();
            EmailSender.Send(getTitle(), "e.librairie.manager@gmail.com");

        } catch (SQLException ex) {
     
        }
    }

    private String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDateTime.format(formatter);
    }
}