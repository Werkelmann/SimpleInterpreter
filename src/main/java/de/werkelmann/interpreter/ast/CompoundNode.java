package de.werkelmann.interpreter.ast;

import java.util.ArrayList;
import java.util.List;

public class CompoundNode extends Ast {

	private final List<Ast> children;

	public CompoundNode() {
		this.children = new ArrayList<>();
	}

	public List<Ast> getChildren() {
		return children;
	}

	public void addChild(Ast child) {
		children.add(child);
	}

}
