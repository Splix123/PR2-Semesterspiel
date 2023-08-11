package backend;

import java.io.Serializable;

/**
 * Die KLasse Reihe repräsentiert eine der vier Reihen an die man im Spiel
 * Karten anlegen kann.
 * 
 * @author Rodez Tazo
 * @author Moritz Rühm
 */
public class Reihe implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Jede Reihe bekommt eine ID von 0-4
	 */
//	public int zaehler = 1;
	private int id;
	/**
	 * Array der Länge 5 für jede Reihe
	 */
	private Karte[] karten = new Karte[5];

	/**
	 * Erstellt das Objekt Reihe mit einer Id. Der Wert von Id wird bei jedem
	 * Konstruktoraufruf inkrementiert.
	 * 
	 * @param id ID der Reihe
	 */
	public Reihe(int id) {
		this.id = id;
	}

	/**
	 * Gibt alle Karten einer Reihen aus
	 * 
	 * @return Alle Karten einer Reihe als Kartenarray
	 */
	public Karte[] getReihe() {
		return this.karten;
	}

	/**
	 * Gibt die Reiehen ID zurück
	 * 
	 * @return die ID einer Reihe
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Addiert den Hornochsenwert jeder Karte einer Reihe und gibt die Summe aus.
	 * 
	 * @return Summe der Hornochsen einer Reihe
	 */
	private int getHornochsenReihe() {
		int hornochsenAnzahl = 0;
		for (Karte k : karten) {
			if (k == null) {
				break;
			} else {
				hornochsenAnzahl += k.getHornochsen();
			}
		}
		return hornochsenAnzahl;
	}

	/**
	 * Überschreibt die toString-Methode um die Ausgabe zu verschönern.
	 * 
	 * @return Ein String, welcher die Reihen-ID, ihre Karten und die addierte
	 *         Hornochsenzahl der Karten ausgibt
	 */
	@Override
	public String toString() {
		String rueckgabe = "Reihe " + id + ": ";
		for (int i = 0; i < karten.length; i++) {
			if (karten[i] == null) {
				break;
			} else {
				rueckgabe += karten[i].getID() + ", ";
			}
		}
		rueckgabe += "Hornochsen: " + getHornochsenReihe();
		return rueckgabe;
	}

	/**
	 * Fuegt eine Karte an die Reihe hinzug. Die Karte wird an den Index ohne Value
	 * von Karte[] gespeichert.
	 * 
	 * @param karte Die Karte die hinzugefügt werden soll
	 */
	public void karteHinzufuegen(Karte karte) {
		for (int i = 0; i < this.karten.length; i++) {
			if (this.karten[i] == null) {
				this.karten[i] = karte;
				break;
			}
		}
	}

	/**
	 * Entfernt alle Karten einer Reihe.
	 */
	public void kartenEntfernen() {
		for (int i = 0; i < this.karten.length; i++) {
			this.karten[i] = null;
		}
	}
}