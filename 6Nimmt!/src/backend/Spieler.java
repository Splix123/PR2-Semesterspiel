package backend;

import java.io.Serializable;
import java.util.ArrayList;

import exceptions.ExceptionDB.NichtVorhandenException;
import exceptions.ExceptionDB.UngueltigeEingabeException;

/**
 * Diese Klasse repräsentiert einen Spieler, welcher in einem Spiel angelegt
 * werden kann.
 * 
 * @author Hendrik Tesch
 */
public class Spieler implements Serializable {

	private static final long serialVersionUID = 1L;
	/** Id und Leben des Spielers */
	private int id, leben = 66;
	/** Name des Spielers */
	private String name;
	/** Geschlecht des Spielers */
	private Geschlecht geschlecht;
	/** linked list */
	private List<Karte> handkartenList;
	/** Instanz von Spiel für Komposition */
	@SuppressWarnings("unused")
	private Spiel spiel;

	/**
	 * Default Konstruktor. Ein Spieler wird mit namen und Geschlecht angelegt, kann
	 * aber auch eine KI darstellen. Wenn die karten eines Spielers
	 * <code>null</code> sind werden dem Spieler einen neue Handkarten Treemap
	 * angelegt.
	 * 
	 * @param name       Der Name unter dem der anzulegende Spieler später angezeigt
	 *                   werden soll
	 * @param geschlecht Das Geschlecht, welches ausgewählt wurde
	 * @param spiel      Instanz von Spiel damit der Spieler nicht ohne Spiel
	 *                   existieren kann
	 * @throws UngueltigeEingabeException Wird von der Methode
	 *                                    <code>Spiel.addSpieler</code> weiter
	 *                                    geworfen
	 */
	public Spieler(String name, String geschlecht, Spiel spiel) throws UngueltigeEingabeException {
		this.spiel = spiel;
		this.name = name;
		while (this.geschlecht == null) {
			switch (geschlecht) {
			case "1":
				this.geschlecht = Geschlecht.m;
				break;
			case "2":
				this.geschlecht = Geschlecht.w;
				break;
			case "3":
				this.geschlecht = Geschlecht.d;
				break;
			case "4":
				this.geschlecht = Geschlecht.ki;
				break;
			default:
				throw new UngueltigeEingabeException("Das ist keine Auswahlmoeglichkeit! Versuche es nochmal.");
			}
		}
		handkartenList = new List<Karte>();

	}

	/**
	 * Konstruktor für Laden mit zusätzlichen Parameter Leben. Setzt Leben und ruft
	 * den Default Konstruktor mit den Paramtetern Name, Geschlecht, Spiel auf.
	 * 
	 * @param name       Der Name unter dem der anzulegende Spieler später angezeigt
	 *                   werden soll
	 * @param geschlecht Das Geschlecht, welches ausgewählt wurde
	 * @param spiel      Instanz von Spiel damit der Spieler nicht ohne Spiel
	 *                   existieren kann
	 * @param leben      Lebenspunkt des Spielers
	 * @throws UngueltigeEingabeException Wird von der Methode
	 *                                    <code>Spiel.addSpieler</code> weiter
	 *                                    geworfen
	 */
	public Spieler(String name, String geschlecht, Spiel spiel, int leben) throws UngueltigeEingabeException {
		this(name, geschlecht, spiel);
		this.leben = leben;
	}

	/**
	 * Konstruktor für Laden mit zusätzlichen Parameter Leben und TreeMap karten.
	 * Setzt Leben und ruft den Default Konstruktor mit den Paramtetern Name,
	 * Geschlecht, Spiel auf. Die TreeMap karten wird null gesetzt, da diese nicht
	 * als Paramteter übergeben wird.
	 * 
	 * @param name           Der Name unter dem der anzulegende Spieler später
	 *                       angezeigt werden soll
	 * @param geschlecht     Das Geschlecht, welches ausgewählt wurde
	 * @param spiel          Instanz von Spiel damit der Spieler nicht ohne Spiel
	 *                       existieren kann
	 * @param leben          Lebenspunkt des Spielers
	 * @param handkartenList Karten des Spielers
	 * @throws UngueltigeEingabeException Wird von der Methode
	 *                                    <code>Spiel.addSpieler</code> weiter
	 *                                    geworfen
	 */
	public Spieler(String name, String geschlecht, Spiel spiel, int leben, List<Karte> handkartenList)
			throws UngueltigeEingabeException {
		this(name, geschlecht, spiel, leben);
		this.handkartenList = handkartenList;
	}

