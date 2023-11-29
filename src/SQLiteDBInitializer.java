import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDBInitializer {
        // JDBC URL for SQLite database
        public static final String JDBC_URL = "jdbc:sqlite:library.db";

        public void createTables() {
            try {
                Class.forName("org.sqlite.JDBC"); // Load SQLite JDBC driver
            } catch (ClassNotFoundException e) {
                System.out.println("SQLite JDBC driver not found.");
                return;
            }
    
            try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
                if (connection != null) {
                    createTables(connection);
                    System.out.println("Tables created successfully.");
                }
            } catch (SQLException e) {
                System.out.println("SQL Exception: " + e.getMessage());
            }
        }
    

    private void createTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        // SQL statements for creating tables
        String createUtilisateurTable = "CREATE TABLE IF NOT EXISTS Utilisateur ("
                + "id_utilisateur INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nom TEXT NOT NULL,"
                + "prenom TEXT NOT NULL,"
                + "login TEXT NOT NULL,"
                + "pwd TEXT NOT NULL,"
                + "role TEXT NOT NULL"
                + ")";

        String createLivreTable = "CREATE TABLE IF NOT EXISTS Livre ("
                + "id_livre INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "titre TEXT NOT NULL,"
                + "auteur TEXT NOT NULL,"
                + "genre TEXT NOT NULL,"
                + "disponibilite TEXT NOT NULL"
                + ")";

        String createEmpruntTable = "CREATE TABLE IF NOT EXISTS Emprunt ("
                + "id_emprunt INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id_utilisateur INTEGER,"
                + "id_livre INTEGER,"
                + "date_emprunt TEXT,"
                + "date_retour TEXT,"
                + "statut TEXT NOT NULL,"
                + "FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id_utilisateur),"
                + "FOREIGN KEY (id_livre) REFERENCES Livre(id_livre)"
                + ")";

        String createReservationTable = "CREATE TABLE IF NOT EXISTS Reservation ("
                + "id_reservation INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id_utilisateur INTEGER,"
                + "id_livre INTEGER,"
                + "date_reservation TEXT,"
                + "statut TEXT NOT NULL,"
                + "FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id_utilisateur),"
                + "FOREIGN KEY (id_livre) REFERENCES Livre(id_livre)"
                + ")";

        // Execute SQL statements
        statement.execute(createUtilisateurTable);
        statement.execute(createLivreTable);
        statement.execute(createEmpruntTable);
        statement.execute(createReservationTable);

        statement.close();
    }
}
