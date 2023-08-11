package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * 
 * @author Moritz Rühm
 */
public class GUI {

	private File backgroundMusic = new File("src/gui/assets/Good_Vibes.wav");
	private ImageIcon backgroundIcon = new ImageIcon("src/gui/assets/Background3.jpg");
	private ImageIcon titleIcon = new ImageIcon("src/gui/assets/Logo.png");
	private ImageIcon settingsIcon = new ImageIcon("src/gui/assets/Settings.png");
	ImageIcon NoMusicIcon = new ImageIcon("src/gui/assets/NoSound.png");
	ImageIcon MusicIcon = new ImageIcon("src/gui/assets/Sound.png");
	private ImageIcon loadIcon = new ImageIcon("src/gui/assets/Load.png");
	private ImageIcon saveIcon = new ImageIcon("src/gui/assets/Save.png");
	private ImageIcon boxIcon = new ImageIcon("src/gui/assets/Box.png");
	private ImageIcon cardIcon = new ImageIcon("src/gui/assets/cards.jpg");
	private Font textFont = new Font("Gill Sans MT", Font.BOLD, 14);
	private Color dunkelBlau = Color.decode("#002060"), schwarz = Color.black, weiss = Color.white,
			opaqueBlau = new Color(68, 114, 196, 50);
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int screenWidth = screenSize.width;
	private int screenheight = screenSize.height;
	private Dimension settingsDialogSize = new Dimension(400, 150);
	private Dimension spielBoxPanelSize = new Dimension(screenWidth / 4, screenheight / 2);
	private Dimension logoPanelSize = new Dimension(screenWidth / 2, screenheight / 4);
	private Dimension popupSize = new Dimension(screenWidth / 7, (screenheight / 15) * 3);
	private Dimension marginLeftPanelSize = new Dimension(100, 1);
	private Dimension buttonSize = new Dimension(screenWidth / 7, screenheight / 15);
	private Dimension kartenSize = new Dimension(90, 120);
	private Dimension reihenAuswahlButtonSize = new Dimension(150, 50);
	private Dimension spielerPanelSize = new Dimension(screenWidth / 4, screenheight);
	ArrayList<KartenButton> reihenKartenButtons = new ArrayList<>();
	ArrayList<JButton> reihenAuswahlButtons = new ArrayList<>();
	ArrayList<KartenButton> handKartenButtons = new ArrayList<>();
	ArrayList<KartenButton> ausgespielteKarteButtons = new ArrayList<>();
	ArrayList<ImageIcon> kartenBilder = new ArrayList<>();

	JFrame frame;
	JPanel cardPanel;
	CardLayout cardLayout;
	BackgroundPanel startPanel, gamePanel;
	JPanel startSettingsButtonPanel, gameSettingsButtonPanel, settingButtonsPanel, startLeftPanel, centerPanel, startButtonPanel, addSpielerPopup, removeSpielerPopup,
			marginLeftPanel, gameLeftPanel, ausgespieltPanel, reihenPanel, reihe, handkartenPanel;
	JDialog settingsDialog;
	SpielerScrollPanel spielerScrollPanel, spielerScrollPanel2;
	SettingsButton stopMusicButton, loadButton, saveButton;
	StartButton spielStartenButton, spielLadenButton, spielerhinzufuegenButton, spielerEntfernenButton,
			platzHalterButton, creditsButton, reihenAuswahlButton;
	JButton startZahnradButton, gameZahnradButton, hinzufuegenButton, entfernenButton,
			entfernenPlatzHalterButton;
	KartenButton kartenButton;
	JLabel boxLabel, logoLabel, namenLabel, geschlechtLabel;
	JTextField namensEingabeField;
	JComboBox<String> geschlechtEingabeField, spielerList;
	JCheckBox durchziehenBox;
	EventHandler h = new EventHandler(this);
	AudioInputStream audioInputStream;
	Clip clip;

