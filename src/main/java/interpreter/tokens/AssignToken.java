package interpreter.tokens;

public class AssignToken extends Token {

	public static final String TOKEN_TYPE = "Assign";

	public AssignToken(String value) {
		super(null);
	}

	@Override
	protected String getTokenType() {
		return TOKEN_TYPE;
	}

}
