package de.werkelmann.interpreter.tokens;

import java.util.Optional;

import de.werkelmann.interpreter.util.Position;

public class Token {

	public static final String END_OF_FILE = "EOF";
	public static final String IDENTIFIER = "Identifier";
	public static final String OPERATOR = "OPERATOR";
	public static final String INTEGER = "Integer";
	public static final String BRACKET = "Bracket";
	public static final String SIGN = "Sign";

	private final String type;
	private final Optional<String> value;
	private final Position position;

	public Token(String type, Position position) {
		this(type, Optional.empty(), position);
	}

	public Token(String type, char value, Position position) {
		this(type, String.valueOf(value), position);
	}

	public Token(String type, String value, Position position) {
		this(type, Optional.of(value), position);
	}

	public Token(String type, Optional<String> value, Position position) {
		this.type = type;
		this.value = value;
		this.position = position;
	}

	@Override
	public String toString() {
		return "(" + getType() + ", " + getValue() + ")";
	}

	public String getType() {
		return type;
	}

	public Optional<String> getValue() {
		return value;
	}

	public Position getPosition() {
		return position;
	}

}
