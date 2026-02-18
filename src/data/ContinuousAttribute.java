package data;

/**
 * La classe {@code ContinuousAttribute} rappresenta un attributo continuo all'interno di un dataset.
 * Un attributo continuo e' caratterizzato da valori numerici che possono essere compresi all'interno di un intervallo, 
 * definito da un valore minimo {@code min} e un valore massimo {@code max}.
 * La classe fornisce metodi per scalare i valori dell'attributo, mappandoli nell'intervallo [0, 1], 
 * per poter confrontare valori appartenenti a domini numerici diversi.
 * 
 * @see Attribute
 */
public class ContinuousAttribute extends Attribute {
	/**
	 * L'estremo superiore dell'intervallo di valori che l'attributo può reamente assumere nel dataset considerato.
	 */
	private double max;
	
	/**
	 * L'estremo inferiore dell'intervallo di valori che l'attributo può reamente assumere nel dataset considerato.
	 */
	private double min;
	
	/**
	 * Costruttore per creare un oggetto {@link ContinuousAttribute}.
	 * Invoca il costruttore della superclasse {@link Attribute} e inizializza i membri per il range di valori 
     * definiti da {@code min} e {@code max}.
     * 
	 * @param name nome dell'attributo
	 * @param index identificativo numerico dell'attributo
	 * @param min estremo inferiore dell'intervallo di valori assumibili
	 * @param max estremo superiore dell'intervallo di valori assumibili
	 * @throws IllegalArgumentException se min >= max oppure se name non e' valido o index < 0
	 */
	public ContinuousAttribute(String name, int index, double min, double max){
		super(name, index);
		
		if (min >= max) {
	        throw new IllegalArgumentException("Il valore minimo deve essere inferiore al massimo.");
	    }
		this.min = min;
		this.max = max;	
	}
	
	/**
	 * Calcola e restituisce il valore scalato del parametro passato in input.
	 * Lo scaling mappa il valore nell'intervallo [0,1] utilizzando la formula:
	 * <pre>{@code v' = (v - min) / (max - min)}</pre>
	 * dove {@code v} e' il valore dell'attributo da scalare.
     * 
	 * @param v valore dell'attributo da scalare
	 * @return valore scalato dell'attributo, compreso nell'intervallo [0,1]
	 */
	double getScaledValue(double v) {
	    v = (v - min) / (max - min);
	    return v;
	}
	
	/**
	 * Restituisce una rappresentazione testuale dello stato dell'oggetto.
	 * Include il nome dell'attributo e gli estremi dell'intervallo.
	 * 
	 * @return stringa rappresentante lo stato dell'attributo continuo
	 */
	@Override
	public String toString() {
	    return super.toString() + " [" + min + ", " + max + "]";
	}

	

}
