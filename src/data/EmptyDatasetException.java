package data;

/**
 * La classe {@code EmptyDatasetException} rappresenta un'eccezione
 * controllata che segnala l'assenza di dati utili per il clustering.
 * <p>
 * Viene sollevata quando si tenta di eseguire l’algoritmo QT-Clustering su un dataset
 * privo di tuple o non inizializzato correttamente.
 * </p>
 *
 */
public class EmptyDatasetException extends Exception {

	/**
	 * Costruisce un'eccezione senza un messaggio specifico
	 */
	public EmptyDatasetException() { }
	
    /**
     * Costruisce un'eccezione con un messaggio descrittivo.
     *
     * @param msg Messaggio che descrive il motivo dell'eccezione.
     */
    public EmptyDatasetException(String msg) {
        super(msg);
    }

    /**
     * Restituisce una rappresentazione testuale dell'eccezione.
     *
     * @return Una stringa che rappresenta l'eccezione.
     */
    @Override
    public String toString() {
        return "EmptyDatasetException: " + getMessage();
    }
}
