package interpreter.tokens;

public class SubtractionOperatorToken extends BinaryOperatorToken {

	public SubtractionOperatorToken(String value) {
		super("-");
	}

	@Override
	public int calculate(int left, int right) {
		return left - right;
	}

}
