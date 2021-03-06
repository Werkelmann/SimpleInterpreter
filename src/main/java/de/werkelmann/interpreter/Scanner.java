package de.werkelmann.interpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import de.werkelmann.interpreter.tokens.Token;
import de.werkelmann.interpreter.tokens.TokenList;
import de.werkelmann.interpreter.util.Position;

public class Scanner {

	private final static Character[] OPERATORS = { '+', '-', '*' };
	private final static Character[] BRACKETS = { '(', ')' };
	private final static Character[] SPECIAL_SIGNS = { ';', '.' };

	private String text;
	private int position;
	private int line;
	private int offset;

	public TokenList scan(String text) {
		this.text = text;
		this.position = 0;
		this.line = 1;
		this.offset = 0;

		List<Token> tokens = new ArrayList<>();
		Token currentToken;
		do {
			currentToken = getNextToken();
			tokens.add(currentToken);
		} while (!currentToken.getType().equals(Token.END_OF_FILE));

		return new TokenList(tokens);
	}

	private Token getNextToken() {
		if (isPositionInRange()) {
			Character currentChar = text.charAt(position);
			return getToken(currentChar);
		}
		return new Token(Token.END_OF_FILE, getPosition());
	}

	private Token getToken(Character currentChar) {
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
				return new Token(Token.END_OF_FILE, getPosition());
			}
		}

		if (currentChar.equals('_')) {
			incrementPosition();
			return new Token(Token.IDENTIFIER, "_" + readString(c -> Character.isLetter(c)), getPosition());
		}

		if (Character.isLetter(currentChar)) {
			String value = readString(c -> Character.isLetter(c));
			if (value.toLowerCase().equals("div")) {
				return new Token(Token.OPERATOR, "div", getPosition());
			}
			return new Token(Token.IDENTIFIER, value, getPosition());
		}

		if (Character.isDigit(currentChar)) {
			return new Token(Token.INTEGER, readString(c -> Character.isDigit(c)), getPosition());
		}

		if (isOperator(currentChar)) {
			incrementPosition();
			return new Token(Token.OPERATOR, currentChar, getPosition());
		}

		if (isBracket(currentChar)) {
			incrementPosition();
			return new Token(Token.BRACKET, currentChar, getPosition());
		}

		if (isSpecialSign(currentChar)) {
			incrementPosition();
			return new Token(Token.SIGN, currentChar, getPosition());
		}

		if (currentChar.equals(':') && peek().equals('=')) {
			incrementPosition();
			incrementPosition();
			return new Token(Token.SIGN, ":=", getPosition());
		}

		throw new RuntimeException("Failure at scanning at position " + position + " Found: " + currentChar);
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

	private String readString(Function<Character, Boolean> criteria) {
		StringBuilder number = new StringBuilder();
		char character = text.charAt(position);
		while (criteria.apply(character)) {
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
