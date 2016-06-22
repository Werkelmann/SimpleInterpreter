package de.werkelmann.test.interpreter;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import de.werkelmann.interpreter.Parser;
import de.werkelmann.interpreter.Scanner;
import de.werkelmann.interpreter.translator.LISPTranslator;

public class LISPTranslatorTest {

	static LISPTranslator trans;
	static Parser parser;
	static Scanner scanner;

	@Before
	public void init() {
		trans = new LISPTranslator();
		parser = new Parser();
		scanner = new Scanner();
	}

	private static String parse(String expr) {
		try {
			return trans.visit(parser.parse(scanner.scan(expr)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Test
	public void testRPN() {
		String expr1 = "1 + 2";
		String expr2 = "-2 + 4";
		String expr3 = "(12 * 4) / 4";
		String expr4 = "---4";
		String expr5 = "4 * (8 + 3)";
		String expr6 = "(5 + 3) * 12 / 3";
		String expr7 = "2 + 3 * 5";

		assertEquals("(+ 1 2)", parse(expr1));
		assertEquals("(+ (- 2) 4)", parse(expr2));
		assertEquals("(/ (* 12 4) 4)", parse(expr3));
		assertEquals("(- (- (- 4)))", parse(expr4));
		assertEquals("(* 4 (+ 8 3))", parse(expr5));
		assertEquals("(/ (* (+ 5 3) 12) 3)", parse(expr6));
		assertEquals("(+ 2 (* 3 5))", parse(expr7));
	}
}
