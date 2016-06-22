package de.werkelmann.test.interpreter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import de.werkelmann.interpreter.Scanner;
import de.werkelmann.interpreter.tokens.AssignToken;
import de.werkelmann.interpreter.tokens.DotToken;
import de.werkelmann.interpreter.tokens.IdentifierToken;
import de.werkelmann.interpreter.tokens.IntegerToken;
import de.werkelmann.interpreter.tokens.SemicolonToken;
import de.werkelmann.interpreter.tokens.Token;
import de.werkelmann.interpreter.tokens.TokenList;

public class ScannerTest {

	Scanner scanner;

	@Before
	public void init() {
		scanner = new Scanner();
	}

	@Test
	public void testInteger() {
		String int1 = "1";
		String int2 = "12345";

		try {
			Token t1 = scan(int1);
			assertTrue(t1 instanceof IntegerToken && Integer.parseInt(t1.getValue()) == 1);
			Token t2 = scan(int2);
			assertTrue(t2 instanceof IntegerToken && Integer.parseInt(t2.getValue()) == 12345);
		} catch (NumberFormatException | ParseException e) {
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
			assertTrue(t1 instanceof IdentifierToken && t1.getValue().equals("jesus"));
			Token t2 = scan(id2);
			assertTrue(t2 instanceof IdentifierToken && t2.getValue().equals("BEGIN"));
		} catch (NumberFormatException | ParseException e) {
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
			assertTrue(t1 instanceof SemicolonToken);
			Token t2 = scan(id2);
			assertTrue(t2 instanceof DotToken);
			Token t3 = scan(id3);
			assertTrue(t3 instanceof AssignToken);
		} catch (NumberFormatException | ParseException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testLongerExpression() {
		try {
			String input = "BEGIN a := 2; END.";
			TokenList tokens = scanner.scan(input);
			Token t1 = tokens.getNextToken();
			assertTrue(t1 instanceof IdentifierToken && t1.getValue().equals("BEGIN"));
			t1 = tokens.getNextToken();
			assertTrue(t1 instanceof IdentifierToken && t1.getValue().equals("a"));
			t1 = tokens.getNextToken();
			assertTrue(t1 instanceof AssignToken);
			t1 = tokens.getNextToken();
			assertTrue(t1 instanceof IntegerToken && Integer.valueOf(t1.getValue()) == 2);
			t1 = tokens.getNextToken();
			assertTrue(t1 instanceof SemicolonToken);
			t1 = tokens.getNextToken();
			assertTrue(t1 instanceof IdentifierToken && t1.getValue().equals("END"));
			t1 = tokens.getNextToken();
			assertTrue(t1 instanceof DotToken);
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	private Token scan(String input) throws ParseException {
		return scanner.scan(input).getNextToken();
	}

}
