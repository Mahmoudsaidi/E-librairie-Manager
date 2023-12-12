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

    // Setup app icon
    private void setAppIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("ProgIcon.png"));
        setIconImage(icon.getImage());
    }

    // Inti the components
    private void initComponents(int bookId) {
        // First checkin g for the book availablity
        if (checkBookAvailability(bookId)) {
            // frame Setups
            setTitle("Rent Book");
            setSize(400, 300);
            setLocationRelativeTo(null);
            setVisible(true);
            // creation of the main Panel "empty"
            JPanel mainPanel = new JPanel(new BorderLayout());

            try {
                // Url/ .bd path
                String jdbcURL = "jdbc:sqlite:library.db";
                // Connect
                Connection connection = DriverManager.getConnection(jdbcURL);
                // Get Every book from table where id == this.id from constractor
                String query = "SELECT * FROM Livre WHERE id_livre = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, bookId);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String bookTitle = resultSet.getString("titre");
                    String author = resultSet.getString("auteur");
                    String genre = resultSet.getString("genre");
                    String availability = resultSet.getString("disponibilite");
                    // justt a static text
                    JLabel titleLabel = new JLabel("Renting Book: " + bookTitle);
                    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
                    mainPanel.add(titleLabel, BorderLayout.NORTH);
                    // justt a static text
                    JLabel authorLabel = new JLabel("Author: " + author);
                    authorLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    authorLabel.setFont(new Font("Arial", Font.BOLD, 18));
                    // justt a static text
                    JLabel genreLabel = new JLabel("Genre: " + genre);
                    genreLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    genreLabel.setFont(new Font("Arial", Font.BOLD, 18));
                    // justt a static text
                    JLabel availabilityLabel = new JLabel("Availability: " + availability);
                    availabilityLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    availabilityLabel.setFont(new Font("Arial", Font.BOLD, 18));
                    // Cut the panel into 4 pieces
                    JPanel infoPanel = new JPanel(new GridLayout(4, 1));
                    // add those to the infoPanel
                    infoPanel.add(authorLabel);
                    infoPanel.add(genreLabel);
                    infoPanel.add(availabilityLabel);
                    // add the infoPanel to the main Panel
                    mainPanel.add(infoPanel, BorderLayout.CENTER);
                    // A text to display the courentdatte from the pc Timer "Date of the rent"
                    JLabel dateLabel = new JLabel("Date of Rent: " + getCurrentDateTime());
                    dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    // add the date to mainPanel
                    mainPanel.add(dateLabel, BorderLayout.SOUTH);
                    // Add those nformatin n the empunt Table using iduser as the refrebce
                    String insertQuery = "INSERT INTO Emprunt (id_utilisateur, id_livre, date_emprunt, statut) VALUES (?, ?, ?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setInt(1,
                            Utilitys.getUserIdFromDatabase(LoginJFrame.getUsername(), LoginJFrame.getpassword()));
                    insertStatement.setInt(2, bookId);
                    insertStatement.setString(3, getCurrentDateTime());
                    insertStatement.setString(4, "en cours");
                    insertStatement.executeUpdate();
                    // Update the diponibilite in the livre table using the book Id
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
            // Add the mainPanel to the window
            add(mainPanel);
            setResizable(false);
        } else { // if the book is not availble we go to the reservation class
            new ReservationJFrame(bookId);
        }
    }

    // return tthe CurrentTme
    private String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDateTime.format(formatter);
    }

    // check the book in the data base if existt return true
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
