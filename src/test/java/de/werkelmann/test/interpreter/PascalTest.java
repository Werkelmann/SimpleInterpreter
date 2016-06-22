package de.werkelmann.test.interpreter;

import static org.junit.Assert.assertEquals;
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
			assertEquals(2, (int) interpreter.getVariables().get(7));
		} catch (ParseException e) {
			fail(e.getMessage());
		}
	}

}
