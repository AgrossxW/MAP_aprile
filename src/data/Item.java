package data;

import java.io.Serializable;

/**
 * Classe astratta che modella un generico item, ovvero una coppia {@code <Attributo, Valore>} 
 * come ad esempio {@code Outlook = "Sunny"}.
 * <p>
 * Rappresenta l'associazione tra un attributo (discreto o continuo) e un valore corrispondente,
 * ed e' progettata per essere estesa da classi concrete che definiscono specifici tipi di item.
 */
public abstract class Item implements Serializable{
	/**
	 * Attributo coinvolto nell'item.
	 */
	private Attribute attribute;
	
	/**
	 * Valore assegnato all'attributo
	 */
	private Object value;
	
	/**
	 * Costruttore: inizializza i membri {@code attribute} e {@code value}.
	 * 
	 * @param attribute l'attributo dell'item
	 * @param value il valore associato all'attributo
	 * @throws IllegalArgumentException se {@code attribute} o {@code value} sono {@code null}
	 */
	protected Item(Attribute attribute, Object value) {
		if (attribute == null) {
	        throw new IllegalArgumentException("L'attributo non può essere null.");
	    }
	    if (value == null) {
	        throw new IllegalArgumentException("Il valore non può essere null.");
	    }
		this.attribute = attribute;
		this.value = value;
	}
	
	/**
	 * Restituisce l'attributo associato all'item.
	 * 
	 * @return l'attributo dell'item
	 */
	Attribute getAttribute() {
		return attribute;
	}
	
	/**
	 * Restituisce il valore associato all'attributo.
	 * 
	 * @return valore dell'item
	 */
	Object getValue() {
		return value;
	}
	
	/**
	 * Calcola la distanza tra questo item e un altro oggetto.
	 * <p>
	 * Il comportamento specifico della distanza dipende dal tipo concreto di item (continuo o discreto)
	 * ed e' definito nelle sottoclassi specifiche.
	 *
	 * @param a l'oggetto con cui calcolare la distanza
	 * @return la distanza tra l'oggetto corrente e l'oggetto {@code a}
	 * @throws IllegalArgumentException se {@code a} non e' valido o non e' compatibile
	 */
	public abstract double distance(Object a) ;

	/**
	 * Restituisce una rappresentazione testuale del valore associato all'item.
	 * 
	 * @return stringa rappresentante il valore assegnato all'attributo
	 */
	public String toString() {
		return value.toString();
	}
	
}
