package de.werkelmann.interpreter.tokens;

public class BracketToken extends Token {

	public static final String TOKEN_TYPE = "Bracket";

	public BracketToken(Character value) {
		this(String.valueOf(value));
	}

	public BracketToken(String value) {
		super(value);
	}

	@Override
	public String getType() {
		return TOKEN_TYPE;
	}

}
