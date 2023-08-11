package backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

import exceptions.ExceptionDB.KeineElementeVorhandenException;

/**
 * Diese Klasse ist dafür zuständig alle ausgespielten Karten und Reihen zu
 * speichern und beinhaltet allgemein Methoden, die die Auspielbarkeit einer
 * Karte prüfen und das eigentliche Ausspielen bewältigen.
 * 
 * @author Nicole Shiner
 */
public class Ausgespielt implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Enthält die von den Spielern ausgespielten Karten mit dem Key Spieler,
	 * sortiert über den KartenId Wert als Integer von kleinster zu größter Karte
	 */
	private TreeMap<Integer, HashMap<Spieler, Karte>> ausgespielteKarten = new TreeMap<Integer, HashMap<Spieler, Karte>>();
	/**
	 * enthält die Reihen mit ReihenId als Key
	 */
	private HashMap<Integer, Reihe> reihen = new HashMap<>();

	/**
	 * Gibt die HashMap reihen mit den 4 Reihen und ihren angelegten Karten zurück.
	 * 
	 * @throws KeineElementeVorhandenException Geworfen, wenn die HashMap reihen
	 *                                         keine Elemente besitzt
	 * @return Alle Karten der Reihen
	 */
	public HashMap<Integer, Reihe> getReihen() throws KeineElementeVorhandenException {
		if (reihen.size() == 0) {
			throw new KeineElementeVorhandenException("Es sind noch keine Reihen angelegt");
		} else {
			return reihen;
		}
	}

	/**
	 * Gibt die TreeMap ausgespielteKarten zurück.
	 * 
	 * @return Alle Karten die bereits gelegt wurden
	 */
	public TreeMap<Integer, HashMap<Spieler, Karte>> getAusgespielteKarten() {
		return ausgespielteKarten;
	}

	/**
	 * Fügt die ID der Karte als Key und eine HashMAp als Value in die TreeMap
	 * ausgespielteKarten. Die HashMap enthält das Spieler Objekt als Key und Karte
	 * als Value. Die Keys der TreeMap sind vom kleinsten zum größten Key sortiert.
	 * 
	 * @param spieler speichert das Objekt Spieler als Key der HashMap
	 * @param karte   speichert die ausgespielte Karte des Spielers als Value in die
	 *                HashMap
	 */
	public void addKarte(Spieler spieler, Karte karte) {
		HashMap<Spieler, Karte> hm = new HashMap<Spieler, Karte>();
		hm.put(spieler, karte);
		ausgespielteKarten.put(karte.getID(), hm);
	}

	/**
	 * Reihenobjekte werden der HashMap reihen hinzugefügt. Die ID jeder Reihe wird
	 * als Key benutzt.
	 * 
	 * @param id_reihe Id Des Reihen Objektes
	 * @param reihe    Das Objekt Reihe
	 */
	public void addReihe(int id_reihe, Reihe reihe) {
		reihen.put(id_reihe, reihe);
	}

	/**
	 * Die Karten ID der letzte Karte jeder Reihe wird mittels einer For-Each
	 * Schleife in die ArrayList reihenIndexLetzteKarte gespeichert und im Anschluss
	 * sortiert. Ist die kleinste Karte der ArrayList größer als die anzulegende
	 * Karte, so wird passtAnReihe auf false gesetzt.
	 * 
	 * @param karte die an eine Reihe angelegt werden soll wird mit der letzten
	 *              Karte jeder Reihe verglichen
	 * @return Boolean Wert true wenn die Karte an eine Reihe passt, false wenn
	 *         nicht
	 */
	public boolean ausspielen(Karte karte) {
		boolean passtAnReihe = false;
		ArrayList<Integer> reihenIndexLetzteKarte = new ArrayList<Integer>();
		for (int key : reihen.keySet()) {
			int idLetzteKarte = indexLetzteKarteVonReihe(key);
			reihenIndexLetzteKarte.add(reihen.get(key).getReihe()[idLetzteKarte].getID());
		}
		Collections.sort(reihenIndexLetzteKarte);
		if (karte.getID() < reihenIndexLetzteKarte.get(0)) {
			passtAnReihe = false;
		} else {
			passtAnReihe = true;
		}
		return passtAnReihe;
	}

	/**
	 * For-Schleife von letztem Index einer Reihe bis 0, dabei wird überprüft ob der
	 * Index eine Karte enhält, also nicht null ist. Falls ja, wird die Schleife
	 * abgebrochen und der Indexwert i zurückgegeben.
	 * 
	 * @param id_reihe gibt die ID der zu überprüfenden Reihe an
	 * @return Index der letzten Karte einer Reihe
	 */
	private int indexLetzteKarteVonReihe(int id_reihe) {
		int i = -1;
		if (reihen.get(id_reihe) != null) {
			for (i = (reihen.get(id_reihe).getReihe().length - 1); i >= 0; i--) {
				if (reihen.get(id_reihe).getReihe()[i] != null) {
					break;
				}
			}
		}
		return i;
	}

	/**
	 * Löscht alle Inhalte aus der HashMap <code>ausgespielteKarten</code>.
	 * 
	 * @throws KeineElementeVorhandenException Geworfen, wenn die TreeMap
	 *                                         <code>ausgespielteKarten</code> keine
	 *                                         Daten enthält und somit auch nicht
	 *                                         gecleared werden koente
	 */
	public void removeAusgespielteKarten() throws KeineElementeVorhandenException {
		if (ausgespielteKarten.size() == 0) {
			throw new KeineElementeVorhandenException("Es sind keine ausgespielten Karten vorhanden");
		} else {
			ausgespielteKarten.clear();
		}
	}

	/**
	 * Entfernt Reihen nach erneuter Kartenausgabe nach 10 Runden.
	 */
	public void setReihen() {
		reihen.clear();
	}
}