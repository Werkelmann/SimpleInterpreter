package interpreter;

import java.text.ParseException;

import interpreter.tokens.BinaryOperatorToken;
import interpreter.tokens.IntegerToken;
import interpreter.tokens.Token;
import interpreter.tokens.TokenList;

public class Parser {

	private TokenList tokens;
	private Token currentToken;

	public int parse(TokenList tokens) throws ParseException {
		this.tokens = tokens;
		this.currentToken = tokens.getNextToken();

		try {
			return expr();
		} catch (NumberFormatException e) {
			throw new ParseException("Failure at position " + tokens.getPosition(), tokens.getPosition());
		}
	}

	private void eat(String expected) throws ParseException {
		if (currentToken.getType().equals(expected)) {
			currentToken = tokens.getNextToken();
		} else {
			throw new ParseException(
					"Wrong syntax at " + tokens.getPosition() + ". Found: " + currentToken + " Expected: " + expected,
					tokens.getPosition());
		}
	}

	public int expr() throws ParseException {
		int result = term();
		while (isExprOperator(currentToken.getValue())) {
			BinaryOperatorToken op = (BinaryOperatorToken) currentToken;
			this.eat(BinaryOperatorToken.TOKEN_TYPE);

			result = calculate(result, op, term());
		}

		return result;
	}

	private boolean isExprOperator(String value) {
		return (value != null && (value.equals("+") || value.equals("-")));
	}

	public int term() throws ParseException {
		int result = factor();
		while (isTermOperator(currentToken.getValue())) {
			BinaryOperatorToken op = (BinaryOperatorToken) currentToken;
			this.eat(BinaryOperatorToken.TOKEN_TYPE);

			result = calculate(result, op, factor());
		}

		return result;
	}

	private boolean isTermOperator(String value) {
		return (value != null && (value.equals("*") || value.equals("/")));
	}

	public int factor() throws ParseException {
		int result = Integer.parseInt(currentToken.getValue());
		this.eat(IntegerToken.TOKEN_TYPE);
		return result;
	}

	private int calculate(int left, BinaryOperatorToken op, int right) throws ParseException {
		return op.calculate(left, right);
	}
}
