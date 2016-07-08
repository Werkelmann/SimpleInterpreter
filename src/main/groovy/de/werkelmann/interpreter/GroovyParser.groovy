package de.werkelmann.interpreter

import de.werkelmann.interpreter.ast.Ast
import de.werkelmann.interpreter.tokens.Token
import de.werkelmann.interpreter.tokens.TokenList

class GroovyParser {

	private TokenList tokens
	private Token currentToken

	public Ast parse(TokenList tokens) {
		this.tokens = tokens
		this.currentToken = tokens.getNextToken()

		Ast result = program()

		return result;
	}
}
