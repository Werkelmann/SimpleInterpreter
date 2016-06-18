package test.interpreter;

import static org.junit.Assert.assertEquals;
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
			assertEquals(7, interpreter.expr(expr1));
			assertEquals(18, interpreter.expr(expr2));
			assertEquals(6, interpreter.expr(expr3));
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	public void testWhitespaces() {
		String expr1 = "3 +4";
		String expr2 = "9+ 9";
		String expr3 = "4  +      2";

		try {
			assertEquals(7, interpreter.expr(expr1));
			assertEquals(18, interpreter.expr(expr2));
			assertEquals(6, interpreter.expr(expr3));
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	public void testMinus() {
		String expr1 = "3-2";
		String expr2 = "9-9";
		String expr3 = "4-2";

		try {
			assertEquals(1, interpreter.expr(expr1));
			assertEquals(0, interpreter.expr(expr2));
			assertEquals(2, interpreter.expr(expr3));
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
			assertEquals(36, interpreter.expr(expr1));
			assertEquals(48, interpreter.expr(expr2));
			assertEquals(1236, interpreter.expr(expr3));
		} catch (ParseException e) {
			fail();
		}
	}

	@Test
	public void testExpression() {
		String expr1 = "32 +    4";
		String expr2 = "19 - 29";
		String expr3 = "1234+ 2234";

		try {
			assertEquals(36, interpreter.expr(expr1));
			assertEquals(-10, interpreter.expr(expr2));
			assertEquals(3468, interpreter.expr(expr3));
		} catch (ParseException e) {
			fail();
		}
	}

}
