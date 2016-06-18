package interpreter.tokens;

import java.util.List;

public class TokenList {

	private final List<Token> tokens;
	private int position;

	public TokenList(List<Token> tokens) {
		this.tokens = tokens;
		this.setPosition(0);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Token getNextToken() {
		if (position < tokens.size()) {
			return tokens.get(position++);
		}
		return tokens.get(tokens.size() - 1);
	}

	public boolean hasNext() {
		return tokens.get(position) instanceof EndOfFileToken;
	}

}
