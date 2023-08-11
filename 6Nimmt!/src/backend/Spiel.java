package backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Pattern;

import exceptions.ExceptionDB.FalscheSpielerAnzahlException;
import exceptions.ExceptionDB.KeineElementeVorhandenException;
import exceptions.ExceptionDB.LogFileException;
import exceptions.ExceptionDB.NichtVorhandenException;
import exceptions.ExceptionDB.UngueltigeEingabeException;
import exceptions.ExceptionDB.keinDatenzugriffException;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;
import interfaces.bedienerInterface;
import interfaces.persistenzInterface;
import loadAndSave.DateiTyp;
import loadAndSave.LogFileTool;
import loadAndSave.PersistenzImplCSV;
import loadAndSave.PersistenzImplSerialisiert;;

/**
 * In dieser KLasse befindet sich die gesamte Logik des Spiels <i>"6nimmt!"</i>
 * und sie ist somit auch die Hauptklasse.
 * 
 * @author Nicole Shiner
 * @author Rodez Tazo
 * @author Moritz Rühm
 */
public class Spiel implements bedienerInterface, Serializable {

	private static final long serialVersionUID = 1L;
	/** Setzte Boolean Wert ob das Spiel beendet ist und aktive Runde als false */
	private boolean istBeendet = false, rundeAktiv = false;
	private boolean gespeichertesSpiel = false;
	/** Spielerobjekt das gerade dran ist */
	private Spieler spielerAmZug;
	/** Spielerobjekt das eine Reihe als Strafe aufnehmen muss */
	private Spieler strafReiheSpieler = null;
	/**
	 * Karte die vom Spieler, welcher eine Strafreihe aufnehmen muss ausgespielt
	 * wurde
	 */
	private Karte strafReiheKarte = null;
	/** alle Spieler */
	private ArrayList<Spieler> alleSpieler = new ArrayList<Spieler>();
	/**
	 * alle aktiven Spieler die bereits Karte ausspielen aufgerufen haben als Key
	 * und ihre Karte als Value
	 */
	private HashMap<Integer, Karte> aktiveSpieler = new HashMap<Integer, Karte>();
	/** 104 Karten */
	private ArrayList<Karte> kartendeck = new ArrayList<Karte>();
	/** Liste mit bereits vergebenen IDs */
	private ArrayList<Integer> idList = new ArrayList<Integer>();
	/** Instanz von Ausgespielt */
	private Ausgespielt ausgespielt = new Ausgespielt();
	/** Instanz von LogFile */
	private LogFileTool log = new LogFileTool();

	private HashMap<Spieler, Thread> threads = new HashMap<Spieler, Thread>();

	/**
	 * Gibt ein Stringarray zuück: 1. bei laufendem Spiel enthält das Stringarray
	 * alle angelegten Spielern mit ihren Lebenspunkten 2. nach Beendigung des
	 * Spiels werden die Spieler in aufsteigender Punktzahl ausgegeben und Gewinner
	 * (mehrere möglich) und Verlierer (mehrere möglich) markiert.
	 * 
	 * @return Ein Stringarray mit dem Namen jedes Spielers
	 * @throws KeineElementeVorhandenException Wird Geworfen, wenn Keine Spieler
	 *                                         vorhanden sind
	 */
	@Override
	public String[] getAlleSpieler() throws KeineElementeVorhandenException {
		String[] output = new String[alleSpieler.size()];
		String spielerInfo = null;
		if (!istBeendet) {
			for (int i = 0; i < alleSpieler.size(); i++) {
				spielerInfo = "";
				spielerInfo += alleSpieler.get(i).getId() + ", " + alleSpieler.get(i).getName() + ", "
						+ alleSpieler.get(i).getGeschlecht() + ", " + alleSpieler.get(i).getLeben();
				if (alleSpieler.get(i).getKarten() != null) {
					spielerInfo += ", Anzahl der Handkarten: " + alleSpieler.get(i).getKarten().length;

				}
				output[i] = spielerInfo;
				log.stringEintragen(spielerInfo);
			}
		} else if (alleSpieler.size() == 0) {
			throw new KeineElementeVorhandenException("Es sind noch keine Spieler vorhanden");
		} else {
			ArrayList<Integer> lebenspunkte = new ArrayList<Integer>();
			for (Spieler s : alleSpieler) {
				lebenspunkte.add(s.getLeben());
			}
			Collections.sort(lebenspunkte);
			Spieler[] spielerAnzahl = new Spieler[alleSpieler.size()];
			for (int i = 0; i < alleSpieler.size(); i++) {
				spielerAnzahl[i] = alleSpieler.get(i);
			}
			int numWinner = lebenspunkte.get(lebenspunkte.size() - 1);
			int outputCounter = 0;
			for (int i : lebenspunkte) {
				for (int j = 0; j < spielerAnzahl.length; j++) {
					if (spielerAnzahl[j] == null) {
						continue;
					} else if ((spielerAnzahl[j].getLeben()) == i) {
						if (i == 0) {
							output[outputCounter] = spielerAnzahl[j].getName() + ", Leben: "
									+ spielerAnzahl[j].getLeben() + " --> Verlierer";
							log.stringEintragen(spielerAnzahl[j].getName() + ", Leben: " + spielerAnzahl[j].getLeben()
									+ " --> Verlierer");
							spielerAnzahl[j] = null;

						} else if (i == numWinner) {
							output[outputCounter] = spielerAnzahl[j].getName() + ", Leben: "
									+ spielerAnzahl[j].getLeben() + " --> Gewinner";
							log.stringEintragen(spielerAnzahl[j].getName() + ", Leben: " + spielerAnzahl[j].getLeben()
									+ " --> Gewinner");
							spielerAnzahl[j] = null;

						} else {
							output[outputCounter] = spielerAnzahl[j].getName() + ", Leben: "
									+ spielerAnzahl[j].getLeben();
							log.stringEintragen(spielerAnzahl[j].getName() + ", Leben: " + spielerAnzahl[j].getLeben());
							spielerAnzahl[j] = null;

							break;
						}
						break;
					}
				}
				outputCounter++;
			}
		}
		return output;
	}

