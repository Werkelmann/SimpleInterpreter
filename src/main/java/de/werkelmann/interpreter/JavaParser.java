package de.werkelmann.interpreter;

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
import de.werkelmann.interpreter.tokens.BracketToken;
import de.werkelmann.interpreter.tokens.EndOfFileToken;
import de.werkelmann.interpreter.tokens.IdentifierToken;
import de.werkelmann.interpreter.tokens.IntegerToken;
import de.werkelmann.interpreter.tokens.OperatorToken;
import de.werkelmann.interpreter.tokens.SignToken;
import de.werkelmann.interpreter.tokens.Token;
import de.werkelmann.interpreter.tokens.TokenList;
import de.werkelmann.interpreter.util.ParserException;

public class JavaParser {

	private TokenList tokens;
	private Token currentToken;

	public Ast parse(TokenList tokens) {
		this.tokens = tokens;
		this.currentToken = tokens.getNextToken();

		Ast result = program();
		if (!(eat(EndOfFileToken.TOKEN_TYPE))) {
			throwParserException(EndOfFileToken.TOKEN_TYPE);
		}
		return result;
	}

	public Ast expr(TokenList tokens) {
		this.tokens = tokens;
		this.currentToken = tokens.getNextToken();

		Ast result = expr();
		if (!(eat(EndOfFileToken.TOKEN_TYPE))) {
			throwParserException(EndOfFileToken.TOKEN_TYPE);
		}
		return result;
	}

	private Ast program() {
		Ast result = compoundStatement();
		if (!eat(SignToken.TOKEN_TYPE)) {
			throwParserException(SignToken.TOKEN_TYPE);
			return null;
		} else {
			nextToken();
			return result;
		}
	}

	private Ast compoundStatement() {
		if (!(checkTokenForTypeAndValue(IdentifierToken.TOKEN_TYPE, "BEGIN"))) {
			throwParserException("BEGIN");
		}
		nextToken();

		List<Ast> nodes = statementList();
		if (!(checkTokenForTypeAndValue(IdentifierToken.TOKEN_TYPE, "END"))) {
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

		while (checkTokenForTypeAndValue(SignToken.TOKEN_TYPE, ";")) {
			nextToken();
			stmts.add(statement());
		}

		return stmts;
	}

	private Ast statement() {

		if (checkTokenForTypeAndValue(IdentifierToken.TOKEN_TYPE, "BEGIN")) {
			return compoundStatement();
		}

		if (eat(IdentifierToken.TOKEN_TYPE) && !currentToken.getValue().equals("END")) {
			return assignStatement();
		}

		return empty();
	}

	private Ast assignStatement() {
		Ast left = variable();
		Token token = currentToken;
		if (!checkTokenForTypeAndValue(SignToken.TOKEN_TYPE, ":=")) {
			throwParserException(":=");
		}
		nextToken();
		Ast right = expr();
		return new AssignNode(left, token, right);
	}

	private Ast variable() {
		Ast result = new VarLeaf(currentToken.getValue());
		if (!this.eat(IdentifierToken.TOKEN_TYPE)) {
			throwParserException(IdentifierToken.TOKEN_TYPE);
		}
		nextToken();
		return result;
	}

	private Ast empty() {
		return new NoOp();
	}

	private Ast expr() {
		Ast result = term();
		while (this.isExprOperator(currentToken.getValue())) {
			OperatorToken op = (OperatorToken) currentToken;
			if (!this.eat(OperatorToken.TOKEN_TYPE)) {
				throwParserException(OperatorToken.TOKEN_TYPE);
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
		while (isTermOperator(currentToken.getValue())) {
			OperatorToken op = (OperatorToken) currentToken;
			if (!this.eat(OperatorToken.TOKEN_TYPE)) {
				throwParserException(OperatorToken.TOKEN_TYPE);
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
		if (this.eat(OperatorToken.TOKEN_TYPE)) {
			String value = currentToken.getValue();
			nextToken();
			result = new UnaryOperationNode(value, this.factor());
			return result;
		}
		if (this.eat(IntegerToken.TOKEN_TYPE)) {
			result = new IntegerLeaf(Integer.parseInt(currentToken.getValue()));
			nextToken();
			return result;
		}
		if (checkTokenForTypeAndValue(BracketToken.TOKEN_TYPE, "(")) {
			nextToken();
			result = expr();
			if (!checkTokenForTypeAndValue(BracketToken.TOKEN_TYPE, ")")) {
				throwParserException(")");
			}
			nextToken();
			return result;
		}
		if (this.eat(IdentifierToken.TOKEN_TYPE)) {
			return variable();
		}
		throwParserException("Found: " + currentToken);
		return null;
	}

	private boolean checkTokenForTypeAndValue(String type, String value) {
		return (eat(type) && currentToken.getValue().toLowerCase().equals(value.toLowerCase()));
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
