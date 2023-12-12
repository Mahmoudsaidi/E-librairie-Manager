import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBookFrame extends JFrame {

    public AddBookFrame() {
        initComponents();
    }
    //this function Constract the addBook Frame / window
    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Add Book");
        setResizable(false);
        //create a white window with no Compnent 
        JPanel addBookPanel = new JPanel();
        addBookPanel.setLayout(null);
        //just a text for ttitle 
        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(50, 50, 80, 20);
        addBookPanel.add(titleLabel);
        //a field for title  
        JTextField titleField = new JTextField();
        titleField.setBounds(150, 50, 200, 20);
        addBookPanel.add(titleField);
        //just text for the Auther 
        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(50, 100, 80, 20);
        addBookPanel.add(authorLabel);
        //a field for Auther
        JTextField authorField = new JTextField();
        authorField.setBounds(150, 100, 200, 20);
        addBookPanel.add(authorField);
        //just text for the Genre
        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setBounds(50, 150, 80, 20);
        addBookPanel.add(genreLabel);
        //a field for Genre
        JTextField genreField = new JTextField();
        genreField.setBounds(150, 150, 200, 20);
        addBookPanel.add(genreField);
        // a button to add a book
        JButton addButton = new JButton("Add");
        addButton.setBounds(150, 200, 80, 30);
        addBookPanel.add(addButton);

        // Listener for the Add button
        addButton.addActionListener(e -> {
            //getting informations from the ui
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();
            //checking if they are not Empty check the IsEmpty Function blow
            if (!title.isEmpty() && !author.isEmpty() && !genre.isEmpty()) {
                // Insert the book into the database
                insertBookIntoDatabase(title, author, genre);
                JOptionPane.showMessageDialog(this, "Book added successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
            }
        });
        //Add those Componet to the frame that we made 
        add(addBookPanel);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    // Function to add a book to the data base 
    private void insertBookIntoDatabase(String title, String author, String genre) {
        // JDBC URL for SqLite database
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            if (connection != null) {
                String insertQuery = "INSERT INTO Livre (titre, auteur, genre, disponibilite) VALUES (?, ?, ?, 'Available')";

                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, genre);
                //preparedStatement.setString(3, "Available");
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
