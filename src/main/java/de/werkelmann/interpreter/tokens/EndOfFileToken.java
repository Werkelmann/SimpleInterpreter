package de.werkelmann.interpreter.tokens;

import de.werkelmann.interpreter.util.Position;

public class EndOfFileToken extends Token {

	public final static String TOKEN_TYPE = "EOF";

	public EndOfFileToken(String value, Position position) {
		super(TOKEN_TYPE, position);
	}

	@Override
	public String getType() {
		return TOKEN_TYPE;
	}

}
