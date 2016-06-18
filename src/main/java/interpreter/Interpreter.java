package interpreter;

import java.text.ParseException;

public class Interpreter {

	private Token currentToken;
	private Scanner scanner;

	public Interpreter() {
		this.scanner = new Scanner();
	}

	private void eat(String tokenType) throws ParseException {
		if (currentToken.getType().equals(tokenType)) {
			currentToken = scanner.getNextToken();
		} else {
			throwException();
		}
	}

	public int expr(String text) throws ParseException {
		scanner.init(text);
		currentToken = scanner.getNextToken();

		int left = Integer.parseInt(currentToken.getValue());
		this.eat(Token.INTEGER);

		String op = currentToken.getValue();
		this.eat(Token.OPERATOR);

		int right = Integer.parseInt(currentToken.getValue());
		this.eat(Token.INTEGER);

		return calculate(left, op, right);
	}

	private Integer calculate(int left, String op, int right) throws ParseException {
		switch (op) {
		case ("+"):
			return left + right;
		case ("-"):
			return left - right;
		default:
			throwException();
			return null;
		}
	}

	private void throwException() throws ParseException {
		throw new ParseException("Invalid input", 0);
	}

}
