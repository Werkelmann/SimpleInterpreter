package interpreter.translator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import interpreter.ast.Ast;
import interpreter.ast.BinaryOperationNode;
import interpreter.ast.IntegerLeaf;
import interpreter.ast.UnaryOperationNode;

public class RPNTranslator {

	public String visit(Ast node) {
		Class<? extends Ast> clazz = node.getClass();
		String methodName = "visit" + clazz.getSimpleName();
		try {
			Method visitorMethod = this.getClass().getMethod(methodName, Ast.class);
			return (String) visitorMethod.invoke(this, node);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			System.err.println("No visitor defined for " + clazz.getSimpleName());
			return "";
		}
	}

	public String visitBinaryOperationNode(Ast node) throws Exception {
		BinaryOperationNode binNode = (BinaryOperationNode) node;
		return visit(binNode.getLeft()) + " " + visit(binNode.getRight()) + " " + binNode.getOperator().getValue();

	}

	public String visitIntegerLeaf(Ast node) {
		return String.valueOf(((IntegerLeaf) node).getValue());
	}

	public String visitUnaryOperationNode(Ast node) throws Exception {
		UnaryOperationNode unNode = (UnaryOperationNode) node;
		switch (unNode.getValue()) {
		case ("+"):
			return visit(unNode.getExpr()) + " +";
		case ("-"):
			return visit(unNode.getExpr()) + " -";
		default:
			throw new Exception("Unknown operator " + unNode.getValue());
		}
	}

}
