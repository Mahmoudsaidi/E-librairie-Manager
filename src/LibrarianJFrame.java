import javax.swing.*;
import java.awt.*;

public class LibrarianJFrame extends JFrame {

    private JFrame currentFrame; // Current active JFrame for different functionalities

    public LibrarianJFrame() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Librarian App");
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());

        JButton addBookButton = new JButton("Add Book");
        addBookButton.addActionListener(e -> {
            SwingUtilities.invokeLater(AddBookFrame::new);
        });
        mainPanel.add(addBookButton);


        JButton viewBooksButton = new JButton("View Books");
        viewBooksButton.addActionListener(e -> {
            SwingUtilities.invokeLater(ViewBooksFrame::new);
        });
        mainPanel.add(viewBooksButton);

        JButton viewStatisticsButton = new JButton("View Statistics");
        viewStatisticsButton.addActionListener(e -> {
            showViewStatisticsFrame(); // Show View Statistics JFrame
        });
        mainPanel.add(viewStatisticsButton);

        add(mainPanel);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }



    private void showViewStatisticsFrame() {
        currentFrame = new JFrame("View Statistics");
        currentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add components, fields, buttons, etc. for viewing statistics
        
        currentFrame.setSize(600, 400);
        currentFrame.setLocationRelativeTo(null);
        currentFrame.setVisible(true);
    }

   
}
