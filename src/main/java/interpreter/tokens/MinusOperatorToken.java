package interpreter.tokens;

public class MinusOperatorToken extends BinaryOperatorToken {

	public MinusOperatorToken(String value) {
		super("-");
	}

	@Override
	public int calculate(int left, int right) {
		return left - right;
	}

}
