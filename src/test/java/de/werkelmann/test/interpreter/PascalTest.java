package de.werkelmann.test.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import de.werkelmann.interpreter.Interpreter;
import de.werkelmann.interpreter.util.ParserException;

public class PascalTest {

	Interpreter interpreter;

	@Before
	public void init() {
		interpreter = new Interpreter();
	}

	@Test
	public void testVariableTable() {
		try {
			String input = "BEGIN a := 2 END.";
			interpreter.execute(input);
			assertEquals(2, interpreter.getVariable("a"));
		} catch (ParserException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testNestedCompound() {
		try {
			// @formatter:off
			String input = "BEGIN \n"
					+ "BEGIN "
					+ "a := 2 "
					+ "END "
					+ "END.";
			// @formatter:on
			interpreter.execute(input);
			assertEquals(2, interpreter.getVariable("a"));
		} catch (ParserException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testStmtList() {
		try {
			String input = "BEGIN BEGIN a := 2; b := 2 END END.";
			interpreter.execute(input);
			assertEquals(2, interpreter.getVariable("a"));
			assertEquals(2, interpreter.getVariable("b"));
		} catch (ParserException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testEmptyString() {
		try {
			String input = "";
			interpreter.execute(input);
			fail("Empty Input should not be allowed");
		} catch (ParserException e) {
			assertTrue(e.getClass().equals(ParserException.class));
		}
	}

	@Test
	public void testCaseInsensitiveKeywords() {
		try {
			String input = "BeGiN begiN a:= 2 end End.";
			interpreter.execute(input);
			assertEquals(2, interpreter.getVariable("a"));
		} catch (ParserException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testUnderscoreInIdentifier() {
		try {
			String input = "BeGiN begiN _a:= 2 end End.";
			interpreter.execute(input);
			assertEquals(2, interpreter.getVariable("_a"));
		} catch (ParserException e) {
			fail(e.getMessage());
		}

		try {
			String input = "BeGiN begiN a_ := 2 end End.";
			interpreter.execute(input);
			fail("Underscore somewhere in identifier is not allowed");
		} catch (ParserException e) {
			assertTrue(e.getClass().equals(ParserException.class));
		}
	}

	@Test
	public void testLookingUpOfVariables() {
		try {
			String input = "BEGIN a := 2; b := a + 4 END.";
			interpreter.execute(input);
			assertEquals(6, interpreter.getVariable("b"));
		} catch (ParserException e) {
			fail(e.getMessage());
		}
	}

}
