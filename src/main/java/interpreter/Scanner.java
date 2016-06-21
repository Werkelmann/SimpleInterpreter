package interpreter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import interpreter.tokens.AssignToken;
import interpreter.tokens.BracketToken;
import interpreter.tokens.DotToken;
import interpreter.tokens.EndOfFileToken;
import interpreter.tokens.IdentifierToken;
import interpreter.tokens.IntegerToken;
import interpreter.tokens.OperatorToken;
import interpreter.tokens.SemikolonToken;
import interpreter.tokens.Token;
import interpreter.tokens.TokenList;

public class Scanner {

	private final static Character[] OPERATORS = { '+', '-', '*', '/' };
	private final static Character[] BRACKETS = { '(', ')' };
	private final static Character[] SPECIAL_SIGNS = { ';', '.' };

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
		if (isPositionInRange()) {
			Character currentChar = text.charAt(position);
			return getToken(currentChar);
		}
		return new EndOfFileToken(null);
	}

	public Token getToken(Character currentChar) throws ParseException {
		while (Character.isWhitespace(currentChar)) {
			incrementPosition();
			currentChar = text.charAt(position);
		}

		if (Character.isLetter(currentChar)) {
			return new IdentifierToken(readString(c -> Character.isLetter(c)));
		}

		if (Character.isDigit(currentChar)) {
			return new IntegerToken(readString(c -> Character.isDigit(c)));
		}

		if (isOperator(currentChar)) {
			incrementPosition();
			return new OperatorToken(String.valueOf(currentChar));
		}

		if (isBracket(currentChar)) {
			incrementPosition();
			return BracketToken.create(currentChar);
		}

		if (isSpecialSign(currentChar)) {
			incrementPosition();
			return createSignToken(currentChar);
		}

		if (currentChar.equals(':') && peek().equals('=')) {
			incrementPosition();
			incrementPosition();
			return new AssignToken(null);
		}

		throw new ParseException("Failure at scanning", position);
	}

	private boolean isOperator(Character currentChar) {
		return Arrays.asList(Scanner.OPERATORS).contains(currentChar);
	}

	private boolean isBracket(Character currentChar) {
		return Arrays.asList(Scanner.BRACKETS).contains(currentChar);
	}

	private boolean isSpecialSign(Character currentChar) {
		return Arrays.asList(Scanner.SPECIAL_SIGNS).contains(currentChar);
	}

	private String readString(CharCriteria criteria) {
		StringBuilder number = new StringBuilder();
		char character = text.charAt(position);
		while (criteria.match(character)) {
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

	private Token createSignToken(Character currentChar) throws ParseException {
		switch (currentChar) {
		case (';'):
			return new SemikolonToken(null);
		case ('.'):
			return new DotToken(null);
		default:
			throw new ParseException("Unexpected char at " + position + ". Found: " + currentChar + " Expected: "
					+ SPECIAL_SIGNS.toString(), position);
		}
	}

	private void incrementPosition() {
		this.position += 1;
	}

	private boolean isPositionInRange() {
		return position <= text.length() - 1;
	}

	private Character peek() {
		return text.charAt(++position);
	}

}
