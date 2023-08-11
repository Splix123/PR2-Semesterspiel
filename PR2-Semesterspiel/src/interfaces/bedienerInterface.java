package interfaces;

import backend.Spiel;
import exceptions.ExceptionDB.KeineElementeVorhandenException;
import exceptions.ExceptionDB.LogFileException;
import exceptions.ExceptionDB.NichtVorhandenException;
import exceptions.ExceptionDB.UngueltigeEingabeException;
import exceptions.ExceptionDB.keinDatenzugriffException;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;
import loadAndSave.DateiTyp;

/**
 * Ist die Schnittstelle zwischen dem Spiel (also der Logik) und den Client
 * anwendungen, sodass diese nicht auf das Spiel als gesamtes zugreifen können.
 * 
 * @author Moritz Rühm
 */
public interface bedienerInterface {

	/**
	 * Dies ist die Interface Methode <code>addSpieler</code> für die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @param name       Dieser wird dem jeweiligen Spieler zugewiesen
	 * @param geschlecht Beschreibt das Geschlecht eines Spielers (kann auch KI
	 *                   sein)
	 * @throws UngueltigeEingabeException Wird geworfen, wenn versucht wird einen
	 *                                    Spieler mit einem ungueltigen Namen zu
	 *                                    erstellen
	 */
	public void addSpieler(String name, String geschlecht) throws UngueltigeEingabeException;

	/**
	 * Dies ist die Interface Methode <code>removeSpieler</code> für die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @param name um den Spieler zu identifizieren
	 * @throws NichtVorhandenException Wird geworfen, wenn versucht wird einen
	 *                                 Spieler zu entfernen, der nicht existiert
	 */
	public void removeSpieler(String name) throws NichtVorhandenException;

	/**
	 * Dies ist die Interface Methode <code>getAlleSpieler</code> für die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @return Ein Stringarray mit dem Namen jedes Spielers
	 * @throws KeineElementeVorhandenException Wird Geworfen, wenn Keine Spieler
	 *                                         vorhanden sind
	 */
	public String[] getAlleSpieler() throws KeineElementeVorhandenException;

	/**
	 * Dies ist die Interface Methode <code>starteSpiel</code> für die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException Wird von der Methode
	 *                                               <code>kartenAusteilen</code>
	 *                                               weiter geworfen
	 * @throws LogFileException                      Wird geworfen, wenn die Logfile
	 *                                               nicht erweitert werden konnte.
	 */
	public void starteSpiel() throws ladenSpeichernFehlgeschlagenException, LogFileException;

	/**
	 * Dies ist die Interface Methode <code>getSpielerAmZug</code> für die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @return Ein Stringarray mit dem Namen des Spielers, der gerade am Zug ist und
	 *         seinen Lebenspunkten
	 */
	public String[] getSpielerAmZug();

	/**
	 * Dies ist die Interface Methode <code>getKartenVonSpielerAmZug</code> für die
	 * Klasse <code>Spiel</code>.
	 * 
	 * @return Ein Stringarray mit allen Karten des Spielers am Zug
	 */
	public String[] getKartenVonSpielerAmZug();

	/**
	 * Dies ist die Interface Methode <code>getAusgespielt</code> für die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @return Ein Stringarray mit allen von den Spielern bereits ausgespielten
	 *         Karten und Reihen
	 * @throws KeineElementeVorhandenException Wird von der Methode
	 *                                         <code>getReihe</code> weiter geworfen
	 */
	public String[] getAusgespielt() throws KeineElementeVorhandenException;

	/**
	 * Dies ist die Interface Methode <code>istSpielBeendet</code> für die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @return Boolean, die beschreibt ob das Spiel beendet wurde oder nicht
	 */
	public boolean istSpielBeendet();

	/**
	 * Dies ist die Interface Methode <code>beendeZug</code> für die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @return die ID des Spielers, als Int-Wert, der eine Reihe als Strafe
	 *         aufnehmen muss
	 * @throws KeineElementeVorhandenException       Wird von der Methode
	 *                                               <code>getReihe</code> weiter
	 *                                               geworfen
	 * @throws NichtVorhandenException               Wird von der Methode
	 *                                               <code>nimmReihe</code> weiter
	 *                                               geworfen
	 * @throws ladenSpeichernFehlgeschlagenException Wird von der Methode
	 *                                               <code>kartenAusteilen</code>
	 *                                               weiter geworfen
	 */
	public int beendeZug()
			throws KeineElementeVorhandenException, NichtVorhandenException, ladenSpeichernFehlgeschlagenException;

