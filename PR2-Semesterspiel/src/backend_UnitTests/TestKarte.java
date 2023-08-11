package backend_UnitTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import backend.Karte;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;

/**
 * Dies ist die Testklasse zur Klasse <code>Karte</code> in der JUnit basierte
 * Tests durchgeführt werden
 * 
 * @author Nicole Shiner
 * @author Cindy Tuku
 */
public class TestKarte {
	private Karte[] karten = new Karte[104];

	/**
	 * Karte.zaehler wird gestartet.
	 * Eine Schleife zur Erstellung aller Karten.
	 */
	@Before
	public void init() {
		Karte.zaehler = 1;
		for (int i = 0; i <= 103; i++) {
			try {
				karten[i] = new Karte();
			} catch (ladenSpeichernFehlgeschlagenException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * Testet, ob die ID's für die Karten richtig zugeteilt werden.
	 */
	@Test
	public void getKartenIdTest() {
		assertEquals(1, karten[0].id);
	}

	/**
	 * Testet die Ausgabe des Kartenwerts.
	 */
	@Test
	public void getKartenWertTest() {
		assertEquals(5, karten[4].wert);
	}

	/**
	 * Test zum Ausschluss gleicher IDs.
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException a
	 */
	@Ignore
	public void ausschlussGleicherID() throws ladenSpeichernFehlgeschlagenException {
		Karte karte = new Karte();
		Karte karte1 = new Karte();
		assertFalse(karte.getID() == karte1.getID());
	}

	/**
	 * Testet die Ausgabe des Hornochsenwerts.
	 */
	@Test
	public void getHornochsenTest() {
		assertTrue(1 == karten[0].getHornochsen());
	}

	/**
	 * Testet, ob die die Hornochsen, die ein Spieler verdient hat auch richtig
	 * gezaehlt werden.
	 */
	@Test
	public void berechnungHornochsenTest() {
		assertEquals(1, karten[0].getHornochsen());
		assertEquals(1, karten[1].getHornochsen());
		assertEquals(2, karten[4].getHornochsen());
		assertEquals(3, karten[9].getHornochsen());
		assertEquals(5, karten[10].getHornochsen());
		assertEquals(7, karten[54].getHornochsen());
	}
}