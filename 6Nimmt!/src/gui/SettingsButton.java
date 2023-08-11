package gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * Diese Klasse beschreibt einen Einstellungsbutton.
 * 
 * @author Moritz Rühm
 */
public class SettingsButton extends JButton {
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor für einen Settingsbutton.
	 * 
	 * @param text Der Text der unter dem Button stehen soll
	 * @param icon Der Button-Icon
	 * @param h    Die Eventhandler-Klasse für den Buttonclick
	 */
	public SettingsButton(String text, ImageIcon icon, EventHandler h) {
		super(text, icon);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setContentAreaFilled(false);
		setBorderPainted(false);
		addActionListener(h);
	}
}
