package loadAndSave;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Diese Klasse erstellt eine sog. Log-Datei, welche jede Zug, Exception, etc...
 * jedes Spiels speichert und in eine Datei schreibt.
 * 
 * @author Cindy Tuku
 */
public class LogFileTool implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient PrintWriter pwriter;
	private transient BufferedWriter bwriter;
	private transient FileWriter fwriter;
	@SuppressWarnings("unused")
	private Date date;

	/**
	 * Ein Übergebener String wird in die Datei geschrieben.
	 * 
	 * @param a Der zu schreibende String
	 */
	public void stringEintragen(String a) {
		try {
			fwriter = new FileWriter("saveGames/LogFiles.csv", true);
			bwriter = new BufferedWriter(fwriter);
			pwriter = new PrintWriter(bwriter);

			pwriter.print(datum());
			pwriter.print(";");
			pwriter.print(zeit());
			pwriter.print(";");
			pwriter.print(a);
			pwriter.print(" ");
			pwriter.print(",");
			pwriter.println();

		} catch (IOException e) {
			// throw new LogFileException("Es konnten keine Writer geöffnet werden");

		} finally {
			try {
				pwriter.close();
				bwriter.close();
				fwriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Berechnet das Datum.
	 * 
	 * @return Das aktuelle Datum
	 */
	public String datum() {
		date = new Date();
		DateFormat form = new SimpleDateFormat("MM-dd-yyyy");
		String datum = form.format(new Date());
		return datum;
	}

	/**
	 * Berechnet die Zeit.
	 * 
	 * @return Die aktuelle Zeit
	 */
	public String zeit() {
		date = new Date();
		DateFormat form = new SimpleDateFormat("HH:mm:ss");
		String zeit = form.format(new Date());
		return zeit;
	}

}
