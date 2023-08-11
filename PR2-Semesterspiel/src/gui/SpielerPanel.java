package gui;

import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * In dieser Klasse wird ein <code>JPanel</code> erzeugt, welches Später die
 * erstellten Spieler mit ihren Bildern beinhaltet.
 * 
 * @author Moritz Rühm
 */
public class SpielerPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private GridLayout spielerLayout;

	/**
	 * Es werden Layout und Abstände gesetzt.
	 */
	public SpielerPanel() {
		spielerLayout = new GridLayout(0, 2);
		spielerLayout.setVgap(5);
		spielerLayout.setHgap(20);
		this.setLayout(spielerLayout);
		this.setOpaque(false);
	}
}