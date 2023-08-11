package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * In dieser Klasse wird ein Hornochsenbild mit den Leben eines Spielers
 * erstellt.
 * 
 * @author Moritz Rühm
 */
public class SpielerBildLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	static int zaehler = 1;
	ImageIcon hornochseIcon, hornochseIcon1 = new ImageIcon("src/gui/assets/Hornochse1.png"),
			hornochseIcon2 = new ImageIcon("src/gui/assets/Hornochse2.png"),
			hornochseIcon3 = new ImageIcon("src/gui/assets/Hornochse3.png"),
			hornochseIcon4 = new ImageIcon("src/gui/assets/Hornochse4.png"),
			hornochseIcon5 = new ImageIcon("src/gui/assets/Hornochse5.png"),
			hornochseIcon6 = new ImageIcon("src/gui/assets/Hornochse6.png"),
			hornochseIcon7 = new ImageIcon("src/gui/assets/Hornochse7.png"),
			hornochseIcon8 = new ImageIcon("src/gui/assets/Hornochse8.png"),
			hornochseIcon9 = new ImageIcon("src/gui/assets/Hornochse9.png"),
			hornochseIcon10 = new ImageIcon("src/gui/assets/Hornochse10.png");
	Image hornochsenImage;
	int scale = 130;

	/**
	 * ein farbiges Hornochsenbild wird zu einem <code>JLabel</code> hinzugefügt und
	 * die Leben als text "obendrauf" geschrieben.
	 * 
	 * @param leben Die Leben des Spielers
	 * @param font  Die Schriftart in der die Leben angezeigt wird
	 */
	public SpielerBildLabel(String leben, Font font) {
		if (zaehler == 21) {
			zaehler = 1;
		}

		switch (zaehler) {
		case 1:
			hornochsenImage = hornochseIcon1.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;
			
		case 2:
			hornochsenImage = hornochseIcon1.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;
			
		case 3:
			hornochsenImage = hornochseIcon2.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;
			
		case 4:
			hornochsenImage = hornochseIcon2.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;

		case 5:
			hornochsenImage = hornochseIcon3.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;

		case 6:
			hornochsenImage = hornochseIcon3.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;

		case 7:
			hornochsenImage = hornochseIcon4.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;

		case 8:
			hornochsenImage = hornochseIcon4.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;
			
		case 9:
			hornochsenImage = hornochseIcon5.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;
			
		case 10:
			hornochsenImage = hornochseIcon5.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;
			
		case 11:
			hornochsenImage = hornochseIcon6.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;
			
		case 12:
			hornochsenImage = hornochseIcon6.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;

		case 13:
			hornochsenImage = hornochseIcon7.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;

		case 14:
			hornochsenImage = hornochseIcon7.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;

		case 15:
			hornochsenImage = hornochseIcon8.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;

		case 16:
			hornochsenImage = hornochseIcon8.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;
			
		case 17:
			hornochsenImage = hornochseIcon9.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;
			
		case 18:
			hornochsenImage = hornochseIcon9.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;

		case 19:
			hornochsenImage = hornochseIcon10.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;

		case 20:
			hornochsenImage = hornochseIcon10.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
			break;
		default:
			break;
		}

		hornochseIcon = new ImageIcon(hornochsenImage);
		this.setIcon(hornochseIcon);
		this.setFont(font);
		this.setForeground(Color.white);
		this.setText(leben);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.CENTER);
		zaehler++;
	}
}
