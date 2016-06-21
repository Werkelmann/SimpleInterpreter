package interpreter.tokens;

public class DotToken extends Token {

	public static final String TOKEN_TYPE = "Dot";

	public DotToken(String value) {
		super(null);
	}

	@Override
	protected String getTokenType() {
		return TOKEN_TYPE;
	}

}
