package data;

/**
 * La classe {@code DiscreteItem} rappresenta una coppia {@code <Attributo discreto - Valore discreto>},
 * ad esempio {@code Outlook = "Sunny"}.
 * <p>
 * Estende la classe astratta {@link Item} per specializzare il comportamento relativo agli attributi discreti.
 * In particolare, ridefinisce il metodo {@code distance} per gestire la distanza simbolica tra valori.
 *
 * @see Item
 */

public class DiscreteItem extends Item{
	
	/**
	 * Costruttore: inizializza l'oggetto {@code DiscreteItem} con un attributo discreto e un valore associato.
     * 
	 * @param attribute l'attributo discreto a cui e' associato il valore
	 * @param value il valore assegnato all'attributo
	 */
	DiscreteItem(DiscreteAttribute attribute, String value) {
		super(attribute, value);
	}
	
	/**
     * Calcola la distanza simbolica tra questo item e un altro oggetto.
     * <p>
     * La distanza e' definita come:
     * <ul>
     *   <li>{@code 0.0} se i valori discreti sono uguali</li>
     *   <li>{@code 1.0} se i valori discreti sono diversi </li>
     * </ul>
     *
     * @param a l'oggetto con cui confrontare questo item
     * @return la distanza simbolica tra i due item
     */
	@Override
	public double distance(Object a) {
	    
		if (!(a instanceof DiscreteItem)) return 1; // rispetta la segnatura
	    
		// La distanza e' 0.0 se i valori discreti sono uguali
	    DiscreteItem other = (DiscreteItem) a;
	    return getValue().equals(other.getValue()) ? 0.0 : 1.0;
	}

	/**
     * Restituisce una rappresentazione testuale dell'oggetto {@code DiscreteItem}.
     * <p>
     * La rappresentazione e' fornita dalla superclasse e consiste nella stringa del valore associato.
     *
     * @return una stringa che rappresenta il valore dell'item
     */
	@Override
	public String toString() {
		return super.toString();
	}

}
