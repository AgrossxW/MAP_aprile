package data;
/**
 * Rappresenta un oggetto di tipo continuo associato a un attributo {@link ContinuousAttribute}.
 * Estende {@link Item} e fornisce il metodo {@link #distance(Object)} e {@link #toString()}.
 * @see Item
 */
public class ContinuousItem extends Item{
	/**
	 * 
	 * @param attribute
	 * @param value
	 */
	ContinuousItem(Attribute attribute, Double value){
		super(attribute, value);
		
	}
	
	/**
	 * Calcola la distanza (in valore assoluto) tra il valore scalato 
	 * dell'oggetto corrente e quello scalato di un altro {@link ContinuousItem}.
	 *
	 * @param a altro oggetto con cui calcolare la distanza; 
	 * @return distanza assoluta tra i valori scalati
	 * @throws IllegalArgumentException se {@code a} e' {@code null} o non e' un {@code ContinuousItem}
     */
    @Override
    public double distance(Object a) {
        if (a == null) {
            throw new IllegalArgumentException("L'oggetto di confronto non può essere null.");
        }
        if (!(a instanceof ContinuousItem)) {
            throw new IllegalArgumentException("L'oggetto di confronto deve essere un ContinuousItem.");
        }

        ContinuousItem other = (ContinuousItem) a;

        double thisScaled = ((ContinuousAttribute) this.getAttribute()).getScaledValue((Double) this.getValue());
        double otherScaled = ((ContinuousAttribute) other.getAttribute()).getScaledValue((Double) other.getValue());

        return Math.abs(thisScaled - otherScaled);
    }
    
    /**
     * Fornisce una rappresentazione testuale dell'oggetto {@code ContinousItem} rappresentato dal solo valore.
     * @return rappresentazione testualle dell'oggetto
	 */
    public String toString(){
    	StringBuilder sb = new StringBuilder();
        sb.append(getValue()); // valore continuo
        return sb.toString();
    }


}
