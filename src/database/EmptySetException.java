package database;

/**
 * Eccezione personalizzata che viene lanciata quando un'operazione di database
 * restituisce un ResultSet vuoto.
 */
public class EmptySetException extends Exception{

	/**
     * Costruttore di default. Crea una nuova eccezione con un messaggio di 
     * dettaglio predefinito.
     */
    public EmptySetException() {
        super("ResultSet vuoto: nessun dato trovato.");
    }
    
    /**
     * Costruttore che permette di specificare un messaggio di dettaglio personalizzato.
     * @param message Il messaggio di dettaglio dell'eccezione.
     */
    public EmptySetException(String msg) {
        super(msg);
    }
    
    /**
     * Restituisce una rappresentazione testuale dell'eccezione.
     *
     * @return Una stringa che rappresenta l'eccezione.
     */
    @Override
    public String toString() {
        return "EmptySetException: " + getMessage();
    }
}
