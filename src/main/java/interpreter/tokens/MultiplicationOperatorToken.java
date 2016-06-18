package interpreter.tokens;

public class MultiplicationOperatorToken extends BinaryOperatorToken {

	public MultiplicationOperatorToken(String value) {
		super(value);
	}

	@Override
	public int calculate(int left, int right) {
		return left * right;
	}

}
