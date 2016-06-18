package interpreter;

import java.text.ParseException;

public class Interpreter {

	private String text;
	private int position;
	Token currentToken;

	private Token getNextToken() throws ParseException {
		currentToken = null;

		if (!assertPositionInRange()) {
			currentToken = new Token(Token.EOF, null);
			return currentToken;
		}

		Character currentChar = text.charAt(position);
		while (Character.isWhitespace(currentChar) && assertPositionInRange()) {
			if (assertPositionInRange()) {
				incrementPosition();
				currentChar = text.charAt(position);
			} else {
				currentToken = new Token(Token.EOF, null);
				return currentToken;
			}
		}

		if (Character.isDigit(currentChar)) {
			currentToken = new Token(Token.INTEGER, readNumber(text));
			return currentToken;
		}

		analyzeChar(currentChar, '+');
		analyzeChar(currentChar, '-');

		if (null != currentToken) {
			return currentToken;
		} else {
			throw new ParseException("Invalid token at position " + (position + 1), position);
		}
	}

	private String readNumber(String text) {
		StringBuilder number = new StringBuilder();
		char character = text.charAt(position);
		while (Character.isDigit(character)) {
			number.append(character);
			incrementPosition();
			if (assertPositionInRange()) {
				character = text.charAt(position);
			} else {
				break;
			}
		}
		return number.toString();
	}

	private Token analyzeChar(Character currentChar, Character character) {
		if (currentChar.equals(character)) {
			incrementPosition();
			currentToken = new Token(Token.OPERATOR, String.valueOf(character));
			return currentToken;
		}
		return null;
	}

	private void eat(String tokenType) throws ParseException {
		if (currentToken.getType().equals(tokenType)) {
			getNextToken();
		}
	}

	public int expr(String text) throws ParseException {
		this.text = text;
		position = 0;
		getNextToken();

		int left = Integer.parseInt(currentToken.getValue());
		this.eat(Token.INTEGER);

		String op = currentToken.getValue();
		this.eat(Token.OPERATOR);

		int right = Integer.parseInt(currentToken.getValue());
		this.eat(Token.INTEGER);

		return calculate(text, left, op, right);
	}

	private int calculate(String text, int left, String op, int right) throws ParseException {
		switch (op) {
		case ("+"):
			return left + right;
		case ("-"):
			return left - right;
		default:
			throw new ParseException("Invalid input", text.length());
		}
	}

	private void incrementPosition() {
		this.position += 1;
	}

	private boolean assertPositionInRange() {
		return position <= text.length() - 1;
	}
}