	/**
	 * Determiniert, welcher Spieler gerade am Zug ist und gibt diesen anschließend
	 * in einem Stringarray aus.
	 * 
	 * @return Ein Stringarray mit dem Namen des Spielers, der gerade am Zug ist und
	 *         seinen Lebenspunkten
	 */
	@Override
	public String[] getSpielerAmZug() {
		String[] output = new String[2];
		if (!rundeAktiv) {
			if (aktiveSpieler.isEmpty()) {
				spielerAmZug = alleSpieler.get(0);
			} else {
				for (Spieler s : alleSpieler) {
					if (aktiveSpieler.containsKey(s.getId())) {
						continue;
					} else {
						spielerAmZug = s;
					}
				}
			}
		}
		// wenn aktive runde false aber bereits ein oder mehr spieler ausspielen()
		// benutzt haben, muss spielerAmZug über contains bedingung ermittelt werden
		output[0] = spielerAmZug.getName() + ", ID: " + spielerAmZug.getId() + ", " + spielerAmZug.getGeschlecht()
				+ ", Leben: " + spielerAmZug.getLeben();
		if (rundeAktiv) {
			output[1] = "1";
		} else {
			output[1] = "0";
		}

		return output;
	}

	/**
	 * Determiniert, welcher Spieler gerade am Zug ist und gibt basierend darauf die
	 * Karten des jeweiligen Spielers aus.
	 * 
	 * @return Ein Stringarray mit allen Karten des Spielers am Zug
	 */
	@Override
	public String[] getKartenVonSpielerAmZug() {
		String[] spielerKarten = new String[10];
		int index = 0;
		if (spielerAmZug.getKarten() != null) {
			for (Karte k : spielerAmZug.getKarten()) {
				if (k != null) {
					spielerKarten[index++] = k.getID() + ", Hornochsen: " + k.getHornochsen();
				}
			}
		} else {
			spielerKarten[0] = "0";
		}
		return spielerKarten;
	}

	/**
	 * Gibt die Reihen und (falls vorhanden) ausgespielten Karten jedes Spielers in
	 * einem Stringarray zurück. Sind keine Reihen und Karten vorhanden, ist das
	 * Stringarray null und es wird eine Exception geworfen.
	 * 
	 * @return Ein Stringarray mit allen von den Spielern bereits ausgespielten
	 *         Karten und Reihen
	 * 
	 * @throws KeineElementeVorhandenException Wird von der Methode
	 *                                         <code>getReihe</code> weiter geworfen
	 */
	@Override
	public String[] getAusgespielt() throws KeineElementeVorhandenException {
		String[] output = null;
		Spieler spieler = null;
		Karte karte = null;
		if (!ausgespielt.getReihen().isEmpty()) {
			output = new String[ausgespielt.getAusgespielteKarten().size() + ausgespielt.getReihen().size()];
			int i = 0;
			for (int key : ausgespielt.getReihen().keySet()) {
				output[i++] = ausgespielt.getReihen().get(key).toString();
				log.stringEintragen(ausgespielt.getReihen().get(key).toString());
			}
			if (!ausgespielt.getAusgespielteKarten().isEmpty()) {
				for (int key : ausgespielt.getAusgespielteKarten().keySet()) {
					HashMap<Spieler, Karte> hm = ausgespielt.getAusgespielteKarten().get(key);
					for (Spieler s : hm.keySet()) {
						spieler = s;
						karte = hm.get(s);
						output[i++] = spieler.getName() + " , ID: " + spieler.getId() + ", Leben: " + spieler.getLeben()
								+ ", Karte: " + +karte.getID() + " mit " + karte.getHornochsen() + " Hornochsen";
					}
				}
			}
		}

		return output;
	}

	/**
	 * Gibt alle Reihen mit ihren Karten die sie beinhalten zurück.
	 * 
	 * @return Alle Reihen
	 */
	private String[] getReihen() throws KeineElementeVorhandenException {
		String[] output = null;
		if (!ausgespielt.getReihen().isEmpty()) {
			output = new String[ausgespielt.getAusgespielteKarten().size() + ausgespielt.getReihen().size()];
			int i = 0;
			for (int key : ausgespielt.getReihen().keySet()) {
				output[i++] = ausgespielt.getReihen().get(key).toString();
			}

		}
		return output;
	}

