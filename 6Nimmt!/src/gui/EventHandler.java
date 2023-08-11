package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.regex.Pattern;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import backend.Spiel;
import exceptions.ExceptionDB.KeineElementeVorhandenException;
import exceptions.ExceptionDB.LogFileException;
import exceptions.ExceptionDB.NichtVorhandenException;
import exceptions.ExceptionDB.UngueltigeEingabeException;
import exceptions.ExceptionDB.keinDatenzugriffException;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;
import interfaces.bedienerInterface;
import loadAndSave.DateiTyp;

/**
 * Dies ist die Eventhandler-Klasse für die Oberflächenklasse <code>GUI</code>
 * und beinhaltet alles was die GUI bei Interaktionen zu tun hat.
 * 
 * @author Moritz Rühm
 * @author Rodez Tazo
 */
public class EventHandler implements ActionListener {
	private GUI gui;
	private bedienerInterface spiel = new Spiel();
	private Object quelle;

	private int spielerAnzahl;
	private int selection;
	private boolean geladen = false;
	private String name;
	private String geschlechtString;
	private String geschlechtInt;
	private String entfernen;
	private int mussReiheNehmen;

	/**
	 * Der Kosntruktor des Eventhandlers.
	 * @param gui Die GUI auf dem der Eventhandler läuft
	 */
	public EventHandler(GUI gui) {
		this.gui = gui;
	}

