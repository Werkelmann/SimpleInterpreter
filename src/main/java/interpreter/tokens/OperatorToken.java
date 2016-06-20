package interpreter.tokens;

public class OperatorToken extends Token {

	public static final String TOKEN_TYPE = "Operator";

	public OperatorToken(String value) {
		super(value);
	}

	@Override
	protected String getTokenType() {
		return TOKEN_TYPE;
	}

}
