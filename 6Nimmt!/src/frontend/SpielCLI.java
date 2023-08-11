package frontend;

import java.util.Arrays;
import java.util.Scanner;

import backend.Spiel;
import exceptions.ExceptionDB.FalscheSpielerAnzahlException;
import exceptions.ExceptionDB.KeineElementeVorhandenException;
import exceptions.ExceptionDB.LogFileException;
import exceptions.ExceptionDB.NichtVorhandenException;
import exceptions.ExceptionDB.UngueltigeEingabeException;
import exceptions.ExceptionDB.keinDatenzugriffException;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;
import interfaces.bedienerInterface;
import loadAndSave.DateiTyp;

/**
 * Ist der <i>"Client"</i> fr die Klasse <code>Spiel</code>, in der sichdie
 * Logik des Spiels befindet. Diese Klasse befindet sich damit im
 * frontend-bereich, da sie lediglich Eingaben bearbeitet und darauf antwortende
 * Methoden aufruft.
 * 
 * @author Hendrik Tesch
 */
public class SpielCLI {
	/**
	 * Beinhaltet die Eingabeverarbeitung des Spiels, welche die Anzhal der Spieler,
	 * ihren Namen und ihr Geschlecht abfragt.
	 * 
	 * @param args Kommandozeilenparameter
	 * @throws LogFileException Wird geworen, wenn das Spiel nicht in die LogFile
	 *                          geschrieben werden kann
	 */
	public static void main(String[] args) throws LogFileException {
		bedienerInterface spiel = new Spiel();
		String eingabe;
		String geschlecht;
		String name;
		Scanner scanner = null;

		while (!spiel.istSpielBeendet()) {
			scanner = new Scanner(System.in);
			System.out.println("Was wollen sie machen?\n1 = Spieler hinzufügen\n2 = Spieler entfernen\n"
					+ "3 = Alle Spieler anzeigen\n4 = Ein Spiel starten\n5 = Ein Spiel laden");
			eingabe = scanner.nextLine();
			switch (eingabe) {
			case "1": {
				System.out.println("Geben sie den Namen des neuen Spielers an:");
				name = scanner.nextLine();
				System.out.println("Bitte gebe dein Geschlecht ein: \n1 = Männlich\n2 = Weiblich\n"
						+ "3 = Divers\n4 = Künstliche Intelligenz");
				geschlecht = scanner.nextLine();
				addSpielerCLI(spiel, name, geschlecht);
				break;
			}
			case "2": {
				System.out.println("Geben sie den Namen des zu entfernenden Spieler an:");
				name = scanner.nextLine();
				removeSpielerCLI(spiel, name);
				break;
			}
			case "3": {
				zeigeAlleSpieler(spiel);
				break;
			}
			case "4": {
				try {
					boolean gespeichert = false;
					starteSpielCLI(spiel, gespeichert);
				} catch (KeineElementeVorhandenException | FalscheSpielerAnzahlException | NumberFormatException
						| NichtVorhandenException e) {
					System.err.println(e.getMessage());
				}
				break;
			}
			case "5": {
				ladenAbfrage(spiel, scanner);
				break;
			}
			default:
				System.err.println("Bitte geben sie eine valide Zahl ein");
			}
		}
		scanner.close();
	}

