package data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * La classe {@code DiscreteAttribute} rappresenta un attributo discreto (categorico) all'interno di un dataset.
 * <p>
 * Un attributo discreto e' caratterizzato da un insieme finito di valori distinti, memorizzati in un {@link java.util.TreeSet}.
 * I valori sono mantenuti in ordine lessicografico e possono essere iterati oppure interrogati per dimensione.
 * </p>
 *
 * <p>
 * Estende la classe {@link Attribute} e implementa l'interfaccia {@link java.lang.Iterable} per consentire
 * la scorribilità dei valori discreti attraverso un {@link java.util.Iterator}.
 * </p>
 *
 * <p><b>Responsabilità principali:</b></p>
 * <ul>
 *   <li>Restituire la cardinalità del dominio discreto;</li>
 *   <li>Consentire l’iterazione ordinata dei valori;</li>
 *   <li>Fornire una rappresentazione testuale completa dell’attributo.</li>
 * </ul>
 *
 * @see Attribute
 * @see java.util.TreeSet
 */
public class DiscreteAttribute extends Attribute implements Iterable<String>{
	/**
	 * TeeSet contenente i valori distinti che costituiscono il dominio dell'attributo.
     * I valori sono memorizzati in ordine lessicografico.
	 */
	private Set<String> values ;
	
	/**
	 * Costruttore: crea un attributo discreto inizializzando il nome, l'indice
     * e i valori distinti del dominio.
	 * 
	 * @param name nome dell'attributo
	 * @param index identificativo numerico dell'attributo
	 * @param values TreeSet di tipo String rappresentanti il dominio dell'attributo
	 */
	public DiscreteAttribute(String name, int index, Set<String> values) {
		super(name, index);
		
		if (values == null || values.isEmpty()) {
		    throw new IllegalArgumentException("Il dominio dell'attributo non può essere vuoto");
		}
		this.values = new TreeSet<>(values);
	}
	
	/**
	 * Restituisce il numero di valori distinti presenti nel dominio dell'attributo.
	 * 
	 * @return numero di valori distinti nel dominio dell'attributo
	 */
	int getNumberOfDistinctValues() {
		return values.size();
	}
	
	/**
	 * Restituisce un {@link java.util.Iterator} che consente di scorrere i valori discreti 
	 * dell'attributo in ordine lessicografico.
	 * <p>
	 * L'iteratore restituito e' quello nativo di {@link java.util.TreeSet}, quindi rispetta 
	 * l'ordinamento naturale dei valori e riflette le modifiche successive al set originale.
	 * </p>
	 *
	 * @return un iteratore sui valori discreti dell'attributo
	 */
	@Override
	public Iterator<String> iterator() {
	    return values.iterator();
	}

	/**
	 * Restituisce una rappresentazione testuale dell'attributo discreto.
	 * Include il nome simbolico e i valori del dominio tra parentesi quadre .
	 *
	 * @return stringa rappresentante lo stato dell'attributo discreto
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(super.toString() + " [");
	    Iterator<String> e = values.iterator();
	
	    while (e.hasNext()) {
	        result.append(e.next());
	        if (e.hasNext()) {
	            result.append(", ");
	        }
	    }
	
	    result.append("]");
	    return result.toString();
	}
	

}
