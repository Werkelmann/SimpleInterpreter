package interpreter.tokens;

public class SemikolonToken extends Token {

	public static final String TOKEN_TYPE = "Semikolon";

	public SemikolonToken(String value) {
		super(null);
	}

	@Override
	protected String getTokenType() {
		return TOKEN_TYPE;
	}

}
