import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class UserJFrame extends JFrame {
    private JTextField searchField;
    //TTable tto containe the result of the search TTable
    private DefaultTableModel searchTableModel;
    private JTable searchResultTable;
     //Table to containe the result of the rented Books Table
    private DefaultTableModel rentedTableModel;
    private JTable rentedBooksTable;

    String user = LoginJFrame.getUsername();

    public UserJFrame() {
        initComponents();
        setAppIcon();
        //dynimeclty add a title 
        setTitle(user + " Platform");
        //Frame Confgration
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setAppIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("ProgIcon.png"));
        setIconImage(icon.getImage());
    }

    private void initComponents() {
        //Block of code that  found that makes he programme Looks Better
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        //Create a main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        //Create a search fild and button
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        //Table for cntaining the searsh resul
        searchTableModel = new DefaultTableModel();
        searchResultTable = new JTable(searchTableModel);
        //creating he scrollpane and addng the search resylt as a constrator varible
        JScrollPane searchScrollPane = new JScrollPane(searchResultTable);
        //Doning the same but for the rented Table The midel one 
        rentedTableModel = new DefaultTableModel();
        rentedBooksTable = new JTable(rentedTableModel);
        JScrollPane rentedScrollPane = new JScrollPane(rentedBooksTable);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        //adding every thing to the search Panel
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        //adding search Panel to the main panel
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(searchScrollPane, BorderLayout.CENTER);
        //adding a button listener and the action will be searchBooks Function difined blow
        searchButton.addActionListener(e -> searchBooks());
        //adding split
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        //creating rented Book Panel
        JPanel rentedBooksPanel = new JPanel(new BorderLayout());
        JLabel rentedBooksLabel = new JLabel("Rented Books");
        rentedBooksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rentedBooksLabel.setFont(new Font("Arial", Font.BOLD, 18));
        rentedBooksPanel.add(rentedBooksLabel, BorderLayout.NORTH);
        rentedBooksPanel.add(rentedScrollPane, BorderLayout.CENTER);
        //creating List of the rented books that can be returned 
        JPanel comboAndButtonPanel = new JPanel(new BorderLayout());
        JComboBox<String> rentedBooksComboBox = new JComboBox<>();
        int userId = Utilitys.getUserIdFromDatabase(user, LoginJFrame.getpassword());
        //updating the list
        Utilitys.updateComboList(userId,rentedBooksComboBox);
        JPanel comboBoxPanel = new JPanel();
        JLabel rentedText = new JLabel("Rented Books to return : ");
        //Adding every thing in the ComboBocPanel
        comboBoxPanel.add(rentedText);
        comboBoxPanel.add(rentedBooksComboBox);
        //creating return button
        JButton returnButton = new JButton("Return Book");
        //action lstebner to get the selected book and return its id then up date it and open a return frame in the return class and update the rented books in the midel of the screen
        returnButton.addActionListener(e -> {
            String selectedBook = rentedBooksComboBox.getSelectedItem().toString();
            int bookId = Utilitys.getBookIdFromDatabase(selectedBook);
            new ReturnJFrame(bookId);
            Utilitys.updateComboList(userId,rentedBooksComboBox);
            loadRentedBooks();
        });

        comboAndButtonPanel.add(comboBoxPanel, BorderLayout.CENTER);
        comboAndButtonPanel.add(returnButton, BorderLayout.SOUTH);
        splitPane.setLeftComponent(rentedBooksPanel);
        splitPane.setRightComponent(comboAndButtonPanel);
        //listener to the searchBook
        searchButton.addActionListener(e -> searchBooks());
        //solution to add buttons to the list "Rent"
        searchResultTable.addMouseListener(new java.awt.event.MouseAdapter() {
            //this is a Overrided Function 
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int column = searchResultTable.getColumnModel().getColumnIndexAtX(evt.getX());
                int row = evt.getY() / searchResultTable.getRowHeight();
                Utilitys.updateComboList(userId,rentedBooksComboBox);
                if (column == searchResultTable.getColumnModel().getColumnIndex("Rent")
                        && row < searchResultTable.getRowCount()) {
                    String bookTitle = searchTableModel.getValueAt(row, 0).toString();
                    int bookId = Utilitys.getBookIdFromDatabase(bookTitle);
                    new RentJFrame(bookId);
                    loadRentedBooks();
                }
            }
        });
        //Add to the main panel
        mainPanel.add(splitPane, BorderLayout.EAST);
        //add to the frame
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
            //if the searchfiled not impty
            searchTableModel.setRowCount(0);
            searchTableModel.setColumnCount(0);
            // creating the result holding table
            searchTableModel.addColumn("Title");
            searchTableModel.addColumn("Author");
            searchTableModel.addColumn("Genre");
            searchTableModel.addColumn("Availability");
            searchTableModel.addColumn("Rent");

            String jdbcURL = "jdbc:sqlite:library.db";
            try (Connection connection = DriverManager.getConnection(jdbcURL)) {

                String query = "SELECT * FROM Livre WHERE titre LIKE ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                // %Term% % is sqlite thing 
                preparedStatement.setString(1, "%" + searchTerm + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                //adding the result to the list in the right side
                while (resultSet.next()) {
                    String title = resultSet.getString("titre");
                    String author = resultSet.getString("auteur");
                    String genre = resultSet.getString("genre");
                    String availability = resultSet.getString("disponibilite");
                    //change the button form rent to Reserve if nott available
                    String rentOrReserve = availability.equals("Available") ? "Rent" : "Reserve";
                    // Multy rowdata 
                    Object[] rowData = {title, author, genre, availability, rentOrReserve};
                    //add those row data tto the searchtable
                    searchTableModel.addRow(rowData);
                }

                resultSet.close();
                preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            // if tthe search field is impty 
            JOptionPane.showMessageDialog(null, "Please enter a search term.");
        }
    }


    //print all the rented books "en cours" or termine same as the search  
    public void loadRentedBooks() {

        rentedTableModel.setRowCount(0);
        rentedTableModel.setColumnCount(0);

        rentedTableModel.addColumn("Title");
        rentedTableModel.addColumn("Rent Date");
        rentedTableModel.addColumn("Status");

        int userId = Utilitys.getUserIdFromDatabase(user, LoginJFrame.getpassword());
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

   
    

}
