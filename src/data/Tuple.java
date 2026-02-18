package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
 * La classe {@code Tuple} rappresenta una riga di un dataset,
 * modellata come una sequenza ordinata di coppie {@code <Attributo, Valore>},
 * ciascuna delle quali e' un oggetto di tipo {@link Item}.
 * Dunque Tuple ha una relazione di associazione con Item.
 * <p>
 * Ogni {@code Tuple} può essere interpretata come un'istanza o un esempio osservato del dataset.
 * 
 */
public class Tuple implements Serializable{
	
	/**
     * Array di oggetti {@link Item}, ognuno dei quali rappresenta una coppia {@code <Attributo, Valore>}.
     */
    private Item[] tuple;

    /**
     * Costruttore: inizializza la struttura dati per contenere una tupla di {@code size} elementi.
     *
     * @param size numero di item (coppie {@code <Attributo, Valore>}) da contenere
     * @throwa IllegalArgumentException se size e' <= 0
     */
    public Tuple(int size) {
    	if (size <= 0) {
    	    throw new IllegalArgumentException("La dimensione della tupla deve essere positiva.");
    	}
    	tuple = new Item[size];
    }
    
    /**
     * Restituisce il numero di item contenuti nella tupla.
     *
     * @return lunghezza della tupla (numero di item)
     */
    public int getLength() {
        return tuple.length;
    }
    
    /**
     * Restituisce l'item (coppia attributo-valore) in posizione {@code i}.
     *
     * @param i indice dell'item da restituire
     * @return item in posizione {@code i}
     * @throws IndexOutOfBoundsException se {@code i} e' negativo o maggiore o uguale alla dimensione della tupla
     */
    public Item get(int i){
    	if (i < 0 || i >= tuple.length) {
            throw new IndexOutOfBoundsException( "Indice " + i + " fuori dai limiti della tupla" );
    	}
            
    	return tuple[i];
    }
    
    /**
    * Inserisce un item {@code c} nella posizione {@code i} della tupla.
     *
     * @param c item da aggiungere
     * @param i indice della posizione in cui inserire l'item
     * @throws IndexOutOfBoundsException se {@code i} e' negativo o maggiore o uguale alla dimensione della tupla
     */
    void add(Item c, int i) {
    	if (i < 0 || i >= tuple.length) {
            throw new IndexOutOfBoundsException("Indice " + i + " fuori dai limiti della tupla" ); }
    	tuple[i] = c;
    }
    
    /**
     * Calcola la distanza tra la tupla corrente e un'altra tupla {@code obj}.
     * <p>
     * La distanza e' calcolata sommando le distanze tra gli item nelle posizioni corrispondenti
     * delle due tuple, usando il metodo {@link Item#distance(Object)}.
     *
     * Se {@code obj} e' {@code null}, viene lanciata un'eccezione {@link IllegalArgumentException}.
     * @param obj tupla con cui calcolare la distanza
     * @return somma delle distanze tra i singoli item
     * @throws IllegalArgumentException se la tupla di confronto e' null o ha dimensione diversa
     */
    public double getDistance(Tuple obj) {
        	
    	if (obj == null) {
            throw new IllegalArgumentException("La tupla di confronto non può essere nulla");
        }
    	else if (this.getLength() != obj.getLength()) {
            throw new IllegalArgumentException("Le tuple devono avere la stessa dimensione.");
        }

       // double d = 0.0;
    	double sumOfDistances = 0.0;

        // Itera su tutti gli item delle tuple
        for (int i = 0; i < tuple.length; i++) {
        	Item thisItem = this.get(i); // get(i) già implementato in Tuple
            Item otherItem = obj.get(i);
            
            // Calcolo della distanza d_item(T1[i], T2[i])
            // Item.distance() si occupa dello scaling/simbolismo corretto
            sumOfDistances += thisItem.distance(otherItem);
        }
        
        return sumOfDistances;
        
        
    	
    }
    
    /**
     * Calcola la distanza media tra la tupla corrente e un insieme di tuple identificate
     * dagli indici {@code clusteredData} all'interno del dataset {@code data}.
     * <p>
     * Utile, ad esempio, per determinare la distanza media da un centroide.
     *
     * @param data oggetto {@link Data} contenente il dataset
     * @param clusteredData Set<Integer> di indici delle tuple da confrontare
     * @return distanza media tra questa tupla e le tuple selezionate nel dataset
     * @throws IllegalArgumentException se il dataset o il set di indici sono null
     */
    public double avgDistance(Data data, Set<Integer> clusteredData) {
        
    	
    	if (data == null) {
            throw new IllegalArgumentException("Il dataset non può essere null");
        }
        if (clusteredData == null) {
            throw new IllegalArgumentException("Il set di indici non può essere null");
        }
        
        double sum = 0.0;
        int count = 0;
        
        Iterator<Integer> it = clusteredData.iterator();
        while(it.hasNext()) {
            int id = it.next();
            if (id < 0 || id >= data.getNumberOfExamples()) {
                throw new IllegalArgumentException("Indice " + id + " fuori dai limiti del dataset");
            }
            sum += this.getDistance(data.getItemSet(id));
            count++;
        }
        return (count > 0) ? (sum / count) : 0;		// evito la divisione per zero quando l’insieme di dati e' vuoto, restituendo semplicemente 0 come valore di fallback
    }

    
    /**
      * Restituisce una rappresentazione testuale della tupla, nel formato:
     * {@code valore1, valore2, ..., valoreN}
     * <p>
     * Ogni valore e' ottenuto tramite il metodo {@code toString()} dell'oggetto {@link Item}.
     * 
     * @return stringa che rappresenta la tupla
     */
    public String toString() {
        String result = "";
        
        for (int i = 0; i < tuple.length; i++) {
            // Aggiunge la rappresentazione dell'item corrente alla stringa
            result += tuple[i].toString();
            
            if (i < tuple.length - 1) {
                result += ", "; 
            }
        }
        
        return result;
    }

    
    
    
}