	/**
	 * Die überschriebene <code>actionPerformed</code>-Methode des Eventhandlers.
	 * 
	 * @param e Das ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		quelle = e.getSource();

		if (quelle == gui.startZahnradButton) {
			gui.settingsDialog.setVisible(true);
		}
		if (quelle == gui.stopMusicButton) {
			if (gui.clip.isActive()) {
				gui.clip.stop();
				gui.stopMusicButton.setIcon(gui.NoMusicIcon);
				gui.stopMusicButton.setText("Musik Starten");
			} else {
				gui.clip.start();
				gui.stopMusicButton.setIcon(gui.MusicIcon);
				gui.stopMusicButton.setText("Musik Stoppen");
			}
		}
		if (quelle == gui.spielStartenButton) {
			spielerAnzahl = 0;
			try {
				spielerAnzahl = spiel.getAlleSpieler().length;
			} catch (KeineElementeVorhandenException ex) {
				JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
			}
			if (spielerAnzahl < 2) {
				JOptionPane.showMessageDialog(gui.frame, "Du hast nicht genügend Spieler angelegt!", "Fehler!",
						JOptionPane.ERROR_MESSAGE);
			} else if (spielerAnzahl > 10) {
				JOptionPane.showMessageDialog(gui.frame, "Du hast zu viele Spieler angelegt!", "Fehler!",
						JOptionPane.ERROR_MESSAGE);
			} else {
				if (!geladen) {
					try {
						spiel.starteSpiel();
					} catch (ladenSpeichernFehlgeschlagenException | LogFileException ex) {
						JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
					}
				}
				displayReihenKarten();
				displaySpielerAmZug(true);
				displayHandKarten();
				gui.cardLayout.next(gui.cardPanel);
			}
		}
		if (quelle == gui.spielLadenButton || quelle == gui.loadButton) {
			FileChooser chooser = new FileChooser(false);
			selection = chooser.showOpenDialog(gui.frame);
			if (selection == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				DateiTyp typ = null;
				if (file.getPath().endsWith(".ser")) {
					typ = DateiTyp.SER;
				} else if (file.getPath().endsWith(".csv")) {
					typ = DateiTyp.CSV;
				}
				try {
					spiel = bedienerInterface.laden(file.getPath(), typ);
				} catch (UngueltigeEingabeException | keinDatenzugriffException
						| ladenSpeichernFehlgeschlagenException ex) {
					JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
				}
				gui.spielerScrollPanel.removeAlleSpieler();
				gui.spielerScrollPanel2.removeAlleSpieler();
				try {
					for (String spieler : spiel.getAlleSpieler()) {
						String[] einSpieler = spieler.split(", ");
						String nameGeladen = einSpieler[1];
						String lebenGeladen = einSpieler[3];
						gui.spielerScrollPanel.addSpieler(nameGeladen, lebenGeladen);
						gui.spielerScrollPanel2.addSpieler(nameGeladen, lebenGeladen);
					}
				} catch (KeineElementeVorhandenException ex) {
					JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
				}
				if (istAktiveRunde(spiel)) {

				}
			}
		}
		if (quelle == gui.spielLadenButton) {
			if (selection == JFileChooser.APPROVE_OPTION) {
				gui.spielerhinzufuegenButton.setEnabled(false);
				gui.spielerEntfernenButton.setEnabled(false);
				geladen = true;
			}
			gui.addSpielerPopup.setVisible(false);
			gui.removeSpielerPopup.setVisible(false);
			gui.spielerScrollPanel.revalidate();
			gui.spielerScrollPanel2.revalidate();
		}
		if (quelle == gui.spielerhinzufuegenButton) {
			if (gui.addSpielerPopup.isVisible()) {
				gui.addSpielerPopup.setVisible(false);
			} else {
				gui.removeSpielerPopup.setVisible(false);
				gui.addSpielerPopup.setVisible(true);
			}
		}
		if (quelle == gui.namensEingabeField) {
			name = gui.namensEingabeField.getText();
			if (Pattern.matches("^[a-zA-Z0-9]{2,8}$", name)) {
				gui.namensEingabeField.setBorder(new LineBorder(Color.black, 1));
				gui.geschlechtLabel.setEnabled(true);
				gui.geschlechtEingabeField.setEnabled(true);
				gui.hinzufuegenButton.setEnabled(true);
			} else {
				gui.namensEingabeField.setBorder(new LineBorder(Color.red, 3));
				gui.geschlechtEingabeField.setEnabled(false);
				gui.hinzufuegenButton.setEnabled(false);
			}
		}
		if (quelle == gui.geschlechtEingabeField) {
			geschlechtString = (String) gui.geschlechtEingabeField.getSelectedItem();
			switch (geschlechtString) {
			case "Männlich":
				geschlechtInt = "1";
				break;

			case "Weiblich":
				geschlechtInt = "2";
				break;

			case "Divers":
				geschlechtInt = "3";
				break;

			case "KI":
				geschlechtInt = "4";
				break;
			default:
				break;
			}
		}
		if (quelle == gui.hinzufuegenButton) {
			geschlechtString = (String) gui.geschlechtEingabeField.getSelectedItem();
			switch (geschlechtString) {
			case "Männlich":
				geschlechtInt = "1";
				break;

			case "Weiblich":
				geschlechtInt = "2";
				break;

			case "Divers":
				geschlechtInt = "3";
				break;

			case "KI":
				geschlechtInt = "4";
				break;
			default:
				break;
			}
			gui.spielerScrollPanel.addSpieler(name, "66");
			gui.spielerScrollPanel2.addSpieler(name, "66");
			try {
				spiel.addSpieler(name, geschlechtInt);
			} catch (UngueltigeEingabeException ex) {
				JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
			}
			gui.addSpielerPopup.setVisible(false);
			gui.namensEingabeField.setText("");
			gui.geschlechtLabel.setEnabled(false);
			gui.geschlechtEingabeField.setEnabled(false);
			gui.hinzufuegenButton.setEnabled(false);
		}
		if (quelle == gui.spielerEntfernenButton) {
			if (gui.removeSpielerPopup.isVisible()) {
				gui.removeSpielerPopup.setVisible(false);
			} else {
				gui.addSpielerPopup.setVisible(false);
				DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) gui.spielerList.getModel();
				model.removeAllElements();
				try {
					for (String spieler : spiel.getAlleSpieler()) {
						String[] einSpieler = spieler.split(", ");
						model.addElement(einSpieler[1]);
					}
				} catch (KeineElementeVorhandenException ex) {
					JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
				}
				gui.spielerList.setModel(model);
				gui.removeSpielerPopup.setVisible(true);
			}
		}
		if (quelle == gui.spielerList) {
			entfernen = (String) gui.spielerList.getSelectedItem();
			gui.entfernenButton.setEnabled(true);
		}
		if (quelle == gui.entfernenButton) {
			gui.removeSpielerPopup.setVisible(false);
			gui.spielerScrollPanel.removeSpieler(entfernen);
			gui.spielerScrollPanel2.removeSpieler(entfernen);
			try {
				spiel.removeSpieler(entfernen);
			} catch (NichtVorhandenException ex) {
				JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (quelle == gui.creditsButton) {
			JOptionPane.showMessageDialog(gui.frame,
					"Dieses Spiel wurde für die Studienleistung im Fach PR2 entworfen von\n\nNicole Shiner, Hendrik Tesch,\nRodez Tazo, Cindy Tuku\nund Moritz Rühm",
					"Credits", JOptionPane.INFORMATION_MESSAGE);
		}
		if (quelle == gui.gameZahnradButton) {
			gui.loadButton.setVisible(true);
			gui.saveButton.setVisible(true);
			gui.settingsDialog.setVisible(true);
		}
		if (quelle == gui.loadButton) {
			displayReihenKarten();
			displayHandKarten();
			gui.settingsDialog.setVisible(false);
		}
		if (quelle == gui.saveButton) {
			FileChooser chooser = new FileChooser(true);
			chooser.showSaveDialog(gui.frame);
			FileFilter filter = chooser.getFileFilter();
			DateiTyp typ = null;
			if (filter.getDescription().equals("CSV")) {
				typ = DateiTyp.CSV;
			} else if (filter.getDescription().equals("Serialisiert")) {
				typ = DateiTyp.SER;
			}
			File file = chooser.getSelectedFile();
			try {
				spiel.speichern(file.getPath(), typ);
			} catch (UngueltigeEingabeException | KeineElementeVorhandenException | keinDatenzugriffException
					| ladenSpeichernFehlgeschlagenException ex) {
				JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
			}
			gui.settingsDialog.setVisible(false);
		}
		for (int i = 0; i < gui.handKartenButtons.size(); i++) {
			if (quelle == gui.handKartenButtons.get(i)) {
				System.out.println("Runde " + getRunde(spiel));
				displaySpielerAmZug(false);
				try {
					spiel.ausspielen(gui.handKartenButtons.get(i).getKartenID());
					System.out.println("gelegt " + gui.handKartenButtons.get(i).getKartenID());
					spiel.beendeZug();
				} catch (KeineElementeVorhandenException | NichtVorhandenException
						| ladenSpeichernFehlgeschlagenException ex) {
					JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
				if (getRunde(spiel) == 10) {
					String dialogString = "Diese Runde ist beendet.\n";
					for (int j = 0; j < spielerAnzahl; j++) {
						try {
							dialogString += spiel.getAlleSpieler()[j] + "\n";
						} catch (KeineElementeVorhandenException ex) {
							JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!",
									JOptionPane.ERROR_MESSAGE);
						}
					}
					displayHandKarten();
					displayReihenKarten();
					dialogString += "Die Karten werden nun neu ausgeteilt.";
					JOptionPane.showMessageDialog(gui.frame, dialogString, "Runde beendet",
							JOptionPane.INFORMATION_MESSAGE);
					displayHandKarten();
					displayReihenKarten();
				}
				displaySpielerAmZug(true);
				if (istKi(spiel)) {
					for (int j = 0; j < spiel.getKartenVonSpielerAmZug().length; j++) {
						gui.handKartenButtons.get(j).resetKarte(false);
					}
					if (!spiel.getBisZumEndeSpielen()) {
						int option = JOptionPane.showConfirmDialog(gui.frame, "Soll die KI bis zum Ende Durchziehen?",
								"Durchziehen?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (option == JOptionPane.YES_OPTION) {
							spiel.setBisZumEndeSpielen(true);
						} else {
							try {
								spiel.ausspielen((-1));
								spiel.beendeZug();
							} catch (NichtVorhandenException | KeineElementeVorhandenException
									| ladenSpeichernFehlgeschlagenException ex) {
								JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!",
										JOptionPane.ERROR_MESSAGE);
								ex.printStackTrace();
							}
						}
					}
				}
				displayHandKarten();
				if (istAktiveRunde(spiel)) {
					boolean bol = false;
					for (int j = 0; j < spielerAnzahl; j++) {
						try {
							mussReiheNehmen = spiel.beendeZug();
						} catch (KeineElementeVorhandenException | NichtVorhandenException
								| ladenSpeichernFehlgeschlagenException ex) {
							JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!",
									JOptionPane.ERROR_MESSAGE);
						}
						if (mussReiheNehmen > 0) {
							bol = true;
							displayHandKarten();
							for (int k = 0; k < spiel.getKartenVonSpielerAmZug().length; k++) {
								gui.handKartenButtons.get(k).resetKarte(false);
							}
							displayReiheNehmen(true);
							int reihe = (int) (Math.random() * (4 - 1 + 1) + 1);
							try {
								spiel.nimmReihe(reihe);
							} catch (NichtVorhandenException | KeineElementeVorhandenException ex) {
								JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!",
										JOptionPane.ERROR_MESSAGE);
							}
						}
						String[] spielerAmZug = spiel.getSpielerAmZug()[0].split(", ");
						String[] leben = spielerAmZug[3].split(": ");
						gui.spielerScrollPanel2.updateLeben(spielerAmZug[0], leben[1]);
						if (!bol) {
							displayReihenKarten();
						}
					}
				}
				if (spiel.istSpielBeendet()) {

				}
			}
		}
		for (int i = 0; i < 4; i++) {
			if (quelle == gui.reihenAuswahlButtons.get(i)) {
				displayReiheNehmen(false);
				displayReihenKarten();
				displayHandKarten();
			}
		}
	}

	/**
	 * Markiert den jetzigen Spieler am Zug Rot und den vorherigen auf Weiß,
	 * basierend auf einem Boolean-Wert.
	 * 
	 * @param amZug Boolean-Wert, ob ein Spieler gerade am Zug ist oder nicht
	 */
	public void displaySpielerAmZug(boolean amZug) {
		String[] spielerAmZugStrings = spiel.getSpielerAmZug();
		String spielerAmZugsString = spielerAmZugStrings[0];
		String[] spielerAmZugSplit = spielerAmZugsString.split(", ");
		if (amZug) {
			SpielerScrollPanel.spielerNameLabelHashMap.get(spielerAmZugSplit[0]).setForeground(Color.red);
		} else {
			SpielerScrollPanel.spielerNameLabelHashMap.get(spielerAmZugSplit[0]).setForeground(Color.white);
		}
	}

