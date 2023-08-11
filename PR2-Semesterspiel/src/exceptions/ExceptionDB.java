package exceptions;

/**
 * Diese Klasse beinhaltet alle exceptions die im Spiel 6Nimmt! geworfen werden.
 * Jede dieser exceptions erbt von der abstrakten Eltern-Klasse
 * <code>SechsNimmtParentException</code>.<br>
 * Fehler ID's:
 * <ol>
 * <li>FalscheSpielerAnzahlException</li>
 * <li>NichtVorhandenException</li>
 * <li>KeineElementeVorhandenException</li>
 * <li>UngueltigeEingabeException</li>
 * <li>keinDatenzugriffException</li>
 * <li>ladenSpeichernFehlgeschlagenException</li>
 * </ol>
 * 
 * @author Moritz Rühm
 */
public class ExceptionDB {

	/**
	 * Diese Exception wird geworfen, wenn die Anzahl der Spieler nicht korrekt ist
	 * und einzelne Spieler entweder hinzugefuegt oder entfernt werden muessen.
	 * 
	 * @author Moritz Ruehm
	 */
	public static class FalscheSpielerAnzahlException extends SechsNimmtParentException {
		private static final long serialVersionUID = -2005657906004256553L;
		private static int id = 1;

		/**
		 * Die Exception Methode, welche ihre ID und die Fehlermeldung an die
		 * <code>SechsNimmtParentException</code> uebergibt.
		 * 
		 * @param fehler Die Fehlermeldung, welche beim catchen der Exception ausgegeben
		 *               werden soll
		 */
		public FalscheSpielerAnzahlException(String fehler) {
			super(id, fehler);
		}
	}

	/**
	 * Diese Exception wird geworfen, wenn der Spieler ein Objekt auswählt, welches
	 * nicht existiert.
	 * 
	 * @author Moritz Ruehm
	 */
	public static class NichtVorhandenException extends SechsNimmtParentException {
		private static final long serialVersionUID = 4557362029540431341L;
		private static int id = 2;

		/**
		 * Die Exception Methode, welche ihre ID und die Fehlermeldung an die
		 * <code>SechsNimmtParentException</code> uebergibt.
		 * 
		 * @param fehler Die Fehlermeldung, welche beim catchen der Exception ausgegeben
		 *               werden soll
		 */
		public NichtVorhandenException(String fehler) {
			super(id, fehler);
		}
	}

	/**
	 * Diese Exception wird geworfen, wenn ein Spieler eine Liste anzeigen möchte,
	 * die noch keine Elemente enthaelt.
	 * 
	 * @author Moritz Ruehm
	 */
	public static class KeineElementeVorhandenException extends SechsNimmtParentException {
		private static final long serialVersionUID = -1088083534203092946L;
		private static int id = 3;

		/**
		 * Die Exception Methode, welche ihre ID und die Fehlermeldung an die
		 * <code>SechsNimmtParentException</code> uebergibt.
		 * 
		 * @param fehler Die Fehlermeldung, welche beim catchen der Exception ausgegeben
		 *               werden soll
		 */
		public KeineElementeVorhandenException(String fehler) {
			super(id, fehler);
		}
	}

	/**
	 * Diese Exception wird geworfen, wenn die Eingabe eines Spielers nicht gueltig
	 * bzw. zutreffend ist.
	 * 
	 * @author Moritz Ruehm
	 */
	public static class UngueltigeEingabeException extends SechsNimmtParentException {
		private static final long serialVersionUID = -2678840065390979375L;
		private static int id = 4;

		/**
		 * Die Exception Methode, welche ihre ID und die Fehlermeldung an die
		 * <code>SechsNimmtParentException</code> uebergibt.
		 * 
		 * @param fehler Die Fehlermeldung, welche beim catchen der Exception ausgegeben
		 *               werden soll
		 */
		public UngueltigeEingabeException(String fehler) {
			super(id, fehler);
			
		}
	}

	/**
	 * Diese Exception wird geworfen, wenn ein Datenzugriff fehlschlaegt.
	 * 
	 * @author Moritz Ruehm
	 */
	public static class keinDatenzugriffException extends SechsNimmtParentException {
		private static final long serialVersionUID = -2429655801193375070L;
		private static int id = 5;

		/**
		 * Die Exception Methode, welche ihre ID und die Fehlermeldung an die
		 * <code>SechsNimmtParentException</code> uebergibt.
		 * 
		 * @param fehler Die Fehlermeldung, welche beim catchen der Exception ausgegeben
		 *               werden soll
		 */
		public keinDatenzugriffException(String fehler) {
			super(id, fehler);
		}
	}

	/**
	 * Diese Exception wird geworfen, wenn ein Datenzugriff fehlschlaegt.
	 * 
	 * @author Moritz Ruehm
	 */
	public static class ladenSpeichernFehlgeschlagenException extends SechsNimmtParentException {
		private static final long serialVersionUID = 1589055804163414520L;
		private static int id = 6;

		/**
		 * Die Exception Methode, welche ihre ID und die Fehlermeldung an die
		 * <code>SechsNimmtParentException</code> uebergibt.
		 * 
		 * @param fehler Die Fehlermeldung, welche beim catchen der Exception ausgegeben
		 *               werden soll
		 */
		public ladenSpeichernFehlgeschlagenException(String fehler) {
			super(id, fehler);
		}
	}
	
	/**
	 * Diese Exception wird geworfen, wenn ein Datenzugriff fehlschlaegt.
	 * 
	 * @author Moritz Ruehm
	 */
	public static class LogFileException extends SechsNimmtParentException {
		private static final long serialVersionUID = -8851104256814133093L;
		private static int id = 7;

		/**
		 * Die Exception Methode, welche ihre ID und die Fehlermeldung an die
		 * <code>SechsNimmtParentException</code> uebergibt.
		 * 
		 * @param fehler Die Fehlermeldung, welche beim catchen der Exception ausgegeben
		 *               werden soll
		 */
		public LogFileException(String fehler) {
			super(id, fehler);
		}
	}
	
	/**
	 * Diese Exception wird geworfen, wenn ein Datenzugriff fehlschlaegt.
	 * 
	 * @author Moritz Ruehm
	 */
	//TODO
	public static class kiThreadExeption extends SechsNimmtParentException {
		private static final long serialVersionUID = -8705846681603744370L;
		private static int id = 8;

		/**
		 * Die Exception Methode, welche ihre ID und die Fehlermeldung an die
		 * <code>SechsNimmtParentException</code> uebergibt.
		 * 
		 * @param fehler Die Fehlermeldung, welche beim catchen der Exception ausgegeben
		 *               werden soll
		 */
		public kiThreadExeption(String fehler) {
			super(id, fehler);
		}
	}

}