	/**
	 * Determiniert die Reihe, in der eine zuvor ausgespielte Karte angelegt werden
	 * muss, indem die niedrigste Differenz zur letzten Karte aus jeder Reihe
	 * berechnet wird und gibt die ID der Reihe mit der nidrigsten Differenz zurück.
	 * 
	 * @param karte Die zuvor Ausgespielte Karte
	 * @return Die ID der Reihe an die die Karte angelegt werden muss
	 * @throws KeineElementeVorhandenException Wird von der Methode
	 *                                         <code>getReihe</code> weiter geworfen
	 */
	private int getReiheZumAnlegen(Karte karte) throws KeineElementeVorhandenException {
		int idReihe = 0;
		int kleinsteDiff = 103;
		TreeMap<Integer, Integer> reihenZumAnlegen = new TreeMap<Integer, Integer>();
		for (int key : ausgespielt.getReihen().keySet()) {
			int indexLetzteKarte = indexLetzteKarteVonReihe(key);
			reihenZumAnlegen.put(ausgespielt.getReihen().get(key).getReihe()[indexLetzteKarte].getID(), key);
		}
		for (int key : reihenZumAnlegen.keySet()) {
			if (key < karte.getID()) {
				int differenz = karte.getID() - key;
				if (differenz < kleinsteDiff) {
					idReihe = reihenZumAnlegen.get(key);
				}
			}
		}
		return idReihe;
	}

	/**
	 * Setter-Methode, welche den aktuellen Spieler am Zug über die ausgespielten
	 * Karten setzt
	 */
	private void setSpielerAmZug() {
		HashMap<Spieler, Karte> hm = null;
		hm = ausgespielt.getAusgespielteKarten().get(ausgespielt.getAusgespielteKarten().firstKey());
		for (Spieler key : hm.keySet()) {
			spielerAmZug = key;
			break;
		}
	}

	/**
	 * Erstellt (insofern korrekt) mit dem übergegebenen Namen ein Spieler-Objekt,
	 * welches dann in der HashMap erstellteSpieler gespeichert wird. Ist das
	 * übergebene Geschlecht 4, wird ein KI Spieler über den Konstruktor der Klasse
	 * Ki, die von Spieler erbt, erstellt
	 * 
	 * @param name       Dieser wird dem jeweiligen Spieler zugewiesen
	 * @param geschlecht Beschreibt das Geschlecht eines Spielers (kann auch KI
	 *                   sein)
	 * @throws UngueltigeEingabeException Wird geworfen, wenn versucht wird einen
	 *                                    Spieler mit einem ungültigen Namen zu
	 *                                    erstellen
	 */
	@Override
	public void addSpieler(String name, String geschlecht) throws UngueltigeEingabeException {
		if (Pattern.matches("^[a-zA-Z0-9]{2,8}$", name) & geschlecht.equals("1") | geschlecht.equals("2")
				| geschlecht.equals("3") | geschlecht.equals("4")) {
			if (geschlecht.equals("4")) {
				KI ki = new KI(name, geschlecht, this);
//				Thread kiThread = new Thread(ki);
//				threads.put(ki, kiThread);
//				kiThread.start();
				for (int i = 1; i < 100; i++) {
					if (idList.contains(i)) {
						continue;
					} else {
						ki.setId(i);
						idList.add(i);
						break;
					}
				}
				alleSpieler.add(ki);
				spielerAmZug = ki;
			} else {
				Spieler spieler = new Spieler(name, geschlecht, this);
				for (int i = 1; i < 100; i++) {
					if (idList.contains(i)) {
						continue;
					} else {
						spieler.setId(i);
						idList.add(i);
						break;
					}
				}
				alleSpieler.add(spieler);
				spielerAmZug = spieler;
			}
			log.stringEintragen("Spieler " + name + " wurde erstellt");
		} else {
			throw new UngueltigeEingabeException(
					"Diese Eingabe ist nicht gültig! Bitte beachte, dass nur Buchstaben und Zahlen erlaubt sind.");
		}
	}

	/**
	 * Ist dafür da, um vorher hinzugefügte Spieler des Spiels wieder zu entfernen
	 * oder überarbeiten.
	 * 
	 * @param name Um den Spieler zu identifizieren
	 * @throws NichtVorhandenException Wird geworfen, wenn versucht wird einen
	 *                                 Spieler zu entfernen, der nicht existiert
	 */
	@Override
	public void removeSpieler(String name) throws NichtVorhandenException {
		boolean nameVorhanden = false;
		for (int i = 0; i < alleSpieler.size(); i++) {
			if (alleSpieler.get(i).getName().equalsIgnoreCase(name)) {
				nameVorhanden = true;
			}
		}
		if (nameVorhanden) {
			for (int i = 0; i < alleSpieler.size(); i++) {
				if (alleSpieler.get(i).getName().equalsIgnoreCase(name)) {
					alleSpieler.remove(i);
				}
			}
			log.stringEintragen("Spieler " + name + " wurde entfernt");

		} else {
			throw new NichtVorhandenException("Dieser Spieler existiert nicht versuche es bitte nocheinmal");
		}
	}

	/**
	 * Methode, die zuallererst prüft ob mind. 2 bis max. 10 Spieler vorhanden sind,
	 * falls ja, wird der Boolean istBeendet auf false gesetzt, das Spiel gestartet
	 * und jedem Spieler werden 10 Karten austeilt.
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException Wird von der Methode
	 *                                               <code>kartenAusteilen</code>
	 *                                               weiter geworfen
	 * @throws LogFileException                      Wird geworfe, wenn das Spiel
	 *                                               nicht in die LogFile
	 *                                               geschrieben werden kann.
	 */
	@Override
	public void starteSpiel() throws ladenSpeichernFehlgeschlagenException, LogFileException {
		kartenAusteilen();
		log.stringEintragen("neues Spiel gestartet");
		istBeendet = false;
	}

