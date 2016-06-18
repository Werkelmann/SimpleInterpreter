package interpreter;

import java.text.ParseException;

import interpreter.tokens.BinaryOperatorToken;
import interpreter.tokens.IntegerToken;
import interpreter.tokens.Token;

public class Interpreter {

	private Token currentToken;
	private Scanner scanner;

	public Interpreter() {
		this.scanner = new Scanner();
	}

	private void eat(String expected) throws ParseException {
		if (currentToken.getType().equals(expected)) {
			currentToken = scanner.getNextToken();
		} else {
			throwException();
		}
	}

	public int expr(String text) throws ParseException {
		scanner.init(text);
		currentToken = scanner.getNextToken();

		int left = Integer.parseInt(currentToken.getValue());
		this.eat(IntegerToken.TOKEN_TYPE);

		BinaryOperatorToken op = (BinaryOperatorToken) currentToken;
		this.eat(BinaryOperatorToken.TOKEN_TYPE);

		int right = Integer.parseInt(currentToken.getValue());
		this.eat(IntegerToken.TOKEN_TYPE);

		return calculate(left, op, right);
	}

	private int calculate(int left, BinaryOperatorToken op, int right) throws ParseException {
		return op.calculate(left, right);
	}

	private void throwException() throws ParseException {
		throw new ParseException("Invalid input", 0);
	}

}
