package frontend_UnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import backend.Spiel;
import backend.Spieler;

/**
 * Dies ist die Testklasse zur KLasse <code>SpielCLI</code> in der JUnit
 * basierte Tests durchgeführt werden.
 * 
 * @author Hendrik Tesch
 */
public class TestSpielCLI {

	Spiel spiel = new Spiel();
	Spieler spieler1;
	String name = "anna";

	/**
	 * a
	 * 
	 * @throws Exception a
	 */
	@Test
	public void testStarteSpiel() throws Exception {

	}

	/**
	 * a
	 */
	@Test
	public void testErstelleSpieler() {
	}

	/**
	 * a
	 */
	@Test
	public void testGetAusgespielt() {
	}

	/**
	 * a
	 */
	@Test
	public void testGetKartenVonSpielerAmZug() {

	}

	/**
	 * a
	 * 
	 * @throws Exception a
	 */
	@Test
	public void testSpielerHinzugefuegtException() throws Exception {
		assertEquals(0, spiel.getAlleSpieler().length);
	}

	/**
	 * a
	 * 
	 * @throws Exception a
	 */
	@Test
	public void testSpielerHinzugefuegt() throws Exception {
		spiel.addSpieler("Nami", "2");
		spiel.addSpieler("Dog", "4");
		assertNotNull(spiel.getAlleSpieler().length);
	}

	/**
	 * a
	 */
	@Test
	public void testBeendeSpiel() {
	}

	/**
	 * a
	 */
	@Test
	public void testEntferneSpieler() {
	}

	/**
	 * a
	 */
	@Test
	public void testNimmReihe() {

	}
}