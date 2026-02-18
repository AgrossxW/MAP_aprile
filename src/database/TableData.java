package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;



import database.TableSchema.Column;

/**
 * modella l’insieme di transazioni collezionate in una tabella. 
 * La singola transazione e' modellata dalla classe Example.
 */
public class TableData {

	DbAccess db;
	

	/**
	 * Costruttore che inizializza l'oggetto TableData con la connessione al database.
	 * @param db L'oggetto DbAccess che gestisce la connessione.
	 */
	public TableData(DbAccess db) {
		this.db = db;
	}

	/**
	 * Restituisce una lista di oggetti Example (transazioni) distinte presenti nella tabella.
	 * @param table Il nome della tabella da interrogare.
	 * @return Una lista di oggetti Example. 
	 * Si utilizza una LinkedList per l'ottima efficienza nell'aggiunta sequenziale di elementi.
	 * @throws SQLException in caso di errore nell'esecuzione della query SQL.
	 * @throws EmptySetException se la query non restituisce alcun risultato.
	 */
	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException{
		
		// Scelta di LinkedList per efficienza nell'aggiunta in coda
		LinkedList<Example> transSet = new LinkedList<Example>();		// LinkedList per memorizzare le transazioni distinte
		Statement statement = null;
		ResultSet rs = null;

		// Ricava lo schema della tabella
		TableSchema tSchema = new TableSchema(db, table);


		String query = "select distinct ";	 // Costruzione della Query SELECT DISTINCT

		for(int i = 0; i < tSchema.getNumberOfAttributes(); i++){
			Column c = tSchema.getColumn(i);
			if(i > 0)
				query += ",";
			query += c.getColumnName();	 	// Aggiunge il nome della colonna alla query
		}
		
		if(tSchema.getNumberOfAttributes()==0)
			throw new SQLException("La tabella non contiene attributi, colonne");
		
		query += (" FROM "+table);
		
		boolean empty = true;

		try{
			statement = db.getConnection().createStatement();
			rs = statement.executeQuery(query);

			while (rs.next()) {
				empty=false;
				Example currentTuple=new Example();
				
				for(int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
					if(tSchema.getColumn(i).isNumber())
						currentTuple.add(rs.getDouble(i+1)); 	
							// l'accesso posizionale alle colonne di un ResultSet parte da 1 per questo i+1
					else
						currentTuple.add(rs.getString(i+1));
				}
				transSet.add(currentTuple); // Aggiunge l'oggetto Example alla lista
			}
			// Verifica ResultSet Vuoto
			if(empty) 
				throw new EmptySetException();
			
		} finally {
			// Chiusura delle Risorse 
			if (rs != null) rs.close();
			if (statement != null) statement.close();
		}
		
		return transSet;

	}

	/**
	 * Restituisce i valori distinti (ordinati in modalità ascendente) di una colonna.
	 * * @param table Il nome della tabella da interrogare.
	 * @param column La colonna di cui ottenere i valori distinti.
	 * @return Un {@code Set<Object>} (implementato come {@code TreeSet}) che contiene i valori distinti, ordinati in modo ascendente.
	 * @throws SQLException in caso di errore nell'esecuzione della query SQL.
	 */
	public  Set<Object>getDistinctColumnValues(String table,Column column) throws SQLException{
		// TreeSet e' scelto per garantire che l'insieme sia ordinato in modalità ascendente.
		Set<Object> valueSet = new TreeSet<Object>();
		Statement statement = null;
		ResultSet rs = null;
		
		TableSchema tSchema = new TableSchema(db,table);
		
		
		String query="select distinct ";
		query+= column.getColumnName();
		query += (" FROM "+table);
		query += (" ORDER BY " +column.getColumnName());
		
		
		try {
			statement = db.getConnection().createStatement();
			rs = statement.executeQuery(query);
			
			while (rs.next()) {
					// Legge il valore dalla prima (e unica) colonna del ResultSet
					if(column.isNumber())
						valueSet.add(rs.getDouble(1)); // getDouble per i tipi numerici
					else
						valueSet.add(rs.getString(1)); // getString per i tipi testuali
			}
		} finally {
			if (rs != null) rs.close();
			if (statement != null)
					statement.close();
		}
		
		return valueSet;

	}

	
	/**
	 * Restituisce il valore aggregato (MIN o MAX) di una colonna specificata.
	 * * @param table Il nome della tabella da interrogare.
	 * @param column La colonna su cui eseguire l'aggregazione.
	 * @param aggregate L'operazione di aggregazione (MIN o MAX) specificata tramite l'enum QUERY_TYPE.
	 * @return L'oggetto che rappresenta il valore minimo o massimo.
	 * @throws SQLException in caso di errore nell'esecuzione della query SQL.
	 * @throws NoValueException se il valore restituito dall'aggregazione e' NULL (tabella vuota o colonna interamente NULL).
	 */
	public  Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException{
		Statement statement = null;
		ResultSet rs = null;
		
		TableSchema tSchema = new TableSchema(db,table);
		Object value = null;
		String aggregateOp = "";
		
		String query = "select ";
		if(aggregate == QUERY_TYPE.MAX)
			aggregateOp += "MAX";
		else if(aggregate == QUERY_TYPE.MIN)
			aggregateOp += "MIN";
		else
			// Protezione per tipi non gestiti
			throw new SQLException("Tipo di aggregazione non valido.");
		
		
		query += aggregateOp + "(" + column.getColumnName() + ") FROM " + table;
		
		try {
			// Apertura dello Statement
			statement = db.getConnection().createStatement();
			rs = statement.executeQuery(query);
			
			if (rs.next()) {
				// Accesso posizionale alla prima (e unica) colonna del ResultSet
				if(column.isNumber())
					value=rs.getDouble(1); 
				else
					value=rs.getString(1);
			}
		}
		finally {
			if (rs != null) rs.close();
			if (statement != null) statement.close();
		}
		
		if(value==null)
			throw new NoValueException("Nessun valore trovato per l'operazione " + aggregateOp+ " sulla colonna "+ column.getColumnName());
			
		return value;

	}

	/**
	 * Restituisce una rappresentazione testuale dell'oggetto TableData.
	 * Vengono incluse le informazioni sull'oggetto DbAccess utilizzato per la connessione.
	 * @return Una stringa che descrive l'oggetto DbAccess.
	 */
	@Override
	public String toString() {
		return "TableData utilizza la seguente configurazione di database:\n" + db.toString();
	}


	

}
