package interpreter.ast;

public class IntegerLeaf extends Ast {

	private final int value;

	public IntegerLeaf(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
