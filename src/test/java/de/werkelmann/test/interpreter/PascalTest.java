package de.werkelmann.test.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import de.werkelmann.interpreter.Interpreter;

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
			assertEquals(2, (int) interpreter.getVariables().get("a"));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testNestedCompound() {
		try {
			String input = "BEGIN BEGIN a := 2 END END.";
			interpreter.execute(input);
			assertEquals(2, (int) interpreter.getVariables().get("a"));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testStmtList() {
		try {
			String input = "BEGIN BEGIN a := 2; b := 2 END END.";
			interpreter.execute(input);
			assertEquals(2, (int) interpreter.getVariables().get("a"));
			assertEquals(2, (int) interpreter.getVariables().get("b"));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testEmptyString() {
		try {
			String input = "";
			interpreter.execute(input);
			fail("Empty Input should not be allowed");
		} catch (ParseException e) {
			assertTrue(e.getClass().equals(ParseException.class));
		}
	}

	@Test
	public void testCaseInsensitiveKeywords() {
		try {
			String input = "BeGiN begiN a:= 2 end End.";
			interpreter.execute(input);
			assertEquals(2, (int) interpreter.getVariables().get("a"));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testUnderscoreInIdentifier() {
		try {
			String input = "BeGiN begiN _a:= 2 end End.";
			interpreter.execute(input);
			assertEquals(2, (int) interpreter.getVariables().get("_a"));
		} catch (ParseException e) {
			fail(e.getMessage());
		}

		try {
			String input = "BeGiN begiN a_ := 2 end End.";
			interpreter.execute(input);
			fail("Underscore somewhere in identifier is not allowed");
		} catch (ParseException e) {
			assertTrue(e.getClass().equals(ParseException.class));
		}
	}

}
