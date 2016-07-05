package de.werkelmann.interpreter.tokens;

import de.werkelmann.interpreter.util.Position;

public class SignToken extends Token {

	public static final String TOKEN_TYPE = "Sign";

	public SignToken(Character value, Position positon) {
		this(String.valueOf(value), positon);
	}

	public SignToken(String value, Position positon) {
		super(value, positon);
	}

	@Override
	public String getType() {
		return TOKEN_TYPE;
	}

}
