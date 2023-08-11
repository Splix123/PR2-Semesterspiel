package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * 
 * Diese Klasse erstellt einen custom <code>JButton</code>, der einer Spielkarte
 * ähnlich ist und auch ein Bild besitzt.
 * 
 * @author Moritz Rühm
 */
public class KartenButton extends JButton {
	private static final long serialVersionUID = 1L;
	private Color borderColor;
	private int kartenID;

	/**
	 * Im Konstruktor wird zuerst ein Platzhalter mit einer
	 * <code>DashedBorder</code> für die Karten erstellt.
	 * 
	 * @param border     Die Farbe des Rahmens
	 * @param kartenSize Die Größe der Karte / des Buttons
	 * @param h          Die Eventhandler-Klasse für den Buttonclick
	 */
	public KartenButton(Color border, Dimension kartenSize, EventHandler h) {
		borderColor = border;
		setPreferredSize(kartenSize);
		setBorder(BorderFactory.createDashedBorder(borderColor, 2, 2, 2, true));
		setEnabled(false);
		addActionListener(h);
	}

	/**
	 * In dieser Methode wird die Karte erst richtig angezeigt und der Platzhalter
	 * wird mit einem Kartenbild ersetzt.
	 * 
	 * @param kartenBild Das Bild der Karte
	 * @param ID         Die KartenID
	 * @param handkarte  Determiniert ob die Karte eine Handkarte oder eine
	 *                   Reihenkarte ist
	 */
	public void displayKarte(ImageIcon kartenBild, int ID, boolean handkarte) {
		this.kartenID = ID;
		setEnabled(true);
		setIcon(kartenBild);
		setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
		if (handkarte) {
			setVisible(true);
		}
	}

	/**
	 * Setzt eine Kartte in ihren Ursprungszustand ohne Bild und einem gestrichelten
	 * Rand zurück.
	 * 
	 * @param handkarte Determiniert ob die Karte eine Handkarte oder eine
	 *                  Reihenkarte ist
	 */
	public void resetKarte(boolean handkarte) {
		setBorder(BorderFactory.createDashedBorder(borderColor, 2, 2, 2, true));
		setIcon(null);
		if (handkarte) {
			setVisible(false);
		} else {
			setEnabled(false);
		}
	}

	/**
	 * Gibt die ID einer Karte zurück.
	 * 
	 * @return Die KartemID
	 */
	public int getKartenID() {
		return kartenID;
	}
}