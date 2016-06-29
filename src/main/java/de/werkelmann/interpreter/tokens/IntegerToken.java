package de.werkelmann.interpreter.tokens;

public class IntegerToken extends Token {

	public static final String TOKEN_TYPE = "Integer";

	public IntegerToken(String value) {
		super(value);
	}

	@Override
	public String getType() {
		return TOKEN_TYPE;
	}

}
