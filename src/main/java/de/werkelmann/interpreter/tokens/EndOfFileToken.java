package de.werkelmann.interpreter.tokens;

public class EndOfFileToken extends Token {

	private final static String TOKEN_TYPE = "EOF";

	public EndOfFileToken(String value) {
		super(TOKEN_TYPE);
	}

	@Override
	public String getType() {
		return TOKEN_TYPE;
	}

}
