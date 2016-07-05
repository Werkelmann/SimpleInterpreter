package de.werkelmann.interpreter.tokens;

import de.werkelmann.interpreter.util.Position;

public class BracketToken extends Token {

	public static final String TOKEN_TYPE = "Bracket";

	public BracketToken(Character value, Position position) {
		this(String.valueOf(value), position);
	}

	public BracketToken(String value, Position position) {
		super(value, position);
	}

	@Override
	public String getType() {
		return TOKEN_TYPE;
	}

}
