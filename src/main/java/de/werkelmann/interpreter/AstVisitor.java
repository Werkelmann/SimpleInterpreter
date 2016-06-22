package de.werkelmann.interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.werkelmann.interpreter.ast.AssignNode;
import de.werkelmann.interpreter.ast.Ast;
import de.werkelmann.interpreter.ast.BinaryOperationNode;
import de.werkelmann.interpreter.ast.CompoundNode;
import de.werkelmann.interpreter.ast.IntegerLeaf;
import de.werkelmann.interpreter.ast.UnaryOperationNode;
import de.werkelmann.interpreter.ast.VarLeaf;

public class AstVisitor {

	public Map<Integer, Integer> globelScope = new HashMap<>();

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

	public int visitCompoundNode(Ast node) {
		CompoundNode cn = (CompoundNode) node;
		for (Ast child : cn.getChildren()) {
			visit(child);
		}
		return 0;
	}

	public int visitNoOp(Ast node) {
		return 0;
	}

	public int visitAssignNode(Ast node) {
		AssignNode an = (AssignNode) node;

		globelScope.put(visit(an.getVar()), visit(an.getExpr()));

		return 0;
	}

	public int visitVarLeaf(Ast node) {
		VarLeaf vl = (VarLeaf) node;

		// return vl.getValue();
		return 7;
	}

}
