package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

/**
 * Diese KLasse ist für die Spieleranzeige im Speil zuständig und erbt von
 * JScrollPane, sodass die Spielerliste bei zu hoher Anzahl auch gescrollt
 * werden kann.
 * 
 * @author Moritz Rühm
 */
public class SpielerScrollPanel extends JScrollPane {

	private static final long serialVersionUID = 1L;
	private SpielerPanel spielerPanel = new SpielerPanel();
	private Font spielerFont = new Font("Gill Sans MT", Font.BOLD, 20);
	static HashMap<String, JLabel> spielerNameLabelHashMap = new HashMap<>();
	static HashMap<String, SpielerBildLabel> spielerBildLabelHashMap = new HashMap<>();

	/*
	 * Der Konstruktor des ScrollPanels.
	 * 
	 * @param spielerPanelSize Die Dimensionen des Panels
	 */
	public SpielerScrollPanel(Dimension spielerPanelSize) {
		this.setViewportView(spielerPanel);
		this.setPreferredSize(spielerPanelSize);
		this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.setBorder(null);
		this.setOpaque(false);
		this.getViewport().setOpaque(false);
	}

	/**
	 * Fügt dem Panel einen Spieler hinzu.
	 * 
	 * @param name  Der name des Spielers
	 * @param leben Die aktuelle Lebensanzahl des Spielers
	 */
	public void addSpieler(String name, String leben) {
		// Spielername
		JLabel spielerNameLabel = new JLabel(name);
		spielerNameLabel.setForeground(Color.white);
		spielerNameLabel.setFont(spielerFont);
		spielerNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		spielerNameLabelHashMap.put(name, spielerNameLabel);
		spielerPanel.add(spielerNameLabel);

		// Spielerbild
		SpielerBildLabel spielerBildLabel = new SpielerBildLabel(leben, spielerFont);
		spielerBildLabelHashMap.put(name, spielerBildLabel);
		spielerPanel.add(spielerBildLabel);
	}

	/**
	 * Entfernt einen Spieler vom Panel.
	 * 
	 * @param name Der Name des zu entfernenden Spielers
	 */
	public void removeSpieler(String name) {
		// Spielername
		spielerPanel.remove(spielerNameLabelHashMap.get(name));
		spielerNameLabelHashMap.remove(name);

		// Spielerbild
		spielerPanel.remove(spielerBildLabelHashMap.get(name));
		spielerBildLabelHashMap.remove(name);
	}

	/**
	 * Entfernt alle Spieler von dem Panel.
	 */
	public void removeAlleSpieler() {
		spielerPanel.removeAll();
		spielerNameLabelHashMap.clear();
		spielerBildLabelHashMap.clear();
	}

	/**
	 * Überschreibt die Leben eines Spielers mit seinem aktuellen Wert.
	 * 
	 * @param name  Der Name des gemeinten Spielers
	 * @param Leben Seine neue Lebensanzahl
	 */
	public void updateLeben(String name, String Leben) {
		System.out.println("update " + name);
		spielerBildLabelHashMap.get(name).setText(Leben);
	}
}
