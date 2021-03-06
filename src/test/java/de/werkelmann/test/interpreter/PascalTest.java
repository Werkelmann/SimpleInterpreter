package de.werkelmann.test.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.werkelmann.interpreter.Interpreter;
import de.werkelmann.interpreter.guice.JavaInterpreterModule;
import de.werkelmann.interpreter.util.ParserException;

public class PascalTest {

	Interpreter interpreter;

	@Before
	public void init() {
		Injector injector = Guice.createInjector(new JavaInterpreterModule());
		interpreter = injector.getInstance(Interpreter.class);
	}

	@Test
	public void testVariableTable() {
		String input = "BEGIN a := 2 END.";
		interpreter.execute(input);
		assertEquals(2, interpreter.getVariable("a"));
	}

	@Test
	public void testNestedCompound() {
		// @formatter:off
		String input = "BEGIN \n"
				+ "BEGIN "
				+ "a := 2 "
				+ "END "
				+ "END.";
		// @formatter:on
		interpreter.execute(input);
		assertEquals(2, interpreter.getVariable("a"));
	}

	@Test
	public void testStmtList() {
		String input = "BEGIN BEGIN a := 2; b := 2 END END.";
		interpreter.execute(input);
		assertEquals(2, interpreter.getVariable("a"));
		assertEquals(2, interpreter.getVariable("b"));
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
		String input = "BeGiN begiN a:= 2 end End.";
		interpreter.execute(input);
	}

	@Test
	public void testAllowedUnderscoreInIdentifier() {
		String input = "BeGiN begiN _a:= 2 end End.";
		interpreter.execute(input);
		assertEquals(2, interpreter.getVariable("_a"));
	}

	@Test
	public void testUnallowedUnderscoreInIdentifier() {
		String input;
		try {
			input = "BeGiN begiN a_ := 2 end End.";
			interpreter.execute(input);
			fail("Underscore somewhere in identifier is not allowed");
		} catch (ParserException e) {
			assertTrue(e.getClass().equals(ParserException.class));
		}
	}

	@Test
	public void testLookingUpOfVariables() {
		String input = "BEGIN a := 2; b := a + 4 END.";
		interpreter.execute(input);
		assertEquals(6, interpreter.getVariable("b"));
	}

}
