package de.werkelmann.interpreter.tokens;

public class EndOfFileToken extends Token {

	public EndOfFileToken(String value) {
		super(null);
	}

	@Override
	protected String getTokenType() {
		return "EOF";
	}

}
