package test.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import interpreter.Interpreter;

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
			fail();
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
			fail();
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
			fail();
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
			fail();
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
			fail();
		}
	}

	@Test
	public void testForParseException() {
		String expr1 = "32 +    a";
		try {
			interpreter.execute(expr1);
			fail();
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
			fail();
		}
	}

}
