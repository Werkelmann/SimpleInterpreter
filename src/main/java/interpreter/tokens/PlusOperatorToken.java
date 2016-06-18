package interpreter.tokens;

public class PlusOperatorToken extends BinaryOperatorToken {

	public PlusOperatorToken(String value) {
		super("+");
	}

	@Override
	public int calculate(int left, int right) {
		return left + right;
	}

}
