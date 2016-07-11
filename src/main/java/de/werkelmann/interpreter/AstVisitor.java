package de.werkelmann.interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

import de.werkelmann.interpreter.ast.AssignNode;
import de.werkelmann.interpreter.ast.Ast;
import de.werkelmann.interpreter.ast.BinaryOperationNode;
import de.werkelmann.interpreter.ast.CompoundNode;
import de.werkelmann.interpreter.ast.IntegerLeaf;
import de.werkelmann.interpreter.ast.UnaryOperationNode;
import de.werkelmann.interpreter.ast.VarLeaf;

public class AstVisitor {

	private Map<String, Integer> globelScope;
	private Map<String, IntBinaryOperator> binaryOperations;
	private Map<String, IntUnaryOperator> unaryOperations;

	public AstVisitor() {
		globelScope = new HashMap<>();
		initOperations();
	}

	private void initOperations() {
		binaryOperations = new HashMap<>();
		binaryOperations.put("+", (a, b) -> a + b);
		binaryOperations.put("-", (a, b) -> a - b);
		binaryOperations.put("*", (a, b) -> a * b);
		binaryOperations.put("div", (a, b) -> a / b);

		unaryOperations = new HashMap<>();
		unaryOperations.put("+", (a) -> a);
		unaryOperations.put("-", (a) -> -a);
	}

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
		return binaryOperations.get(binNode.getOperator().getValue().get()).applyAsInt(visit(binNode.getLeft()),
				visit(binNode.getRight()));
	}

	public int visitIntegerLeaf(Ast node) {
		return ((IntegerLeaf) node).getValue();
	}

	public int visitUnaryOperationNode(Ast node) throws Exception {
		UnaryOperationNode unNode = (UnaryOperationNode) node;
		return unaryOperations.get(unNode.getValue()).applyAsInt(visit(unNode.getExpr()));
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
		globelScope.put(visitVarLeafInAssignment(an.getVar()), visit(an.getExpr()));
		return 0;
	}

	public int visitVarLeaf(Ast node) {
		VarLeaf vl = (VarLeaf) node;
		return globelScope.get(vl.getValue());
	}

	public String visitVarLeafInAssignment(Ast node) {
		return ((VarLeaf) node).getValue();
	}

	public int getVariable(String key) {
		return globelScope.get(key);
	}

}
