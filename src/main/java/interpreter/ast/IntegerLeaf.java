package interpreter.ast;

import java.text.ParseException;

public class IntegerLeaf extends Ast {

	private final int value;

	public IntegerLeaf(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public int calculate() throws ParseException {
		return value;
	}

}
