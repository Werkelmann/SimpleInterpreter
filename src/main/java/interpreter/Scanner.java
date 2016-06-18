package interpreter;

import java.text.ParseException;
import java.util.Arrays;

import interpreter.tokens.BinaryOperatorToken;
import interpreter.tokens.EndOfFileToken;
import interpreter.tokens.IntegerToken;
import interpreter.tokens.Token;

public class Scanner {

	private final static Character[] OPERATORS = { '+', '-' };

	private String text;
	private int position;

	public void init(String text) {
		this.text = text;
		position = 0;
	}

	public Token getNextToken() throws ParseException {
		try {
			Character currentChar = text.charAt(position);
			while (Character.isWhitespace(currentChar)) {
				incrementPosition();
				currentChar = text.charAt(position);
			}

			if (Character.isDigit(currentChar)) {
				return new IntegerToken(readNumber(text));
			}

			if (isOperator(currentChar)) {
				incrementPosition();
				return BinaryOperatorToken.create(currentChar);
			}
		} catch (IndexOutOfBoundsException e) {
			return new EndOfFileToken(null);
		}

		throw new ParseException("Unparsable input", text.length());
	}

	private boolean isOperator(Character currentChar) {
		return Arrays.asList(Scanner.OPERATORS).contains(currentChar);
	}

	private String readNumber(String text) {
		StringBuilder number = new StringBuilder();
		char character = text.charAt(position);
		while (Character.isDigit(character)) {
			number.append(character);
			incrementPosition();
			if (isPositionInRange()) {
				character = text.charAt(position);
			} else {
				break;
			}
		}
		return number.toString();
	}

	private void incrementPosition() {
		this.position += 1;
	}

	private boolean isPositionInRange() {
		return position <= text.length() - 1;
	}

}
