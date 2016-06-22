package de.werkelmann.interpreter.tokens;

public class IdentifierToken extends Token {

	public static final String TOKEN_TYPE = "Identifier";

	public IdentifierToken(String value) {
		super(value);
	}

	@Override
	protected String getTokenType() {
		return TOKEN_TYPE;
	}

}
