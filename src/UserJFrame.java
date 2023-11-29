import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserJFrame extends JFrame {
    private JTextField searchField;
    private DefaultTableModel searchTableModel;
    private JTable searchResultTable;
    private DefaultTableModel rentedTableModel;
    private JTable rentedBooksTable;

    String user = LoginJFrame.getUsername();

    public UserJFrame() {
        initComponents();
        setTitle(user + " Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + user + "!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchTableModel = new DefaultTableModel();
        searchResultTable = new JTable(searchTableModel);
        JScrollPane searchScrollPane = new JScrollPane(searchResultTable);

        rentedTableModel = new DefaultTableModel();
        rentedBooksTable = new JTable(rentedTableModel);
        JScrollPane rentedScrollPane = new JScrollPane(rentedBooksTable);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(searchScrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(e -> searchBooks());
         // Rent button action listener for opening RentJFrame
        searchResultTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            int column = searchResultTable.getColumnModel().getColumnIndexAtX(evt.getX());
            int row = evt.getY() / searchResultTable.getRowHeight();

            if (column == searchResultTable.getColumnModel().getColumnIndex("Rent") && row < searchResultTable.getRowCount()) {
                String bookTitle = searchTableModel.getValueAt(row, 0).toString();
                int bookId = getBookIdFromDatabase(bookTitle);
                RentJFrame rentPage = new RentJFrame(bookId);
                rentPage.setVisible(true);
            }
        }
    });
    

        add(mainPanel);
        setResizable(false);

        // Set up rented books table on the right side
        JPanel rentedBooksPanel = new JPanel(new BorderLayout());
        JLabel rentedBooksLabel = new JLabel("Rented Books");
        rentedBooksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rentedBooksLabel.setFont(new Font("Arial", Font.BOLD, 18));
        rentedBooksPanel.add(rentedBooksLabel, BorderLayout.NORTH);
        rentedBooksPanel.add(rentedScrollPane, BorderLayout.CENTER);

        mainPanel.add(rentedBooksPanel, BorderLayout.EAST);

        loadRentedBooks(); // Load rented books for the current user initially
    }
    private int getBookIdFromDatabase(String bookTitle) {
        int bookId = 0; // Default value
    
        // Your database connection code here to retrieve the book ID based on the book title
    
        // Example code for database retrieval (replace with your own)
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

    private void searchBooks() {
        String searchTerm = searchField.getText().trim();

        if (!searchTerm.isEmpty()) {
            searchTableModel.setRowCount(0);
            searchTableModel.setColumnCount(0);

            searchTableModel.addColumn("Title");
            searchTableModel.addColumn("Author");
            searchTableModel.addColumn("Genre");
            searchTableModel.addColumn("Availability");
            searchTableModel.addColumn("Rent");

            String jdbcURL = "jdbc:sqlite:library.db";
            try (Connection connection = DriverManager.getConnection(jdbcURL)) {
                String query = "SELECT * FROM Livre WHERE titre LIKE ? AND disponibilite = 'Available'";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "%" + searchTerm + "%");
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String title = resultSet.getString("titre");
                    String author = resultSet.getString("auteur");
                    String genre = resultSet.getString("genre");
                    String availability = resultSet.getString("disponibilite");

                    Object[] rowData = {title, author, genre, availability, "Rent"};
                    searchTableModel.addRow(rowData);
                }

                resultSet.close();
                preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please enter a search term.");
        }
    }

    private void loadRentedBooks() {
        rentedTableModel.setRowCount(0);
        rentedTableModel.setColumnCount(0);
    
        rentedTableModel.addColumn("Title");
        rentedTableModel.addColumn("Rent Date");
        rentedTableModel.addColumn("Status");
        rentedTableModel.addColumn("Return"); // Column for the Return button
    
        int userId = LoginJFrame.getUserIdFromDatabase(user, LoginJFrame.getpassword());
        String jdbcURL = "jdbc:sqlite:library.db";
    
        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            String query = "SELECT Livre.titre, Emprunt.date_emprunt, Emprunt.statut, Emprunt.id_livre " +
                    "FROM Emprunt " +
                    "INNER JOIN Livre ON Emprunt.id_livre = Livre.id_livre " +
                    "WHERE Emprunt.id_utilisateur = ?";
    
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                String title = resultSet.getString("titre");
                String rentDate = resultSet.getString("date_emprunt");
                String status = resultSet.getString("statut");
                int bookId = resultSet.getInt("id_livre");
    
                // Create a Return button for each book
                JButton returnButton = new JButton("Return");
                returnButton.addActionListener(e -> returnBook(bookId)); // Call returnBook with the bookId
    
                // Add the book details and button to the table
                Object[] rowData = {title, rentDate, status, returnButton};
                rentedTableModel.addRow(rowData);
            }
    
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
        // Set cell renderer and editor for the "Return" column
        rentedBooksTable.getColumn("Return").setCellRenderer(new ButtonRenderer());
        rentedBooksTable.getColumn("Return").setCellEditor(new ButtonEditor(new JCheckBox()));
    }
    

    private Object returnBook(int bookId) {
        return null;
    }
    

    
    }

