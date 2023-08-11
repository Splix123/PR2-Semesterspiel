package backend_UnitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;

import backend.Geschlecht;
import backend.Karte;
import backend.Spiel;
import backend.Spieler;
import exceptions.ExceptionDB.UngueltigeEingabeException;

/**
 * Dies ist die Testklasse zur KLasse <code>Spieler</code> in der JUnit basierte
 * Tests durchgeführt werden.
 * 
 * @author Nicole Shiner
 * @author Rodez Tazo
 * @author Cindy Tuku
 */
public class TestSpieler {
	protected Spiel spiel;
	String name1 = "Frank";
	String name2 = "Nina";
	String name3 = "Sam";
	String name4 = "KI";
	Spieler spieler1;
	Spieler spieler2;
	Spieler spieler3;
	Spieler spieler4;
	Spieler spielertest;
	Karte karte1;
	Karte karte2;
	
	/**
	 * Initialisierung eines Spiels und erstellen einiger Spieler zum Testen.
	 * 
	 * @throws UngueltigeEingabeException a
	 */
	@Before
	public void setUp() throws UngueltigeEingabeException {
		spiel = new Spiel();
		spieler1 = new Spieler(name1, "1", spiel);
		spieler2 = new Spieler(name2, "2", spiel);
		spieler3 = new Spieler(name3, "3", spiel);
		spieler4 = new Spieler(name4, "4", spiel);
		spieler1.karteHinzufuegen(karte1);
	}

	/**
	 * Testet, ob den Spielern die richtigen Namen zugewiesen wurde.
	 */
	@Ignore
	public void spielerNameTest() {
		assertEquals("Frank", spieler1.getName());
		assertEquals("Nina", spieler2.getName());
		assertEquals("Sam", spieler3.getName());
		assertEquals("KI", spieler4.getName());
	}

	/**
	 * Testet, ob das Geschlecht der angelegten Spieler auf die korrekten Werte
	 * gesetzt wurde.
	 * 
	 * @throws UngueltigeEingabeException a
	 */
	@Ignore
	public void geschlechtsTest() throws UngueltigeEingabeException {
		assertEquals(Geschlecht.m, spieler1.getGeschlecht());
		assertEquals(Geschlecht.w, spieler2.getGeschlecht());
		assertEquals(Geschlecht.d, spieler3.getGeschlecht());
		assertEquals(Geschlecht.ki, spieler4.getGeschlecht());
	}

//	/**
//	 * Test ob eine Exception bei der Wahl eines nicht existierenden Geschlechts.
//	 * 
//	 * @throws UngueltigeEingabeException a
//	 */
//	@Test(expected = UngueltigeEingabeException.class)
//	public void geschlechtsTestException() throws UngueltigeEingabeException {
//		spielertest = new Spieler("Versuch", "5", spiel);
//		assertEquals(Geschlecht.m, spielertest.getGeschlecht());
//	}

	/**
	 * Testet, ob die Variable <code>leben</code> jedes Spieler funktioniert.
	 */

	@Ignore
	public void getLebenTest() {
		assertEquals(66, spieler1.getLeben());
	}

	/**
	 * Testet, ob es moeglich ist die Leben eines Spielers zu verändern.
	 */
	@Ignore
	public void setLebenTest() {
		spieler1.setLeben(10);
		assertEquals(56, spieler1.getLeben());
	}

}

