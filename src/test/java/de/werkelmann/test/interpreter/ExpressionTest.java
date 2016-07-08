package de.werkelmann.test.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.werkelmann.interpreter.Interpreter;
import de.werkelmann.interpreter.util.ParserException;

public class ExpressionTest {

	private Interpreter interpreter;

	@Before
	public void init() {
		interpreter = new Interpreter();
	}

	@Test
	public void testSimpleExpressions() {
		String expr1 = "3+4";
		String expr2 = "9+9";
		String expr3 = "4+2";

		assertEquals(7, interpreter.expr(expr1));
		assertEquals(18, interpreter.expr(expr2));
		assertEquals(6, interpreter.expr(expr3));
	}

	@Test
	public void testWhitespaces() {
		String expr1 = "3 +4";
		String expr2 = " 9+ 9";
		String expr3 = "4  +      2 ";

		assertEquals(7, interpreter.expr(expr1));
		assertEquals(18, interpreter.expr(expr2));
		assertEquals(6, interpreter.expr(expr3));
	}

	@Test
	public void testDifferentOperators() {
		String expr1 = "3-2";
		String expr2 = "9*9";
		String expr3 = "4div2";

		assertEquals(1, interpreter.expr(expr1));
		assertEquals(81, interpreter.expr(expr2));
		assertEquals(2, interpreter.expr(expr3));
	}

	@Test
	public void testNonSingleDigits() {
		String expr1 = "32+4";
		String expr2 = "19+29";
		String expr3 = "1234+2";

		assertEquals(36, interpreter.expr(expr1));
		assertEquals(48, interpreter.expr(expr2));
		assertEquals(1236, interpreter.expr(expr3));
	}

	@Test
	public void testExpression() {
		String expr1 = "32 +    4";
		String expr2 = "19 - 29 ";
		String expr3 = "   1234+ 2234";

		assertEquals(36, interpreter.expr(expr1));
		assertEquals(-10, interpreter.expr(expr2));
		assertEquals(3468, interpreter.expr(expr3));
	}

	@Test
	public void testForParseException() {
		String expr2 = "32 +";
		try {
			interpreter.expr(expr2);
			fail("Should throw a ParserException");
		} catch (ParserException e) {
			assertTrue(e.getClass().equals(ParserException.class));
		}

		String expr3 = "+";
		try {
			interpreter.expr(expr3);
			fail("Should throw a ParserException");
		} catch (ParserException e) {
			assertTrue(e.getClass().equals(ParserException.class));
		}

		String expr4 = "";
		try {
			interpreter.expr(expr4);
			fail("Should throw a ParseException");
		} catch (ParserException e) {
			assertTrue(e.getClass().equals(ParserException.class));
		}
	}

	@Test
	public void testExpressionsWithVariousLength() {
		String expr1 = "32 + 4 - 8 - 22 + 2";
		String expr2 = "3";
		String expr3 = "1+2-3+4-5+6";

		assertEquals(8, interpreter.expr(expr1));
		assertEquals(3, interpreter.expr(expr2));
		assertEquals(5, interpreter.expr(expr3));
	}

	@Test
	public void testPrecedences() {
		String expr1 = "9 + 4 div 4";
		String expr2 = "100 - 2 * 30";

		assertEquals(10, interpreter.expr(expr1));
		assertEquals(40, interpreter.expr(expr2));
	}

	@Test
	public void testUnaryOperator() {
		String expr1 = "-9 + 4";
		String expr2 = "+100 - 2 * 30";
		String expr3 = "-2";

		assertEquals(-5, interpreter.expr(expr1));
		assertEquals(40, interpreter.expr(expr2));
		assertEquals(-2, interpreter.expr(expr3));
	}

	@Test
	public void testRoundBrackets() {
		String expr1 = "11 - (2 - 1)";
		String expr2 = "(100 - 2) * 3";
		String expr3 = "(3)";
		String expr6 = "(3+4) * ((12 div 4) * 3)";

		assertEquals(10, interpreter.expr(expr1));
		assertEquals(294, interpreter.expr(expr2));
		assertEquals(3, interpreter.expr(expr3));
		assertEquals(63, interpreter.expr(expr6));
	}

	@Test
	public void testMissingClosingRoundBracket() {
		String expr4 = "(3";
		try {
			interpreter.expr(expr4);
			fail("Missing closing bracket not reckognized");
		} catch (ParserException e) {
			assertTrue(e.getClass().equals(ParserException.class));
		}
	}

	@Test
	public void testMissingOpeningRoundBracket() {
		String expr5 = "4)";
		try {
			interpreter.expr(expr5);
			fail("Missing opening bracket not reckognized");
		} catch (ParserException e) {
			assertTrue(e.getClass().equals(ParserException.class));
		}
	}

}
