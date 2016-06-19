package interpreter;

import java.text.ParseException;

import interpreter.ast.Ast;

public class AstTraverser {

	public int calculate(Ast ast) throws ParseException {
		return ast.calculate();
	}

}
