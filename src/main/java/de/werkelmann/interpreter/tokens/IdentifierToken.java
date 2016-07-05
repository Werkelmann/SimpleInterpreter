package de.werkelmann.interpreter.tokens;

import de.werkelmann.interpreter.util.Position;

public class IdentifierToken extends Token {

	public static final String TOKEN_TYPE = "Identifier";

	public IdentifierToken(String value, Position position) {
		super(value, position);
	}

	@Override
	public String getType() {
		return TOKEN_TYPE;
	}

}
