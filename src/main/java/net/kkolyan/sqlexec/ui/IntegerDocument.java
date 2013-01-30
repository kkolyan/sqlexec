package net.kkolyan.sqlexec.ui;

import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;

public class IntegerDocument implements Document {

    public static void apply(JTextComponent component) {
        IntegerDocument integerDocument = new IntegerDocument(component.getDocument());
        component.setDocument(integerDocument);
    }

	private Document document;

	private IntegerDocument(Document document) {
		this.document = document;
	}

	@Override
	public void addDocumentListener(DocumentListener listener) {
		document.addDocumentListener(listener);
	}

	@Override
	public void addUndoableEditListener(UndoableEditListener listener) {
		document.addUndoableEditListener(listener);
	}

	@Override
	public Position createPosition(int offs) throws BadLocationException {
		return document.createPosition(offs);
	}

	@Override
	public Element getDefaultRootElement() {
		return document.getDefaultRootElement();
	}

	@Override
	public Position getEndPosition() {
		return document.getEndPosition();
	}

	@Override
	public int getLength() {
		return document.getLength();
	}

	@Override
	public Object getProperty(Object key) {
		return document.getProperty(key);
	}

	@Override
	public Element[] getRootElements() {
		return document.getRootElements();
	}

	@Override
	public Position getStartPosition() {
		return document.getStartPosition();
	}

	@Override
	public String getText(int offset, int length) throws BadLocationException {
		return document.getText(offset, length);
	}

	@Override
	public void getText(int offset, int length, Segment txt) throws BadLocationException {
		document.getText(offset, length, txt);
	}

	@Override
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
			if (str == null) {
				return;
			}
			String oldString = getText(0, getLength());
			String newString = oldString.substring(0, offset) + str
					+ oldString.substring(offset);
			try {
				Integer.parseInt(newString + "0");
				while (offset == 0 && str.startsWith("0")) {
					str = str.substring(1);
				}
				if (str.isEmpty() && getLength() == 0) {
					str = "0";
				}
				document.insertString(offset, str, a);
			} catch (NumberFormatException e) {
				//
			}
	}

	@Override
	public void putProperty(Object key, Object value) {
		document.putProperty(key, value);
	}

	@Override
	public void remove(int offs, int len) throws BadLocationException {
		document.remove(offs, len);
		if (getLength() == 0) {
//			document.insertString(0, "0", SimpleAttributeSet.EMPTY);
		}
	}

	@Override
	public void removeDocumentListener(DocumentListener listener) {
		document.removeDocumentListener(listener);
	}

	@Override
	public void removeUndoableEditListener(UndoableEditListener listener) {
		document.removeUndoableEditListener(listener);
	}

	@Override
	public void render(Runnable r) {
		document.render(r);
	}
}
