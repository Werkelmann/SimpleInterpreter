package de.werkelmann.interpreter.ast;

public class UnaryOperationNode extends Ast {

	private final String value;
	private final Ast expr;

	public UnaryOperationNode(final String op, final Ast expr) {
		this.value = op;
		this.expr = expr;
	}

	public String getValue() {
		return value;
	}

	public Ast getExpr() {
		return expr;
	}

}
