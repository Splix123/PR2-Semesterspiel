package loadAndSave;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import exceptions.ExceptionDB.keinDatenzugriffException;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;
import interfaces.persistenzInterface;

/**
 * Diese Klasse bietet die Möglichkeit ein angefangenes Spiel im CSV-Format zu
 * speichern und Später wieder zu laden.
 * 
 * @author Moritz Rühm
 * @author Rodez Tazo
 * @author Cindy Tuku
 */
public class PersistenzImplCSV implements persistenzInterface {
	private boolean schreibend = false;
	private String pfad = null;
	private PrintWriter pw = null;
	private FileWriter fw = null;
	private FileReader fr = null;
	private BufferedReader br = null;

	/**
	 * In dieser Methode werden basierend auf der Boolean <code>schreibend</code>
	 * alle Write- bzw. Readstreams geöffnet.
	 * 
	 * @param p Property, welche alle informationen über den Speicherort, sowie das
	 *          zu lesende/speichernde Spiel beinhaltet
	 * @throws keinDatenzugriffException             Wird geworfen, wenn kein Pfad
	 *                                               eingegeben wurde
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen, wenn der
	 *                                               eingegebene Pfad, aus dem die
	 *                                               Datei geöffnet werden soll
	 *                                               nicht existiert
	 */
	@Override
	public void oeffnen(Properties p) throws keinDatenzugriffException, ladenSpeichernFehlgeschlagenException {
		pfad = (String) p.get("pfad");
		if (pfad == null) {
			throw new keinDatenzugriffException("ungueltiger Pfad eingegeben! Versuche es nocheinmal.");
		}
		schreibend = (boolean) p.get("schreibend");
		if (schreibend) {
			try {
				fw = new FileWriter(pfad);
				pw = new PrintWriter(fw);
			} catch (IOException e) {
				throw new ladenSpeichernFehlgeschlagenException(
						"Es gab ein Problem mit dem Schreiben der Datei! Versuche es noch einmal.");
			}
		} else {
			try {
				fr = new FileReader(pfad);
				br = new BufferedReader(fr);
			} catch (IOException e) {
				throw new ladenSpeichernFehlgeschlagenException(
						"Es gab ein Problem mit dem Lesen der Datei! Versuche es noch einmal.");
			}
		}
	}

	/**
	 * In dieser Methode werden alle zuvor geöffneten Streams wieder korrekt
	 * geschlossen, sodass keine Daten im Buffer zurückbleiben.
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen, wenn die Streams
	 *                                               nicht korrekt geschlossen
	 *                                               werden können
	 */
	@Override
	public void schliessen() throws ladenSpeichernFehlgeschlagenException {
		try {
			if (pw != null) {
				pw.close();
				pw = null;
			}
			if (fw != null) {
				fw.close();
				fw = null;
			}
			if (fr != null) {
				fr.close();
				fr = null;
			}
			if (br != null) {
				br.close();
				br = null;
			}
		} catch (IOException e) {
			throw new ladenSpeichernFehlgeschlagenException(
					"Die Datei konnte nicht komplett geschlossen/geöffnet werden!");
		}
	}

