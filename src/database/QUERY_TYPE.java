package database;

/**
 * Enumerazione utilizzata per specificare l'operatore SQL di aggregazione
 * da applicare a una colonna del database.
 * <p>
 * Le costanti di questa enum vengono utilizzate per formulare query SQL 
 * che calcolano il valore minimo o massimo.
 * </p>
 */
public enum QUERY_TYPE {
	
    /**
     * Rappresenta l'operatore di aggregazione SQL MIN (valore minimo).
     */
	MIN, 
    
    /**
     * Rappresenta l'operatore di aggregazione SQL MAX (valore massimo).
     */
	MAX
}