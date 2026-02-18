package mining;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import data.Data;
import data.Tuple;
import data.EmptyDatasetException;

/**
 * La classe {@code QTMiner} implementa l'algoritmo QT-Clustering (Quality Threshold Clustering)
 * per il raggruppamento di dati discreti in cluster, in base a una soglia di distanza (raggio) specificata.
 * <p>
 * Ogni cluster e' rappresentato da un centroide ({@link Tuple}) e contiene indici delle tuple assegnate.
 * I cluster finali vengono raccolti in un oggetto {@link ClusterSet}, la cui gestione e' responsabilità di {@code QTMiner}.
 * </p>
 *
 * <p><b>Relazioni:</b></p>
 * <ul>
 *   <li>{@code QTMiner} ha una <b>composizione</b> con {@link ClusterSet}: ne e' responsabile della creazione e della vita.</li>
 *   <li>{@code QTMiner} ha una <b>associazione</b> con {@link Data}: usa i dati, ma non ne gestisce la vita.</li>
 * </ul>
 *
 */
public class QTMiner implements Serializable {
	
	/**
	 * Insieme di cluster generati durante il clustering.
	 */
	private Set<Cluster> C ;
	
	/**
	 * Raggio generati durante il clustering.
	 */
	private double radius;
	
	/**
	  * Costruttore: inizializza l'oggetto {@code QTMiner} con un raggio specificato.
	 *
	 * @param radius raggio di clustering (distanza massima per l'inclusione in un cluster)
	 * @throws IllegalArgumentException se {@code radius} e' minore o uguale a zero 
	 */
	public QTMiner(double radius) {
		if (radius <= 0) {
	        throw new IllegalArgumentException("Il raggio deve essere maggiore di zero: " + radius);
	    }
		this.radius = radius;
		C = new TreeSet<>();
		
	}
	
	/**
	 * Costruttore che inizializza QTMiner leggendo l'insieme di cluster (C) da un file serializzato.
	 *
	 * @param fileName percorso e nome del file da cui leggere l'oggetto
	 * @throws FileNotFoundException se il file non esiste
	 * @throws IOException se si verifica un errore I/O durante la lettura
	 * @throws ClassNotFoundException se la classe dell'oggetto serializzato non viene trovata
	 * @throws IllegalArgumentException se l'oggetto letto e' null
	 */
	@SuppressWarnings("unchecked")	
	// indica al compilatore di non segnalare avvisi di cast non controllato (unchecked cast),
	// serve per evitare warning da readObject(): Sopprime il warning sul cast non controllato da Object a Set<Cluster>
	
	public QTMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		
	    // try-with-resources apre e chiude automaticamente il flusso 'in'
	    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
	        // Lettura dell'oggetto serializzato e cast a Set<Cluster>
	        this.C = (Set<Cluster>) in.readObject();