	/**
	 * Lädt das Spiel im CSV Format
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException wenn Daten verändert wurden die
	 *                                               den gespeicherten Spielstand
	 *                                               nicht mehr Spielkonform machen
	 */
	@Override
	public Object lesen() throws ladenSpeichernFehlgeschlagenException {
		Properties p = new Properties();
		String line = null;
		String[] spielDaten = null, spielerDaten = null, reihenDaten = null, kartenDaten = null,
				ausgespieltDaten = null;
		int x;
		try {
			// Spiel
			line = br.readLine();
			spielDaten = line.split(";");
			if (spielDaten[0].equals("SPIEL")) {
				p.put("spielerAnzahl", spielDaten[1]);
				p.put("ausgespieltAnzahl", spielDaten[2]);
				p.put("rundeAktiv", spielDaten[3]);
				p.put("strafReiheSpielerId", spielDaten[4]);
				p.put("strafReiheKarteId", spielDaten[5]);
			} else {
				throw new ladenSpeichernFehlgeschlagenException(
						"Die Grunddaten des Spiels konnten nicht geladen werden!");
			}
			// Spieler
			x = 0;
			for (int i = 0; i < Integer.parseInt(spielDaten[1]); i++) {
				line = br.readLine();
				spielerDaten = line.split(";");
				if (spielerDaten[0].equals("SPIELER")) {
					p.put("name" + i, spielerDaten[2]);
					p.put("geschlecht" + i, spielerDaten[3]);
					// TODO keine exeption
					if ((0 < Integer.parseInt(spielerDaten[4]) || (66 >= Integer.parseInt(spielerDaten[4])))) {
						p.put("leben" + i, spielerDaten[4]);
					} else {
						throw new ladenSpeichernFehlgeschlagenException(
								"Die Grunddaten des " + i + ". Spielers konnten nicht geladen werden! Ungültige Leben");
					}
					p.put("handKartenAnzahl" + i, spielerDaten[5]);
					// Handkarten
					for (int j = 0; j < Integer.parseInt(spielerDaten[5]); j++) {
						line = br.readLine();
						kartenDaten = line.split(";");
						if (kartenDaten[0].equals("KARTE")) {
							p.put("handKarte" + x, kartenDaten[1]);
							x++;
						} else {
							throw new ladenSpeichernFehlgeschlagenException(
									"Die Handkarten des " + (i + 1) + ". Spielers konnten nicht geladen werden!");
						}
					}
				} else {
					throw new ladenSpeichernFehlgeschlagenException(
							"Die Grunddaten des " + i + ". Spielers konnten nicht geladen werden!");
				}
			}
			// Reihen
			x = 0;
			for (int i = 0; i < 4; i++) {
				line = br.readLine();
				reihenDaten = line.split(";");
				if (reihenDaten[0].equals("REIHE")) {
					if (reihenDaten[1].equals(String.valueOf(i + 1))) {
						p.put("reihenID" + i, reihenDaten[1]);
						p.put("kartenAnzahl" + i, reihenDaten[2]);
						for (int j = 0; j < Integer.parseInt(reihenDaten[2]); j++) {
							line = br.readLine();
							kartenDaten = line.split(";");
							if (kartenDaten[0].equals("KARTE")) {
								p.put("reihenkarte" + x, kartenDaten[1]);
								x++;
							} else {
								throw new ladenSpeichernFehlgeschlagenException(
										"Die Karten der " + i + ". Reihen konnten nicht geladen werden!");
							}
						}
					} else {
						throw new ladenSpeichernFehlgeschlagenException(
								"Eine Reihe konnte nicht geladen werden, da ihre ID nicht stimm!");
					}
				} else {
					throw new ladenSpeichernFehlgeschlagenException(
							"Die Grunddaten der " + i + ". Reihe konnten nicht geladen werden!");
				}
			}
			// Ausgespielt
			for (int i = 0; i < Integer.parseInt(spielDaten[2]); i++) {
				line = br.readLine();
				ausgespieltDaten = line.split(";");
				if (ausgespieltDaten[0].equals("AUSGESPIELT")) {
					p.put("ausgespieltKarte" + i, ausgespieltDaten[1]);
					p.put("ausgespieltSpieler" + i, ausgespieltDaten[2]);
				} else {
					throw new ladenSpeichernFehlgeschlagenException("Fehler bei Karten lesen!");
				}
			}
		} catch (NullPointerException | NumberFormatException | IOException e) {
			throw new ladenSpeichernFehlgeschlagenException(
					"Das Spiel konnte nicht geladen werden!\nVersuche eine andere Datei");
		}
		return p;
	}

	/**
	 * Speichert das Spiel im CSV Format
	 * 
	 * @param p Property, welche alle informationen über den Speicherort, sowie das
	 *          zu lesende/speichernde Spiel beinhaltet
	 */
	@Override
	public void schreiben(Properties p) {
		int x;
		// Spiel
		pw.printf("SPIEL;%d;%d;%s;%s;%s;\n", p.get("spielerAnzahl"), p.get("ausgespieltAnzahl"), p.get("rundeAktiv"), p.get("strafReiheSpielerId"), p.get("strafReiheKarteId"));
		// Spieler
		x = 0;
		for (int i = 0; i < (int) p.get("spielerAnzahl"); i++) {
			pw.printf("SPIELER;%d;%s;%s;%d;%d;\n", p.get("spielerId" + i), p.get("name" + i), p.get("geschlecht" + i),
					p.get("leben" + i), p.get("handKartenAnzahl" + i));
			// Handkarten
			for (int j = 0; j < (int) p.get("handKartenAnzahl" + i); j++) { // Schön machen
				pw.printf("KARTE;%d;\n", p.get("handKarte" + x));
				x++;
			}
		}
		// Reihen
		x = 0;
		for (int i = 0; i < 4; i++) {
			pw.printf("REIHE;%d;%d;\n", p.get("reiheId" + i), p.get("kartenAnzahl" + i));

			for (int j = 0; j < (int) p.get("kartenAnzahl" + i); j++) {
				pw.printf("KARTE;%d;\n", p.get("reihenKarte" + x));
				x++;
			}
		}
		// Ausgespielt
		for (int i = 0; i < (int) p.get("ausgespieltAnzahl"); i++) {
			pw.printf("AUSGESPIELT;%d;%d;\n", p.get("ausgespielt" + i), p.get("spielerId" + i));
		}
	}
}