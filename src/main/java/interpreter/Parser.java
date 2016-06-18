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

		return expr();
	}

	private void eat(String expected) throws ParseException {
		if (currentToken.getType().equals(expected)) {
			currentToken = tokens.getNextToken();
		} else {
			throwException();
		}
	}

	public int expr() throws ParseException {
		currentToken = tokens.getNextToken();
		int left = Integer.parseInt(currentToken.getValue());
		this.eat(IntegerToken.TOKEN_TYPE);

		while (!(currentToken instanceof EndOfFileToken)) {
			BinaryOperatorToken op = (BinaryOperatorToken) currentToken;
			this.eat(BinaryOperatorToken.TOKEN_TYPE);

			int right = Integer.parseInt(currentToken.getValue());
			this.eat(IntegerToken.TOKEN_TYPE);

			left = calculate(left, op, right);
		}

		return left;
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
