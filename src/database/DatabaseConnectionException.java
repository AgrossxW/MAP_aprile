package database;
/**
 * Eccezione personalizzata utilizzata per modellare un errore critico 
 * durante il processo di connessione al database.
 * * Questa eccezione viene sollevata tipicamente se:
 * <ul>
 * <li>Il driver JDBC non viene trovato o caricato correttamente.</li>
 * <li>La connessione tramite DriverManager.getConnection() fallisce (es. credenziali errate, server offline).</li>
 * </ul>
 * Estende {@link java.lang.Exception} e non {@link java.lang.RuntimeException} 
 * forzando la gestione dell'errore (checked exception).
 * @see DbAccess
 */
public class DatabaseConnectionException extends Exception {

    /**
     * Costruttore di default. Crea una nuova eccezione con un messaggio di 
     * dettaglio predefinito.
     */
    public DatabaseConnectionException() {
        super("Errore di connessione al database. Impossibile stabilire una Connection.");
    }
    
    /**
     * Costruttore che permette di specificare un messaggio di dettaglio personalizzato.
     * @param message Il messaggio di dettaglio dell'eccezione.
     */
    public DatabaseConnectionException(String msg) {
        super(msg);
    }
    
    /**
     * Restituisce una rappresentazione testuale dell'eccezione.
     *
     * @return Una stringa che rappresenta l'eccezione.
     */
    @Override
    public String toString() {
        return "DatabaseConnectionException: " + getMessage();
    }
}