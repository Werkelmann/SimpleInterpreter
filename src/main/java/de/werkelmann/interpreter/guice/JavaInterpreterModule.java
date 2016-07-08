package de.werkelmann.interpreter.guice;

import com.google.inject.AbstractModule;

import de.werkelmann.interpreter.parser.JavaParser;
import de.werkelmann.interpreter.parser.Parser;

public class JavaInterpreterModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Parser.class).to(JavaParser.class);
	}

}
