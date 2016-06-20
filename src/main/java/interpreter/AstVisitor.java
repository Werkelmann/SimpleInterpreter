package interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import interpreter.ast.Ast;
import interpreter.ast.BinaryOperationNode;
import interpreter.ast.IntegerLeaf;
import interpreter.ast.UnaryOperationNode;

public class AstVisitor {

	public int visit(Ast node) {
		Class<? extends Ast> clazz = node.getClass();
		String methodName = "visit" + clazz.getSimpleName();
		try {
			Method visitorMethod = this.getClass().getMethod(methodName, Ast.class);
			return (int) visitorMethod.invoke(this, node);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			System.err.println("No visitor defined for " + clazz.getSimpleName());
			return 0;
		}
	}

	public int visitBinaryOperationNode(Ast node) throws Exception {
		BinaryOperationNode binNode = (BinaryOperationNode) node;
		switch (binNode.getOperator().getValue()) {
		case ("+"):
			return visit(binNode.getLeft()) + visit(binNode.getRight());
		case ("-"):
			return visit(binNode.getLeft()) - visit(binNode.getRight());
		case ("*"):
			return visit(binNode.getLeft()) * visit(binNode.getRight());
		case ("/"):
			return visit(binNode.getLeft()) / visit(binNode.getRight());
		default:
			throw new Exception("Unknown operator " + binNode.getOperator().getValue());
		}
	}

	public int visitIntegerLeaf(Ast node) {
		return ((IntegerLeaf) node).getValue();
	}

	public int visitUnaryOperationNode(Ast node) throws Exception {
		UnaryOperationNode unNode = (UnaryOperationNode) node;
		switch (unNode.getValue()) {
		case ("+"):
			return visit(unNode.getExpr());
		case ("-"):
			return -visit(unNode.getExpr());
		default:
			throw new Exception("Unknown operator " + unNode.getValue());
		}
	}

}
