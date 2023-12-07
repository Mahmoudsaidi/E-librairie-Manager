
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
    private void setAppIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("ProgIcon.png"));
        setIconImage(icon.getImage());
    }

    private void initComponents() {
        setTitle("Librarian App");
        setResizable(false);
    
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
    
        // Load icons
        ImageIcon addBookIcon = new ImageIcon(getClass().getResource("add_book_icon.png"));
        ImageIcon viewBooksIcon = new ImageIcon(getClass().getResource("view_books_icon.png"));
        ImageIcon viewStatisticsIcon = new ImageIcon(getClass().getResource("view_statistics_icon.png"));
    
        JButton addBookButton = new JButton("Add Book", addBookIcon);
        addBookButton.addActionListener(e -> {
            SwingUtilities.invokeLater(AddBookFrame::new);
        });
        mainPanel.add(addBookButton);
    
        JButton viewBooksButton = new JButton("View Books", viewBooksIcon);
        viewBooksButton.addActionListener(e -> {
            SwingUtilities.invokeLater(ViewBooksFrame::new);
        });
        mainPanel.add(viewBooksButton);
    
        JButton viewStatisticsButton = new JButton("View Statistics", viewStatisticsIcon);
        viewStatisticsButton.addActionListener(e -> {
            showViewStatisticsFrame();
        });
        mainPanel.add(viewStatisticsButton);
    
        add(mainPanel);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showViewStatisticsFrame() {
        currentFrame = new JFrame("View Statistics");

        initializeStatisticsComponents(currentFrame);

        currentFrame.setSize(600, 400);
        currentFrame.setLocationRelativeTo(null);
        currentFrame.setResizable(false);
        currentFrame.setVisible(true);
    }

    private void initializeStatisticsComponents(JFrame frame) {
        JPanel statisticsPanel = new JPanel(new GridLayout(3, 1));
        int numrented = getRentedBooksCount();
        int totalBooks = getTotalBooks();
        double percentageRented = ((double) numrented / totalBooks) * 100; // Calculate the percentage

        int progressBarValue = (int) percentageRented;
        System.out.println(progressBarValue);
        JLabel totalBooksLabel = new JLabel("Total Books: " + totalBooks + " Rented Books: " + numrented);

        totalBooksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statisticsPanel.add(totalBooksLabel);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(progressBarValue);
        statisticsPanel.add(progressBar);

        JLabel sampleLabel = new JLabel(
                "This is 'The rented Book / The Total Books' Overall , the most rented book is " + getMostRentedBook());
        sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statisticsPanel.add(sampleLabel);

        frame.add(statisticsPanel, BorderLayout.CENTER);
    }

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

    public int getRentedBooksCount() {
        int rentedBooksCount = 0;
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

    private static final String JDBC_URL = "jdbc:sqlite:library.db";

    public static String getMostRentedBook() {
        String mostRentedBook = "";

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