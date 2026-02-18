package mining;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.Data;

/**
 * Rappresenta un insieme di cluster.
 * <p>
 * {@code ClusterSet} ha una relazione di **composizione** con {@link Cluster}, 
 * poiché contiene direttamente oggetti {@code Cluster} la cui esistenza dipende da quella del {@code ClusterSet}.
 * <p>
 * Inoltre, ha una relazione di **associazione** con {@link data.Data}, 
 * in quanto utilizza riferimenti a oggetti {@code Data} per operazioni di rappresentazione, 
 * senza esserne responsabile della loro creazione o distruzione.
 *
 * @see Cluster
 * @see data.Data
 */
public class ClusterSet implements Iterable<Cluster>{
	
	/**
	 * Array di cluster presenti nell'insieme.
	 */
	private Set<Cluster> C;
	
	/**
	 * Costruttore di default.
	 * <p>
	 * Inizializza l'array dei cluster come vuoto.
	 */
	public ClusterSet() {
		C = new TreeSet<>();
	}
	
	/**
	 * Aggiunge un nuovo cluster all'insieme.
	 * <p>
	 * Utilizzando un {@link java.util.TreeSet}, il cluster viene aggiunto solo se non e' già presente,
	 * evitando duplicati. Non e' necessario riallocare array o gestire manualmente l'ordinamento,
	 * perché il {@code TreeSet} gestisce automaticamente sia la dinamica della memoria sia l'ordine
	 * degli elementi, basato sull'implementazione di {@link Comparable} in {@link Cluster}.
	 *
	 * @param c cluster da aggiungere all'insieme
	 * @throws IllegalArgumentException se {@code c} e' null
	 */
	void add(Cluster c){
		if (c == null) {
            throw new IllegalArgumentException("Il cluster da aggiungere non può essere null.");
        }
		C.add(c);
	
	}

	
	/**
	 * Restituisce una rappresentazione testuale dettagliata dell'insieme dei cluster.
	 * <p>
	 * Include per ogni cluster:
	 * <ul>
	 *   <li>il centroide</li>
	 *   <li>le tuple associate</li>
	 *   <li>la distanza di ciascuna tupla dal centroide</li>
	 *   <li>la distanza media dal centroide</li>
	 * </ul>
	 * Usa il metodo {@link Cluster#toString(data.Data)}.
	 *
	 * @param data riferimento al dataset per l'accesso ai valori delle tuple
	 * @return stringa descrittiva dei cluster e dei loro dati
	 * @throws IllegalArgumentException se {@code data} e' null
	 */
	public String toString(Data data) {
		if (data == null) {
            throw new IllegalArgumentException("Il dataset non può essere null.");
        }
		StringBuilder sb = new StringBuilder();
	    int i = 0;
	    for (Cluster cluster : C) {
	        if (cluster != null)
	            sb.append(i++).append(":").append(cluster.toString(data)).append("\n");
	    }
	    return sb.toString();
	}

	/**
	 * Restituisce una rappresentazione testuale dell'insieme dei cluster.
	 * <p>
	 * Per ciascun cluster, viene invocato {@link Cluster#toString()} per descrivere il solo centroide.
	 *
	 * @return stringa contenente i centroidi dei cluster
	 */
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    Iterator<Cluster> it = C.iterator();
	    int index = 1;
	    
	    while(it.hasNext()) {
	    	Cluster cluster = it.next();
		    sb.append(index).append(":").append(cluster.toString()).append("\n");
		    index ++;
	    }
	    return sb.toString();
	}

	
	/**
	 * Restituisce un iteratore sui cluster contenuti nell'insieme.
	 * <p>
	 * L'iteratore permette di scorrere tutti i {@link Cluster} presenti in questo {@code ClusterSet}
	 * senza modificarne il contenuto.
	 *
	 * @return {@code Iterator<Cluster>} per iterare sui cluster del set 
	 */
	@Override
	public Iterator<Cluster> iterator() {
		return C.iterator();
	}
}
