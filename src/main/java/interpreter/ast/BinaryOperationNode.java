package interpreter.ast;

import interpreter.tokens.Token;

public class BinaryOperationNode extends Ast {

	private final Ast left;
	private final Ast right;
	private final Token operator;

	public BinaryOperationNode(Ast left, Token operator, Ast right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	public Ast getLeft() {
		return left;
	}

	public Ast getRight() {
		return right;
	}

	public Token getOperator() {
		return operator;
	}

}
