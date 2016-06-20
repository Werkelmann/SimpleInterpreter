package interpreter.tokens;

public class IntegerToken extends Token {

	public static final String TOKEN_TYPE = "Integer";

	public IntegerToken(String value) {
		super(value);
	}

	@Override
	protected String getTokenType() {
		return TOKEN_TYPE;
	}

}
