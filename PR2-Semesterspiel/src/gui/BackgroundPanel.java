package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * Diese Klasse erstellt ein JPanel mit einem Background.
 * 
 * @author Moritz Rühm
 */
public class BackgroundPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image img;

	/**
	 * der Konstruktor der Klasse setzt das Background-Image, sowie das Layout fest.
	 * 
	 * @param img Das Background-Image, welches benutzt werden soll
	 */
	public BackgroundPanel(Image img) {
		this.img = img;
		setLayout(new BorderLayout());
	}

	/**
	 * Diese Methode überschreibt die <code>paintComponent</code>-Methode des
	 * JPanels, um ihm einen Hintergrund zu geben.
	 * 
	 * @param g Die Graphics der Komponente
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (img != null)
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}