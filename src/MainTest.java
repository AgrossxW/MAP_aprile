import data.Data;
import data.EmptyDatasetException;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;
import keyboardinput.Keyboard;
import mining.ClusteringRadiusException;
import mining.QTMiner;
import java.sql.SQLException;

public class MainTest {

	public static void main(String[] args) {
		char scelta;
		
		// L'OUTER TRY cattura solo DatabaseConnectionException (errore di connessione critico)
		try { 
			do {
                
                System.out.println("----------------------------------------");
                System.out.println("Inserisci il nome della tabella per l'apprendimento:");
                String tableName = Keyboard.readString();
                
                Data data = null;

				// L'INNER TRY cattura gli errori legati all'esistenza della tabella e al clustering
				try { 
                    
                    System.out.println("\nTentativo di caricamento dati dalla tabella '" + tableName + "'...");
                    // Data lancia: DatabaseConnectionException (propagata), SQLException, EmptySetException, NoValueException
					data = new Data(tableName); 

					System.out.println("\n--- Dataset Caricato con " + data.getNumberOfExamples() + " Transazioni Distinte ---");
					System.out.println(data);
					
					double radius = 0.0;
					System.out.println("Inserisci il raggio di clustering (> 0):");
					radius = Keyboard.readDouble();
                    
                    // QTMiner lancia: ClusteringRadiusException
					QTMiner qt = new QTMiner(radius);
					int numIter = qt.compute(data);
					
					System.out.println("\n--- Risultato del Clustering ---");
					System.out.println("Numero di cluster trovati: " + numIter);
					System.out.println(qt.getC().toString(data));
                    
				} catch (ClusteringRadiusException e) {
                    // Gestisce l'errore specifico dell'algoritmo QT
					System.out.println("\n⚠️ ERRORE di Clustering (Raggio):");
					System.out.println(data != null ? data.getNumberOfExamples() + " transazioni in un unico cluster!" : e.getMessage());
				
                } catch (EmptySetException | SQLException | NoValueException | IllegalArgumentException e) {
                    // Gestisce errori di caricamento dati/schema/query (tabella vuota, tabella inesistente, valori nulli)
                    System.out.println("\n❌ ERRORE durante il caricamento/elaborazione della tabella '" + tableName + "':");
                    System.out.println(e.getMessage());
				
				}
                
                // Richiesta di nuova esecuzione
				System.out.println("\n----------------------------------------");
				System.out.println("Vuoi eseguire un nuovo test? (y/n)");
				scelta = Keyboard.readChar();
                
			} while (scelta == 'y' || scelta == 'Y');
            
		}catch(DatabaseConnectionException e) {
             // ✅ CATTURA RAGGIUNGIBILE: Qui gestiamo la connessione fallita che termina il ciclo
			System.out.println("\n🔥 ERRORE FATALE DI CONNESSIONE AL DATABASE:");
			System.out.println(e.getMessage());
		} catch(EmptyDatasetException e) {
            // La traccia del tuo collega lo richiede, ma se non è lanciato altrove, può essere rimosso.
            // Lo tengo solo per completezza se altre parti del codice lo lanciano.
			System.out.println(e.getMessage());
		}
	}
}