	/**
	 * Diese Methode setzt die Variable isBeendet auf <code>true</code>, sobald
	 * keiner der Spieler mehr Karten besitzt.
	 * 
	 * @return Boolean, die beschreibt ob das Spiel beendet wurde oder nicht
	 */
	@Override
	public boolean istSpielBeendet() {
		return this.istBeendet;
	}

	/**
	 * Beendet den Zug eines Spielers:
	 * <ul>
	 * <li>In der Kartenauswahlphase und setzt den naechsten SpielerAmZug oder setzt
	 * die Variable rundeAktiv auf true wenn alle Spieler eine Karte ausgespielt
	 * haben</li>
	 * <li>sobald die Runde aktiv ist, wird die Karte, wenn sie passt, an eine Reihe
	 * angelegt und ggf. nimmReihe aufgerufen. Danach wird der nächste Spieler für
	 * die Zugreihenfolge gesetzt</li>
	 * </ul>
	 * Sofern der Spieler eine KI ist und keine Karte passt, wird die Methode
	 * nimmReihe aufgerufen und der int Wert über die KI Methode reiheWaehlen
	 * ermittelt.
	 * 
	 * @return die ID des Spielers, als Int-Wert, der eine Reihe als Strafe
	 *         aufnehmen muss, sofern KI eine Reihe aufnehmen muss, wird der Wert 0
	 *         zurückgegeben
	 * @throws KeineElementeVorhandenException       Wird von der Methode
	 *                                               <code>getReihe</code> weiter
	 *                                               geworfen
	 * @throws NichtVorhandenException               Wird von der Methode
	 *                                               <code>nimmReihe</code> weiter
	 *                                               geworfen
	 * @throws ladenSpeichernFehlgeschlagenException Wird von der Methode
	 *                                               <code>kartenAusteilen</code>
	 *                                               weiter geworfen
	 */
	@Override
	public int beendeZug()
			throws KeineElementeVorhandenException, NichtVorhandenException, ladenSpeichernFehlgeschlagenException {
		int output = 0;
		// hier sind wir in der Karte vom Spieler auswählen Phase
		if (!rundeAktiv) {
			if (aktiveSpieler.size() == alleSpieler.size()) {
				rundeAktiv = true;
				spielerAmZug = null;
			} else {
				for (Spieler spieler : alleSpieler) {
					if (aktiveSpieler.containsKey(spieler.getId())) {
						continue;
					} else {
						spielerAmZug = spieler;
					}
				}
			}
		}
		// Alle Spieler haben ihre Karte ausgewählt und wir befinden uns nun in der
		// Karten an Reihe anlegen Phase.
		if (rundeAktiv) {
			if (spielerAmZug == null) {
				setSpielerAmZug();
			} else {
				if (!ausgespielt.ausspielen(aktiveSpieler.get(spielerAmZug.getId()))) {
					output = spielerAmZug.getId();
					strafReiheSpieler = spielerAmZug;
					strafReiheKarte = aktiveSpieler.get(spielerAmZug.getId());
					if (spielerAmZug instanceof KI) {
						output = 0;
						int gewaehlteReiheKi = ((KI) spielerAmZug).reiheWaehlen(getReihen());
						nimmReihe(gewaehlteReiheKi);
					}
				} else {
					if (istSechsteKarte(aktiveSpieler.get(spielerAmZug.getId()))) {
						output = 0;
						strafReiheSpieler = spielerAmZug;
						strafReiheKarte = aktiveSpieler.get(spielerAmZug.getId());
						nimmReihe(getReiheZumAnlegen(aktiveSpieler.get(spielerAmZug.getId())));
					} else {
						output = 0;
						Karte karte = aktiveSpieler.get(spielerAmZug.getId());
						ausgespielt.getReihen().get(getReiheZumAnlegen(karte)).karteHinzufuegen(karte);
						strafReiheSpieler = null;
						strafReiheKarte = null;
						if (spielerAmZug.getLeben() <= 0) {
							istBeendet = true;
						}
					}
				}
				if (!ausgespielt.getAusgespielteKarten().isEmpty()) {
					ausgespielt.getAusgespielteKarten().remove(ausgespielt.getAusgespielteKarten().firstKey());
				}
				if (ausgespielt.getAusgespielteKarten().isEmpty()) {
					rundeAktiv = false;
					aktiveSpieler.clear();
				} else {
					setSpielerAmZug();
				}
			}
			if (istBeendet) {
				kartenAusteilen();
			}
		}
		return output;
	}

