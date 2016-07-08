package de.werkelmann.interpreter.parser;

import de.werkelmann.interpreter.ast.Ast;
import de.werkelmann.interpreter.tokens.TokenList;

public interface Parser {

	public Ast parse(TokenList tokens);
}
