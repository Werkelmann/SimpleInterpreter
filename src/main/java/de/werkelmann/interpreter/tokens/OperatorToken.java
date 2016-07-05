package de.werkelmann.interpreter.tokens;

import de.werkelmann.interpreter.util.Position;

public class OperatorToken extends Token {

	public static final String TOKEN_TYPE = "Operator";

	public OperatorToken(Character value, Position position) {
		this(String.valueOf(value), position);
	}

	public OperatorToken(String value, Position position) {
		super(value, position);
	}

	@Override
	public String getType() {
		return TOKEN_TYPE;
	}

}
