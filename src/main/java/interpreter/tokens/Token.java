package interpreter.tokens;

public abstract class Token {

	private final String value;
	protected final String type;

	public Token(String value) {
		this.value = value;
		this.type = getTokenType();
	}

	protected abstract String getTokenType();

	@Override
	public String toString() {
		return "(" + getType() + ", " + getValue() + ")";
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

}