	/**
	 * Diese Methode spielt eine vom Spieler ausgesuchte Karte aus, speichert sie zu
	 * den ausgespielten Karten und entfernt sie aus den Handkarten des Spielers.
	 * Ist der Spieler eine KI, so wird die auszuspielende KArte über die KI Logik
	 * Methode karteWaehlen() bestimmt.
	 * 
	 * @param id_karte Karte die ausgespielt werden soll, sofern KI, wird der Wert
	 *                 105 übergeben
	 * @throws NichtVorhandenException         NichtVorhandenException Wird von der
	 *                                         Methode <code>spielerAmZug</code>
	 *                                         weiter geworfen
	 * @throws KeineElementeVorhandenException Wird von der Methode
	 *                                         <code>getAusgespielt</code> weiter
	 *                                         geworfen
	 */
	@Override
	public void ausspielen(int id_karte) throws NichtVorhandenException, KeineElementeVorhandenException {
		int idKarte = id_karte;
		Karte gewaehlteKarte = null;
		if (id_karte == -1) {
			String[] spielerHandkarten = new String[spielerAmZug.getKarten().length];
			int i = 0;
			for (Karte k : spielerAmZug.getKarten()) {
				spielerHandkarten[i] = String.valueOf(k.getID());
				i++;
			}
			if (spielerAmZug instanceof KI) {
				idKarte = ((KI) spielerAmZug).karteWaehlen(getAusgespielt(), spielerHandkarten);
			}
		}
		for (Karte k : spielerAmZug.getKarten()) {
			if (k != null) {
				if (k.getID() == idKarte) {
					gewaehlteKarte = k;
				}
			}
		}
		if (gewaehlteKarte != null) {
			ausgespielt.addKarte(spielerAmZug, gewaehlteKarte);
			aktiveSpieler.put(spielerAmZug.getId(), gewaehlteKarte);
			spielerAmZug.karteEntfernen(idKarte);
		}

		else {
			throw new NichtVorhandenException("Karte nicht vorhanden!");
		}
	}

	/**
	 * Wird aufgerufen wenn ein Spieler eine Reihe aufnehmen muss, entweder wenn die
	 * Karte an keine Reihe passt, weil sie zu klein ist oder sie die 6. Karte ist.
	 * Die Summe der Hornochsen der Reihe werden vom Leben des Spielers abgezogen
	 * 
	 * @param id_reihe Ist die ID der Reihe, die genommen werden muss
	 * @throws NichtVorhandenException         Wird geworfen, wenn eine Karte an
	 *                                         eine nicht existierende Reihe
	 *                                         angelegt werden soll
	 * @throws KeineElementeVorhandenException Wird von der Methode
	 *                                         <code>getReihen</code> weiter
	 *                                         geworfen
	 */
	@Override
	public void nimmReihe(int id_reihe) throws NichtVorhandenException, KeineElementeVorhandenException {
		if (spielerAmZug.getLeben() <= 0) {
			istBeendet = true;
		}
		if (id_reihe <= 4) {
			Karte[] reiheVonKarten = null;
			reiheVonKarten = ausgespielt.getReihen().get(id_reihe).getReihe();
			for (int counter = 0; counter < reiheVonKarten.length; counter++) {
				if (reiheVonKarten[counter] == null) {
					continue;
				} else {
					strafReiheSpieler.setLeben(reiheVonKarten[counter].getHornochsen());
				}
			}
			ausgespielt.getReihen().get(id_reihe).kartenEntfernen();
			ausgespielt.getReihen().get(id_reihe).karteHinzufuegen(strafReiheKarte);
			if (strafReiheSpieler.getLeben() <= 0) {
				istBeendet = true;
			}
		} else if (id_reihe >= 5) {
			throw new NichtVorhandenException("Diese Reihe existiert nicht. Waehle eine andere aus!");
		}
		strafReiheSpieler = null;
		strafReiheKarte = null;
		if (gespeichertesSpiel) {
			setSpielerAmZug();
			gespeichertesSpiel = false;
		}

	}

	/**
	 * Determiniert ob die als Parameter mitgegebene Karte die 6. Karte an einer
	 * Reihe ist und diese somit aufgenommen werden muss.
	 * 
	 * @param karte Die gerade Angelegte Karte
	 * @return Boolean wert, ob die Reihe genommen werden muss oder nicht
	 * @throws KeineElementeVorhandenException Wird von der Methode
	 *                                         <code>getReiheZumAnlegen</code>
	 *                                         weiter geworfen
	 */
	private boolean istSechsteKarte(Karte karte) throws KeineElementeVorhandenException {
		boolean mussReiheNehmen = false;
		if (indexLetzteKarteVonReihe(getReiheZumAnlegen(karte)) == 4) {
			mussReiheNehmen = true;
		}
		return mussReiheNehmen;
	}

	/**
	 * Gibt den Index der Letzten Karte einer Reihe zurück
	 * 
	 * @param id_reihe Die ID zu ueberpruefenden Reihe
	 * @return Index der letzten Karte
	 * @throws KeineElementeVorhandenException Wird von der Methode
	 *                                         <code>Ausgespielt.getReiheZumAnlegen</code>
	 *                                         weiter geworfen
	 */
	private int indexLetzteKarteVonReihe(int id_reihe) throws KeineElementeVorhandenException {
		int i = -1;
		int reihenLength = ausgespielt.getReihen().get(id_reihe).getReihe().length;
		if (ausgespielt.getReihen().get(id_reihe) != null) {
			for (i = (reihenLength - 1); i >= 0; i--) {
				if (ausgespielt.getReihen().get(id_reihe).getReihe()[i] != null) {
					break;
				}
			}
		}
		return i;
	}

	/**
	 * Diese Methode löscht das alte Kartendeck und teilt gemischte Karten an
	 * Spieler und Reihen aus,
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException
	 */
	private void kartenAusteilen() throws ladenSpeichernFehlgeschlagenException {
		kartendeck.clear();
		ausgespielt.setReihen();
		for (int i = 0; i < 4; i++) {

		}
		Karte.zaehler = 1;
		erstelleKartendeck();
		Collections.shuffle(kartendeck);
		kartenausgabeReihen();
		kartenausgabeSpieler();

		log.stringEintragen("neues Kartendeck");
	}

