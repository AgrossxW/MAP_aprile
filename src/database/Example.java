package database;

import java.util.ArrayList;
import java.util.List;


/** Classe che modella una transazione letta dalla base di dati
 * 
 */
public class Example implements Comparable<Example>{
	
	private List<Object> example = new ArrayList<Object>();		// lista di oggetti che compongono la transazione

	/** Aggiunge un oggetto alla transazione
	 * 
	 * @param o oggetto da aggiungere
	 */
	public void add(Object o){
		example.add(o);
	}
	
	/**
	 * Restituisce l'oggetto alla posizione i della transazione
	 * @param i	 posizione dell'oggetto
	 * @return l'oggetto alla posizione i
	 */
	public Object get(int i){
		return example.get(i);
	}

	/**
	 * Confronta l'oggetto {@code Example} corrente con l'oggetto {@code Example} specificato ({@code ex})
	 * per stabilire l'ordinamento.
	 * <p>
	 * Il confronto avviene in modo lessicografico (sequenziale) su tutti gli attributi:
	 * il confronto si ferma e restituisce il risultato non appena viene trovata
	 * la prima coppia di attributi diversi.
	 * </p> 
	 * @param ex l'oggetto {@code Example} da confrontare con l'oggetto corrente
	 * @return un valore negativo se l'oggetto corrente e' minore di {@code ex},
	 *         un valore positivo se l'oggetto corrente e' maggiore di {@code ex},
	 *         zero se sono considerati equivalenti
	 */
	public int compareTo(Example ex) {

		int i = 0;
		for (Object o : ex.example) {
			if (!o.equals(this.example.get(i)))						// confronto lessicografico
				return ((Comparable) o).compareTo(example.get(i));		
			i++;
		}
		return 0;
	}

	/** Restituisce una rappresentazione testuale della transazione 
	 * @return la rappresentazione testuale della transazione
	 */
	@Override
	public String toString(){
		StringBuilder sb= new StringBuilder();
		for(Object o:example)
			sb.append(o.toString()+ " ");
		return sb.toString();
	}
	
}