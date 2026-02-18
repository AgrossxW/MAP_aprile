package mining;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import data.Data;
import data.Tuple;

/**
 * La classe {@code Cluster} rappresenta un singolo cluster nell'algoritmo di clustering.
 * Ogni cluster e' composto da un centroide ({@link Tuple}) e da un insieme di indici che 
 * rappresentano le tuple assegnate a quel cluster.
 *
 * <p>Relazione di associazione con {@link Tuple}, poiché un cluster contiene riferimenti a tuple
 * ma non le possiede direttamente.
 * 
 * Un {@code Cluster} e' gestito da {@code QTMiner} e può essere aggiunto a un insieme di cluster 
 * tramite {@link ClusterSet}.
 * </p> * 
 *
 * @see data.Tuple
 */
public class Cluster implements Iterable<Integer>, Comparable<Cluster>, Serializable{
	/**
	 * Tupla che rappresenta il centroide del cluster.
	 */
	private Tuple centroid;

	/**
	 * Insieme di tuple associate al cluster.
	 */
	private Set<Integer> clusteredData;	// uso direttamente un Set<Integer> per salvare gli indici delle tuple già assegnate
	
	/**
	 * Costruttore che inizializza un cluster con un centroide e un insieme di tuple già assegnate.
	 * 
	 * @param centroid tupla che rappresenta il centroide del cluster
	 * @param clusteredData insieme di ID delle tuple assegnate al cluster
	 * @throws IllegalArgumentException se {@code centroid} e' null
	 */
	public Cluster(Tuple centroid){
		
		if (centroid == null) {
            throw new IllegalArgumentException("Il centroide non può essere null.");
        }
		this.centroid = centroid;
		clusteredData = new HashSet<>() ;
	}
		
	/**
	 * Restituisce il centroide del cluster.
	 *
	 * @return la tupla che rappresenta il centroide
	 */
	Tuple getCentroid(){
		return centroid;
	}
	
	/**
	 * Aggiunge una tupla al cluster tramite il suo identificatore.
	 * <p>
	 * Internamente utilizza il metodo {@link data.ArraySet#add(int)} per registrare l'ID della tupla 
	 * nel set di tuple assegnate al cluster. Se l'ID e' già presente, il metodo non apporta modifiche.
	 *
	 * @param id identificatore della tupla da assegnare al cluster
	 * @return {@code true} se l'ID e' stato aggiunto con successo (non era già presente), {@code false} altrimenti
	 * @throws IllegalArgumentException se {@code id} e' negativo 
	 */
	boolean addData(int id){
		if (id < 0) {
            throw new IllegalArgumentException("L'ID della tupla non può essere negativo.");
        }
		return clusteredData.add(id);
		
	}

	/**
	 * Verifica se una tupla (transazione), identificata dal suo ID, e' attualmente assegnata al cluster.
	 * Cioe' se e' clusterizzata nell'array corrente
	 * <p>
	 * Internamente richiama il metodo {@link data.ArraySet#get(int)} per verificare la presenza dell'ID.
	 *
	 * @param id identificatore della tupla da verificare
	 * @return {@code true} se la tupla e' presente nel cluster, {@code false} altrimenti
	 * @throws IllegalArgumentException se {@code id} e' negativo
	 */
	boolean contain(int id){
		return clusteredData.contains(id); // contains(Object o) ?!
	}
	

	/**
	 * Rimuove dal cluster la tupla identificata dall'ID.
	 * <p>
	 * Utilizza il metodo {@link data.ArraySet#delete(int)} per eliminare l'ID dal set delle tuple assegnate.
	 * Se l'ID non e' presente, non viene effettuata alcuna modifica.
	 *
	 * @param id identificatore della tupla da rimuovere
	 * @throws IllegalArgumentException se {@code id} e' negativo
	 */
	void removeTuple(int id){
		if (id < 0) {
            throw new IllegalArgumentException("L'ID della tupla non può essere negativo.");
        }
		clusteredData.remove(id);	// remove(Object o) ?!
	}
	
	/**
	 * Restituisce il numero di tuple attualmente assegnate al cluster.
	 * <p>
	 * Il valore restituito corrisponde alla cardinalità dell'{@link data.ArraySet} associato.
	 *
	 * @return numero di tuple presenti nel cluster (dimensione)
	 */
	int getSize(){
		return clusteredData.size();
	}
	
	/**
	 * Confronta questo cluster con un altro cluster in base alla dimensione (numero di tuple).
	 * <p>
	 * Utilizza {@link Integer#compare(int, int)} per restituire:
	 * <ul>
	 *   <li>un valore negativo se questo cluster ha meno elementi dell'altro,</li>
	 *   <li>zero se hanno la stessa dimensione,</li>
	 *   <li>un valore positivo se questo cluster ha più elementi dell'altro.</li>
	 * </ul>
	 * </p>
	 *
	 * @param o il cluster con cui confrontare questo oggetto
	 * @return un intero negativo, zero o positivo a seconda del confronto delle dimensioni
	 */
	@Override
	public int compareTo(Cluster o) {
		if (o == null) {
            throw new IllegalArgumentException("Il cluster da confrontare non può essere null.");
        }
		return Integer.compare(this.getSize(), o.getSize());
	}

	/**
	 * 
	 */
	@Override
	public Iterator<Integer> iterator() {
		return clusteredData.iterator();
	}

	/**
	 * Restituisce una rappresentazione testuale del cluster.
	 * <p>
	 * La stringa include:
	 * <ul>
	 *   <li>Il centroide del cluster</li>
	 *   <li>Le tuple assegnate, con distanza dal centroide</li>
	 *   <li>La distanza media dal centroide</li>
	 * </ul>
	 *
	 * @param data riferimento al dataset per recuperare i valori delle tuple
	 * @return stringa descrittiva del cluster e delle sue tuple
	 * @throws IllegalArgumentException se {@code data} e' null
	 */
	public String toString(Data data) {
		if (data == null) {
            throw new IllegalArgumentException("Il dataset non può essere null.");
        }
		
		StringBuilder sb = new StringBuilder();
	    sb.append("Centroid=(");

	    for (int i = 0; i < centroid.getLength(); i++) {
	        sb.append(centroid.get(i)).append(" ");
	    }
	    sb.append(")\nExamples:\n");

	    Iterator<Integer> it = clusteredData.iterator();
	    while (it.hasNext()) {
	        int id = it.next();
	        sb.append("[");
	        for (int j = 0; j < data.getNumberOfAttributes(); j++) {
	            sb.append(data.getAttributeValue(id, j)).append(" ");
	        }
	        sb.append("] dist=").append(getCentroid().getDistance(data.getItemSet(id))).append("\n");
	    }

	    sb.append("\nAvgDistance=").append(getCentroid().avgDistance(data, clusteredData));
	    return sb.toString();
	}
	
	/**
	 * Restituisce una rappresentazione compatta del cluster.
	 * Mostra solo il centroide come concatenazione dei valori.
	 *
	 * @return stringa del centroide del cluster
	 */
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Centroid=(");
	    for (int i = 0; i < centroid.getLength(); i++) {
	        sb.append(centroid.get(i)); // concatena i valori
	    }
	    sb.append(")");
	    return sb.toString();
	}

}
