package de.werkelmann.interpreter;

import java.text.ParseException;
import java.util.Map;

public class Interpreter {

	private Scanner scanner;
	private Parser parser;
	private AstVisitor visitor;

	public Interpreter() {
		this.scanner = new Scanner();
		this.parser = new Parser();
		this.visitor = new AstVisitor();
	}

	public int execute(String input) throws ParseException {
		return visitor.visit(parser.parse(scanner.scan(input)));
	}

	public int expr(String input) throws ParseException {
		return visitor.visit(parser.expr(scanner.scan(input)));
	}

	public Map<String, Integer> getVariables() {
		return visitor.globelScope;
	}

}
