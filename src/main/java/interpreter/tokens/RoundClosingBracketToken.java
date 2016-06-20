package interpreter.tokens;

public class RoundClosingBracketToken extends BracketToken {

	public static final String TOKEN_TYPE = "RoundClosingBracket";

	public RoundClosingBracketToken(String value) {
		super(")");
	}

	@Override
	protected String getTokenType() {
		return TOKEN_TYPE;
	}

}
