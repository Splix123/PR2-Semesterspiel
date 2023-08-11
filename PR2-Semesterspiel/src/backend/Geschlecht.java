package backend;

import java.io.Serializable;

/**
 * Diese Enum KLasse ist zur Bestimmung des Geschlechts (kann auch KI sein)
 * eines Spielers da.
 * 
 * @author Hendrik Tesch
 */
public enum Geschlecht  implements Serializable {
	/**
	 * Beschreibt das Männliche Geschlecht des Spielers
	 */
	m,

	/**
	 * Beschreibt das Weibliche Geschlecht
	 */
	w,

	/**
	 * Beschreibt ein Diverses Geschlecht
	 */
	d,

	/**
	 * Beschreibt eine KI
	 */
	ki;
}