package de.werkelmann.interpreter.tokens;

import de.werkelmann.interpreter.util.Position;

public abstract class Token {

	private final String value;
	protected final String type;
	private final Position position;

	public Token(Character value, Position position) {
		this(String.valueOf(value), position);
	}

	public Token(String value, Position position) {
		this.value = value;
		this.type = getType();
		this.position = position;
	}

	@Override
	public String toString() {
		return "(" + getType() + ", " + getValue() + ")";
	}

	public abstract String getType();

	public String getValue() {
		return value;
	}

	public Position getPosition() {
		return position;
	}

}
