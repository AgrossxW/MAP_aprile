package database;

/**
 * estende Exception per Modellare l'assenza di un valore all'interno di un ResultSet.
 * <p>
 * Questa eccezione viene lanciata quando si tenta di accedere a un valore in
 * un ResultSet che non contiene alcun dato.
 * </p> 
 */
public class NoValueException extends Exception{
	
	/**
     * Costruttore di default. Crea una nuova eccezione con un messaggio di 
     * dettaglio predefinito.
     */
    public NoValueException() {
        super("Nessun valore trovato nel ResultSet.");
    }
    
    /**
     * Costruttore che permette di specificare un messaggio di dettaglio personalizzato.
     * @param message Il messaggio di dettaglio dell'eccezione.
     */
    public NoValueException(String msg) {
        super(msg);
    }
    
    /**
     * Restituisce una rappresentazione testuale dell'eccezione.
     *
     * @return Una stringa che rappresenta l'eccezione.
     */
    @Override
    public String toString() {
        return "NoValueException: " + getMessage();
    }

}
