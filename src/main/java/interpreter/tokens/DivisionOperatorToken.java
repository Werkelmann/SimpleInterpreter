package interpreter.tokens;

public class DivisionOperatorToken extends BinaryOperatorToken {

	public DivisionOperatorToken(String value) {
		super(value);
	}

	@Override
	public int calculate(int left, int right) {
		return left / right;
	}

}
