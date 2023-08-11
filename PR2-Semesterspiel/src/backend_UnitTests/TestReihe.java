package backend_UnitTests;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import backend.Karte;
import backend.Reihe;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;

/**
 * Dies ist die Testklasse zur KLasse <code>Reihe</code> in der JUnit basierte
 * Tests durchgefuehrt werden.
 * 
 * @author Nicole Shiner
 * @author Cindy Tuku
 */
public class TestReihe {
	Reihe reihe1 = new Reihe(1);

	/**
	 * Karte.zaehler wird gestartet.
	 * Ein Objekt von Reihe wird initialisert. 
	 */
	@Before
	public void setUp() {
		reihe1 = new Reihe(1);
	}

	/**
	 * Testet, ob Reihen erstellt werden koennen
	 */
	@Test
	public void getReiheTest() {
		Assert.assertNotNull(reihe1.getReihe());
	}

	/**
	 * Testet, die korrekte ID Vergabe fuer die Reihen
	 */
	@Test
	public void getIdTest() {
		Assert.assertEquals(1, reihe1.getID());
	}

	/**
	 * Testet ob eine Karte richtig hinzugefuegt wurde.
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException a
	 */
	@Test
	public void karteHinzufuegenTest() throws ladenSpeichernFehlgeschlagenException {
		Karte karte1 = new Karte();
		reihe1.karteHinzufuegen(karte1);
		Assert.assertTrue(karte1 == reihe1.getReihe()[0]);
		Assert.assertNotNull(reihe1.getReihe()[0]);
	}

	/**
	 * Testet ob die Anzahl der Hornochsen orndungsgemaess ausgegeben wird.
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException a
	 */
	@Ignore
	public void hornachsenSummeTest() throws ladenSpeichernFehlgeschlagenException {
		int summe = 0;
		Karte karte = new Karte();
		Karte karte1 = new Karte();
		Karte karte2 = new Karte();
		reihe1.karteHinzufuegen(karte1);
		reihe1.karteHinzufuegen(karte2);
		reihe1.karteHinzufuegen(karte);
		if (reihe1 != null) {
			System.out.println("here");
			for (int i = 0; i < reihe1.getReihe().length - 1; i++) {
				if (reihe1.getReihe()[i] != null) {
					System.out.println(reihe1.getReihe()[i]);
					summe += reihe1.getReihe()[i].getHornochsen();
				}
			}
		}
		assertTrue(3 == summe);
	}

	/**
	 * Testet ob die Karte eines Stapels entfernt wurde.
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException a
	 */
	@Test
	public void karteEntfernenTest() throws ladenSpeichernFehlgeschlagenException {
		Karte karte1 = new Karte();
		Karte karte2 = new Karte();
		reihe1.karteHinzufuegen(karte1);
		reihe1.karteHinzufuegen(karte2);
		reihe1.kartenEntfernen();
		Assert.assertNull(reihe1.getReihe()[0]);
	}
}