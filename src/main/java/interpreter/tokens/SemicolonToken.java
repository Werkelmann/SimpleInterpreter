package interpreter.tokens;

public class SemicolonToken extends Token {

	public static final String TOKEN_TYPE = "Semikolon";

	public SemicolonToken(String value) {
		super(null);
	}

	@Override
	protected String getTokenType() {
		return TOKEN_TYPE;
	}

}
