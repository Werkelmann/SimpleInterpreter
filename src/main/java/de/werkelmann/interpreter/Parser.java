package de.werkelmann.interpreter;

import java.text.ParseException;
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
import de.werkelmann.interpreter.tokens.AssignToken;
import de.werkelmann.interpreter.tokens.DotToken;
import de.werkelmann.interpreter.tokens.EndOfFileToken;
import de.werkelmann.interpreter.tokens.IdentifierToken;
import de.werkelmann.interpreter.tokens.IntegerToken;
import de.werkelmann.interpreter.tokens.OperatorToken;
import de.werkelmann.interpreter.tokens.RoundClosingBracketToken;
import de.werkelmann.interpreter.tokens.RoundOpeningBracketToken;
import de.werkelmann.interpreter.tokens.SemicolonToken;
import de.werkelmann.interpreter.tokens.Token;
import de.werkelmann.interpreter.tokens.TokenList;

public class Parser {

	private TokenList tokens;
	private Token currentToken;

	public Ast parse(TokenList tokens) throws ParseException {
		this.tokens = tokens;
		this.currentToken = tokens.getNextToken();

		Ast result = program();
		if (!(currentToken instanceof EndOfFileToken)) {
			throw new ParseException("Expected: EndOfFile Found: " + currentToken, tokens.getPosition());
		}
		return result;
	}

	private Ast program() throws ParseException {
		Ast result = compoundStatement();
		if (!eat(DotToken.TOKEN_TYPE)) {
			throw new ParseException(
					"Error at Position " + tokens.getPosition() + ". Expected: . Found: " + currentToken,
					tokens.getPosition());
		} else {
			nextToken();
			return result;
		}
	}

	private Ast compoundStatement() throws ParseException {
		if (!(eat(IdentifierToken.TOKEN_TYPE) && currentToken.getValue().equals("BEGIN"))) {
			throw new ParseException(
					"Error at Position " + tokens.getPosition() + ". Expected: BEGIN Found: " + currentToken,
					tokens.getPosition());
		}
		nextToken();

		List<Ast> nodes = statementList();
		if (!(eat(IdentifierToken.TOKEN_TYPE) && currentToken.getValue().equals("END"))) {
			throw new ParseException(
					"Error at Position " + tokens.getPosition() + ". Expected: END Found: " + currentToken,
					tokens.getPosition());
		}
		CompoundNode result = new CompoundNode();
		nextToken();
		for (Ast node : nodes) {
			result.addChild(node);
		}
		return result;
	}

	private List<Ast> statementList() throws ParseException {
		List<Ast> stmts = new ArrayList<>();

		Ast node = statement();
		stmts.add(node);

		while (eat(SemicolonToken.TOKEN_TYPE)) {
			nextToken();
			stmts.add(statement());
		}

		return stmts;
	}

	private Ast statement() throws ParseException {

		if (eat(IdentifierToken.TOKEN_TYPE) && currentToken.getValue().equals("BEGIN")) {
			return compoundStatement();
		}

		if (eat(IdentifierToken.TOKEN_TYPE) && !currentToken.getValue().equals("END")) {
			return assignStatement();
		}

		return empty();
	}

	private Ast assignStatement() throws ParseException {
		Ast left = variable();
		Token token = currentToken;
		if (!eat(AssignToken.TOKEN_TYPE)) {
			throw new ParseException(
					"Error at Position " + tokens.getPosition() + ". Expected: := Found: " + currentToken,
					tokens.getPosition());
		}
		nextToken();
		Ast right = expr();
		return new AssignNode(left, token, right);
	}

	private Ast variable() throws ParseException {
		Ast result = new VarLeaf(currentToken.getValue());
		eat(IdentifierToken.TOKEN_TYPE);
		nextToken();
		return result;
	}

	private Ast empty() {
		return new NoOp();
	}

	private Ast expr() throws ParseException {
		Ast result = term();
		while (this.isExprOperator(currentToken.getValue())) {
			OperatorToken op = (OperatorToken) currentToken;
			this.eat(OperatorToken.TOKEN_TYPE);
			this.nextToken();

			result = new BinaryOperationNode(result, op, term());
		}

		return result;
	}

	private boolean isExprOperator(String value) {
		return (value != null && (value.equals("+") || value.equals("-")));
	}

	private Ast term() throws ParseException {
		Ast result = factor();
		while (isTermOperator(currentToken.getValue())) {
			OperatorToken op = (OperatorToken) currentToken;
			this.eat(OperatorToken.TOKEN_TYPE);
			this.nextToken();
			result = new BinaryOperationNode(result, op, factor());
		}

		return result;
	}

	private boolean isTermOperator(String value) {
		return (value != null && (value.equals("*") || value.equals("/")));
	}

	private Ast factor() throws ParseException {
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
		if (this.eat(RoundOpeningBracketToken.TOKEN_TYPE)) {
			nextToken();
			result = expr();
			if (!this.eat(RoundClosingBracketToken.TOKEN_TYPE)) {
				throw new ParseException(
						"Error at Position " + tokens.getPosition() + ". Missing round closing bracket!",
						tokens.getPosition());
			}
			nextToken();
			return result;
		}
		if (this.eat(IdentifierToken.TOKEN_TYPE)) {
			nextToken();
			return variable();
		}
		throw new ParseException("Error at Position " + tokens.getPosition(), tokens.getPosition());
	}

	private boolean eat(String expected) throws ParseException {
		return currentToken.getType().equals(expected);
	}

	private void nextToken() {
		currentToken = tokens.getNextToken();
	}

}
