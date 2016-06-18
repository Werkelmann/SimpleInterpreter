package interpreter.tokens;

public class AdditionOperatorToken extends BinaryOperatorToken {

	public AdditionOperatorToken(String value) {
		super("+");
	}

	@Override
	public int calculate(int left, int right) {
		return left + right;
	}

}
