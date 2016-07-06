package de.werkelmann.interpreter;

import de.werkelmann.interpreter.util.ParserException;

public class Interpreter {

	private Scanner scanner;
	private Parser parser;
	private AstVisitor visitor;

	public Interpreter() {
		this.scanner = new Scanner();
		this.parser = new Parser();
		this.visitor = new AstVisitor();
	}

	public int execute(String input) throws ParserException {
		return visitor.visit(parser.parse(scanner.scan(input)));
	}

	public int expr(String input) throws ParserException {
		return visitor.visit(parser.expr(scanner.scan(input)));
	}

	public int getVariable(String key) {
		return visitor.getVariable(key);
	}

}
