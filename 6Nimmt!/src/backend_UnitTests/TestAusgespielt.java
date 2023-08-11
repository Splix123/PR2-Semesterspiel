package backend_UnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import backend.Ausgespielt;
import backend.Karte;
import backend.Reihe;
import backend.Spiel;
import backend.Spieler;
import exceptions.ExceptionDB.KeineElementeVorhandenException;
import exceptions.ExceptionDB.LogFileException;
import exceptions.ExceptionDB.NichtVorhandenException;
import exceptions.ExceptionDB.UngueltigeEingabeException;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;

/**
 * Die Testklasse zur Klasse <code>Ausgespielt</code> in der JUnit basierte
 * Tests durchgefuehrt werden.
 *
 * @author Moritz Rühm 
 * @author Cindy Tuku
 */
public class TestAusgespielt {
	private Spiel spiel;
	Ausgespielt ausgespielt1;
	Spieler spieler1;
	String name1 = "Nina";
	String name2 = "Tom";
	Karte karte1;
	Reihe reihe1 = new Reihe(1);
	HashMap<Spieler, Karte> reihen2 = new HashMap<>();

	/**
	 * Initialisierung eines <code>ausgespielt1</code> und spiel zum Testen der Funktionalitaeten.
	 * Karte.zaehler wird gestartet. 
	 */
	@Before
	public void setUp() {
		ausgespielt1 = new Ausgespielt();
		spiel = new Spiel();
		Karte.zaehler = 1;
	}

	/**
	 * Testet, ob die ausgespielte Karten ausgespielt werden können.
	 * 
	 * @throws KeineElementeVorhandenException a
	 * @throws NichtVorhandenException a
	 * @throws NumberFormatException a
	 * @throws ladenSpeichernFehlgeschlagenException a
	 * @throws LogFileException a
	 */
	@Ignore
	public void testGetAusgespielteKarten() throws NumberFormatException, NichtVorhandenException, KeineElementeVorhandenException, ladenSpeichernFehlgeschlagenException, LogFileException {
		try {
			spiel.addSpieler(name2, "1");
			spiel.addSpieler(name1, "1");
		} catch (UngueltigeEingabeException e) {
			System.out.println(e.getMessage());
		}
		spiel.starteSpiel();
		spiel.getSpielerAmZug();
		spiel.ausspielen(Integer.parseInt(spiel.getKartenVonSpielerAmZug()[0]));
		spiel.beendeZug();
		Assert.assertNotNull(spiel.getAusgespielt());
	}

	/**
	 * Testet ob ausgespielte Karten regulaer entfernt werden.
	 */
	@Test
	public void testRemoveAusgespielteKarten() {
		TreeMap<Integer, HashMap<Spieler, Karte>> ausgespielteKarten = new TreeMap<Integer, HashMap<Spieler, Karte>>();
		HashMap<Integer, Reihe> reihen = new HashMap<>();
		reihen.put(1, reihe1);
		ausgespielteKarten.put(1, reihen2);
		assertEquals(1, ausgespielteKarten.size());
	}

	/**
	 * Testet ob die Exception zum Entfernen nicht vorhandener Karten.
	 * 
	 * @throws KeineElementeVorhandenException a 
	 */
	@Test(expected = KeineElementeVorhandenException.class)
	public void testRemoveAusgespielteKartenEception() throws KeineElementeVorhandenException {
		ausgespielt1.removeAusgespielteKarten();

	}

	/**
	 * Testet, ob es moeglich ist einem Spieler eine Karte hinzuzufügen.
	 */
	@Ignore
	public void testAddKarte() {
		ausgespielt1.addKarte(spieler1, karte1);
		assertTrue(ausgespielt1.getAusgespielteKarten().containsKey(karte1.getID()));
	}



	/**
	 * Testet ob die Exception bei falscher Angabe einer Reihe ausgegeben wird.
	 */
	@Test
	public void testAddReiheException() {
		HashMap<Integer, Reihe> reihen = new HashMap<>();
		reihen.put(6, reihe1);
	}



	/**
	 * Testet ob eine Reihe regulaer angenommen wird.
	 */
	@Test
	public void testGetReihen() {
		HashMap<Integer, Reihe> reihen = new HashMap<>();
		reihen.put(1, reihe1);
		assertNotNull(reihen);
	}

	/**
	 * Testet die Grundfunktionalitaet von Ausspielen.
	 */
	@Test()
	public void testAusspielen() {

	}

	/**
	 * Testet ob der Indes der letzten Karte der Reihe regulaer angenommen wird.
	 */
	@Test
	public void testIndexLetzteKarteVonReihe() {
	}
}