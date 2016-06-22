package de.werkelmann.interpreter.ast;

public class VarLeaf extends Ast {

	private final String value;

	public VarLeaf(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
