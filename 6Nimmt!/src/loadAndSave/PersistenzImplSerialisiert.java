package loadAndSave;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import exceptions.ExceptionDB.keinDatenzugriffException;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;
import interfaces.persistenzInterface;

/**
 * Diese Klasse handelt das Serialisierte Speichern eines Spiels. Hier werden
 * Streams geöffnet und geschlossen, sowie ganze Spielobjekte gespeichert.
 * 
 * @author Moritz Rühm
 * @author Rodez Tazo
 * @author Cindy Tuku
 */
public class PersistenzImplSerialisiert implements persistenzInterface {
	private boolean schreibend = false;
	private String pfad = null;
	private ObjectOutputStream oos = null;
	private FileOutputStream fos = null;
	private ObjectInputStream ois = null;
	private FileInputStream fis = null;

	/**
	 * In dieser Methode werden basierend auf der Boolean <code>schreibend</code>
	 * OutputStreams oder InputStreams geoeffnet
	 * 
	 * @param p Property, welche alle informationen über den Speicherort, sowie das
	 *          zu lendende/speichernde Spiel beinhaltet
	 * @throws keinDatenzugriffException             Wird geworfen, wenn kein Pfad
	 *                                               eingegeben wurde
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen, wenn die Streams
	 *                                               nicht korrekt geöffnet werden
	 *                                               können
	 */
	public void oeffnen(Properties p) throws keinDatenzugriffException, ladenSpeichernFehlgeschlagenException {
		pfad = (String) p.get("pfad");
		if (pfad == null) {
			throw new keinDatenzugriffException("ungueltiger Pfad eingegeben! Versuche es nocheinmal.");
		}
		schreibend = (boolean) p.get("schreibend");
		if (schreibend) {
			try {
				fos = new FileOutputStream(pfad);
				oos = new ObjectOutputStream(fos);
			} catch (IOException e) {
				throw new ladenSpeichernFehlgeschlagenException(
						"Es gab ein Problem mit dem Schreiben der Datei! Versuche es noch einmal.");
			}
		} else {
			try {
				fis = new FileInputStream(pfad);
				ois = new ObjectInputStream(fis);
			} catch (IOException e) {
				throw new ladenSpeichernFehlgeschlagenException(
						"Es gab ein Problem mit dem Lesen der Datei! Versuche es noch einmal.");
			}
		}
	}

	/**
	 * In dieser Methode werden alle zuvor geöffneten Streams wieder korrekt
	 * geschlossen, sodass keine Daten im Buffer zurückbleiben. schließt ein File
	 * und diendazugehoerigen Streams oder Writer oder Reader In dieser Methode
	 * werden basierend auf der Boolean <code>schreibend</code> OutputStreams oder
	 * InputStreams geschlossen nd auf null gesetzt.
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen, wenn die Streams
	 *                                               nicht korrekt geschlossen
	 *                                               werden können
	 */
	public void schliessen() throws ladenSpeichernFehlgeschlagenException {
		try {
			if (fos != null) {
				fos.close();
				fos = null;
			}
			if (oos != null) {
				oos.close();
				oos = null;
			}
			if (fis != null) {
				fis.close();
				fis = null;
			}
			if (ois != null) {
				ois.close();
				ois = null;
			}
		} catch (IOException e) {
			throw new ladenSpeichernFehlgeschlagenException(
					"Die Datei konnte nicht komplett geschlossen/geöffnet werden!");
		}
	}

	/**
	 * Das im Parameter p Properties gespeicherte Objekt wird aus ein File gelesen.
	 * 
	 * @return spiel das geladene Spiel-objekt Methode wird ein File uebergeben und
	 *         aus diesem File wird rausgelsen
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen, wenn ein
	 *                                               Spielobjekt nicht korrekt
	 *                                               geladen werden konnte
	 */
	public Object lesen() throws ladenSpeichernFehlgeschlagenException {
		Object spiel = null;
		try {
			spiel = (Object) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			throw new ladenSpeichernFehlgeschlagenException("Die Datei konnte nicht geladen werden!");
		}
		return spiel;
	}

	/**
	 * Das in Parameter p Properties gespeicherte Objekt wird in ein File
	 * geschrieben.
	 * 
	 * @param p Property, welche alle informationen über den Speicherort, sowie das
	 *          zu lendende/speichernde Spiel beinhaltet Methode wird ein Objekt
	 *          uebergeben und diese wird mithilfe von Streams in ein File
	 *          gespeichert
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen wenn ein zu
	 *                                               speicherndes Spiel nocht
	 *                                               vorhanden oder fehlerhaft ist.
	 */
	public void schreiben(Properties p) throws ladenSpeichernFehlgeschlagenException {
		Object spiel = null;
		spiel = (Object) p.get("spielobjekt");
		if (spiel == null) {
			throw new ladenSpeichernFehlgeschlagenException(
					"Es konnte nicht gespeichert werden, da kein Spiel gefunden wurde");
		}
		try {
			oos.writeObject(spiel);
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			throw new ladenSpeichernFehlgeschlagenException("Die Datei konnte nicht gespeichert werden!");
		}
	}
}