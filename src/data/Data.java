package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.NoValueException;
import database.TableData;
import database.TableSchema;
import database.TableSchema.Column;

/**
 * La classe {@code Data} rappresenta un insieme di transazioni, ciascuna descritta da un insieme di attributi discreti.
 * Essa contiene lo schema degli attributi (header) e i dati veri e propri (le transazioni).
 * 
 * <p>
 * La classe {@code Data} ha le seguenti relazioni con altre classi:
 * </p>
 * 
 * <ul>
 *   <li><b>Composizione con {@link Attribute}:</b> Ogni oggetto {@code Data} possiede uno o più oggetti {@code Attribute},
 *       e la vita di questi oggetti dipende dalla vita dell'oggetto {@code Data}. In altre parole, quando un oggetto {@code Data}
 *       viene distrutto, anche gli oggetti {@code Attribute} ad esso associati vengono distrutti.</li>
 *   <li><b>Associazione con {@link Tuple}:</b> Un oggetto {@code Data} e' associato a una o più {@code Tuple}, 
 *       ma non le possiede direttamente. Le {@code Tuple} sono utilizzate per rappresentare le transazioni, ma 
 *       possono esistere indipendentemente dall'oggetto {@code Data}.</li>
 * </ul>
 * 
 * @see Attribute
 * @see Tuple
 */
public class Data implements Iterable<Attribute>{
	
	/**
	 * Lista di oggetti {@code Example} che rappresenta le transazioni distinte del dataset,
     * caricate dal database. Ogni {@code Example} e' una lista di valori.
	 */
	private List<Example> data = new ArrayList<Example>();
	
	/**
	 * Rappresenta la cardinalità dell’insieme di transazioni contenuti nel dataset (numero di righe in {@code Object[][] data})
	 */
	private int numberOfExamples;
	
	/**
	 * LinkedList di {@code Attribute[]} che definisce lo schema (header) del dataset.
	 */
	private List<Attribute> attributeSet ;
	
	/**
	 * Costruttore: Carica i dati di addestramento da una tabella del database.
	 * * @param tableName Il nome della tabella da cui caricare i dati.
	 * @throws DatabaseConnectionException se fallisce la connessione al database.
	 * @throws SQLException se si verifica un errore durante l'interrogazione del database.
	 * @throws EmptySetException se la tabella e' vuota.
	 * @throws NoValueException se il valore non viene trovato nel ResultSet
	 * @throws IllegalArgumentException se un attributo ha valori numerici e non e' gestito come ContinuousAttribute.
	 */
	public Data(String tableName) throws DatabaseConnectionException, SQLException, EmptySetException, NoValueException{
		
		DbAccess db = new DbAccess();
		db.initConnection();
		
		TableSchema tSchema = new TableSchema(db, tableName);
		TableData tData = new TableData(db);

		// Inizializzazione di attributeSet
		attributeSet = new LinkedList<>();

		// Popolamento di attributeSet (logica invariata)
		for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
			Column column = tSchema.getColumn(i);
			
			if (column.isNumber()) {
				Object min = tData.getAggregateColumnValue(tableName, column, database.QUERY_TYPE.MIN);
				Object max = tData.getAggregateColumnValue(tableName, column, database.QUERY_TYPE.MAX);
				
				if (!(min instanceof Double && max instanceof Double)) {
					throw new IllegalArgumentException("Attributo numerico con valore min/max non Double: " + column.getColumnName());
				}
				
				attributeSet.add(new ContinuousAttribute(column.getColumnName(), i, (Double)min, (Double)max));

			} else {
				TreeSet<String> discreteValues = new TreeSet<>();
				
				for (Object val : tData.getDistinctColumnValues(tableName, column)) {
					if (!(val instanceof String)) {
						throw new IllegalArgumentException("Attributo discreto con valore non String: " + column.getColumnName());
					}
					discreteValues.add((String) val);
				}
				
				attributeSet.add(new DiscreteAttribute(column.getColumnName(), i, discreteValues));
			}
		}

		// Caricamento delle transazioni distinte. Il tipo restituito e' List<Example>, compatibile con List<Example> data.
		data = tData.getDistinctTransazioni(tableName);
		numberOfExamples = data.size();
		
