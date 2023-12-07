import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewBooksFrame extends JFrame {
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public ViewBooksFrame() {
        initComponents();
        setTitle("View Books");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setAppIcon();
        displayBooksFromDatabase();
    }

    private void setAppIcon() {
        ImageIcon icon = new ImageIcon(getClass().getResource("ProgIcon.png"));
        setIconImage(icon.getImage());
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Table setup
        String[] columnNames = { "Title", "Author", "Genre", "Availability", "Remove" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        bookTable = new JTable(tableModel);

        // Add a button to each row for removal
        bookTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        bookTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), bookTable, tableModel));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void displayBooksFromDatabase() {
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            if (connection != null) {
                String query = "SELECT * FROM Livre";

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String title = resultSet.getString("titre");
                    String author = resultSet.getString("auteur");
                    String genre = resultSet.getString("genre");
                    String availability = resultSet.getString("disponibilite");

                    tableModel.addRow(new Object[] { title, author, genre, availability });
                }

                resultSet.close();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private DefaultTableModel tableModel;

    public ButtonEditor(JCheckBox checkBox, JTable bookTable, DefaultTableModel tableModel) {
        super(checkBox);
        this.tableModel = tableModel;

        button = new JButton("Remove");
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = bookTable.getSelectedRow();
                if (row != -1) {
                    removeBookFromDatabase(row);
                    tableModel.removeRow(row);
                }
            }
        });
    }

    private void removeBookFromDatabase(int row) {
        String bookTitle = (String) tableModel.getValueAt(row, 0);
        String jdbcURL = "jdbc:sqlite:library.db";

        try (Connection connection = DriverManager.getConnection(jdbcURL)) {
            if (connection != null) {
                String query = "DELETE FROM Livre WHERE titre = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, bookTitle);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(UIManager.getColor("Button.background"));
        }
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText();
    }
}
