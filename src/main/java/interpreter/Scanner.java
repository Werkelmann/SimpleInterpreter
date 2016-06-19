package interpreter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import interpreter.tokens.BinaryOperatorToken;
import interpreter.tokens.BracketToken;
import interpreter.tokens.EndOfFileToken;
import interpreter.tokens.IntegerToken;
import interpreter.tokens.Token;
import interpreter.tokens.TokenList;

public class Scanner {

	private final static Character[] OPERATORS = { '+', '-', '*', '/' };
	private final static Character[] BRACKETS = { '(', ')' };

	private String text;
	private int position;

	public TokenList scan(String text) throws ParseException {
		this.text = text;
		this.position = 0;
		List<Token> tokens = new ArrayList<>();
		Token currentToken;
		do {
			currentToken = getNextToken();
			tokens.add(currentToken);
		} while (!(currentToken instanceof EndOfFileToken));

		return new TokenList(tokens);
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
				return new BinaryOperatorToken(String.valueOf(currentChar));
			}

			if (isBracket(currentChar)) {
				incrementPosition();
				return BracketToken.create(currentChar);
			}
		} catch (IndexOutOfBoundsException e) {
			return new EndOfFileToken(null);
		}

		throw new ParseException("Failure at scanning", position);
	}

	private boolean isOperator(Character currentChar) {
		return Arrays.asList(Scanner.OPERATORS).contains(currentChar);
	}

	private boolean isBracket(Character currentChar) {
		return Arrays.asList(Scanner.BRACKETS).contains(currentChar);
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
