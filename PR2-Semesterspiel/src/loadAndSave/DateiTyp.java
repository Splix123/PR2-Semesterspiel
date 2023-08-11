package loadAndSave;

/**
 * Diese Enum-Klasse unterscheidet zwischen zwei Speichermöglichkeiten:
 * <ol>
 * <li>Serialisiert:
 * <ul>
 * <li>Speichert ein Spiel-Objekt als ganzes, und somit auch seine abhängigen
 * KLassen.</li>
 * </ul>
 * </li>
 * <li>CSV:
 * <ul>
 * <li>Schreibt alle nötigen Daten in eine sog. CSV-Datei, welche dann aus
 * dieser später auch wieder ausgelesen werden können.</li>
 * </ul>
 * </li>
 * </ol>
 * 
 * @author Moritz Rühm
 * @author Rodez Tazo
 */
public enum DateiTyp {
	/**
	 * Beschreibt das serialisierte Speichern.
	 */
	SER,

	/**
	 * Beschreibt das Speichern in einer CSV-Datei.
	 */
	CSV
}