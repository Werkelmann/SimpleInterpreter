package interpreter.tokens;

public abstract class BinaryOperatorToken extends Token {

	public static final String TOKEN_TYPE = "Operator";

	public BinaryOperatorToken(String value) {
		super(value);
	}

	@Override
	protected String getTokenType() {
		return TOKEN_TYPE;
	}

	public abstract int calculate(int left, int right);

	public static Token create(Character character) {
		switch (character) {
		case ('+'):
			return new PlusOperatorToken(null);
		case ('-'):
			return new MinusOperatorToken(null);
		}
		throw new IllegalArgumentException(character + " is not a valid operator");
	}

}
