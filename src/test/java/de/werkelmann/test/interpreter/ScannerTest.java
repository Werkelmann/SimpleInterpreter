package de.werkelmann.test.interpreter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.werkelmann.interpreter.Scanner;
import de.werkelmann.interpreter.tokens.Token;
import de.werkelmann.interpreter.tokens.TokenList;

public class ScannerTest {

	Scanner scanner;

	@Before
	public void init() {
		scanner = new Scanner();
	}

	private boolean testTokenForTypeAndValue(Token token, String type, Integer value) {
		return testTokenForTypeAndValue(token, type, String.valueOf(value));
	}

	private boolean testTokenForTypeAndValue(Token token, String type, String value) {
		return token.getType().equals(type) && token.getValue().get().equals(value);
	}

	@Test
	public void testInteger() {
		String int1 = "1";
		String int2 = "12345";

		try {
			Token t1 = scan(int1);
			assertTrue(testTokenForTypeAndValue(t1, Token.INTEGER, 1));
			Token t2 = scan(int2);
			assertTrue(testTokenForTypeAndValue(t2, Token.INTEGER, 12345));
		} catch (NumberFormatException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testIdentifier() {
		String id1 = "jesus";
		String id2 = "BEGIN";
		String id3 = "12asd"; // TODO should fail

		try {
			Token t1 = scan(id1);
			assertTrue(testTokenForTypeAndValue(t1, Token.IDENTIFIER, "jesus"));
			Token t2 = scan(id2);
			assertTrue(testTokenForTypeAndValue(t2, Token.IDENTIFIER, "BEGIN"));
		} catch (NumberFormatException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testSpecialSigns() {
		String id1 = ";";
		String id2 = ".";
		String id3 = ":=";

		try {
			Token t1 = scan(id1);
			assertTrue(testTokenForTypeAndValue(t1, Token.SIGN, ";"));
			Token t2 = scan(id2);
			assertTrue(testTokenForTypeAndValue(t2, Token.SIGN, "."));
			Token t3 = scan(id3);
			assertTrue(testTokenForTypeAndValue(t3, Token.SIGN, ":="));
		} catch (NumberFormatException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testLongerExpression() {
		String input = "BEGIN a := 2; END.";
		TokenList tokens = scanner.scan(input);
		Token t1 = tokens.getNextToken();
		assertTrue(testTokenForTypeAndValue(t1, Token.IDENTIFIER, "BEGIN"));
		t1 = tokens.getNextToken();
		assertTrue(testTokenForTypeAndValue(t1, Token.IDENTIFIER, "a"));
		t1 = tokens.getNextToken();
		assertTrue(testTokenForTypeAndValue(t1, Token.SIGN, ":="));
		t1 = tokens.getNextToken();
		assertTrue(testTokenForTypeAndValue(t1, Token.INTEGER, 2));
		t1 = tokens.getNextToken();
		assertTrue(testTokenForTypeAndValue(t1, Token.SIGN, ";"));
		t1 = tokens.getNextToken();
		assertTrue(testTokenForTypeAndValue(t1, Token.IDENTIFIER, "END"));
		t1 = tokens.getNextToken();
		assertTrue(testTokenForTypeAndValue(t1, Token.SIGN, "."));
	}

	private Token scan(String input) {
		return scanner.scan(input).getNextToken();
	}

}
