package de.werkelmann.test.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import de.werkelmann.interpreter.Interpreter;

public class InterpreterTest {

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

		try {
			assertEquals(7, interpreter.execute(expr1));
			assertEquals(18, interpreter.execute(expr2));
			assertEquals(6, interpreter.execute(expr3));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testWhitespaces() {
		String expr1 = "3 +4";
		String expr2 = " 9+ 9";
		String expr3 = "4  +      2 ";

		try {
			assertEquals(7, interpreter.execute(expr1));
			assertEquals(18, interpreter.execute(expr2));
			assertEquals(6, interpreter.execute(expr3));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testDifferentOperators() {
		String expr1 = "3-2";
		String expr2 = "9*9";
		String expr3 = "4/2";

		try {
			assertEquals(1, interpreter.execute(expr1));
			assertEquals(81, interpreter.execute(expr2));
			assertEquals(2, interpreter.execute(expr3));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testNonSingleDigits() {
		String expr1 = "32+4";
		String expr2 = "19+29";
		String expr3 = "1234+2";

		try {
			assertEquals(36, interpreter.execute(expr1));
			assertEquals(48, interpreter.execute(expr2));
			assertEquals(1236, interpreter.execute(expr3));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testExpression() {
		String expr1 = "32 +    4";
		String expr2 = "19 - 29 ";
		String expr3 = "   1234+ 2234";

		try {
			assertEquals(36, interpreter.execute(expr1));
			assertEquals(-10, interpreter.execute(expr2));
			assertEquals(3468, interpreter.execute(expr3));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testForParseException() {
		String expr2 = "32 +";
		try {
			interpreter.execute(expr2);
			fail("Should throw a ParseException");
		} catch (ParseException e) {
			assertTrue(e instanceof ParseException);
		}

		String expr3 = "+";
		try {
			interpreter.execute(expr3);
			fail("Should throw a ParseException");
		} catch (ParseException e) {
			assertTrue(e instanceof ParseException);
		}

		String expr4 = "";
		try {
			interpreter.execute(expr4);
			fail("Should throw a ParseException");
		} catch (ParseException e) {
			assertTrue(e instanceof ParseException);
		}
	}

	@Test
	public void testExpressionsWithVariousLength() {
		String expr1 = "32 + 4 - 8 - 22 + 2";
		String expr2 = "3";
		String expr3 = "1+2-3+4-5+6";

		try {
			assertEquals(8, interpreter.execute(expr1));
			assertEquals(3, interpreter.execute(expr2));
			assertEquals(5, interpreter.execute(expr3));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testPrecedences() {
		String expr1 = "9 + 4 / 4";
		String expr2 = "100 - 2 * 30";

		try {
			assertEquals(10, interpreter.execute(expr1));
			assertEquals(40, interpreter.execute(expr2));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testUnaryOperator() {
		String expr1 = "-9 + 4";
		String expr2 = "+100 - 2 * 30";
		String expr3 = "-2";

		try {
			assertEquals(-5, interpreter.execute(expr1));
			assertEquals(40, interpreter.execute(expr2));
			assertEquals(-2, interpreter.execute(expr3));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRoundBrackets() {
		String expr1 = "11 - (2 - 1)";
		String expr2 = "(100 - 2) * 3";
		String expr3 = "(3)";
		String expr6 = "(3+4) * ((12 / 4) * 3)";

		String expr4 = "(3";
		String expr5 = "4)";

		try {
			assertEquals(10, interpreter.execute(expr1));
			assertEquals(294, interpreter.execute(expr2));
			assertEquals(3, interpreter.execute(expr3));
			assertEquals(63, interpreter.execute(expr6));
		} catch (ParseException e) {
			fail(e.getMessage());
		}

		try {
			interpreter.execute(expr4);
			fail("Missing closing bracket not reckognized");
		} catch (ParseException e) {
			assertTrue(e instanceof ParseException);
		}

		try {
			interpreter.execute(expr5);
			fail("Missing opening bracket not reckognized");
		} catch (ParseException e) {
			assertTrue(e instanceof ParseException);
		}
	}

}
