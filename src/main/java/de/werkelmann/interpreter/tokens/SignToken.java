package de.werkelmann.interpreter.tokens;

public class SignToken extends Token {

	public static final String TOKEN_TYPE = "Sign";

	public SignToken(Character value) {
		this(String.valueOf(value));
	}

	public SignToken(String value) {
		super(value);
	}

	@Override
	public String getType() {
		return TOKEN_TYPE;
	}

}