	/**
	 * Hier wird ein 104 Karten großes 6nimmt! Kartendeck erstellt.
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException
	 */
	private void erstelleKartendeck() throws ladenSpeichernFehlgeschlagenException {
		for (int i = 0; i < 104; i++) {
			Karte karte = new Karte();
			kartendeck.add(karte);
		}
	}

	/**
	 * Diese Methode teilt Karten an die Spieler aus.
	 */
	private void kartenausgabeSpieler() {
		for (Spieler spieler : alleSpieler) {
			for (int j = 0; j <= 9; j++) {
				spieler.karteHinzufuegen(kartendeck.get(0));
				kartendeck.remove(0);
			}
			log.stringEintragen(spielerAmZug + " " + getKartenVonSpielerAmZug());
		}
	}

	/**
	 * Diese Methode teilt Karten an die Reihen aus.
	 */
	private void kartenausgabeReihen() {
		for (int i = 0; i < 4; i++) {
			Reihe reihe = new Reihe(i + 1);
			reihe.karteHinzufuegen(kartendeck.get(0));
			kartendeck.remove(0);
			ausgespielt.addReihe(reihe.getID(), reihe);
		}
	}

	private void setAusgespielt(Object o) {
		this.ausgespielt = (Ausgespielt) o;
	}