	/**
	 * Erstellt einen Spieler mit der Methode <code>addSpieler</code> aus der Kasse
	 * <code>Spiel</code>. Die Methode ist hauptsächlich zur vereinfachung und
	 * besseren Veranschaulichung des Codes da.
	 * 
	 * @param spiel      Das Spielobjekt, in dem der Spieler erzeugt werden soll
	 * @param name       Der Name den der Spieler spter hat
	 * @param geschlecht Das Geschlecht, welches der Spieler hat
	 */
	private static void addSpielerCLI(bedienerInterface spiel, String name, String geschlecht) {
		try {
			spiel.addSpieler(name, geschlecht);
			System.out.println("--> Spieler " + name + " erstellt.");
		} catch (UngueltigeEingabeException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Entfernt einen Spieler mit der Methode <code>removeSpieler</code> aus der
	 * Kasse <code>Spiel</code>. Die Methode ist hauptschlich zur vereinfachung und
	 * besseren Veranschaulichung des Codes da.
	 * 
	 * @param spiel Das Spielobjekt, in dem der Spieler entfernt werden soll
	 * @param name  Der Name des zu entfernenden Spielers
	 */
	private static void removeSpielerCLI(bedienerInterface spiel, String name) {
		try {
			spiel.removeSpieler(name);
			System.out.println("Der Spieler " + name + " wurde entfernt");
		} catch (NichtVorhandenException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * laesst einen Spieler eine Reihe mit der Methode <code>nimmReihe</code> aus
	 * der Kasse <code>Spiel</code> aufnehmen. Die Methode ist hauptsächlich zur
	 * vereinfachung und besseren Veranschaulichung des Codes da.
	 * 
	 * @param spiel    Das Spielobjekt indem der Spieler die Reihe nehmen muss
	 * @param id_Reihe Die ID der Reihe, die aufzunehmen ist
	 */
	private static void nimmReihe(bedienerInterface spiel, int id_Reihe) {
		try {
			spiel.nimmReihe(id_Reihe);
		} catch (NichtVorhandenException | KeineElementeVorhandenException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Gibt alle vorhandenen Spieler (sofern welche angelegt wurden) in der Konsole
	 * aus
	 * 
	 * @param spiel Das Spielobjekt, von dem der Spieler angezeigt werdenen sollen
	 */
	private static void zeigeAlleSpieler(bedienerInterface spiel) {
		try {
			if (Arrays.toString(spiel.getAlleSpieler()).length() != 2) {
				System.out.println(Arrays.toString(spiel.getAlleSpieler()));
			} else {
				System.err.println("Noch keine Spieler erstellt!");
			}
		} catch (KeineElementeVorhandenException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * In der <code>starteSpiel</code>-Methode lauft der restliche Teil des Spiels
	 * ab. Hier werden die Karten gelegt die Reihen- sowie Handkartenanzeigen
	 * gemacht und Runden wie auch Spieldurchläufe dokumentiert und gezeigt, bis ein
	 * Spieler Verloren hat.
	 * 
	 * @param spiel       Das zu startende Spielobjekt
	 * @param gespeichert Boolean Wert ob es sich um ein gespeichertes, also
	 *                    geladenes, Spiel handelt
	 * @throws KeineElementeVorhandenException
	 * @throws FalscheSpielerAnzahlException
	 * @throws NichtVorhandenException
	 * @throws NumberFormatException
	 * @throws UngueltigeEingabeException
	 */
	private static void starteSpielCLI(bedienerInterface spiel, boolean gespeichert)
			throws KeineElementeVorhandenException, FalscheSpielerAnzahlException, NumberFormatException,
			NichtVorhandenException, LogFileException {
		boolean spielGespeichert = gespeichert;
		boolean ersteRundeLaden = false;
		int zahl = spiel.getAlleSpieler().length;
		if (zahl < 2) {
			throw new FalscheSpielerAnzahlException("Du hast nicht genügend Spieler erstellt um ein Spiel zu starten. "
					+ "Erstelle noch mindestens " + (2 - zahl) + " Spieler!");
		} else if (zahl > 10) {
			throw new FalscheSpielerAnzahlException("Du hast zu viele Spieler erstellt um ein Spiel zu starten. "
					+ "Lösche mindestens " + (zahl - 10) + " Spieler!");
		} else {
			int rundenCounter = getRunde(spiel);
			boolean gueltigeEingabe = false;
			int counter = numRestlicheSpieler(spiel);
			Scanner scanner = new Scanner(System.in);
			while (!spiel.istSpielBeendet()) {
				if (!spielGespeichert) {
					try {
						spiel.starteSpiel();
					} catch (ladenSpeichernFehlgeschlagenException e) {
						System.err.println(e.getMessage());
					}
				} else {
					ersteRundeLaden = true;
				}
				rundenCounter = getRunde(spiel);
				counter = numRestlicheSpieler(spiel);
				if (counter == 0) {
					System.out.println("Karten austeilen....");
				}
				System.out.println("-------------------");

				while (rundenCounter <= 10) {
					zeigeAlleSpieler(spiel);
					System.out.println("\n\t-- Runde " + rundenCounter
							+ " --\n-- zm- eingeben, um das Zwischenmenü aufzurufen --\n");

					getAusgespielteReihen(spiel);
					if (counter == 0) {
						System.out.println("\nSpieler wählen Karte zum Auspielen: \n");
					}

					// Abfrage für Laden wenn noch nicht alle Spieler ausspielen() aufgerufen haben
					if (!istAktiveRunde(spiel)) {
						for (int i = 0; i < counter; i++) {
							System.out.println("\nSpieler am Zug: " + spiel.getSpielerAmZug()[0]);
							do {
								try {
									if (istKi(spiel)) {
										if (!spiel.getBisZumEndeSpielen()) {
											System.out.println(getKartenVonSpielerAmZug(spiel));
											System.out.println(
													"\nSoll Ki Spieler bis zum Ende durchspielen? \n1 = ja\n2 = nein");
											do {
												try {
													String input = scanner.nextLine();
													if (input.equals("1")) {
														spiel.setBisZumEndeSpielen(true);
														gueltigeEingabe = true;
													} else if (input.equals("2")) {
														spiel.setBisZumEndeSpielen(false);
														gueltigeEingabe = true;
													} else if (input.equals("zm")) {
														lsbMethode(spiel, rundenCounter, scanner);
														gueltigeEingabe = true;
													} else {
														throw new UngueltigeEingabeException(
																"Ungueltige Eingabe. Versuche es nochmal.");
													}
												} catch (UngueltigeEingabeException e) {
													System.err.println(e.getMessage());
												}
											} while (!gueltigeEingabe);

											gueltigeEingabe = false;
										}

										try {
											spiel.ausspielen((-2));
											spiel.beendeZug();
										} catch (NichtVorhandenException | KeineElementeVorhandenException
												| ladenSpeichernFehlgeschlagenException e) {
											System.err.println(e.getMessage());
										}
										gueltigeEingabe = true;
									} else {
										System.out.println("Wähle Karte zum ausspielen: ");
										System.out.println(getKartenVonSpielerAmZug(spiel) + "\n");
										String input = scanner.nextLine();
										for (int j = 0; j < spiel.getKartenVonSpielerAmZug().length; j++) {
											if (spielerHandkarten(spiel)[j] != null
													&& spielerHandkarten(spiel)[j].equals(input)) {
												try {
													spiel.ausspielen(Integer.parseInt(input));
													spiel.beendeZug();
												} catch (NumberFormatException | NichtVorhandenException
														| KeineElementeVorhandenException
														| ladenSpeichernFehlgeschlagenException e) {
													System.err.println(e.getMessage());
												}
												System.out.println("\nDie Karte " + input + " wurde ausgespielt\n");
												gueltigeEingabe = true;
												break;
											}
										}
										if (!gueltigeEingabe && input.equalsIgnoreCase("zm")) {
											lsbMethode(spiel, rundenCounter, scanner);
											gueltigeEingabe = true;
										}
										if (!gueltigeEingabe) {
											throw new UngueltigeEingabeException(
													"Diese Karte konnte nicht ausgespielt werden! Versuche es nochmal.");
										}
										if (input.equalsIgnoreCase("zm")) {
											gueltigeEingabe = false;
										}
									}
								} catch (UngueltigeEingabeException e) {
									System.err.println(e.getMessage());
								}
							} while (!gueltigeEingabe);
							gueltigeEingabe = false;
						}
						ersteRundeLaden = false;
						System.out.println("\nAusgespielte Karten: ");
						getAusgespielteKarten(spiel);
						System.out.println("\nKarten werden angelegt...\n");
					}

					// Abfrage für Laden wenn rundeAktiv ist und alle Spieler bereits eine KArte zum
					// ausspielen gewählt haben
					if (istAktiveRunde(spiel)) {
						counter = numRestlicheSpieler(spiel);
						if (ersteRundeLaden && reiheNehmenLaden(spiel)) {
							System.out.println("\nSpieler am Zug: " + spiel.getSpielerAmZug()[0]);
							do {
								try {
									System.out.println(
											"\nKarte passt an keine Reihe. Es muss eine Reihe aufgenommen werden. Waehle Reihe: ");
									getAusgespielteReihen(spiel);
									String input = scanner.nextLine();
									if (input.equals("1") | input.equals("2") | input.equals("3") | input.equals("4")) {
										nimmReihe(spiel, Integer.parseInt(input));
										getAusgespielteReihen(spiel);
										System.out.println("\nDie Reihe " + input + " wurde aufgenommen");
										gueltigeEingabe = true;
									}
									if (!gueltigeEingabe && input.equalsIgnoreCase("zm")) {
										lsbMethode(spiel, rundenCounter, scanner);
										gueltigeEingabe = true;
									}
									if (!gueltigeEingabe) {
										throw new UngueltigeEingabeException(
												"Diese Karte konnte nicht ausgespielt werden! Versuche es nochmal.");
									}
									if (input.equalsIgnoreCase("zm")) {
										gueltigeEingabe = false;
									}
								} catch (UngueltigeEingabeException e) {
									System.err.println(e.getMessage());
								}
							} while (!gueltigeEingabe);
							gueltigeEingabe = false;
							ersteRundeLaden = false;
						}
						for (int i = 0; i < counter; i++) {
							if (spiel.istSpielBeendet()) {
								break;
							} else {
								try {
									int idSpielerReiheNehmen = spiel.beendeZug();
									if (idSpielerReiheNehmen > 0) {
										System.out.println(getSpielerReiheNehmen(spiel, idSpielerReiheNehmen));
										do {
											try {
												System.out.println(
														"\nKarte passt an keine Reihe. Es muss eine Reihe aufgenommen werden. Waehle Reihe: ");
												getAusgespielteReihen(spiel);
												String input = scanner.nextLine();
												if (input.equals("1") | input.equals("2") | input.equals("3")
														| input.equals("4")) {
													nimmReihe(spiel, Integer.parseInt(input));
													getAusgespielteReihen(spiel);
													System.out.println("\nDie Reihe " + input + " wurde aufgenommen");
													gueltigeEingabe = true;
												}
												if (!gueltigeEingabe && input.equalsIgnoreCase("zm")) {
													lsbMethode(spiel, rundenCounter, scanner);
													gueltigeEingabe = true;
												}
												if (!gueltigeEingabe) {
													throw new UngueltigeEingabeException(
															"Diese Karte konnte nicht ausgespielt werden! Versuche es nochmal.");
												}
												if (input.equalsIgnoreCase("zm")) {
													gueltigeEingabe = false;
												}
											} catch (UngueltigeEingabeException e) {
												System.err.println(e.getMessage());
											}
										} while (!gueltigeEingabe);
										gueltigeEingabe = false;
									}
								} catch (NumberFormatException | KeineElementeVorhandenException
										| NichtVorhandenException | ladenSpeichernFehlgeschlagenException e) {
									System.err.println(e.getMessage());
								}
							}
							ersteRundeLaden = false;
						}
					}

					if (spiel.istSpielBeendet()) {
						beendeSpiel(spiel, rundenCounter);
						System.out.println("Das Spiel ist beendet");
						break;
					}
					getAusgespielteReihen(spiel);
					System.out.println("Zwischenstand:");
					System.out.println("\t-- Ende Runde " + rundenCounter + " --\n");
					rundenCounter++;
				}
//				if (spiel.istSpielBeendet()) {
//					break;
//				} else {
//					System.out.println("\n\t-- Ende Runde " + rundenCounter + " --\n");
//					System.out.println(Arrays.toString(spiel.getAlleSpieler()));
//					rundenCounter++;
//				}
				System.out.println(
						"\n\t\t\t\t\t\t-- Es wurden alle Handkarten ausgespielt. Neue Karten werden gemischt... "
								+ " --\n");

				if (spiel.istSpielBeendet()) {
					beendeSpiel(spiel, rundenCounter);
					System.out.println("Das Spiel ist beendet");
					break;
				} else {
					rundenCounter = 1;
					gespeichert = false;
				}
			}
			System.exit(0);
		}
	}

	/**
	 * Teilt die String Ausgabe der Spielerkarten und speichert die Karten ID als
	 * int Wert in ein Array
	 * 
	 * @param spiel Das Spielobjekt, von dem die Handkarten angezeigt werdenen
	 *              sollen
	 * @return Liste mit den Handkarten des Spielers als int
	 */
	private static String[] spielerHandkarten(bedienerInterface spiel) {
		String[] spielerHandkarten = new String[getKartenVonSpielerAmZug(spiel).length()];
		for (int i = 0; i < spiel.getKartenVonSpielerAmZug().length; i++) {
			if (spiel.getKartenVonSpielerAmZug()[i] == null) {
				continue;
			}
			String[] kartenInfo = spiel.getKartenVonSpielerAmZug()[i].split(",");
			spielerHandkarten[i] = kartenInfo[0];
		}
		return spielerHandkarten;
	}

	/**
	 * Gibt nocheinmal sauber den Spielcounter, sowie das Scoreboard mit markierten
	 * Gewinnern und Verlierern aus.
	 * 
	 * @param spiel         Das zu beendende Spielobjekt
	 * @param spielCounter  Integer-Angabe, wieviele Spiele gespielt worden sind
	 * @param rundenCounter Integer-Angabe, wiviele Runden im letzten Spipel vor
	 *                      beendigung gespielt wurden
	 */
	private static void beendeSpiel(bedienerInterface spiel, int rundenCounter) {
		System.out.println("-----------------------------------------------------------");
		System.out.println("Der Endstand in Runde " + rundenCounter + " lautet:");
		try {
			for (int i = 0; i < spiel.getAlleSpieler().length; i++) {
				System.out.println(spiel.getAlleSpieler()[i]);
			}
		} catch (KeineElementeVorhandenException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Gibt die in dieser Runde ausgespielten Reihen aus.
	 * 
	 * @param spiel Das Spielobjekt in dem die Ausgabe stattfinden soll
	 * @throws KeineElementeVorhandenException
	 */
	private static void getAusgespielteReihen(bedienerInterface spiel) throws KeineElementeVorhandenException {
		for (int i = 0; i < 4; i++) {
			if (spiel.getAusgespielt()[i].equals(null)) {
				continue;
			} else {
				System.out.println(spiel.getAusgespielt()[i]);
			}
		}
	}

	/**
	 * a
	 * 
	 * @param spiel
	 * @throws KeineElementeVorhandenException
	 */
	private static void getAusgespielteKarten(bedienerInterface spiel) throws KeineElementeVorhandenException {
		String output = "";
		if (spiel.getAusgespielt() != null) {
			for (int i = 4; i < spiel.getAusgespielt().length; i++) {
				if (spiel.getAusgespielt()[i].equals(null)) {
					continue;
				} else {
					String[] karte = spiel.getAusgespielt()[i].split(": ");
					String[] karteInfo = karte[3].split(" ");
					output += karteInfo[0];
					if (i < spiel.getAusgespielt().length - 1) {
						output += ", ";
					}
				}
			}
		}
		System.out.println(output);
	}

	/**
	 * Gibt die Karten des Spielers aus, der gerade am Zug ist.
	 * 
	 * @param spiel Das Spielobjekt in dem die Ausgabe stattfinden soll
	 * @return Die Karten des Spielers
	 */
	private static String getKartenVonSpielerAmZug(bedienerInterface spiel) {
		String output = "";
		for (String s : spiel.getKartenVonSpielerAmZug()) {
			if (s == null) {
				continue;
			} else {
				output += s + " | ";
			}
		}
		return removeLastCharacter(output);
	}

	/**
	 * Entfernt die letzten Zeichen eines Strings
	 * 
	 * @param str String bei dem die letzten Zeichen entfernt werden sollen
	 * @return String ohne Trennzeichen
	 */
	private static String removeLastCharacter(String str) {
		String result = null;
		if ((str != null) && (str.length() > 0)) {
			result = str.substring(0, str.length() - 3);
		}
		return result;
	}

	/**
	 * Überprüft anhand des Geschlechts ob der Spieler am Zug eine KI ist.
	 * 
	 * @param spiel Das Spielobjekt indem der Wert ermittelt werden soll
	 * @return True wenn der SpielerAmZug Geschlecht "ki" ist
	 */
	private static boolean istKi(bedienerInterface spiel) {
		boolean spielerIstKi = false;
		String[] spielerInfo = spiel.getSpielerAmZug()[0].split(", ");
		if (spielerInfo[2].equals("ki")) {
			spielerIstKi = true;
		}
		return spielerIstKi;
	}

	/**
	 * Berechnet die aktuelle Runde anhand der verbleibenden Spieler Karten und dem
	 * Boolean istAktiveRunde().
	 * 
	 * @param spiel Das Spielobjekt indem der Wert ermittelt werden soll
	 * @return int Wert der aktuellen Runde
	 */
	private static int getRunde(bedienerInterface spiel) {
		int rundencounter = 0;
		int arrayLength = spiel.getKartenVonSpielerAmZug().length;
		if (spiel.getKartenVonSpielerAmZug() != null) {
			for (int i = 0; i < arrayLength; i++) {
				if (spiel.getKartenVonSpielerAmZug()[i] != null) {
					rundencounter++;
					continue;
				} else {
					break;
				}
			}
		}
		if (istAktiveRunde(spiel)) {
			return (10 - rundencounter);
		} else {
			return (10 - rundencounter) + 1;
		}
	}

	/**
	 * Gibt zurück ob die Runde aktiv ist, man sich also in der Phase des Spiels
	 * befindet, in dem KArten an Reihen angelegt werden.
	 * 
	 * @param spiel Das Spielobjekt indem der Wert ermittelt werden soll
	 * @return Boolean Wert True wenn Runde aktiv, False wenn nicht aktiv
	 */
	private static boolean istAktiveRunde(bedienerInterface spiel) {
		boolean istAktiveRunde = false;
		String spieler[] = spiel.getSpielerAmZug();
		if (spieler[1].equals("1")) {
			istAktiveRunde = true;
		}
		return istAktiveRunde;
	}

	/**
	 * Berechnet die wie viele Spieler in einer Spielphase noch aufgerufen werden
	 * müssen.
	 * 
	 * @param spiel Das Spielobjekt indem der Wert ermittelt werden soll
	 * @return die Anzahl der Spieler die noch nicht dran waren
	 * @throws KeineElementeVorhandenException
	 */
	private static int numRestlicheSpieler(bedienerInterface spiel) {
		int num;
		try {
			if (istAktiveRunde(spiel)) {
				num = spiel.getAusgespielt().length - 4;
			} else {
				num = spiel.getAlleSpieler().length - (spiel.getAusgespielt().length - 4);
			}
		} catch (KeineElementeVorhandenException e) {
			num = 0;
		}
		return num;
	}

	/**
	 * Zugangsmethode um Speichern, Laden und Beenden zu können zu jedem Zeitpunkt
	 * während des Spielverlaufs
	 * 
	 * @param spiel
	 * @param rundenCounter
	 * @param scanner
	 * @throws LogFileException
	 */
	private static void lsbMethode(bedienerInterface spiel, int rundenCounter, Scanner scanner)
			throws LogFileException {
		String eingabe = "";
		if (eingabe != null) {
			System.out.println("Sie befinden sich im ZwischenMenü\nWas möchten sie machen?"
					+ "\n1 = BEENDEN: Spiel beenden\n2 = SPEICHERN Spiel speichern\n3 = LADEN: alten Spielstand laden");
			eingabe = scanner.nextLine();
			switch (eingabe) {
			case "1":
				System.out.println("Spiel wird beendet");
				beendeSpiel(spiel, getRunde(spiel));
				System.exit(0);
				break;
			case "2":
				speicherAbfrage(spiel, scanner);
				break;
			case "3":
				ladenAbfrage(spiel, scanner);
				break;
			}

		}
	}

	/**
	 * Fragt bei Auswahl von Speichern ab in welchem Format gespeichert werden soll
	 * 
	 * @param spiel
	 * @param scanner
	 * @throws LogFileException
	 */
	private static void speicherAbfrage(bedienerInterface spiel, Scanner scanner) throws LogFileException {
		String eingabe = "";
		boolean gespeichert = false;
		while (!gespeichert) {
			System.out.println("In welchem Format soll gespeichert werden?\n1 = Serialisiert\n2 = CSV");
			eingabe = scanner.nextLine();
			try {
				switch (eingabe) {
				case "1":
					spiel.speichern("saveGames/SpielstandSER_1", DateiTyp.SER);
					gespeichert = true;
					break;

				case "2":
					spiel.speichern("saveGames/SpielstandCSV_1", DateiTyp.CSV);
					gespeichert = true;
					break;

				default:
					System.err.println("Dieses Format gibt es leider nicht! Wähle ein anderes aus");
					break;
				}
				System.out.println("Das Spiel ist gespeichert");
			} catch (UngueltigeEingabeException | KeineElementeVorhandenException | keinDatenzugriffException
					| ladenSpeichernFehlgeschlagenException e) {
				System.err.println(e.getMessage());
			}
			System.out.println("Was möchten Sie machen?\n1 = weiterspielen\n2 = Spielstand laden");
			eingabe = scanner.nextLine();
			switch (eingabe) {
			case "1":
				System.out.println("Viel Spaß beim Weiterspielen");
				break;
			case "2":
				ladenAbfrage(spiel, scanner);
			}
		}

	}

	/**
	 * Fragt bei Auswahl von Laden ab in welchem Format gespeichert werden soll
	 * 
	 * @param spiel
	 * @param scanner
	 * @throws LogFileException
	 */
	private static void ladenAbfrage(bedienerInterface spiel, Scanner scanner) throws LogFileException {
		String eingabe = "";
		boolean geladen = false;
		while (!geladen) {
			System.out.println("Aus welchem Format soll geladen werden?\n1 = Serialisiert\n2 = CSV");
			eingabe = scanner.nextLine();
			try {
				switch (eingabe) {
				case "1":
					spiel = bedienerInterface.laden("saveGames/SpielstandSER_1", DateiTyp.SER);
					geladen = true;
					break;
				case "2":
					spiel = bedienerInterface.laden("saveGames/SpielstandCSV_1", DateiTyp.CSV);
					try {
						spiel.getAusgespielt();
					} catch (KeineElementeVorhandenException e) {
						System.err.println(e.getMessage());
					}
					geladen = true;
					break;
				default:
					System.err.println("Dieses Format gibt es leider nicht! Wähle ein anderes aus");
					break;
				}
				System.out.println("Das Spiel wurde geladen");
			} catch (UngueltigeEingabeException | keinDatenzugriffException | ladenSpeichernFehlgeschlagenException e) {
				System.err.println(e.getMessage());
			}
		}
		try {
			starteSpielCLI(spiel, geladen);
			System.out.println(spiel.getAusgespielt());
		} catch (NumberFormatException | KeineElementeVorhandenException | FalscheSpielerAnzahlException
				| NichtVorhandenException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Gibt den Spieler mit der ID spielerID, welche als Parameter übergeben wird,
	 * zurück indem er die ID der Spieler mit spielerId vergleicht.
	 * 
	 * @param spiel     Das Spielobjekt indem der Spieler ermittelt werden soll
	 * @param spielerId von dem Spieler der eine Reihe als Strafe aufnehmen muss
	 * @return Spieler mit ID, Geschlecht und Leben der eine Reihe aufnehmen muss
	 * @throws KeineElementeVorhandenException
	 */
	private static String getSpielerReiheNehmen(bedienerInterface spiel, int spielerId)
			throws KeineElementeVorhandenException {
		String spielerReiheNehmen = "";
		for (String s : spiel.getAlleSpieler()) {
			String[] spieler = s.split(",");
//			System.out.println("spielerID reihe nehmen: " + spielerId);
			if (Integer.parseInt(spieler[0]) == spielerId) {
				spielerReiheNehmen = "Spieler am Zug: " + spieler[1] + ", ID: " + spieler[0] + ", " + spieler[2] + ", "
						+ spieler[3];
				break;
			}
		}
//		System.out.println("getSpielerReiheNehmen(): " + spielerReiheNehmen);
		return spielerReiheNehmen;
	}

	/**
	 * a
	 * 
	 * @param spiel a
	 * @return a
	 * @throws KeineElementeVorhandenException a
	 */
	private static boolean reiheNehmenLaden(bedienerInterface spiel) throws KeineElementeVorhandenException {
		boolean mussNimmReiheAufrufen = false;
		String[] spielerIds = new String[spiel.getAusgespielt().length - 4];
		if (spiel.getAusgespielt() != null) {
			for (int i = 4; i < spiel.getAusgespielt().length; i++) {
				if (spiel.getAusgespielt()[i] != null) {
//					System.out.println(spiel.getAusgespielt()[i]);
					String[] spielerInfo = spiel.getAusgespielt()[i].split(": ");
					String[] spielerInfo2 = spielerInfo[1].split(", ");
					spielerIds[i - 4] = spielerInfo2[0];
				}
			}
		}
		String[] spielerAmZugInfo = spiel.getSpielerAmZug()[0].split(", ");
		String spielerAmZugId = spielerAmZugInfo[1];
		for (String s : spielerIds) {
			if (s.equals(spielerAmZugId)) {
			} else {
				mussNimmReiheAufrufen = true;
			}
		}
		return mussNimmReiheAufrufen;
	}
}