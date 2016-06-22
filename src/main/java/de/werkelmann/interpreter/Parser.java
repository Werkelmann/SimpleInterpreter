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

		try {
			Ast result = program();
			if (!(currentToken instanceof EndOfFileToken)) {
				throw new Exception("Expected: EndOfFile Found: " + currentToken);
			}
			return result;
		} catch (Exception e) {
			throw new ParseException("Failure at position " + tokens.getPosition() + " " + e.getMessage(),
					tokens.getPosition());
		}
	}

	private Ast program() throws ParseException {
		Ast result = compoundStatement();
		if (!eat(DotToken.TOKEN_TYPE)) {
			throw new ParseException(
					"Error at Position " + tokens.getPosition() + ". Expected: . Found: " + currentToken,
					tokens.getPosition());
		} else {
			return result;
		}
	}

	private Ast compoundStatement() throws ParseException {
		int pos = tokens.getPosition() - 1;
		if (!(eat(IdentifierToken.TOKEN_TYPE) && tokens.getTokenAt(pos).getValue().equals("BEGIN"))) {
			throw new ParseException(
					"Error at Position " + tokens.getPosition() + ". Expected: BEGIN Found: " + currentToken,
					tokens.getPosition());
		}

		List<Ast> nodes = statementList();
		pos = tokens.getPosition() - 1;
		if (!(eat(IdentifierToken.TOKEN_TYPE) && tokens.getTokenAt(pos).getValue().equals("END"))) {
			throw new ParseException(
					"Error at Position " + tokens.getPosition() + ". Expected: END Found: " + currentToken,
					tokens.getPosition());
		}
		CompoundNode result = new CompoundNode();
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
			stmts.add(statement());
		}

		// if (!eat(IdentifierToken.TOKEN_TYPE)) {
		// throw new ParseException(
		// "Error at Position " + tokens.getPosition() + ". Expected: END Found:
		// " + currentToken,
		// tokens.getPosition());
		// }

		return stmts;
	}

	private Ast statement() throws ParseException {
		int pos = tokens.getPosition() - 1;

		if (eat(IdentifierToken.TOKEN_TYPE) && tokens.getTokenAt(pos).getValue().equals("BEGIN")) {
			tokens.setPosition(pos);
			return compoundStatement();
		}
		currentToken = tokens.getTokenAt(pos);
		tokens.setPosition(pos);

		if (eat(IdentifierToken.TOKEN_TYPE) && !tokens.getTokenAt(pos).getValue().equals("END")) {
			// tokens.setPosition(pos);
			// currentToken = tokens.getNextToken();
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
		Ast right = expr();
		return new AssignNode(left, token, right);
	}

	private Ast variable() throws ParseException {
		Ast result = new VarLeaf(currentToken.getValue());
		eat(IdentifierToken.TOKEN_TYPE);
		return result;
	}

	private Ast empty() {
		return new NoOp();
	}

	private Ast expr() throws ParseException {
		Ast result = term();
		while (isExprOperator(currentToken.getValue())) {
			OperatorToken op = (OperatorToken) currentToken;
			this.eat(OperatorToken.TOKEN_TYPE);

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

			result = new BinaryOperationNode(result, op, factor());
		}

		return result;
	}

	private boolean isTermOperator(String value) {
		return (value != null && (value.equals("*") || value.equals("/")));
	}

	private Ast factor() throws ParseException {
		Ast result;
		Token temp = currentToken;
		if (this.eat(OperatorToken.TOKEN_TYPE)) {
			return new UnaryOperationNode(temp.getValue(), this.factor());
		}
		if (this.eat(IntegerToken.TOKEN_TYPE)) {
			result = new IntegerLeaf(Integer.parseInt(temp.getValue()));
			return result;
		}
		if (this.eat(RoundOpeningBracketToken.TOKEN_TYPE)) {
			result = expr();
			if (!this.eat(RoundClosingBracketToken.TOKEN_TYPE)) {
				throw new ParseException(
						"Error at Position " + tokens.getPosition() + ". Missing round closing bracket!",
						tokens.getPosition());
			}
			return result;
		}
		if (this.eat(IdentifierToken.TOKEN_TYPE)) {
			return variable();
		}
		throw new ParseException("Error at Position " + tokens.getPosition(), tokens.getPosition());
	}

	private boolean eat(String expected) throws ParseException {
		if (currentToken.getType().equals(expected)) {
			currentToken = tokens.getNextToken();
			return true;
		} else {
			return false;
		}
	}

}