	/**
	 * Diese Methode lädt ein zuvor gespeichertes Spiel.
	 * 
	 * @param pfad Der Dateipfad aus dem ein Spiel geladen werden soll
	 * @param typ  Welche art des Spiecherns ausgewaehlt wurde soll
	 * @return Spiel Das geladene Spiel-Objekt
	 * @throws UngueltigeEingabeException            Wird geworfen, wenn keiner der
	 *                                               DatenTypen "CSV" oder "SER"
	 *                                               ausgewaehlt wurde
	 * @throws keinDatenzugriffException             Wird von der Methode
	 *                                               <code>PersistenzImplCSV/SER.oeffnen</code>
	 *                                               weiter geworfen
	 * @throws ladenSpeichernFehlgeschlagenException Wird von der Methode
	 *                                               <code>PersistenzImplCSV/SER.schliessen</code>
	 *                                               weiter geworfen
	 */
	@SuppressWarnings("unused")
	public static Spiel laden(String pfad, DateiTyp typ)
			throws UngueltigeEingabeException, keinDatenzugriffException, ladenSpeichernFehlgeschlagenException {
		persistenzInterface datei = null;
		Spiel spiel = null;
		Ausgespielt ausgespielt = null;
		LogFileTool log = null;
		ArrayList<Integer> erstellteKarten = new ArrayList<>();
		ArrayList<Integer> erstellteReihen = new ArrayList<>();
		Properties pCSV;
		Properties pPfad = new Properties();
		pPfad.put("pfad", pfad);
		pPfad.put("schreibend", false);
		int x;
		switch (typ) {
		case SER:
			datei = new PersistenzImplSerialisiert();
			datei.oeffnen(pPfad);
			spiel = (Spiel) datei.lesen();
			break;
		case CSV:
			datei = new PersistenzImplCSV();
			datei.oeffnen(pPfad);
			pCSV = (Properties) datei.lesen();
			// Spiel
			spiel = new Spiel();
			spiel.rundeAktiv = Boolean.parseBoolean((String) pCSV.get("rundeAktiv"));
//			// SpielerAmZug und Karte für Speichern/Laden bei nimmReihe()
//			if((pCSV.get("strafReiheSpielerId")) != "0") {
//				for(Spieler s : spiel.alleSpieler) {
//					if(s.getId() == (Integer.parseInt(pCSV.getProperty("strafReiheSpielerId")))){
//						spiel.strafReiheSpieler = s;
//						spiel.spielerAmZug = s;
//						spiel.gespeichertesSpiel = true;
//					}
//				}
//			}
//			if((pCSV.get("strafReiheKarteId")) != "0") {
//				if (!erstellteKarten.contains(Integer.parseInt((String) pCSV.get("strafReiheKarteId")))) {
//					Karte karte = new Karte(Integer.parseInt((String) pCSV.get("strafReiheKarteId")));
//					erstellteKarten.add(Integer.parseInt((String) pCSV.get("strafReiheKarteId")));
//					spiel.strafReiheKarte = karte;
//				}
//			}

			// Spieler
			x = 0;
			for (int i = 0; i < Integer.parseInt((String) pCSV.get("spielerAnzahl")); i++) {
				String geschlecht = "";
				switch ((String) pCSV.get("geschlecht" + i)) {
				case "m":
					geschlecht = "1";
					break;
				case "w":
					geschlecht = "2";
					break;
				case "d":
					geschlecht = "3";
					break;
				case "ki":
					geschlecht = "4";
					break;
				default:
					throw new ladenSpeichernFehlgeschlagenException(
							"Der Spieler konnte nicht geladen werden, da sein Geschlecht nicht unterstützt wird!");
				}
				if (0 < (Integer.parseInt((String) pCSV.getProperty("leben" + i)))
						&& 66 >= (Integer.parseInt((String) pCSV.getProperty("leben" + i)))) {
					spiel.addSpieler((String) pCSV.get("name" + i), geschlecht);
					spiel.alleSpieler.get(i).setLeben(66 - (Integer.parseInt((String) pCSV.getProperty("leben" + i))));
				} else if (0 == (Integer.parseInt((String) pCSV.getProperty("leben" + i)))) {
					throw new ladenSpeichernFehlgeschlagenException(
							"Leben des Spielers " + (String) pCSV.get("name" + i) + " ist 0, wodurch er verloren hat!");
				} else {
					throw new ladenSpeichernFehlgeschlagenException(
							"Leben des Spielers " + (String) pCSV.get("name" + i) + " sind ungültig!");
				}
				// SpielerAmZug und Karte für Speichern/Laden bei nimmReihe()
				if ((pCSV.get("strafReiheSpielerId")) != "0") {
					for (Spieler s : spiel.alleSpieler) {
						if (s.getId() == (Integer.parseInt(pCSV.getProperty("strafReiheSpielerId")))) {
							spiel.strafReiheSpieler = s;
							spiel.spielerAmZug = s;
							spiel.gespeichertesSpiel = true;
						}
					}
				}
				// Handkarten
				for (int j = 0; j < Integer.parseInt((String) pCSV.get("handKartenAnzahl" + i)); j++) {
					if (!erstellteKarten.contains(Integer.parseInt((String) pCSV.get("handKarte" + x)))) {
						Karte karte = new Karte(Integer.parseInt((String) pCSV.get("handKarte" + x)));
						erstellteKarten.add(Integer.parseInt((String) pCSV.get("handKarte" + x)));
						spiel.alleSpieler.get(i).karteHinzufuegen(karte);
						x++;
					} else {
						throw new ladenSpeichernFehlgeschlagenException("Eine Karte konnte nicht erstellt werden!");
					}
				}
				if ((pCSV.get("strafReiheKarteId")) != "0") {
					if (!erstellteKarten.contains(Integer.parseInt((String) pCSV.get("strafReiheKarteId")))) {
						Karte karte = new Karte(Integer.parseInt((String) pCSV.get("strafReiheKarteId")));
						erstellteKarten.add(Integer.parseInt((String) pCSV.get("strafReiheKarteId")));
						spiel.strafReiheKarte = karte;
					}
				}
			}
			// Reihen
			x = 0;
			ausgespielt = new Ausgespielt();
			for (int i = 0; i < 4; i++) {
				if (!erstellteReihen.contains(Integer.parseInt((String) pCSV.get("reihenID" + i)))) {
					Reihe reihe = new Reihe(Integer.parseInt((String) pCSV.get("reihenID" + i)));
					for (int j = 0; j < Integer.parseInt((String) pCSV.get("kartenAnzahl" + i)); j++) {
						if (!erstellteKarten.contains(Integer.parseInt((String) pCSV.get("reihenkarte" + x)))) {
							Karte karte = new Karte(Integer.parseInt((String) pCSV.get("reihenkarte" + x)));
							erstellteKarten.add(Integer.parseInt((String) pCSV.get("reihenkarte" + x)));
							reihe.karteHinzufuegen(karte);
							x++;
						} else {
							throw new ladenSpeichernFehlgeschlagenException(
									"Die Karte konnte nicht erstellt werden, da sie schon existiert!");
						}
					}
					ausgespielt.addReihe(i + 1, reihe);
					erstellteReihen.add(i + 1);
				} else {
					throw new ladenSpeichernFehlgeschlagenException(
							"Die Reihe konnte nicht erstellt werden, da sie schon existiert!");
				}
			}

			// Ausgespielte Karten
			for (int i = 0; i < Integer.parseInt((String) pCSV.get("ausgespieltAnzahl")); i++) {
				if (!erstellteKarten.contains(Integer.parseInt((String) pCSV.get("ausgespieltKarte" + i)))) {
					Karte karte = new Karte(Integer.parseInt((String) pCSV.get("ausgespieltKarte" + i)));
					erstellteKarten.add(Integer.parseInt((String) pCSV.get("ausgespieltKarte" + i)));
					ausgespielt.addKarte(
							spiel.alleSpieler.get(Integer.parseInt((String) pCSV.get("ausgespieltSpieler" + i)) - 1),
							karte);
					spiel.aktiveSpieler.put(Integer.parseInt((String) pCSV.get("ausgespieltSpieler" + i)), karte);
				} else {
					throw new ladenSpeichernFehlgeschlagenException(
							"Die Karte konnte nicht erstellt werden, da sie schon existiert!");
				}
			}
			spiel.setAusgespielt(ausgespielt);
			break;
		default:
			throw new UngueltigeEingabeException(
					"Diese Eingabe war leider Falsch! Du kannst nur zwischen serialisierter und CSV Speicherung auswaehlen.");
		}
		if (log != null) {
			log.stringEintragen("SpielLogFile geladen");
		}
		datei.schliessen();
		return spiel;
	}