	/**
	 * Gibt den Namen eines Spielers zurück.
	 * 
	 * @return Der Spielername
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gibt die id des Spielers zurück.
	 * 
	 * @return Die ID des Spielers
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Setzt die ID eines Spielers.
	 * 
	 * @param id Die ID die ein Spieler bekommen soll
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gibt die aktuellen Leben eines Spielers zurück.
	 * 
	 * @return Die Leben eines Spieler
	 */
	public int getLeben() {
		int leben = 0;
		if (this.leben < 0) {
			leben = 0;
		} else {
			leben = this.leben;
		}
		return leben;
	}

	/**
	 * Gibt das Geschlecht des Spielers zurück (kann auch KI sein).
	 * 
	 * @return Geschlecht des Spielers
	 */
	public Enum<?> getGeschlecht() {
		return this.geschlecht;
	}

	/**
	 * Gibt die Handkarten des Spielers zurück.
	 * 
	 * @return Die Handkarten des Spielers als Array
	 */
	public Karte[] getKarten() {
		Karte[] handkarten = new Karte[handkartenList.getLength()];

		if (!handkartenList.isEmpty()) {
			ArrayList<Karte> ark = handkartenList.getAll();
			int i = 0;
			for (Karte o : ark) {
				handkarten[i] = o;
				i++;
			}

			return handkarten;
		} else {
			return null;
		}
	}

	/**
	 * Gibt die gesamte Liste zurück.
	 * 
	 * @return die gesamte Liste als <code>List</code>
	 */
	public List<Karte> getList() {
		return handkartenList;
	}

	/**
	 * Setzt die Leben des Spielers indem der gegebene Integer-Wert von dem
	 * vorhandenen Leben abgezogen wird.
	 * 
	 * @param anzahlHornochsen Die abzuziehenden Leben
	 */
	public void setLeben(int anzahlHornochsen) {
		this.leben -= anzahlHornochsen;
	}

	/**
	 * Überschreibt die toString-Methode um die Ausgabe zu verschönern.
	 * 
	 * @return Ein String, welcher die Reihen-ID, ihre Karten und die addierte
	 *         Hornochsenzahl der Karten ausgibt
	 */
	@Override
	public String toString() {
		String rueckgabe = ("Spieler Nummer " + (this.id) + " mit dem Namen " + this.getName() + " und dem Geschlecht "
				+ geschlecht + " hat folgende Karten: ");
		for (Object o : handkartenList.getAll()) {
			rueckgabe += (Karte) o + "\n";
		}

		return rueckgabe;
	}

	/**
	 * Fügt den Handkarten des Spielers eine übergebene Karte hinzu.
	 * 
	 * @param karte Die Karte die dem Spieler Hinzugefügt werden soll
	 */
	public void karteHinzufuegen(Karte karte) {
		handkartenList.insert(karte);
	}

	/**
	 * Entfernt (sofern vorhanden) eine Karte aus den Handkarten des Spielers.
	 * 
	 * @param id_karte Karte, die vom Spieler entfernt werden soll
	 * @throws NichtVorhandenException Wird geworfen, wenn die zu entfernende Karte
	 *                                 in den Handkarten des Spielers nicht
	 *                                 existiert
	 */
	public void karteEntfernen(int id_karte) throws NichtVorhandenException {
		handkartenList.remove(getKarte(id_karte));
	}

	

	/**
	 * Gibt das Kartenobjekt mit der ID gleich dem übergebenen Wert zurück.
	 * 
	 * @param id_karte ID der gewählten Karte
	 * @return das Kartenobjekt mit der gleichen ID wie der übergebene Parameter
	 * @throws NichtVorhandenException Wird geworfen, wenn die Karte in den
	 *                                 Handkarten des Spielers nicht existiert
	 */
	private Karte getKarte(int id_karte) throws NichtVorhandenException {
		Karte[] handkarten = getKarten();
		for (Karte k : handkarten) {
			if (k.getID() == id_karte) {
				return k;
			}
		}
		throw new NichtVorhandenException("Karte nicht vorhanden! Wähle eine andere aus.");
	}
}