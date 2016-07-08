package de.werkelmann.interpreter;

import com.google.inject.Inject;

import de.werkelmann.interpreter.parser.Parser;

public class Interpreter {

	private Scanner scanner;
	private Parser parser;
	private AstVisitor visitor;

	@Inject
	public Interpreter(Parser parser) {
		this.scanner = new Scanner();
		this.parser = parser;
		this.visitor = new AstVisitor();
	}

	public int execute(String input) {
		return visitor.visit(parser.parse(scanner.scan(input)));
	}

	public int expr(String input) {
		return visitor.visit(parser.expr(scanner.scan(input)));
	}

	public int getVariable(String key) {
		return visitor.getVariable(key);
	}

}
