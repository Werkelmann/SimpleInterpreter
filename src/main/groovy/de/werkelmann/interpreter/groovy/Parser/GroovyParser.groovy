package de.werkelmann.interpreter.groovy.Parser

import de.werkelmann.interpreter.ast.Ast
import de.werkelmann.interpreter.parser.Parser
import de.werkelmann.interpreter.tokens.Token
import de.werkelmann.interpreter.tokens.TokenList
import de.werkelmann.interpreter.util.ParserException

class GroovyParser implements Parser {

	private TokenList tokens
	private Token currentToken

	@Override
	public Ast parse(TokenList tokens) {
		this.tokens = tokens
		this.currentToken = tokens.getNextToken()

		//		Ast result = program()
		//		if (!(eat(EndOfFileToken.TOKEN_TYPE))) {
		//			throwParserException(EndOfFileToken.TOKEN_TYPE)
		//		}

		//		return result;
		return null;
	}

	public Ast expr(TokenList tokens) {
		this.tokens = tokens
		this.currentToken = tokens.getNextToken()

		Ast result = expr()
		if (!(eat(EndOfFileToken.TOKEN_TYPE))) {
			throwParserException(EndOfFileToken.TOKEN_TYPE)
		}

		return result;
	}



	private boolean eat(String expected) {
		return currentToken.getType().equals(expected)
	}

	private void throwParserException(String expected) {
		throw new ParserException("Expected: " + expected + " Found: " + currentToken + " at " + currentToken.getPosition(),
		currentToken.getPosition())
	}
}
