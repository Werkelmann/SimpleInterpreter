package interpreter.tokens;

public class BinaryOperatorToken extends Token {

	public static final String TOKEN_TYPE = "Operator";

	public BinaryOperatorToken(String value) {
		super(value);
	}

	@Override
	protected String getTokenType() {
		return TOKEN_TYPE;
	}

}
