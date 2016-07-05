package de.werkelmann.interpreter.util;

public class Position {

	private final int line;
	private final int column;

	public Position(final int line, final int column) {
		this.line = line;
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}

	@Override
	public String toString() {
		return "Line: " + getLine() + " Position: " + getColumn();
	}
}