	/**
	 * a
	 */
	public GUI() {
		frame = new JFrame();
		frame.setIconImage(titleIcon.getImage()); // Funktioniert nur auf Windows
		frame.setTitle("6Nimmt!");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		// CardPanel
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		frame.setContentPane(cardPanel);
		
		// Settings-Popup
		settingsDialog = new JDialog(frame, "Einstellungen", false); // False nochmal checken
		settingsDialog.setSize(settingsDialogSize);
		settingsDialog.setResizable(false);
		settingsDialog.setLocationRelativeTo(null);

		// Setting Buttons
		FlowLayout settingsButtonPaneLayout = new FlowLayout();
		settingsButtonPaneLayout.setHgap(30);
		settingButtonsPanel = new JPanel(settingsButtonPaneLayout);
		settingsDialog.add(settingButtonsPanel);
		stopMusicButton = new SettingsButton("Musik Stoppen", MusicIcon, h);
		settingButtonsPanel.add(stopMusicButton);
		loadButton = new SettingsButton("Laden", loadIcon,  h);
		loadButton.setVisible(false);
		settingButtonsPanel.add(loadButton);
		saveButton = new SettingsButton("Speichern", saveIcon, h);
		saveButton.setVisible(false);
		settingButtonsPanel.add(saveButton);

		// Startscreen
		startPanel = new BackgroundPanel(backgroundIcon.getImage());
		cardPanel.add(startPanel, "start");
		
		//startLeftPanel
		startLeftPanel= new JPanel(new BorderLayout());
		startLeftPanel.setOpaque(false);
		startPanel.add(startLeftPanel, BorderLayout.CENTER);

		// SettingsButtonPanel
		startSettingsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		startSettingsButtonPanel.setOpaque(false);
		startLeftPanel.add(startSettingsButtonPanel, BorderLayout.NORTH);

		// Settings Button
		startZahnradButton = new JButton(settingsIcon);
		startZahnradButton.setContentAreaFilled(false);
		startZahnradButton.setBorderPainted(false);
		startZahnradButton.addActionListener(h);
		startSettingsButtonPanel.add(startZahnradButton);

		// Spielbox
		Image boxImage = boxIcon.getImage().getScaledInstance(screenWidth / 4, screenheight / 2, Image.SCALE_DEFAULT);
		boxIcon = new ImageIcon(boxImage);
		boxLabel = new JLabel(boxIcon);
		boxLabel.setPreferredSize(spielBoxPanelSize);
		startLeftPanel.add(boxLabel, BorderLayout.WEST);

		// Center Panel
		centerPanel = new JPanel(); //Gridlayout 3,1
		centerPanel.setOpaque(false);
		startLeftPanel.add(centerPanel, BorderLayout.CENTER);

		// Logo
		Image titleImage = titleIcon.getImage().getScaledInstance(screenWidth / 2, screenheight / 2 + screenheight / 4,
				Image.SCALE_DEFAULT);
		titleIcon = new ImageIcon(titleImage);
		logoLabel = new JLabel(titleIcon);
		logoLabel.setPreferredSize(logoPanelSize);
		centerPanel.add(logoLabel);

		// Startbuttons
		startButtonPanel = new JPanel(new GridLayout(6, 1, 0, 3));


		spielStartenButton = new StartButton("Spiel Starten", textFont, weiss, opaqueBlau, dunkelBlau, buttonSize, h);
		startButtonPanel.add(spielStartenButton);
		spielLadenButton = new StartButton("Spiel laden", textFont, weiss, opaqueBlau, dunkelBlau, buttonSize, h);
		startButtonPanel.add(spielLadenButton);
		spielerhinzufuegenButton = new StartButton("Spieler hinzufügen", textFont, weiss, opaqueBlau, dunkelBlau,
				buttonSize, h);
		startButtonPanel.add(spielerhinzufuegenButton);
		spielerEntfernenButton = new StartButton("Spieler entfernen", textFont, weiss, opaqueBlau, dunkelBlau,
				buttonSize, h);
		startButtonPanel.add(spielerEntfernenButton);
		platzHalterButton = new StartButton("", textFont, weiss, opaqueBlau, dunkelBlau, buttonSize, h);
		platzHalterButton.setVisible(false);
		startButtonPanel.add(platzHalterButton);
		creditsButton = new StartButton("Credits", textFont, weiss, opaqueBlau, dunkelBlau, buttonSize, h);
		startButtonPanel.add(creditsButton);

		startButtonPanel.setOpaque(false);
		centerPanel.add(startButtonPanel);
		
		// Spieler hinzufügen-popup
		addSpielerPopup = new JPanel(new GridLayout(6, 1));
		addSpielerPopup.setBorder(new LineBorder(dunkelBlau, 3, true));
		addSpielerPopup.setPreferredSize(popupSize);
		addSpielerPopup.setOpaque(false);
		addSpielerPopup.setVisible(false);
		centerPanel.add(addSpielerPopup);

		// Namenseingabe
		namenLabel = new JLabel("Name:");
		namenLabel.setFont(textFont);
		namenLabel.setForeground(dunkelBlau);
		addSpielerPopup.add(namenLabel);

		namensEingabeField = new JTextField();
		namensEingabeField.setBorder(new LineBorder(schwarz, 1, true));
		namensEingabeField.addActionListener(h);
		addSpielerPopup.add(namensEingabeField);

		// Geschlechtseingabe
		geschlechtLabel = new JLabel("Geschlecht:");
		geschlechtLabel.setFont(textFont);
		geschlechtLabel.setForeground(dunkelBlau);
		geschlechtLabel.setEnabled(false);
		addSpielerPopup.add(geschlechtLabel);

		String[] geschlecht = { "Männlich", "Weiblich", "Divers", "KI" };
		geschlechtEingabeField = new JComboBox<>(geschlecht);
		geschlechtEingabeField.setEnabled(false);
		geschlechtEingabeField.addActionListener(h);
		addSpielerPopup.add(geschlechtEingabeField);

		// Hinzufügen-Button
		hinzufuegenButton = new JButton("Hinzufügen");
		hinzufuegenButton.setEnabled(false);
		hinzufuegenButton.addActionListener(h);
		addSpielerPopup.add(hinzufuegenButton);

		// Spieler entfernen-popup
		removeSpielerPopup = new JPanel(new GridLayout(3, 1));
		removeSpielerPopup.setBorder(new LineBorder(dunkelBlau, 3, true));
		removeSpielerPopup.setPreferredSize(popupSize);
		removeSpielerPopup.setOpaque(false);
		removeSpielerPopup.setVisible(false);
		centerPanel.add(removeSpielerPopup);

		// Entfernen Spielerliste
		spielerList = new JComboBox<>();
		spielerList.addActionListener(h);
		removeSpielerPopup.add(spielerList);

		// Platzhalter
		entfernenPlatzHalterButton = new JButton();
		entfernenPlatzHalterButton.setVisible(false);
		removeSpielerPopup.add(entfernenPlatzHalterButton);

		// Entfernen-Button
		entfernenButton = new JButton("Entfernen");
		entfernenButton.setEnabled(false);
		entfernenButton.addActionListener(h);
		removeSpielerPopup.add(entfernenButton);

		// Spielerpanel
		spielerScrollPanel = new SpielerScrollPanel(spielerPanelSize);
		startPanel.add(spielerScrollPanel, BorderLayout.EAST);

		
		
		
		
		
		// Gamescreen
		gamePanel = new BackgroundPanel(backgroundIcon.getImage());
		cardPanel.add(gamePanel, "game");

		// Kartenbilder
		BufferedImage cardBufferedImage = new BufferedImage(cardIcon.getIconWidth(), cardIcon.getIconHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = cardBufferedImage.createGraphics();
		cardIcon.paintIcon(null, graphics, 0, 0);
		graphics.dispose();
		for (int i = 0; i < 1122; i += 102) {
			for (int j = 0; j < 660; j += 66) {
				Image scaled = cardBufferedImage.getSubimage(j, i, 66, 102);
				cardIcon = new ImageIcon(
						scaled.getScaledInstance(kartenSize.width, kartenSize.height, Image.SCALE_DEFAULT));
				kartenBilder.add(cardIcon);
			}
		}

		// Margin left Panel
		marginLeftPanel = new JPanel();
		marginLeftPanel.setPreferredSize(marginLeftPanelSize);
		marginLeftPanel.setOpaque(false);
		gamePanel.add(marginLeftPanel, BorderLayout.WEST);

		// SettingsButtonPanel
		gameSettingsButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		gameSettingsButtonPanel.setOpaque(false);
		marginLeftPanel.add(gameSettingsButtonPanel);

		// Settings Button
		gameZahnradButton = new JButton(settingsIcon);
		gameZahnradButton.setContentAreaFilled(false);
		gameZahnradButton.setBorderPainted(false);
		gameZahnradButton.addActionListener(h);
		gameSettingsButtonPanel.add(gameZahnradButton);
		
		// Left Panel
		gameLeftPanel = new JPanel(new BorderLayout());
		gameLeftPanel.setOpaque(false);
		gamePanel.add(gameLeftPanel, BorderLayout.CENTER);

		// Reihenpanel
		reihenPanel = new JPanel();
		reihenPanel.setLayout(new BoxLayout(reihenPanel, BoxLayout.Y_AXIS));
		reihenPanel.setOpaque(false);
		gameLeftPanel.add(reihenPanel, BorderLayout.CENTER);

		// Reihenkarten + Auswahlbuttons
		for (int i = 0; i < 4; i++) {
			reihe = new JPanel(new FlowLayout(FlowLayout.LEFT));
			reihe.setOpaque(false);
			for (int j = 0; j < 5; j++) {
				kartenButton = new KartenButton(schwarz, kartenSize, h);
				reihenKartenButtons.add(kartenButton);
				reihe.add(kartenButton);
			}
			reihenAuswahlButton = new StartButton("Auswählen", textFont, weiss, opaqueBlau, dunkelBlau,
					reihenAuswahlButtonSize, h);
			reihenAuswahlButton.setVisible(false);
			reihenAuswahlButtons.add(reihenAuswahlButton);
			reihe.add(reihenAuswahlButton);
			reihenPanel.add(reihe);
		}

		// Handkartenpanel
		handkartenPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		handkartenPanel.setOpaque(false);
		gameLeftPanel.add(handkartenPanel, BorderLayout.SOUTH);

		// Handkarten
		for (int i = 0; i < 10; i++) {
			kartenButton = new KartenButton(schwarz, kartenSize, h);
			kartenButton.setVisible(false);
			handKartenButtons.add(kartenButton);
			handkartenPanel.add(kartenButton);
			
		}

		// Spielerpanel
		spielerScrollPanel2 = new SpielerScrollPanel(spielerPanelSize);
		gamePanel.add(spielerScrollPanel2, BorderLayout.EAST);

		try {
			audioInputStream = AudioSystem.getAudioInputStream(backgroundMusic.getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Der Song konnte nicht gefunden werden", "Fehler!",
					JOptionPane.ERROR_MESSAGE);
		}
//		cardLayout.next(cardPanel); //nur für testing
		frame.setVisible(true);
	}
}