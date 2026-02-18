package mining;
/**
 * La classe {@code ClusteringRadiusException} rappresenta un'eccezione
 * controllata che segnala la generazione di un solo cluster da parte
 * dell’algoritmo QT-Clustering.
 * <p>
 * Tale condizione e' considerata un errore logico nel contesto dell’algoritmo,
 * in quanto un clustering efficace deve produrre più di un gruppo di dati.
 * </p>
 */
public class ClusteringRadiusException extends Exception {
	
	/**
	 * Costruisce un'eccezione senza un messaggio specifico
	 * 
	 */
	public ClusteringRadiusException() { }
	
	/**
     * Costruisce un'eccezione con un messaggio descrittivo.
     *
     * @param msg Messaggio che descrive il motivo dell'eccezione.
     */
	public ClusteringRadiusException(String msg) {
        super(msg);
    }
	
	/**
     * Restituisce una rappresentazione testuale dell'eccezione.
     *
     * @return Una stringa che rappresenta l'eccezione.
     */
    @Override
    public String toString() {
        return "ClusteringRadiusException: " + getMessage();
    }
}