	/**
	 * Diese Methode Speichert ein Spiel entweder Serialisierten oder im CSV-Format.
	 * 
	 * @param pfad Der Dateipfad in den ein Spiel gespeichert werden soll
	 * @param typ  Welche art des Spiecherns ausgewählt wurde
	 * @throws UngueltigeEingabeException            Wird geworfen, wenn ein Spieler
	 *                                               keinen der beiden Speichertypen
	 *                                               auszuwaehlen
	 * @throws KeineElementeVorhandenException       Wird von den Methoden
	 *                                               <code>getAlleSpieler</code>,
	 *                                               <code>getAusgespielt</code> und
	 *                                               <code>Ausgespielt.getReihen</code>
	 *                                               weiter geworfen
	 * @throws keinDatenzugriffException             Wird von der Methode
	 *                                               <code>PersistenzImplCSV/SER.oeffnen</code>
	 *                                               weiter geworfen
	 * @throws ladenSpeichernFehlgeschlagenException Wird von der Methode
	 *                                               <code>PersistenzImplCSV/SER.schliessen</code>
	 *                                               weiter geworfen
	 */
	@Override
	public void speichern(String pfad, DateiTyp typ) throws UngueltigeEingabeException, KeineElementeVorhandenException,
			keinDatenzugriffException, ladenSpeichernFehlgeschlagenException {
		persistenzInterface datei = null;
		Properties p = new Properties();
		int x, y;
		if (typ == DateiTyp.CSV) {
			p.put("pfad", pfad + ".csv");
		} else if (typ == DateiTyp.SER) {
			p.put("pfad", pfad + ".ser");
		}
		p.put("schreibend", true);
		// Spiel
		p.put("spielobjekt", this);
		p.put("spielerAnzahl", getAlleSpieler().length);
		p.put("ausgespieltAnzahl", ausgespielt.getAusgespielteKarten().keySet().size());
		p.put("rundeAktiv", String.valueOf(rundeAktiv));
		if (strafReiheSpieler != null) {
			p.put("strafReiheSpielerId", strafReiheSpieler.getId());
		} else {
			p.put("strafReiheSpielerId", "0");
		}
		if (strafReiheKarte != null) {
			p.put("strafReiheKarteId", strafReiheKarte.getID());
		} else {
			p.put("strafReiheKarteId", "0");
		}
		// Spieler
		x = 0;
		for (int i = 0; i < alleSpieler.size(); i++) {
			p.put("spielerId" + i, alleSpieler.get(i).getId());
			p.put("name" + i, alleSpieler.get(i).getName());
			p.put("geschlecht" + i, alleSpieler.get(i).getGeschlecht());
			p.put("leben" + i, alleSpieler.get(i).getLeben());
			p.put("handKartenAnzahl" + i, alleSpieler.get(i).getKarten().length);
			// Handkarten
			for (Karte k : alleSpieler.get(i).getKarten()) {
				p.put("handKarte" + x, k.getID());
				x++;
			}
		}
		// Reihen
		x = 0;
		y = 0;
		int kartenAnzahl;
		for (int id : ausgespielt.getReihen().keySet()) {
			kartenAnzahl = 0;
			p.put("reiheId" + x, ausgespielt.getReihen().get(id).getID());
			for (int i = 0; i < ausgespielt.getReihen().get(id).getReihe().length; i++) {
				if (ausgespielt.getReihen().get(id).getReihe()[i] != null) {
					kartenAnzahl++;
				}
			}
			p.put("kartenAnzahl" + x, kartenAnzahl);
			for (int j = 0; j < kartenAnzahl; j++) {
				p.put("reihenKarte" + y, ausgespielt.getReihen().get(id).getReihe()[j].id);
				y++;
			}
			x++;
		}
		// ausgespielt
		x = 0;
		for (int id : ausgespielt.getAusgespielteKarten().keySet()) {
			HashMap<Spieler, Karte> hm = ausgespielt.getAusgespielteKarten().get(id);
			int spielerId = 0;
			for (Spieler keySpieler : hm.keySet()) {
				spielerId = keySpieler.getId();
			}
			p.put("ausgespielt" + x, id);
			p.put("spielerId" + x, spielerId);
			x++;
		}
		switch (typ) {
		case SER:
			datei = new PersistenzImplSerialisiert();
			break;
		case CSV:
			datei = new PersistenzImplCSV();
			break;
		default:
			throw new UngueltigeEingabeException(
					"Diese Eingabe war leider Falsch! Du kannst nur zwischen serialisierter und CSV Speicherung auswaehlen.");
		}
		datei.oeffnen(p);
		datei.schreiben(p);
		datei.schliessen();
	}

	/**
	 * Gibt den Boolean Wert true oder false der Varibale bisZumEndeSpielen einer KI
	 * zurück
	 * 
	 * @return den Boolean Wert true oder false eines KI Spielers
	 * 
	 */
	@Override
	public boolean getBisZumEndeSpielen() {

//		synchronized ((KI) spielerAmZug) {
//			((KI) spielerAmZug).threadFortsetzen();
//		}

		boolean bisZumEndeSpielen = false;
		if (spielerAmZug instanceof KI) {
			bisZumEndeSpielen = ((KI) spielerAmZug).getBisZumEndeSpielenKi();

		}
		return bisZumEndeSpielen;
	}

	/**
	 * Setzt den Boolean Wert bisZumEndeSpielen eines Ki Spielers über die SpielCLI
	 * auf den übergebenen Wert.
	 * 
	 * @param bisZumEndeSpielen Boolean Wert true oder false
	 */
	@Override
	public void setBisZumEndeSpielen(boolean bisZumEndeSpielen) {

		if (spielerAmZug instanceof KI) {
			((KI) spielerAmZug).setBisZumEndeSpielenKi(bisZumEndeSpielen);
		}
	}
}