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
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel(new BorderLayout());

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

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);

        JPanel rentedBooksPanel = new JPanel(new BorderLayout());
        JLabel rentedBooksLabel = new JLabel("Rented Books");
        rentedBooksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rentedBooksLabel.setFont(new Font("Arial", Font.BOLD, 18));
        rentedBooksPanel.add(rentedBooksLabel, BorderLayout.NORTH);
        rentedBooksPanel.add(rentedScrollPane, BorderLayout.CENTER);

        JPanel comboAndButtonPanel = new JPanel(new BorderLayout());
        JComboBox<String> rentedBooksComboBox = new JComboBox<>();
        int userId = LoginJFrame.getUserIdFromDatabase(user, LoginJFrame.getpassword());
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            String query = "SELECT Livre.titre, Emprunt.date_emprunt, Emprunt.statut " +
                    "FROM Emprunt " +
                    "INNER JOIN Livre ON Emprunt.id_livre = Livre.id_livre " +
                    "WHERE Emprunt.id_utilisateur = ? AND Emprunt.statut = 'en cours'";
            ;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("titre");
                rentedBooksComboBox.addItem(title);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        JPanel comboBoxPanel = new JPanel();
        JLabel rentedText = new JLabel("Rented Books to return : ");
        comboBoxPanel.add(rentedText);
        comboBoxPanel.add(rentedBooksComboBox);

        JButton returnButton = new JButton("Return Book");
        returnButton.addActionListener(e -> {
            String selectedBook = rentedBooksComboBox.getSelectedItem().toString();
            int bookId = getBookIdFromDatabase(selectedBook);
            new ReturnJFrame(bookId);
            loadRentedBooks();
        });

        comboAndButtonPanel.add(comboBoxPanel, BorderLayout.CENTER);
        comboAndButtonPanel.add(returnButton, BorderLayout.SOUTH);

        splitPane.setLeftComponent(rentedBooksPanel);
        splitPane.setRightComponent(comboAndButtonPanel);
        searchButton.addActionListener(e -> searchBooks());

        searchResultTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = searchResultTable.getColumnModel().getColumnIndexAtX(evt.getX());
                int row = evt.getY() / searchResultTable.getRowHeight();

                if (column == searchResultTable.getColumnModel().getColumnIndex("Rent")
                        && row < searchResultTable.getRowCount()) {
                    String bookTitle = searchTableModel.getValueAt(row, 0).toString();
                    int bookId = getBookIdFromDatabase(bookTitle);
                    RentJFrame rentPage = new RentJFrame(bookId);
                    rentPage.setVisible(true);
                    loadRentedBooks();
                }
            }
        });

        mainPanel.add(splitPane, BorderLayout.EAST);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        loadRentedBooks();
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
                String query = "SELECT * FROM Livre WHERE titre LIKE ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "%" + searchTerm + "%");
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String title = resultSet.getString("titre");
                    String author = resultSet.getString("auteur");
                    String genre = resultSet.getString("genre");
                    String availability = resultSet.getString("disponibilite");
                    
                    String rentOrReserve = availability.equals("Available") ? "Rent" : "Reserve";

                    Object[] rowData = {title, author, genre, availability, rentOrReserve};
    
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

    public void loadRentedBooks() {
        rentedTableModel.setRowCount(0);
        rentedTableModel.setColumnCount(0);
        JComboBox<String> rentedBooksComboBox = new JComboBox<>();
        rentedBooksComboBox.setPreferredSize(new Dimension(200, 25));

        rentedTableModel.addColumn("Title");
        rentedTableModel.addColumn("Rent Date");
        rentedTableModel.addColumn("Status");

        int userId = LoginJFrame.getUserIdFromDatabase(user, LoginJFrame.getpassword());
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            String query = "SELECT Livre.titre, Emprunt.date_emprunt, Emprunt.statut " +
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

                Object[] rowData = { title, rentDate, status };
                rentedTableModel.addRow(rowData);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private int getBookIdFromDatabase(String bookTitle) {
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
    

}
