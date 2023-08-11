package backend;

import java.io.Serializable;
import java.util.ArrayList;

import exceptions.ExceptionDB.NichtVorhandenException;

/**
 * Diese Klasse enthält alle Methoden um eine generische Liste zu erstellen. Die
 * einzelnen Objekte werden als <code>ListNode</code>'s abgespeichert und über
 * den ersten Knoten aufgerufen.
 * 
 * @author Nicole Shiner
 * @param <T> Steht für den generischen Objekttyp <i>Type</i>
 */
public class List<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** Instanz des generischen Anfangsknoten */
	ListNode<T> head;
	/** Länge der Liste */
	int length;

	/**
	 * Default Konstruktor der den head als null initialisiert.
	 */
	public List() {
		this.head = null;
	}

	/**
	 * Fügt einen Knoten am Anfang der Liste hinzu.
	 * 
	 * @param data Daten des Knoten
	 */
	public void addFirst(T data) {
		if (data != null) {
			ListNode<T> n = new ListNode<>(data, null);
			if (this.head == null) {
				head = n;
			} else {
				ListNode<T> temp = head;
				head = n;
				head.setNext(temp);
			}
			length++;
		}
	}

	/**
	 * Fügt ein Objekt zur List hinzu. Das Objekt wird in einer über HashCode
	 * sortierten Liste an der richtigen Stelle hinzugefügt.
	 * 
	 * @param data Objekt das hinzugefügt werden soll
	 */
	public void insert(T data) {
		if (data != null) {
			ListNode<T> newNode = new ListNode<>(data, null);
			ListNode<T> n = head;
			if (head == null) {
				head = newNode;
			} else {
				if (newNode.getData().hashCode() < head.getData().hashCode()) {
					head = newNode;
					head.setNext(n);
				} else {
					ListNode<T> temp = null;
					while (n != null && n.getData().hashCode() < newNode.getData().hashCode()) {
						temp = n;
						n = temp.getNext();
					}
					newNode.setNext(n);
					temp.setNext(newNode);
				}
			}
			length++;
		}
	}

	/**
	 * Entfernt den Knoten aus der Liste.
	 * 
	 * @param data der zu entfernende Typ
	 * @throws NichtVorhandenException Wird geworfen, wenn die zu entfernende Karte
	 *                                 in den Handkarten des Spielers nicht
	 *                                 existiert
	 */
	public void remove(T data) throws NichtVorhandenException {
		if (data != null) {
			boolean containsKey = false;
			ListNode<T> temp = head;
			if (head.getData().equals(data)) {
				head = head.getNext();
				containsKey = true;
			}
			while (temp.getNext() != null) {
				if (temp.getNext().getData().equals(data)) {
					containsKey = true;
					if (temp.getNext().getNext() != null) {
						temp.setNext(temp.getNext().getNext());
					} else {
						temp.setNext(null);
					}
				} else {
					temp = temp.getNext();
				}
			}
			if (containsKey == false) {
				throw new NichtVorhandenException("Diese Karte existiert nicht! Wähle eine andere aus.");
			}
			if (containsKey == true) {
				length--;
			}

		}
	}

	/**
	 * entfernt alle Knoten der Liste und setzt den Anfangsknoten auf null.
	 */
	public void clear() {
		head = null;
	}

	/**
	 * Überprüft ob die Liste leer ist.
	 * 
	 * @return Boolean ob die Liste leer ist
	 */
	public boolean isEmpty() {
		return (head == null);
	}

	/**
	 * Gibt die Länge der Liste zurück.
	 * 
	 * @return Die Anzahl der Knoten
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Gibt den Ersten Knoten einer Liste zurück.
	 * 
	 * @return Der erste Knoten
	 */
	public T getFirst() {
		if (head != null) {
			return head.getData();
		} else {
			return null;
		}
	}

	/**
	 * Gibt alle Knoten der Liste zurück.
	 * 
	 * @return eine ArrayList des Typs Objekt
	 */
	public ArrayList<T> getAll() {
		ArrayList<T> list = new ArrayList<T>();
		int num = this.getLength();
		if (num == 0) {
			return list;
		}
		ListNode<T> n = head;
		while (n != null) {
			list.add(n.getData());
			n = n.getNext();
		}
		return list;
	}

	/**
	 * Überschreibt die toString-Methode für eine angepasste Konsolenausgabe.
	 */
	public String toString() {
		String output = "";
		ListNode<T> n = head;
		if (n == null) {
			return "{}";
		}
		while (n.getNext() != null) {
			output += n.getData() + ", ";
			n = n.getNext();
		}
		output += n.getData();
		return output;
	}
}