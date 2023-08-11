package backend;

import java.io.Serializable;
import java.util.TreeMap;

import exceptions.ExceptionDB.KeineElementeVorhandenException;
import exceptions.ExceptionDB.NichtVorhandenException;
import exceptions.ExceptionDB.UngueltigeEingabeException;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;
import interfaces.bedienerInterface;

/**
 * Diese Klasse stellt einen Spieler inform einer Künstlichen Intelligenz dar.
 * Sie ersetzt echte Spieler falls erwünscht. Ausserdem erbt KI von der Klasse
 * <code>Spieler</code>.
 * 
 * @author Moritz Rühm
 */
public class KI extends Spieler implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * Boolean Wert ob die KI bis zum Ende durchspielen soll
	 */
	private boolean bisZumEndeSpielen;
	private bedienerInterface spiel;
	private boolean theadAktiv = false;

	/**
	 * Überschreibt Spieler Konstruktor wenn Geschlecht "ki" gewählt wurde.
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
	public KI(String name, String geschlecht, Spiel spiel) throws UngueltigeEingabeException {
		super(name, geschlecht, spiel);
		this.spiel = spiel;
	}

	/**
	 * Gibt den Boolean bisZumEndeSpielen des Ki Spielers zurück. Falls true, spielt
	 * der Ki Spieler automatisch bis zum ende des Spiels. Falls false, erfolgt eine
	 * Abfrage in der SpielCLI, dadurch können die Züge der Ki betrachtet werden.
	 * 
	 * @return Boolean Wert
	 */
	public boolean getBisZumEndeSpielenKi() {
		return bisZumEndeSpielen;
	}

	/**
	 * Setzt den Boolean bisZumEndeSpielen auf true oder false.
	 * 
	 * @param bisZumEndeSpielen, einen Boolean Wert
	 */
	public void setBisZumEndeSpielenKi(boolean bisZumEndeSpielen) {
		this.bisZumEndeSpielen = bisZumEndeSpielen;
	}

	/**
	 * Speichert die letzte Karte jeder Reihe und die Info ob die Reihe = 5 Karten
	 * hat als Boolean. Es werden nur Reihen betrachtet die weniger als 5 Karten
	 * haben und mit den Handkarten angeschaut. Die Handkarte, welche die kleinste
	 * Differenz zur letzten Karte einer Reihe hat, wird gespeichert und zurück
	 * gegeben. Haben alle Reihen 5 Karten, wird die kleinste Handkarte gewählt. Hat
	 * ein Spieler nur noch eine Karte wird diese ausgespielt.
	 * 
	 * @param reihen     übergibt ausgespielte Karten nur Reihen werden betrachtet
	 * @param handkarten übergibt Handkarten des Spielers
	 * @return Karte für ausspielen
	 */
	public int karteWaehlen(String[] reihen, String[] handkarten) {
		// if letzte Karte Abfrage
		if (handkarten.length == 1) {
			return Integer.parseInt(handkarten[0]);
		}
		int gespeicherteKarte = 0;
		int klDiff = 105;
		String[] letzteKarte = new String[4];
		boolean[] reihenInfoZumAnlegen = new boolean[4];
		boolean alleReihenfunf = true;
		int passendeKarten = 0;
		for (int i = 0; i < 4; i++) {
			String[] reihenSplit = reihen[i].split(": ");
			String[] reihenInfo = reihenSplit[1].split(", ");
			if (reihenInfo.length <= 5) {
				reihenInfoZumAnlegen[i] = false;
			} else if (reihenInfo.length > 5) {
				reihenInfoZumAnlegen[i] = true;
			}
			if (!reihenInfoZumAnlegen[i]) {

				alleReihenfunf = false;
			}
			letzteKarte[i] = reihenInfo[(reihenInfo.length - 2)];
		}
		// Überprueft wie viele Handkarten passend sind
		for (int i = 0; i < handkarten.length; i++) {
			if (kartepasst(letzteKarte, handkarten[i])) {
				passendeKarten += 1;
			}
		}
		// falls alle Karten < letzte Reihen Karte dann wird die kleinste Handkarte
		// ausgespielt
		if (passendeKarten == 0) {
			gespeicherteKarte = Integer.parseInt(handkarten[0]);
			return gespeicherteKarte;
		}
		if (alleReihenfunf) {
			return Integer.parseInt(handkarten[0]);
		}
		for (int i = 0; i < handkarten.length; i++) {
			if (!kartepasst(letzteKarte, handkarten[i])) {
				continue;
			}
			if (klDiff > aktuelleDiff(handkarten[i], letzteKarte, reihenInfoZumAnlegen)) {
				klDiff = aktuelleDiff(handkarten[i], letzteKarte, reihenInfoZumAnlegen);
				gespeicherteKarte = Integer.parseInt(handkarten[i]);
			}
		}
		if (klDiff == 105) {
			gespeicherteKarte = Integer.parseInt(handkarten[0]);
			return gespeicherteKarte;
		}
		return gespeicherteKarte;
	}

	/**
	 * Speichert die Differenz der Handkarte zur letzten Karte jeder Reihe. Die
	 * kleinste Differenz wird gespeichert und zurück gegeben.
	 * 
	 * @param karte       Handkarte die überprüft wird
	 * @param letzteKarte der Reihen
	 * @return kleinste Differenz von der Handkarte zur letzten Karte der Reihen
	 */
	private int aktuelleDiff(String karte, String[] letzteKarte, boolean[] reihenInfoZumAnlegen) {
		int kldiff = 105;
		for (int i = 0; i < letzteKarte.length; i++) {
			if (Integer.parseInt(letzteKarte[i]) < Integer.parseInt(karte)) {
				if (reihenInfoZumAnlegen[i]) {
					continue;
				}
				int neueDiff = Integer.parseInt(karte) - Integer.parseInt(letzteKarte[i]);
				if (neueDiff < kldiff) {
					kldiff = neueDiff;
				}
			}
		}
		return kldiff;
	}

	/**
	 * gibt true zurück wenn die Karte an eine Reihe passt, also die Karte größer
	 * ist als eine der letzten Reihenkarten.
	 * 
	 * @param letzteKarte letzte Karte der Reihen
	 * @param karte       zu überprüfende Handkarte
	 * @return true wenn die Karte passt, sie also größer als eine letzte Karte der
	 *         Reihen ist
	 */
	private boolean kartepasst(String[] letzteKarte, String karte) {
		boolean output = false;
		for (int i = 0; i < 4; i++) {
			if (Integer.parseInt(letzteKarte[i]) < Integer.parseInt(karte)) {
				output = true;
			}
		}
		return output;
	}

	/**
	 * Wählt die Reihe mit der kleinsten Hornochsensumme.
	 * 
	 * @param reihen die zu überprüfenden Reihen
	 * @return Id der Reihe mit der kleinsten Hornochsensumme
	 */
	public int reiheWaehlen(String[] reihen) {
		TreeMap<Integer, Integer> reihenHornochsen = new TreeMap<Integer, Integer>();
		for (int i = 0; i < 4; i++) {
			String[] reihenInfo = reihen[i].split(": ");
			String hornochsensumme = reihenInfo[2].trim();
			reihenHornochsen.put(Integer.parseInt(hornochsensumme), i + 1);
		}
		int gewaehlteReiheID = reihenHornochsen.get(reihenHornochsen.firstKey());
		return gewaehlteReiheID;
	}

	/**
	 * Die Run-Methode eines Threads.
	 */
//	@Override
//	public void run() {
//		while (true) {
//			synchronized (this) {
//				while (!theadAktiv) {
//
//					try {
//						wait();
//					} catch (InterruptedException e) {
//						System.err.println(e.getMessage());
//					}
//				}
//				try {
//					spiel.ausspielen(-1);
//					spiel.beendeZug();
//				} catch (NichtVorhandenException | KeineElementeVorhandenException
//						| ladenSpeichernFehlgeschlagenException e) {
//					System.err.println(e.getMessage());
//				}
//				threadPausieren();
//			}
//		}
//	}

	/**
	 * Pausiert einen Thread, bzw. Lässt ihn warten.
	 */
//	public void threadPausieren() {
//		theadAktiv = false;
//	}

	/**
	 * Setzt einen Thread nach dem Pausieren weider fort.
	 */
//	public void threadFortsetzen() {
//		theadAktiv = true;
//		notify();
//	}
}