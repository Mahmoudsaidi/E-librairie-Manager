import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;

public class RentJFrame extends JFrame {

    public RentJFrame(int bookId) {
        initComponents(bookId);
        setAppIcon();
    }
    private void setAppIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("ProgIcon.png"));
        setIconImage(icon.getImage());
    }

    private void initComponents(int bookId) {

        if (checkBookAvailability(bookId)) {
            setTitle("Rent Book");
            setSize(400, 300);
            setLocationRelativeTo(null);

        setVisible(true);
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

                    JLabel titleLabel = new JLabel("Renting Book: " + bookTitle);
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

                    JLabel dateLabel = new JLabel("Date of Rent: " + getCurrentDateTime());
                    dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    mainPanel.add(dateLabel, BorderLayout.SOUTH);

                    String insertQuery = "INSERT INTO Emprunt (id_utilisateur, id_livre, date_emprunt, statut) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setInt(1,
                    Utilitys.getUserIdFromDatabase(LoginJFrame.getUsername(), LoginJFrame.getpassword()));
                    insertStatement.setInt(2, bookId);
                    insertStatement.setString(3, getCurrentDateTime());
                    insertStatement.setString(4, "en cours");
                    insertStatement.executeUpdate();

                    String updateQuery = "UPDATE Livre SET disponibilite = 'Indisponible' WHERE id_livre = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, bookId);
                    updateStatement.executeUpdate();

                    resultSet.close();
                    preparedStatement.close();
                    insertStatement.close();
                    updateStatement.close();
                }
                connection.close();
            } catch (SQLException ex) {
                
            }
            add(mainPanel);
            setResizable(false);
        } else
        {
            new ReservationJFrame(bookId);
        }
    }

    private String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDateTime.format(formatter);
    }

    private boolean checkBookAvailability(int bookId) {
        boolean isAvailable = false;
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            String query = "SELECT disponibilite FROM Livre WHERE id_livre = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String availability = resultSet.getString("disponibilite");
                isAvailable = availability.equals("Available");
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
           
        }

        return isAvailable;
    }

}
