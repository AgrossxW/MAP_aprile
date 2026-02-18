package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Modella lo schema (struttura) di una specifica tabella del database.
 * <p>
 * Questa classe interroga i metadati JDBC per recuperare nomi e tipi delle colonne
 * e li mappa in tipi astratti semplici ("string" o "number") per l'uso nell'applicazione.
 * </p>
 */
public class TableSchema {
	
	DbAccess db;	// Riferimento all'oggetto di accesso al DB per ottenere la connessione.
	
	/**
	 * Classe interna annidata per modellare una singola colonna (attributo) della tabella.
	 * Incapsula il nome e il tipo astratto della colonna.
	 */
	public class Column{
		private String name;
		private String type;
		
		/**
		 * Costruttore per la colonna.
		 * @param name Il nome effettivo della colonna nel DB.
		 * @param type Il tipo astratto mappato ("string" o "number").
		 */
		Column(String name,String type){
			this.name=name;
			this.type=type;
		}
		
		/**
		 * Restituisce il nome della colonna.
		 * @return Il nome (String) della colonna.
		 */
		public String getColumnName(){
			return name;
		}
		
		/**
		 * Verifica se la colonna memorizza un valore numerico.
		 * @return {@code true} se il tipo astratto e' "number", {@code false} altrimenti.
		 */
		public boolean isNumber(){
			return type.equals("number");
		}
		
		/**
		 * Restituisce una rappresentazione testuale della colonna.
		 * @return Una stringa nel formato "nome:tipo".
		 */
		public String toString(){
			return name + ":" + type;
		}
	}
	
	List<Column> tableSchema = new ArrayList<Column>();		// Lista delle colonne (attributi) della tabella.
	
	/**
	 * Costruttore che inizializza lo schema della tabella interrogando i metadati JDBC.
	 * <p>
	 * Mappa i tipi SQL comuni a tipi astratti semplici ("string" o "number").
	 * </p>
	 * @param db Riferimento all'oggetto di accesso al DB per ottenere la connessione.
	 * @param tableName Il nome della tabella di cui si vuole ottenere lo schema.
	 * @throws SQLException Se si verifica un errore durante l'accesso ai metadati del database.
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException{

		this.db = db;
		HashMap<String,String> mapSQL_JAVATypes = new HashMap<String, String>();	// Mappa per convertire tipi SQL a tipi astratti.
		// Popolamento della mappa con tipi SQL comuni.
		
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
		
		
		Connection conn = db.getConnection();
		DatabaseMetaData meta = conn.getMetaData();		// Ottiene i metadati del database
	    ResultSet res = meta.getColumns(null, null, tableName, null);	// Ottiene le colonne della tabella specificata.
		   
	    while (res.next()) {
			if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
				tableSchema.add(new Column(
	        				res.getString("COLUMN_NAME"),
	        				mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
	        				);
	      }
	      res.close();		// chiude il ResultSet
	
	
	    
	    }

		/**
		 * Restituisce il numero di attributi (colonne) nella tabella.
		 * @return Il numero di attributi (int).
		 */
		public int getNumberOfAttributes(){
			return tableSchema.size();
		}
		
		/**
		 * Restituisce la colonna (attributo) alla posizione specificata.
		 * @param index L'indice della colonna da recuperare (0-based).
		 * @return L'oggetto Column corrispondente all'indice specificato.
		 */
		public Column getColumn(int index){
			return tableSchema.get(index);
		}

		/**
		 * Restituisce una rappresentazione testuale dello schema della tabella.
		 * @return Una stringa che elenca tutte le colonne con i loro nomi e tipi.
		 */
		public String toString(){
			StringBuilder sb = new StringBuilder();
			for(Column c : tableSchema){
				sb.append(c.toString() + " ");
			}
			return sb.toString();
		}
}

		     


