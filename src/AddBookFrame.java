import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBookFrame extends JFrame {

    public AddBookFrame() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Add Book");
        setResizable(false);

        JPanel addBookPanel = new JPanel();
        addBookPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(50, 50, 80, 20);
        addBookPanel.add(titleLabel);

        JTextField titleField = new JTextField();
        titleField.setBounds(150, 50, 200, 20);
        addBookPanel.add(titleField);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(50, 100, 80, 20);
        addBookPanel.add(authorLabel);

        JTextField authorField = new JTextField();
        authorField.setBounds(150, 100, 200, 20);
        addBookPanel.add(authorField);

        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setBounds(50, 150, 80, 20);
        addBookPanel.add(genreLabel);

        JTextField genreField = new JTextField();
        genreField.setBounds(150, 150, 200, 20);
        addBookPanel.add(genreField);

        JButton addButton = new JButton("Add");
        addButton.setBounds(150, 200, 80, 30);
        addBookPanel.add(addButton);

        // ActionListener for the Add button
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String genre = genreField.getText().trim();

            if (!title.isEmpty() && !author.isEmpty() && !genre.isEmpty()) {
                // Insert the book details into the database
                insertBookIntoDatabase(title, author, genre);
                // Optionally, show a confirmation dialog or perform other actions upon successful insertion
                JOptionPane.showMessageDialog(this, "Book added successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
            }
        });

        add(addBookPanel);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void insertBookIntoDatabase(String title, String author, String genre) {
        // JDBC URL for SQLite database
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            if (connection != null) {
                String insertQuery = "INSERT INTO Livre (titre, auteur, genre, disponibilite) VALUES (?, ?, ?, 'Available')";

                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, genre);

                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
