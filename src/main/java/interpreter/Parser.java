package interpreter;

import java.text.ParseException;

import interpreter.tokens.BinaryOperatorToken;
import interpreter.tokens.EndOfFileToken;
import interpreter.tokens.IntegerToken;
import interpreter.tokens.Token;
import interpreter.tokens.TokenList;

public class Parser {

	private int position;
	private TokenList tokens;
	private Token currentToken;

	public int parse(TokenList tokens) throws ParseException {
		this.tokens = tokens;
		this.setPosition(0);
		this.currentToken = tokens.getNextToken();

		try {
			return expr();
		} catch (NumberFormatException e) {
			throw new ParseException("Failure at position " + position, position);
		}
	}

	private void eat(String expected) throws ParseException {
		if (currentToken.getType().equals(expected)) {
			currentToken = tokens.getNextToken();
		} else {
			throwException();
		}
	}

	public int term() throws ParseException {
		int result = Integer.parseInt(currentToken.getValue());
		this.eat(IntegerToken.TOKEN_TYPE);
		return result;
	}

	public int expr() throws ParseException {
		int result = term();
		while (!(currentToken instanceof EndOfFileToken)) {
			BinaryOperatorToken op = (BinaryOperatorToken) currentToken;
			this.eat(BinaryOperatorToken.TOKEN_TYPE);

			result = calculate(result, op, term());
		}

		return result;
	}

	private int calculate(int left, BinaryOperatorToken op, int right) throws ParseException {
		return op.calculate(left, right);
	}

	private void throwException() throws ParseException {
		throw new ParseException("Invalid input", 0);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
