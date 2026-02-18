package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe per l'accesso e la gestione della connessione alla base di dati MapDB 
 * utilizzando il driver JDBC di MySQL.
 */
public class DbAccess {

	// Attributi per la configurazione del Driver e del DBMS
    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver"; 
    private final String DBMS = "jdbc:mysql";
    private final String SERVER = "localhost"; 		// Identificativo del server
    private final String DATABASE = "MapDB"; 		// Nome della base di dati
    private final int PORT = 3306; 					// La porta su cui il DBMS MySQL accetta le connessioni
    private final String USER_ID = "MapUser"; 		// Nome dell’utente per l’accesso
    private final String PASSWORD = "map"; 			// Password di autenticazione
    
    // Attributo per la connessione
    private Connection conn; // Gestisce una connessione

    /**
     * Inizializza la connessione al database MapDB.
     * Impartisce al class loader l’ordine di caricare il driver MySQL 
     * e inizializza l'oggetto Connection.
     * @throws DatabaseConnectionException in caso di fallimento nel caricamento del driver o nella connessione.
     */
    public void initConnection() throws DatabaseConnectionException {
        
        // Caricamento del driver
        try{
            // Class.forName() forza il caricamento del driver
            Class.forName(DRIVER_CLASS_NAME); 
        } catch(ClassNotFoundException e) {
            System.out.println("[!] Driver not found: " + e.getMessage());
            // Incapsulamento dell'errore
            throw new DatabaseConnectionException("Driver non trovato: " + e.getMessage());
        }

        // Definizione della stringa di connessione (URL del database)
        String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
                    + "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";
        
        System.out.println("Connection's String: " + connectionString);
        
        try {
            conn = DriverManager.getConnection(connectionString);		// stabilisce la connessione
            System.out.println("[+] Connessione stabilita con successo.");
        } catch(SQLException e) {
            System.out.println("[!] SQL Exception: " + e.getMessage());
            System.out.println("[!] SQLState: " + e.getSQLState());
            // Incapsulamento dell'errore
            throw new DatabaseConnectionException("Connessione al DB fallita (SQL): " + e.getMessage()); 
        }
    }

    /**
     * Restituisce l'oggetto Connection gestito dalla classe.
     * @return L'oggetto Connection corrente.
     */
    public Connection getConnection() {
        // Restituisce la connessione (aggregazione con Connection)
        return conn;
    }

    /**
     * Chiude la connessione al database.
     * @throws SQLException se si verifica un errore durante la chiusura della connessione.
     */
    public void closeConnection() throws DatabaseConnectionException {
        try {
	    	if (conn != null && !conn.isClosed()) {
	            conn.close();
	            System.out.println("[-] Connessione chiusa.");
	        }
        }catch(SQLException e) {
        	throw new DatabaseConnectionException("Errore durante la chiusura della connessione: " + e.getMessage());
        }
    }
        
     /** 
      * Metodo toString per rappresentare lo stato dell'oggetto DbAccess.
      * @return Una stringa che rappresenta lo stato dell'oggetto DbAccess.
      */
      @Override
      public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("DBMS: ").append(DBMS).append("\n");
            sb.append("Server: ").append(SERVER).append("\n");
            sb.append("Database: ").append(DATABASE).append("\n");
            sb.append("Port: ").append(PORT).append("\n");
            sb.append("User ID: ").append(USER_ID).append("\n");
            // Non includiamo la password per motivi di sicurezza
            return sb.toString();
      }
    
}
