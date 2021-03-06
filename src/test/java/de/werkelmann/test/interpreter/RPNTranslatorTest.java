package de.werkelmann.test.interpreter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.werkelmann.interpreter.Scanner;
import de.werkelmann.interpreter.parser.JavaRecursiveDescentParser;
import de.werkelmann.interpreter.translator.RPNTranslator;

public class RPNTranslatorTest {

	private RPNTranslator trans;
	private JavaRecursiveDescentParser parser;
	private Scanner scanner;

	@Before
	public void init() {
		trans = new RPNTranslator();
		parser = new JavaRecursiveDescentParser();
		scanner = new Scanner();
	}

	private String parse(String expr) {
		return trans.visit(parser.expr(scanner.scan(expr)));
	}

	@Test
	public void testRPN() {
		String expr1 = "1 + 2";
		String expr2 = "-2 + 4";
		String expr3 = "(12 * 4) div 4";
		String expr4 = "---4";
		String expr5 = "4 * (8 + 3)";
		String expr6 = "(5 + 3) * 12 div 3";

		assertEquals("1 2 +", parse(expr1));
		assertEquals("2 - 4 +", parse(expr2));
		assertEquals("12 4 * 4 div", parse(expr3));
		assertEquals("4 - - -", parse(expr4));
		assertEquals("4 8 3 + *", parse(expr5));
		assertEquals("5 3 + 12 * 3 div", parse(expr6));
	}
}