	        // Controllo opzionale: l'insieme letto non deve essere null
	        if (this.C == null) {
	            throw new IllegalArgumentException("L'insieme dei cluster letto dal file e' null.");
	        }
	    }
	    // Nessun finally necessario: il flusso viene chiuso automaticamente
	}

	/**
	 * Apre il file identificato da fileName e salva l'oggetto riferito da C in tale file (serializzazione).
	 *
	 * @param fileName percorso e nome del file dove salvare l'oggetto
	 * @throws FileNotFoundException se il file non può essere creato o sovrascritto
	 * @throws IOException se si verifica un errore I/O durante la scrittura
	 */
	public void salva(String fileName) throws FileNotFoundException, IOException {
	    
	    // Uso di try-with-resources: 'out' viene chiuso automaticamente
	    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
	        
	        // Salva l'insieme di cluster riferito da C (che deve implementare Serializable)
	        out.writeObject(this.C); 
	        
	    }
	}
	
	
	/**
	 * Restituisce l'insieme dei cluster ottenuti dall'operazione di clustering.
	 *
	 * @return insieme dei cluster generati, mai {@code null}
	 */
	public ClusterSet getC() {
		ClusterSet clusterSet = new ClusterSet(); // crea un nuovo ClusterSet vuoto
	    for (Cluster c : C) {
	        clusterSet.add(c); // aggiunge ciascun cluster dal Set interno
	    }
	    return clusterSet; // restituisce il ClusterSet completo
	}
	
	/**
	 * Applica l'algoritmo QT-Clustering al dataset specificato.
	 * Le tuple vengono raggruppate in cluster disgiunti (clustering) massimizzando la numerosità
	 * di ogni cluster entro il vincolo di distanza (raggio).
	 * Espelle un eccezione di tipo EmptyDatasetException se il dataset e' vuoto o 
	 * espelle un eccezione di tipo ClusteringRadiusException se tutti i dati sono stati raggruppati in un unico cluster.
	 *
	 * @param data dataset da clusterizzare
	 * @return numero di cluster trovati
	 * @throws EmptyDatasetException se il dataset e' vuoto
	 * @throws ClusteringRadiusException se l'algoritmo produce un solo cluster
	 * @throws IllegalArgumentException se {@code data} e' {@code null}
	 */
	public int compute(Data data) throws ClusteringRadiusException, EmptyDatasetException {
		if (data == null) {
	        throw new IllegalArgumentException("Il dataset non può essere null.");
	    }
		
		if (data.getNumberOfExamples() == 0) {
	        throw new EmptyDatasetException("Il dataset e' vuoto.");
	    }

	    int numClusters = 0;
	    Set<Integer> isClustered = new HashSet<>();

	    while (isClustered.size() < data.getNumberOfExamples()) {
	        Cluster candidateCluster = buildCandidateCluster(data, isClustered);

	        if (candidateCluster == null || candidateCluster.getSize() == 0) {
	            break;
	        }

	        // Aggiungi il cluster al Set C
	        C.add(candidateCluster);
	        
	        // Aggiungi gli elementi del cluster a isClustered usando iterator
	        Iterator<Integer> it = candidateCluster.iterator();
	        while (it.hasNext()) {
	            Integer id = it.next();
	            isClustered.add(id);
	        }

	        numClusters++;
	    }
	    
	    if(numClusters == 1){
	    	throw new ClusteringRadiusException("L'algoritmo ha prodotto un solo cluster.");
	    }

	    return numClusters;
	}


	/**
	 * Costruisce un cluster candidato centrato su ciascuna tupla non ancora clusterizzata
	 * e restituisce il cluster che contiene il maggior numero di tuple entro il raggio.
	 *
	 * @param data il dataset da clusterizzare
	 * @param isClustered insieme di indici delle tuple già assegnate a un cluster
	 * @return cluster con il numero massimo di tuple compatibili (o null se nessuno)
	 * @throws IllegalArgumentException se {@code data} o {@code isClustered} sono {@code null}
	 */
	private Cluster buildCandidateCluster(Data data, Set<Integer> isClustered) {
	    
		if (data == null) {
	        throw new IllegalArgumentException("Il dataset non può essere null.");
	    }
	    if (isClustered == null) {
	        throw new IllegalArgumentException("L'insieme degli indici clusterizzati non può essere null.");
	    }

	    Cluster bestCluster = null;

	    for (int i = 0; i < data.getNumberOfExamples(); i++) {
	        if (isClustered.contains(i)) {
	            // Tupla già clusterizzata, salta
	            continue;
	        }
	        
	        Tuple centroid = data.getItemSet(i);
	        Cluster cluster = new Cluster(centroid);

	        // Aggiungo come primo elemento il centroide stesso
	        cluster.addData(i);

	        for (int j = 0; j < data.getNumberOfExamples(); j++) {
	            if (i == j || isClustered.contains(j)) {
	                // Salto il centroide stesso e le tuple già clusterizzate
	                continue;
	            }

	            Tuple candidate = data.getItemSet(j);
	            if (centroid.getDistance(candidate) <= radius) {
	                cluster.addData(j);
	            }
	        }
	        
	        if (bestCluster == null || cluster.getSize() > bestCluster.getSize()) {
	            bestCluster = cluster;
	        }
	    }

	    return bestCluster;
	}


	
	
	/**
	 * Restituisce una rappresentazione testuale dei cluster trovati.
	 * Ciascun cluster e' rappresentato dal metodo {@link ClusterSet.toString()}
	 *
	 * @return stringa contenente i centroidi di ciascun cluster
	 */
	@Override
	public String toString() {
		return C.toString();
	}


}