	/**
	 * Überschreibt die angezeigten Handkarten mit denen des neuen Spielers am Zug.
	 */
	public void displayHandKarten() {
		String[] kartenVonSpielerAmZug = spiel.getKartenVonSpielerAmZug();
		if (!kartenVonSpielerAmZug[0].equals("0")) {
			for (int i = 0; i < 10; i++) {
				System.out.println(kartenVonSpielerAmZug[i] != null);
				if (kartenVonSpielerAmZug[i] != null) {
					String[] karte = kartenVonSpielerAmZug[i].split(", ");
					gui.handKartenButtons.get(i).displayKarte(gui.kartenBilder.get(Integer.parseInt(karte[0]) - 1),
							Integer.parseInt(karte[0]), true);
				} else {
					gui.handKartenButtons.get(i).resetKarte(true);
				}
			}
			System.out.println();
		}
	}

	/**
	 * Überschreibt die Reihenkarten mit neuen, z.B. nachdem eine Reihe aufgenommen
	 * werden musste.
	 */
	public void displayReihenKarten() {
		String[] reihenKarten = null;
		try {
			reihenKarten = spiel.getAusgespielt();
		} catch (KeineElementeVorhandenException ex) {
			JOptionPane.showMessageDialog(gui.frame, ex.getMessage(), "Fehler!", JOptionPane.ERROR_MESSAGE);
		}
		int zaehler = 0;
		for (int i = 0; i < 4; i++) {
			String[] reihe = reihenKarten[i].substring(9).split(", ");
			for (int j = 0; j < reihe.length - 1; j++) {
				gui.reihenKartenButtons.get(zaehler + j).displayKarte(
						gui.kartenBilder.get(Integer.parseInt(reihe[j]) - 1), Integer.parseInt(reihe[j]), false);
			}
			for (int j = reihe.length - 1; j < 5; j++) {
				gui.reihenKartenButtons.get(zaehler + j).resetKarte(false);
			}
			zaehler += 5;
		}

	}

	/**
	 * Zeigt die Reihenauswahl-Buttons oder lässt sie verschwinden, basierend auf
	 * einem Boolean-Wert.
	 * 
	 * @param nehmen ob die Buttons angezeigt oder transparent werden sollen
	 */
	public void displayReiheNehmen(boolean nehmen) {
		if (nehmen) {
			for (int j = 0; j < gui.handKartenButtons.size(); j++) {
				gui.handKartenButtons.get(j).setEnabled(false);
			}
			for (int j = 0; j < 4; j++) {
				gui.reihenAuswahlButtons.get(j).setVisible(true);
			}
		} else {
			for (int j = 0; j < 4; j++) {
				gui.reihenAuswahlButtons.get(j).setVisible(false);
			}
			for (int j = 0; j < gui.handKartenButtons.size(); j++) {
				gui.handKartenButtons.get(j).setEnabled(true);
			}
		}
	}

	/**
	 * Berechnet die aktuelle Runde anhand der verbleibenden Spieler Karten und dem
	 * Boolean istAktiveRunde().
	 * 
	 * @param spiel Das Spielobjekt indem der Wert ermittelt werden soll
	 * @return int Wert der aktuellen Runde
	 */
	private int getRunde(bedienerInterface spiel) {
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
}