package de.werkelmann.interpreter.tokens;

public abstract class Token {

	private final String value;
	protected final String type;

	public Token(Character value) {
		this(String.valueOf(value));
	}

	public Token(String value) {
		this.value = value;
		this.type = getType();
	}

	@Override
	public String toString() {
		return "(" + getType() + ", " + getValue() + ")";
	}

	public abstract String getType();

	public String getValue() {
		return value;
	}

}
