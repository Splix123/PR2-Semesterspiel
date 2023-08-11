package interfaces;

import java.util.Properties;

import exceptions.ExceptionDB.keinDatenzugriffException;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;

/**
 * Ist die Schnittstelle zwischen dem Spiel und dem Speichermechanismus für das
 * Speichern als serialisierte Datei, oder im CSV-Format.
 * 
 * @author Moritz Rühm
 * @author Rodez Tazo
 * @author Cindy Tuku
 */
public interface persistenzInterface {

	/**
	 * In dieser Methode wird je nach dem, ob als CSV-Datei oder serialisierte Datei
	 * gespeichert oder geladen werden soll, Reader und Writer bzw. Output- und
	 * InputStreams geöffnet, wobei der Pfad aus den Properties genommen wird.
	 * 
	 * @param p Property, welche alle informationen über den Speicherort, sowie das
	 *          zu lendende/speichernde Spiel beinhaltet Methode wird ein Objekt
	 *          uebergeben und diese wird mithilfe von Streams in ein File
	 *          gespeichert
	 * @throws keinDatenzugriffException             Wird geworfen, wenn kein Pfad
	 *                                               eingegeben wurde
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen, wenn der
	 *                                               mitgegebene Pfad zum öffnen der
	 *                                               Datei nicht stimmt, oder einer
	 *                                               der Streams nicht geöffnet oder
	 *                                               geschlossen werden kann
	 */
	public void oeffnen(Properties p) throws keinDatenzugriffException, ladenSpeichernFehlgeschlagenException;

	/**
	 * In dieser Methode wird je nach dem ob als CSV-Datei oder serialisiert
	 * gespeichert oder geladen wurde, werden Reader und Writer oder InputStream und
	 * OutputStream geschlossen.
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen, wenn einer der
	 *                                               Streams nicht korrekt
	 *                                               geschlossen werden kann
	 */
	public void schliessen() throws ladenSpeichernFehlgeschlagenException;

	/**
	 * Das in Properties gespeicherte Objekt wird aus einer File gelesen.
	 * 
	 * @return Object Dabei handelt es sich entweder um ein ganzes Spiel-Objekt
	 *         (SER) oder um Properties (CSV), welche den Spielstand beschreiben
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen, wenn das Lesen
	 *                                               der Datei durch Falsche Daten
	 *                                               nicht funktioniert hat
	 */
	public Object lesen() throws ladenSpeichernFehlgeschlagenException;

	/**
	 * Das in der Properties gespeicherte Objekt wird in eine File geschrieben.
	 * 
	 * @param p Property, welche alle informationen über den Speicherort, sowie das
	 *          zu lendende/speichernde Spiel beinhaltet Methode wird ein Objekt
	 *          uebergeben und diese wird mithilfe von Streams in ein File
	 *          gespeichert
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen wenn das
	 *                                               Schreiben in eine SER- oder
	 *                                               CSV-Datei nicht korrekt
	 *                                               funktioniert
	 */
	public void schreiben(Properties p) throws ladenSpeichernFehlgeschlagenException;
}