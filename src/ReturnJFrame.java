import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReturnJFrame extends JFrame {

    public ReturnJFrame(int bookId) {
        initComponents(bookId);
        setAppIcon();
        //frame Configration
        setTitle("Return Book");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setAppIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("ProgIcon.png"));
        setIconImage(icon.getImage());
    }
    //init pANEL
    private void initComponents(int bookId) {
        //Creation a main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        try {
            // url/path
            String jdbcURL = "jdbc:sqlite:library.db";
            // connecting 
            Connection connection = DriverManager.getConnection(jdbcURL);

            String query = "SELECT * FROM Livre WHERE id_livre = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // getting informations from the qery
                String bookTitle = resultSet.getString("titre");
                String author = resultSet.getString("auteur");
                String genre = resultSet.getString("genre");
                String availability = resultSet.getString("disponibilite");
                //Printt the name of the books thatt gonna be returned
                JLabel titleLabel = new JLabel("Returning Book: " + bookTitle);
                titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
                mainPanel.add(titleLabel, BorderLayout.NORTH);
                //Printt the Author
                JLabel authorLabel = new JLabel("Author: " + author);
                 titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
                mainPanel.add(titleLabel, BorderLayout.NORTH);
                //Printt the Genre 
                JLabel genreLabel = new JLabel("Genre: " + genre);
                 titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
                mainPanel.add(titleLabel, BorderLayout.NORTH);
                 //Printt the Availability 
                JLabel availabilityLabel = new JLabel("Availability: " + availability);
                 titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
                mainPanel.add(titleLabel, BorderLayout.NORTH);
                //creatte and add those to the infoPanel 
                JPanel infoPanel = new JPanel(new GridLayout(4, 1));
                infoPanel.add(authorLabel);
                infoPanel.add(genreLabel);
                infoPanel.add(availabilityLabel);
                //theen add them in the main panel
                mainPanel.add(infoPanel, BorderLayout.CENTER);
                //Print the curent time
                JLabel dateLabel = new JLabel("Date of Return: " + getCurrentDateTime());
                dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
                //add infopanel to the main panel
                mainPanel.add(dateLabel, BorderLayout.SOUTH);
                //send those to the functton named Confirmreturn Deffined blow
                confirmReturn(bookId, getCurrentDateTime(), connection);

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
            //Updating the emprunt table to termine n the data base 
            String updateQuery = "UPDATE Emprunt SET date_retour = ?, statut = ? WHERE id_livre = ? AND statut = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setString(1, returnTime);
            updateStatement.setString(2, "termine");
            updateStatement.setInt(3, bookId);
            updateStatement.setString(4, "en cours");
            updateStatement.executeUpdate();

            //updating the Avalablty and sendng an email 
            String livreUpdateQuery = "UPDATE Livre SET disponibilite = ? WHERE id_livre = ?";
            PreparedStatement livreUpdateStatement = connection.prepareStatement(livreUpdateQuery);
            livreUpdateStatement.setString(1, "Available");
            livreUpdateStatement.setInt(2, bookId);
            livreUpdateStatement.executeUpdate();
            updateStatement.close();
            livreUpdateStatement.close();
            //Class have to re vue
            EmailSender.Send(Utilitys.getTitle(bookId), "e.librairie.manager@gmail.com");

        } catch (SQLException ex) {
     
        }
    }
    //return the currentt date
    private String getCurrentDateTime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return currentDateTime.format(formatter);
    }
}
