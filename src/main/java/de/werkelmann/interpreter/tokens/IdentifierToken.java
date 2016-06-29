package de.werkelmann.interpreter.tokens;

public class IdentifierToken extends Token {

	public static final String TOKEN_TYPE = "Identifier";

	public IdentifierToken(String value) {
		super(value);
	}

	@Override
	public String getType() {
		return TOKEN_TYPE;
	}

}
