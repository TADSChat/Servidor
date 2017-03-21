package br.univel.view;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Campo baseado em um JTextField que permite a inserção apenas de numeros
 * @author dread
 *
 */
public class JNumberField extends JTextField {
	public JNumberField() {
		this(null);
	}

	/**
	 * Receve um texto e valida a inserção caracter a caracter
	 * @param text
	 */
	public JNumberField(String text) {
		super(text);
		setDocument(new PlainDocument() {
			@Override
			public void insertString(int offs, String string, AttributeSet attribute) throws BadLocationException {
				for (int i = 0; i < string.length(); i++) {
					if (Character.isDigit(string.charAt(i)) == false)
						return;
				}
				super.insertString(offs, string, attribute);
			}
		});
	}
}