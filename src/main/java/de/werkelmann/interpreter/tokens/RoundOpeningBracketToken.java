package de.werkelmann.interpreter.tokens;

public class RoundOpeningBracketToken extends BracketToken {

	public static final String TOKEN_TYPE = "RoundOpeningBracket";

	public RoundOpeningBracketToken(String value) {
		super("(");
	}

	@Override
	protected String getTokenType() {
		return TOKEN_TYPE;
	}

}