	/**
	 * Dies ist die Interface Methode <code>ausspielen</code> fuer die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @param id_karte Karte die ausgespielt werden soll
	 * @throws NichtVorhandenException         NichtVorhandenException Wird von der
	 *                                         Methode <code>spielerAmZug</code>
	 *                                         weiter geworfen
	 * @throws KeineElementeVorhandenException Wird von der Methode
	 *                                         <code>getAusgespielt</code> weiter
	 *                                         geworfen
	 */
	public void ausspielen(int id_karte) throws NichtVorhandenException, KeineElementeVorhandenException;

	/**
	 * Dies ist die Interface Methode <code>nimmReihe</code> für die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @param id_reihe id_reihe Ist die ID der Reihe, die genommen werden muss
	 * @throws NichtVorhandenException         Wird geworfen, wenn eine Karte an
	 *                                         eine nicht existierende Reihe
	 *                                         angelegt werden soll
	 * @throws KeineElementeVorhandenException Wird von der Methode
	 *                                         <code>getReihen</code> weiter
	 *                                         geworfen
	 */
	public void nimmReihe(int id_reihe) throws NichtVorhandenException, KeineElementeVorhandenException;

	/**
	 * Dies ist die Interface Methode <code>getBisZumEndeSpielen</code> für die
	 * Klasse <code>Spiel</code>.
	 * 
	 * @return gibt den Boolean Wert true oder false der Varibale bisZumEndeSpielen
	 *         des Ki Spielers zurück
	 */
	public boolean getBisZumEndeSpielen();

	/**
	 * Dies ist die Interface Methode <code>setBisZumEndeSpielen</code> für die
	 * Klasse <code>Spiel</code>.
	 * 
	 * @param bisZumEndeSpielen Wert auf den die Varibale bisZumEndeSpielen des KI
	 *                          Spielers gesetzt werden soll
	 */
	public void setBisZumEndeSpielen(boolean bisZumEndeSpielen);

	/**
	 * Dies ist die Interface Methode <code>laden</code> für die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @param pfad Der Dateipfad aus dem ein Spiel geladen werden soll
	 * @param typ  Welche art des Spiecherns ausgewählt wurde
	 * @return Spiel Das geladene Spiel-Objekt
	 * @throws keinDatenzugriffException             Wird von der Methode
	 *                                               <code>Spiel.laden</code> weiter
	 *                                               geworfen
	 * @throws UngueltigeEingabeException            Wird von der Methode
	 *                                               <code>Spiel.laden</code> weiter
	 *                                               geworfen
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen sobald das Laden
	 *                                               einer falschen Datei oder eines
	 *                                               falschen Pfads stattfindet
	 */
	public static Spiel laden(String pfad, DateiTyp typ)
			throws UngueltigeEingabeException, keinDatenzugriffException, ladenSpeichernFehlgeschlagenException {
		bedienerInterface spiel = null;
		spiel = Spiel.laden(pfad, typ);
		return (Spiel) spiel;
	}

	/**
	 * Dies ist die Interface Methode <code>speichern</code> für die Klasse
	 * <code>Spiel</code>.
	 * 
	 * @param pfad Der Dateipfad in den ein Spiel gespeichert werden soll
	 * @param typ  Welche art des Spiecherns ausgewählt wurde
	 * @throws UngueltigeEingabeException            Wird geworfen, wenn ein Spieler
	 *                                               keinen der beiden Speichertypen
	 *                                               auszuwählen
	 * @throws KeineElementeVorhandenException       Wird von den Methoden
	 *                                               <code>getAlleSpieler</code>,
	 *                                               <code>getAusgespielt</code> und
	 *                                               <code>Ausgespielt.getReihen</code>
	 *                                               weiter geworfen
	 * @throws keinDatenzugriffException             Wird von der Methode
	 *                                               <code>PersistenzImplCSV/SER.oeffnen</code>
	 *                                               weiter geworfen
	 * @throws ladenSpeichernFehlgeschlagenException Wird von der Methode
	 *                                               <code>PersistenzImplCSV/SER.schliessen</code>
	 *                                               weiter geworfen
	 */
	public void speichern(String pfad, DateiTyp typ) throws UngueltigeEingabeException, KeineElementeVorhandenException,
			keinDatenzugriffException, ladenSpeichernFehlgeschlagenException;
}