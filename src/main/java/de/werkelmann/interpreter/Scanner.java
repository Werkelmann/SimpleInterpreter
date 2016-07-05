package de.werkelmann.interpreter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.werkelmann.interpreter.tokens.BracketToken;
import de.werkelmann.interpreter.tokens.EndOfFileToken;
import de.werkelmann.interpreter.tokens.IdentifierToken;
import de.werkelmann.interpreter.tokens.IntegerToken;
import de.werkelmann.interpreter.tokens.OperatorToken;
import de.werkelmann.interpreter.tokens.SignToken;
import de.werkelmann.interpreter.tokens.Token;
import de.werkelmann.interpreter.tokens.TokenList;
import de.werkelmann.interpreter.util.CharCriteria;
import de.werkelmann.interpreter.util.Position;

public class Scanner {

	private final static Character[] OPERATORS = { '+', '-', '*' };
	private final static Character[] BRACKETS = { '(', ')' };
	private final static Character[] SPECIAL_SIGNS = { ';', '.' };

	private String text;
	private int position;
	private int line;
	private int offset;

	public TokenList scan(String text) throws ParseException {
		this.text = text;
		this.position = 0;
		this.line = 1;
		this.offset = 0;

		List<Token> tokens = new ArrayList<>();
		Token currentToken;
		do {
			currentToken = getNextToken();
			tokens.add(currentToken);
		} while (!(currentToken instanceof EndOfFileToken));

		return new TokenList(tokens);
	}

	private Token getNextToken() throws ParseException {
		if (isPositionInRange()) {
			Character currentChar = text.charAt(position);
			return getToken(currentChar);
		}
		return new EndOfFileToken(null, getPosition());
	}

	private Token getToken(Character currentChar) throws ParseException {
		if (isLineBreak(currentChar)) {
			incrementPosition();
			line++;
			this.offset = this.position;
		}

		while (Character.isWhitespace(currentChar)) {
			incrementPosition();
			try {
				currentChar = text.charAt(position);
			} catch (IndexOutOfBoundsException e) {
				return new EndOfFileToken(null, getPosition());
			}
		}

		if (currentChar.equals('_')) {
			incrementPosition();
			return new IdentifierToken("_" + readString(c -> Character.isLetter(c)), getPosition());
		}

		if (Character.isLetter(currentChar)) {
			String value = readString(c -> Character.isLetter(c));
			if (value.toLowerCase().equals("div")) {
				return new OperatorToken("div", getPosition());
			}
			return new IdentifierToken(value, getPosition());
		}

		if (Character.isDigit(currentChar)) {
			return new IntegerToken(readString(c -> Character.isDigit(c)), getPosition());
		}

		if (isOperator(currentChar)) {
			incrementPosition();
			return new OperatorToken(currentChar, getPosition());
		}

		if (isBracket(currentChar)) {
			incrementPosition();
			return new BracketToken(currentChar, getPosition());
		}

		if (isSpecialSign(currentChar)) {
			incrementPosition();
			return new SignToken(currentChar, getPosition());
		}

		if (currentChar.equals(':') && peek().equals('=')) {
			incrementPosition();
			incrementPosition();
			return new SignToken(":=", getPosition());
		}

		throw new ParseException("Failure at scanning at position " + position + " Found: " + currentChar, position);
	}

	private boolean isLineBreak(Character currentChar) {
		// TODO find line breaks in input
		// !String.valueOf(currentChar).matches(".") ?
		return false;
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

	private void incrementPosition() {
		this.position += 1;
	}

	private boolean isPositionInRange() {
		return position <= text.length() - 1;
	}

	private Character peek() {
		return text.charAt(++position);
	}

	private Position getPosition() {
		return new Position(line, position - offset);
	}

}
