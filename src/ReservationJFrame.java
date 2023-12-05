import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationJFrame extends JFrame {

    private JLabel titleLabel;
    private JLabel dateLabel;
    private JLabel bookInfoLabel;

    public ReservationJFrame(int bookId) {
        initComponents(bookId);
        setTitle("Book Reservation");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents(int bookId) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        titleLabel = new JLabel();
        dateLabel = new JLabel();
        bookInfoLabel = new JLabel();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:library.db")) {
            String currentDate = getCurrentDateTime();
            int id_utilisateur = LoginJFrame.getUserIdFromDatabase(LoginJFrame.getUsername(), LoginJFrame.getpassword());

            String insertQuery = "INSERT INTO Reservation (id_utilisateur, id_livre, date_reservation, statut) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, id_utilisateur);
            insertStatement.setInt(2, bookId);
            insertStatement.setString(3, currentDate);
            insertStatement.setString(4, "en cours");
            insertStatement.executeUpdate();

            String bookQuery = "SELECT titre, auteur FROM Livre WHERE id_livre = ?";
            PreparedStatement bookStatement = connection.prepareStatement(bookQuery);
            bookStatement.setInt(1, bookId);
            ResultSet bookResultSet = bookStatement.executeQuery();

            if (bookResultSet.next()) {
                String bookTitle = bookResultSet.getString("titre");
                String bookAuthor = bookResultSet.getString("auteur");

                titleLabel.setText("Book Reserved: " + bookTitle);
                titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(titleLabel, BorderLayout.NORTH);

                bookInfoLabel.setText("Author: " + bookAuthor);
                bookInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(bookInfoLabel, BorderLayout.CENTER);

                dateLabel.setText("Date of Reservation: " + currentDate);
                dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(dateLabel, BorderLayout.SOUTH);
            } else {
                JOptionPane.showMessageDialog(null, "Book details not found.");
            }

            insertStatement.close();
            bookResultSet.close();
            bookStatement.close();
            JOptionPane.showMessageDialog(null, "Book reservation successful!");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to reserve book.");
        }

        add(mainPanel);
        setResizable(false);
    }

    private String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDateTime.format(formatter);
    }

}