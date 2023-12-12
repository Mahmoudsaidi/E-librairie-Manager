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
        setAppIcon();
        //Window Config
        setTitle("Book Reservation");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void setAppIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("ProgIcon.png"));
        setIconImage(icon.getImage());
    }
    //initalizer 
    private void initComponents(int bookId) {
        //create the main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        //create lablels
        titleLabel = new JLabel();
        dateLabel = new JLabel();
        bookInfoLabel = new JLabel();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:library.db")) {
            //Setup date and id
            String currentDate = getCurrentDateTime();
            int id_utilisateur = Utilitys.getUserIdFromDatabase(LoginJFrame.getUsername(), LoginJFrame.getpassword());
            //Insert Informattions
            String insertQuery = "INSERT INTO Reservation (id_utilisateur, id_livre, date_reservation, statut) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, id_utilisateur);
            insertStatement.setInt(2, bookId);
            insertStatement.setString(3, currentDate);
            insertStatement.setString(4, "en cours");
            insertStatement.executeUpdate();
            //Get in fromatios form he .bd to print them
            String bookQuery = "SELECT titre, auteur FROM Livre WHERE id_livre = ?";
            PreparedStatement bookStatement = connection.prepareStatement(bookQuery);
            bookStatement.setInt(1, bookId);
            ResultSet bookResultSet = bookStatement.executeQuery();

            if (bookResultSet.next()) {
                //Result of the qery
                String bookTitle = bookResultSet.getString("titre");
                String bookAuthor = bookResultSet.getString("auteur");
                //Prntt them in textLables  
                // 1/
                titleLabel.setText("Book Reserved: " + bookTitle);
                titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(titleLabel, BorderLayout.NORTH);
                // 2/
                bookInfoLabel.setText("Author: " + bookAuthor);
                bookInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(bookInfoLabel, BorderLayout.CENTER);
                // 3/
                dateLabel.setText("Date of Reservation: " + currentDate);
                dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
                mainPanel.add(dateLabel, BorderLayout.SOUTH);
            } else {
                JOptionPane.showMessageDialog(null, "Book details not found.");
            }
            //Close statment and connection 
            insertStatement.close();
            bookResultSet.close();
            bookStatement.close();
            JOptionPane.showMessageDialog(null, "Book reservation successful!");

        } catch (SQLException ex) {
            
        }

        //add those to the Frame
        add(mainPanel);
        setResizable(false);
    }

    private String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDateTime.format(formatter);
    }

}