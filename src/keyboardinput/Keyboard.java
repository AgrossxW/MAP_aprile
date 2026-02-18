//********************************************************************
//
//  Keyboard.java       Author: Lewis and Loftus
//
//  Facilitates keyboard input by abstracting details about input
//  parsing, conversions, and exception handling.
//********************************************************************

package keyboardinput;

import java.io.* ;
import java.util.* ;

/**
 * La classe {@code Keyboard} fornisce metodi statici per facilitare
 * la lettura di input da tastiera in applicazioni Java.
 * 
 * <p>Incapsula la gestione della lettura da tastiera,
 * inclusa la tokenizzazione dell’input, la gestione delle eccezioni
 * e la conversione in vari tipi primitivi Java come {@code int},
 * {@code double}, {@code boolean}, ecc.</p>
 * 
 * <p>Questa classe e' utile per semplificare l'interazione testuale
 * con l’utente, offrendo una sintassi più compatta e leggibile
 * rispetto all’uso diretto di {@code Scanner} o {@code BufferedReader}.</p>
 * 
 * <p>Comprende anche strumenti per la gestione degli errori di input
 * e consente di decidere se stampare o meno i messaggi di errore.</p>
 * 
 * <p>Originariamente sviluppata da Lewis e Loftus.</p>
 * 
 * @author Lewis
 * @author Loftus
 */
public class Keyboard {
	// ************* Error Handling Section **************************

	private static boolean printErrors = true;

	private static int errorCount = 0;

	/**
	 * Restituisce il numero totale di errori verificatisi durante l'input.
	 * @return numero di errori registrati
	 */
	public static int getErrorCount() {
		return errorCount;
	}

	/**
	 * Reimposta il contatore degli errori a zero.
	 * @param count parametro non utilizzato
	 */
	public static void resetErrorCount(int count) {
		errorCount = 0;
	}

	/**
	 * Indica se i messaggi di errore vengono stampati a video.
	 * @return {@code true} se gli errori sono stampati, {@code false} altrimenti
	 */
	public static boolean getPrintErrors() {
		return printErrors;
	}

	/**
	 * Imposta se stampare o meno i messaggi di errore.
	 * @param flag {@code true} per abilitare la stampa degli errori
	 */
	public static void setPrintErrors(boolean flag) {
		printErrors = flag;
	}

	/**
	 * Incrementa il contatore degli errori e stampa un messaggio se la stampa e' attiva.
	 * @param str messaggio di errore
	 */
	private static void error(String str) {
		errorCount++;
		if (printErrors)
			System.out.println(str);
	}

	// ************* Tokenized Input Stream Section ******************

	private static String current_token = null;

	private static StringTokenizer reader;

	private static BufferedReader in = new BufferedReader(
			new InputStreamReader(System.in));

	/**
	 * Restituisce il prossimo token disponibile dall'input.
	 * @return prossimo token
	 */
	private static String getNextToken() {
		return getNextToken(true);
	}

	/**
	 * Restituisce il prossimo token disponibile, saltando o meno i delimitatori.
	 * @param skip se {@code true}, salta i delimitatori
	 * @return prossimo token
	 */
	private static String getNextToken(boolean skip) {
		String token;

		if (current_token == null)
			token = getNextInputToken(skip);
		else {
			token = current_token;
			current_token = null;
		}

		return token;
	}

	/**
	 * Legge il prossimo token dalla tastiera.
	 * @param skip se {@code true}, salta delimitatori
	 * @return token letto
	 */
	private static String getNextInputToken(boolean skip) {
		final String delimiters = " \t\n\r\f";
		String token = null;

		try {
			if (reader == null)
				reader = new StringTokenizer(in.readLine(), delimiters, true);

			while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
				while (!reader.hasMoreTokens())
					reader = new StringTokenizer(in.readLine(), delimiters,
							true);

				token = reader.nextToken();
			}
		} catch (Exception exception) {
			token = null;
		}

		return token;
	}

	/**
	 * Verifica se ci sono ancora token disponibili sulla riga corrente.
	 * @return {@code true} se non ci sono più token
	 */
	public static boolean endOfLine() {
		return !reader.hasMoreTokens();
	}

	// ************* Reading Section *********************************

	/**
	 * Legge una stringa composta da più token.
	 * @return stringa letta
	 */
	public static String readString() {
		String str;

		try {
			str = getNextToken(false);
			while (!endOfLine()) {
				str = str + getNextToken(false);
			}
		} catch (Exception exception) {
			error("Error reading String data, null value returned.");
			str = null;
		}
		return str;
	}

	/**
	 * Legge un singolo token come parola.
	 * @return parola letta
	 */
	public static String readWord() {
		String token;
		try {
			token = getNextToken();
		} catch (Exception exception) {
			error("Error reading String data, null value returned.");
			token = null;
		}
		return token;
	}

	/**
	 * Legge un valore booleano (true o false).
	 * @return valore booleano
	 */
	public static boolean readBoolean() {
		String token = getNextToken();
		boolean bool;
		try {
			if (token.toLowerCase().equals("true"))
				bool = true;
			else if (token.toLowerCase().equals("false"))
				bool = false;
			else {
				error("Error reading boolean data, false value returned.");
				bool = false;
			}
		} catch (Exception exception) {
			error("Error reading boolean data, false value returned.");
			bool = false;
		}
		return bool;
	}

	/**
	 * Legge un singolo carattere.
	 * @return carattere letto
	 */
	public static char readChar() {
		String token = getNextToken(false);
		char value;
		try {
			if (token.length() > 1) {
				current_token = token.substring(1, token.length());
			} else
				current_token = null;
			value = token.charAt(0);
		} catch (Exception exception) {
			error("Error reading char data, MIN_VALUE value returned.");
			value = Character.MIN_VALUE;
		}

		return value;
	}

	/**
	 * Legge un intero.
	 * @return valore intero
	 */
	public static int readInt() {
		String token = getNextToken();
		int value;
		try {
			value = Integer.parseInt(token);
		} catch (Exception exception) {
			error("Error reading int data, MIN_VALUE value returned.");
			value = Integer.MIN_VALUE;
		}
		return value;
	}

	/**
	 * Legge un valore long.
	 * @return valore long
	 */
	public static long readLong() {
		String token = getNextToken();
		long value;
		try {
			value = Long.parseLong(token);
		} catch (Exception exception) {
			error("Error reading long data, MIN_VALUE value returned.");
			value = Long.MIN_VALUE;
		}
		return value;
	}

	/**
	 * Legge un valore float.
	 * @return valore float
	 */
	public static float readFloat() {
		String token = getNextToken();
		float value;
		try {
			value = (new Float(token)).floatValue();
		} catch (Exception exception) {
			error("Error reading float data, NaN value returned.");
			value = Float.NaN;
		}
		return value;
	}

	/**
	 * Legge un valore double.
	 * @return valore double
	 */
	public static double readDouble() {
		String token = getNextToken();
		double value;
		try {
			value = (new Double(token)).doubleValue();
		} catch (Exception exception) {
			error("Error reading double data, NaN value returned.");
			value = Double.NaN;
		}
		return value;
	}
}
