package de.werkelmann.interpreter;

public class Interpreter {

	private Scanner scanner;
	private JavaParser parser;
	private AstVisitor visitor;

	public Interpreter() {
		this.scanner = new Scanner();
		this.parser = new JavaParser();
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
