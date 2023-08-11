package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 * Diese Klasse erstellt einen custom <code>JButton</code>, welcher später im
 * Startscreen wiederberwendet wird.
 * 
 * @author Moritz Rühm
 */
public class StartButton extends JButton {

	private static final long serialVersionUID = 1L;

	/**
	 * In diesem Konstruktor werden alle Parameter des Buttons bearbeitet.
	 * 
	 * @param text            Der Text der auf dem Button angezeigt werden soll
	 * @param font            Die Schriftart für den Text
	 * @param textColor       Die farbe für den Text
	 * @param backgroundColor Die Hintergrundfarbe für die Buttons
	 * @param borderColor     Die Rahmenfarbe für die Buttons
	 * @param buttonSize      Die Größe des Buttons
	 * @param h               Der Eventhandler, zu dem der Button hinzugefügt wird
	 */
	public StartButton(String text, Font font, Color textColor, Color backgroundColor, Color borderColor,
			Dimension buttonSize, EventHandler h) {
		this.setText(text.toUpperCase());
		this.setFont(font);
		this.setForeground(textColor);
		this.setBackground(backgroundColor);
		this.setOpaque(true);
		this.setBorder(new LineBorder(borderColor, 3, true));
		this.setPreferredSize(buttonSize);
		this.addActionListener(h);
	}
}