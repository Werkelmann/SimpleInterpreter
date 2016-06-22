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
			fail();
		} catch (ParseException e) {
			assertTrue(e instanceof ParseException);
		}
	}

}