		db.closeConnection();
	}
	
	/**
	 * Restituisce il numero di transazioni (righe) presenti nel dataset.
	 * 
	 * @return numero di righe in {@code Object[][] data}
	 */
	public int getNumberOfExamples(){
		return numberOfExamples;
	}
	
	/**
	 * Restituisce il numero di attributi esplicativi (colonne) della tabella.
	 * 
	 * @return numero di attributi
	 */
	public int getNumberOfAttributes(){
		return attributeSet.size();
	}
	
	/**
	 * Restituisce l'array contenente lo schema degli attributi {@code attributeSet}.
	 * 
	 * @return array di oggetti {@code Attribute}
	 */
	Attribute[] getAttributeSchema() {
	    return attributeSet.toArray(new Attribute[0]);
	}
	
	/**
	 * Restituisce il valore corrispondente a una determinata cella del dataset.
	 * Il valore viene estratto dalla transazione {@link Example} all'indice specificato.
	 * @param exampleIndex indice della riga (transazione)
	 * @param attributeIndex indice della colonna (attributo)
	 * @return valore dell'attributo per la transazione specificata
	 * @throws IndexOutOfBoundsException se exampleIndex o attributeIndex non sono validi
	 */
	public Object getAttributeValue(int exampleIndex, int attributeIndex){
		if (exampleIndex < 0 || exampleIndex >= numberOfExamples) {
	        throw new IndexOutOfBoundsException(
	            "Indice della riga non valido: " + exampleIndex);
	    }
	    if (attributeIndex < 0 || attributeIndex >= attributeSet.size()) {
	        throw new IndexOutOfBoundsException(
	            "Indice dell'attributo non valido: " + attributeIndex);
	    }
	    Example example = data.get(exampleIndex); 
		// Restituisce il valore all'indice dell'attributo (la colonna)
		return example.get(attributeIndex); 
	}
	
	/**
	 * Restituisce l'attributo in posizione {@code index} all'interno dello schema.
	 * 
	 * @param index indice dell'attributo
	 * @return l'attributo alla posizione specificata
	 * @throws IndexOutOfBoundsException se l'indice non e' valido
	 */
	Attribute getAttribute(int index) {
		if (index < 0 || index >= attributeSet.size()) {
	        throw new IndexOutOfBoundsException(
	            "Indice dell'attributo non valido: " + index);
	    }
		return attributeSet.get(index);
	}
	
	/**
	 * Crea e restituisce un oggetto {@link Tuple} che rappresenta la transazione
	 * corrispondente alla riga {@code index} della matrice {@code data}.
	 * <p>
	 * Per ogni attributo nello schema {@code attributeSet}, il metodo utilizza lo RTTI
	 * per determinare se creare un {@link ContinuousItem} (per {@link ContinuousAttribute})
	 * o un {@link DiscreteItem} (per {@link DiscreteAttribute}). Gli item vengono aggiunti
	 * alla {@link Tuple} rispettando l'ordine degli attributi.
	 * </p>
	 *
	 * @param index indice della riga da convertire in {@link Tuple}
	 * @return oggetto {@link Tuple} contenente gli item della transazione
	 * @throws IndexOutOfBoundsException se l'indice della riga non e' valido
	 * @throws IllegalStateException se viene incontrato un tipo di attributo non gestito
	 */
	public Tuple getItemSet(int index) {
		if (index < 0 || index >= numberOfExamples) {
	        throw new IndexOutOfBoundsException(
	            "Indice della riga non valido: " + index);
	    }
		
		Tuple tuple = new Tuple(attributeSet.size());
		Example example = data.get(index); // Ottiene l'Example (la riga)
		
	    Iterator<Attribute> it = attributeSet.iterator();
	    int i = 0;
	    
	    while(it.hasNext()) {
	    	
	    	Attribute attr = it.next();
	        Object value = example.get(i);

	        if (attr instanceof ContinuousAttribute) {
	        	if (!(value instanceof Double)) {
	                throw new IllegalStateException(
	                    "Valore atteso Double per " + attr.getName() + " ma trovato " +
	                    (value == null ? "null" : value.getClass().getSimpleName())
	                );
	            }
	            tuple.add(new ContinuousItem(attr, (Double) value), i);
	        } else if (attr instanceof DiscreteAttribute) {
	            if (!(value instanceof String)) {
	                throw new IllegalStateException(
	                    "Valore atteso String per " + attr.getName() + " ma trovato " + 
	                    (value == null ? "null" : value.getClass().getSimpleName())
	                );
	            }
	            tuple.add(new DiscreteItem((DiscreteAttribute) attr, (String) value), i);
	        } else {
	            throw new IllegalStateException("Tipo di attributo non gestito :" + attr.getClass());
	        }

	        i++;
	    }
	    
	    return tuple;
	}


	/**
	 * Restituisce un iteratore per scorrere tutti gli attributi (header) del dataset.
	 *
	 * @return {@code Iterator<Attribute>} per iterare sugli attributi del dataset
	 */
	@Override
	public Iterator<Attribute> iterator() {
	    return attributeSet.iterator();
	}

	/**
	 * Restituisce una stringa che rappresenta lo stato dell'intero dataset.
	 * Ogni riga mostra l'indice e i valori degli attributi per quella transazione. 
	 * 
	 * @return rappresentazione testuale dell'intero dataset
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("\n--- Schema del Dataset ---\n");
	    
	    // Stampa dell'intestazione (nomi degli attributi)
	    int colIndex = 0;
	    for (Attribute attr : attributeSet) {
	        sb.append(attr.getName()).append("[").append(colIndex).append("]").append("\t");
	        colIndex++;
	    }
	    sb.append("\n--------------------------\n");
	    
	    // Stampa dei dati (transazioni)
	    for (int i = 0; i < numberOfExamples; i++) {
	        sb.append(i).append(": ");
	        sb.append(data.get(i).toString()); // data.get(i) e' garantito essere Example
	        sb.append("\n");
	    }
	    
	    return sb.toString();
	}
}


