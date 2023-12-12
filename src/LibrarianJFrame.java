
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LibrarianJFrame extends JFrame {

    private JFrame currentFrame;

    public LibrarianJFrame() {
        initComponents();
        setAppIcon();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // load the programme icon and set it
    private void setAppIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("ProgIcon.png"));
        setIconImage(icon.getImage());
    }

    // init the frame components
    private void initComponents() {
        // Title and resizabelitty
        setTitle("Librarian App");
        setResizable(false);
        // create an empty panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());

        // Load Button icons
        ImageIcon addBookIcon = new ImageIcon(getClass().getResource("add_book_icon.png"));
        ImageIcon viewBooksIcon = new ImageIcon(getClass().getResource("view_books_icon.png"));
        ImageIcon viewStatisticsIcon = new ImageIcon(getClass().getResource("view_statistics_icon.png"));
        // addbutton
        JButton addBookButton = new JButton("Add Book", addBookIcon);
        addBookButton.addActionListener(e -> {
            // send to tthe addbookframe class
            SwingUtilities.invokeLater(AddBookFrame::new);
        });
        // add to the mainPanel
        mainPanel.add(addBookButton);
        // viewButton
        JButton viewBooksButton = new JButton("View Books", viewBooksIcon);
        viewBooksButton.addActionListener(e -> {
            // send to the ViewBook Class
            SwingUtilities.invokeLater(ViewBooksFrame::new);
        });
        // add to the mainPanel
        mainPanel.add(viewBooksButton);
        // ViewSttatsButton
        JButton viewStatisticsButton = new JButton("View Statistics", viewStatisticsIcon);
        viewStatisticsButton.addActionListener(e -> {
            // send to the function blow
            showViewStatisticsFrame();
        });
        // add to the mainPanel
        mainPanel.add(viewStatisticsButton);
        // add to the frame
        add(mainPanel);
        // Frame Related
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // creation the showView Frame
    private void showViewStatisticsFrame() {
        currentFrame = new JFrame("View Statistics");

        initializeStatisticsComponents(currentFrame);

        currentFrame.setSize(600, 400);
        currentFrame.setLocationRelativeTo(null);
        currentFrame.setResizable(false);
        currentFrame.setVisible(true);
    }

    private void initializeStatisticsComponents(JFrame frame) {
        // Cuting the Panel into segments
        JPanel statisticsPanel = new JPanel(new GridLayout(3, 1));
        // Getting total Rented books
        int numrented = getRentedBooksCount();
        // Gettinh total books
        int totalBooks = getTotalBooks();
        // Doing some math to get the % of rented books
        double percentageRented = ((double) numrented / totalBooks) * 100;
        // setting the progressBar automaticly
        int progressBarValue = (int) percentageRented;
        // Printing some informattions
        JLabel totalBooksLabel = new JLabel("Total Books: " + totalBooks + " Rented Books: " + numrented);
        totalBooksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // add TotalBooksLable to the main Statistics Panel
        statisticsPanel.add(totalBooksLabel);
        // adding the progressBar Graphly
        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        // min/max valus
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        // Set Progressbar Value
        progressBar.setValue(progressBarValue);
        // add ProgressBar to the statistics Panel
        statisticsPanel.add(progressBar);
        // The text for explaing the progress bar and the most rented books
        JLabel sampleLabel = new JLabel(
                "This is 'The rented Book / The Total Books' Overall , the most rented book is " + getMostRentedBook());
        sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // add the msg tto the statistics Panel.
        statisticsPanel.add(sampleLabel);
        // add every thng the frame
        frame.add(statisticsPanel, BorderLayout.CENTER);
    }

    /* Helpers methods */
    // return int of the total books
    public int getTotalBooks() {
        int totalBooks = 0;
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            if (connection != null) {
                String query = "SELECT COUNT(*) AS total FROM Livre";

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                if (resultSet.next()) {
                    totalBooks = resultSet.getInt("total");
                }

                resultSet.close();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalBooks;
    }

    // Number of rented books Over all "En cours" / "Termne"
    public int getRentedBooksCount() {
        int rentedBooksCount = 0;
        // Jdbc Local Path
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            if (connection != null) {
                String query = "SELECT COUNT(DISTINCT id_livre) AS rented_books_count FROM Emprunt";

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                // Get the total count of rented books from the result set
                if (resultSet.next()) {
                    rentedBooksCount = resultSet.getInt("rented_books_count");
                }

                resultSet.close();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions here
        }

        return rentedBooksCount;
    }

    // tHIS functrion return the mostrented book
    public static String getMostRentedBook() {
        String mostRentedBook = "";
        // Jdbc Local Path
        String JDBC_URL = "jdbc:sqlite:library.db";
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            if (connection != null) {
                // Query to find the book with the highest rental count
                String query = "SELECT Livre.titre " +
                        "FROM Livre " +
                        "LEFT JOIN Emprunt ON Livre.id_livre = Emprunt.id_livre " +
                        "GROUP BY Livre.titre " +
                        "ORDER BY COUNT(Emprunt.id_emprunt) DESC LIMIT 1";

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                if (resultSet.next()) {
                    mostRentedBook = resultSet.getString("titre");
                }

                resultSet.close();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mostRentedBook;
    }

}