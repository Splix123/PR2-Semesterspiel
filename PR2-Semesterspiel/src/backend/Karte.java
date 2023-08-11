package backend;

import java.io.Serializable;

import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;

/**
 * Die Klasse Karte wird benötigt um Karten Objekte zu erzeugen. Die Karten
 * Objekte besitzen eine ID um die Karte eindeutig zu identifizieren, da jede
 * Karte nur einmal im Spiel existiert. Der Wert stellt die Zahl auf der Karte
 * dar, und Hornochsen steht für die Strafpunkte die eine Karte geben kann.
 * 
 * @author Hendrik Tesch
 */
public class Karte implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Der Integer zaehler wird für die inkrementierung bei der Kartenerstellung im
	 * Konstruktor benutzt.
	 */
	public static int zaehler = 1;

	/**
	 * Der Integer id ist die ID einer Karte.
	 */
	public int id;

	/**
	 * Der Byte wert ist der Wert einer Karte.
	 */
	public byte wert;
	/**
	 * Der Hornochsenwert fängt bei 1 an
	 */
	private byte hornochsen = 1;

	/**
	 * Karten werden mit einer int ID und einem Wert erstellt, im Fall von 6Nimmt
	 * werden Karten von 1 - 104 erstellt. Die Id wird per Konstruktorauruf
	 * inkrementiert. Jede Karte besitzt einen Hornochsenwert, dieser wird per
	 * Division ohne Rest (Modulo) ermittelt:
	 * <ul>
	 * <li>Hierbei wird unterschieden zwischen Karten, die durch 5 und 10 teilbar
	 * sind, Paschzahlen und andere Zahlen.</li>
	 * <li>Je nach Teibarkeit addieren sich die Hornochsen (20 ist teilbar durch 5
	 * und 10). Sonderfall: 55 durch 5 teilbar und Paschzahl.</li>
	 * </ul>
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException beim Versuch der Erstellung
	 *                                               einer ungültigen Karte
	 */
	public Karte() throws ladenSpeichernFehlgeschlagenException {
		this.id = zaehler;
		this.wert = (byte) zaehler++;
		if (this.id > 104 || this.wert > 104) {
			throw new ladenSpeichernFehlgeschlagenException("Die erstellte Karte ist ungültig!");
		} else {
			if (this.wert % 5 == 0) {
				this.hornochsen += 1;
			}
			if (this.wert % 10 == 0) {
				this.hornochsen += 1;
			}
			if (this.wert % 11 == 0) {
				this.hornochsen += 4;
			}
			if (this.wert == 55) {
				this.hornochsen = 7;
			}
		}
	}

	/**
	 * Karten werden mit einem übergebenen int Wert erstellt, 1 - 104, erstellt. Die
	 * Id wird per Konstruktorauruf inkrementiert. Jede Karte besitzt einen
	 * Hornochsenwert, dieser wird per Division ohne Rest (Modulo) ermittelt:
	 * <ul>
	 * <li>Hierbei wird unterschieden zwischen Karten, die durch 5 und 10 teilbar
	 * sind, Paschzahlen und andere Zahlen.</li>
	 * <li>Je nach Teibarkeit addieren sich die Hornochsen (20 ist teilbar durch 5
	 * und 10). Sonderfall: 55 durch 5 teilbar und Paschzahl.</li>
	 * </ul>
	 * 
	 * @param wert Den Wert, den die Karte haben soll
	 * @throws ladenSpeichernFehlgeschlagenException Wird geworfen wenn eine Karte
	 *                                               geladen wird, die einen Wert
	 *                                               über 104 besitzt
	 */
	public Karte(int wert) throws ladenSpeichernFehlgeschlagenException {
		this.id = wert;
		this.wert = (byte) wert;
		if (this.id > 104 || this.wert > 104) {
			throw new ladenSpeichernFehlgeschlagenException("Die erstellte Karte ist ungültig!");
		} else {
			if (this.wert % 5 == 0) {
				this.hornochsen += 1;
			}
			if (this.wert % 10 == 0) {
				this.hornochsen += 1;
			}
			if (this.wert % 11 == 0) {
				this.hornochsen += 4;
			}
			if (this.wert == 55) {
				this.hornochsen = 7;
			}
		}
	}

	/**
	 * Gibt den byte Wert der Variable hornochsen der Karte zurück.
	 * 
	 * @return Die Minuspunkte die vom Leben eines Spielers abgezogen werden
	 */
	public int getHornochsen() {
		return this.hornochsen;
	}

	/**
	 * Gibt die Karten-ID zurueck.
	 * 
	 * @return Die ID und somit der Wert einer Karte
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Überschreibt die toString-Methode um die Ausgabe zu verschönern.
	 * 
	 * @return Ein String, der die ID und Hornochsenanzahl der jeweiligen Karte
	 *         ausgibt
	 */
	@Override
	public String toString() {
		String name = (" Karte " + this.wert + " mit " + this.hornochsen + " Hornochsen\n");
		return name;
	}
	
	/**
	 * Überschreibt die hashCode-Methode.
	 * 
	 * @return die ID der Karte
	 */
	@Override
	public int hashCode() {
		return this.getID();
	}
}