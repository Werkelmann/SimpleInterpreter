package de.werkelmann.interpreter.parser;

import java.util.ArrayList;
import java.util.List;

import de.werkelmann.interpreter.ast.AssignNode;
import de.werkelmann.interpreter.ast.Ast;
import de.werkelmann.interpreter.ast.BinaryOperationNode;
import de.werkelmann.interpreter.ast.CompoundNode;
import de.werkelmann.interpreter.ast.IntegerLeaf;
import de.werkelmann.interpreter.ast.NoOp;
import de.werkelmann.interpreter.ast.UnaryOperationNode;
import de.werkelmann.interpreter.ast.VarLeaf;
import de.werkelmann.interpreter.tokens.Token;
import de.werkelmann.interpreter.tokens.TokenList;
import de.werkelmann.interpreter.util.ParserException;

public class JavaRecursiveDescentParser implements Parser {

	private TokenList tokens;
	private Token currentToken;

	@Override
	public Ast parse(TokenList tokens) {
		this.tokens = tokens;
		this.currentToken = tokens.getNextToken();

		Ast result = program();
		if (!(eat(Token.END_OF_FILE))) {
			throwParserException(Token.END_OF_FILE);
		}
		return result;
	}

	@Override
	public Ast expr(TokenList tokens) {
		this.tokens = tokens;
		this.currentToken = tokens.getNextToken();

		Ast result = expr();
		if (!(eat(Token.END_OF_FILE))) {
			throwParserException(Token.END_OF_FILE);
		}
		return result;
	}

	private Ast program() {
		Ast result = compoundStatement();
		if (!checkTokenForTypeAndValue(Token.SIGN, ".")) {
			throwParserException(Token.SIGN);
			return null;
		} else {
			nextToken();
			return result;
		}
	}

	private Ast compoundStatement() {
		if (!(checkTokenForTypeAndValue(Token.IDENTIFIER, "BEGIN"))) {
			throwParserException("BEGIN");
		}
		nextToken();

		List<Ast> nodes = statementList();
		if (!(checkTokenForTypeAndValue(Token.IDENTIFIER, "END"))) {
			throwParserException("END");
		}
		CompoundNode result = new CompoundNode();
		nextToken();
		for (Ast node : nodes) {
			result.addChild(node);
		}
		return result;
	}

	private List<Ast> statementList() {
		List<Ast> stmts = new ArrayList<>();

		Ast node = statement();
		stmts.add(node);

		while (checkTokenForTypeAndValue(Token.SIGN, ";")) {
			nextToken();
			stmts.add(statement());
		}

		return stmts;
	}

	private Ast statement() {

		if (checkTokenForTypeAndValue(Token.IDENTIFIER, "BEGIN")) {
			return compoundStatement();
		}

		if (eat(Token.IDENTIFIER) && !currentToken.getValue().get().equals("END")) {
			return assignStatement();
		}

		return empty();
	}

	private Ast assignStatement() {
		Ast left = variable();
		Token token = currentToken;
		if (!checkTokenForTypeAndValue(Token.SIGN, ":=")) {
			throwParserException(":=");
		}
		nextToken();
		Ast right = expr();
		return new AssignNode(left, token, right);
	}

	private Ast variable() {
		Ast result = new VarLeaf(currentToken.getValue().get());
		if (!this.eat(Token.IDENTIFIER)) {
			throwParserException(Token.IDENTIFIER);
		}
		nextToken();
		return result;
	}

	private Ast empty() {
		return new NoOp();
	}

	private Ast expr() {
		Ast result = term();
		while (this.isExprOperator(currentToken.getValue().orElse(""))) {
			Token op = currentToken;
			if (!this.eat(Token.OPERATOR)) {
				throwParserException(Token.OPERATOR);
			}
			this.nextToken();

			result = new BinaryOperationNode(result, op, term());
		}

		return result;
	}

	private boolean isExprOperator(String value) {
		return (value != null && (value.equals("+") || value.equals("-")));
	}

	private Ast term() {
		Ast result = factor();
		while (isTermOperator(currentToken.getValue().orElse(""))) {
			Token op = currentToken;
			if (!this.eat(Token.OPERATOR)) {
				throwParserException(Token.OPERATOR);
			}
			this.nextToken();
			result = new BinaryOperationNode(result, op, factor());
		}

		return result;
	}

	private boolean isTermOperator(String value) {
		return (value != null && (value.equals("*") || value.equals("div")));
	}

	private Ast factor() {
		Ast result;
		if (this.eat(Token.OPERATOR)) {
			String value = currentToken.getValue().get();
			nextToken();
			result = new UnaryOperationNode(value, this.factor());
			return result;
		}
		if (this.eat(Token.INTEGER)) {
			result = new IntegerLeaf(Integer.parseInt(currentToken.getValue().get()));
			nextToken();
			return result;
		}
		if (checkTokenForTypeAndValue(Token.BRACKET, "(")) {
			nextToken();
			result = expr();
			if (!checkTokenForTypeAndValue(Token.BRACKET, ")")) {
				throwParserException(")");
			}
			nextToken();
			return result;
		}
		if (this.eat(Token.IDENTIFIER)) {
			return variable();
		}
		throwParserException("Found: " + currentToken);
		return null;
	}

	private boolean checkTokenForTypeAndValue(String type, String value) {
		return (eat(type) && currentToken.getValue().get().toLowerCase().equals(value.toLowerCase()));
	}

	private boolean eat(String expected) {
		return currentToken.getType().equals(expected);
	}

	private void nextToken() {
		currentToken = tokens.getNextToken();
	}

	private void throwParserException(String expected) {
		throw new ParserException(
				"Expected: " + expected + " Found: " + currentToken + " at " + currentToken.getPosition(),
				currentToken.getPosition());
	}

}
