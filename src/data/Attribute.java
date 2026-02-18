package data;

import java.io.Serializable;

/**
 * La classe {@code Attribute} rappresenta un attributo generico all'interno di un dataset. 
 * Ogni attributo ha un nome e un indice univoco che lo identifica. Le sottoclassi di {@code Attribute}
 * possono essere utilizzate per rappresentare attributi specifici come quelli discreti o continui.
 * 
 * @see DiscreteAttribute
 * @see ContinuousAttribute
 */
public abstract class Attribute implements Serializable{
	
	/**
	 * Nome simbolico dell'attributo.
	 */
	private String name;

	/**
	 * Identificativo numerico dell'attributo.
	 * Indica la posizione dell'attributo nella tupla (es: 1° attributo, 2° attributo...).
	 */
	private int index;
	
	/**
	 * Costruttore della classe {@code Attribute} : inizializza i valori dei membri name e index.
	 * 
	 * @param name nome dell'attributo
 	 * @param index identificativo numerico dell'attributo 
 	 * @throws IllegalArgumentException se name e' nullo o vuoto oppure se index e' negativo
	 */
	protected Attribute(String name, int index){
		if (name == null || name.isEmpty()) {
	        throw new IllegalArgumentException("Il nome dell'attributo non può essere nullo o vuoto");
	    }
	    if (index < 0) {
	        throw new IllegalArgumentException("L'indice dell'attributo non può essere negativo");
	    }
		this.name = name;
		this.index = index;
	}
	
	/**
	 * Restituisce il nome simbolico dell'attributo.
	 * 
	 * @return nome dell'attributo
	 */
	String getName() {
		return name;
	}
	
	/**
	 * Restituisce l'identificativo numerico dell'attributo.
	 * 
	 * @return L'identificativo numerico dell'attributo
	 */
	int getIndex() {
		return index;
	}
	
	/**
	 * Restituisce una rappresentazione testuale dello stato dell'oggetto.
	 * La rappresentazione include solamente il nome dell'attributo.
	 * 
	 * @return stringa rappresentante lo stato dell'oggetto
	 */
	@Override
	public String toString() {
		return getName();
	}
	
}