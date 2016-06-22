package de.werkelmann.interpreter.ast;

import de.werkelmann.interpreter.tokens.Token;

public class AssignNode extends Ast {

	private final Ast var;
	private final Token token;
	private final Ast expr;

	public AssignNode(Ast var, Token token, Ast expr) {
		this.var = var;
		this.token = token;
		this.expr = expr;
	}

	public Ast getVar() {
		return var;
	}

	public Ast getExpr() {
		return expr;
	}

	public Token getToken() {
		return token;
	}

}
