package interpreter.tokens;

public abstract class BracketToken extends Token {

	public BracketToken(String value) {
		super(value);
	}

	public static Token create(Character character) {
		switch (character) {
		case ('('):
			return new RoundOpeningBracketToken(null);
		case (')'):
			return new RoundClosingBracketToken(null);
		}
		throw new IllegalArgumentException(character + " is not a valid operator");
	}

}
