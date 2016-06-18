package interpreter;

public class Token {

	public static final String INTEGER = "INTEGER";
	public static final String OPERATOR = "OPERATOR";
	public static final String EOF = "EOF";

	private final String type;
	private final String value;

	public Token(String type, String value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public String toString() {
		return "(" + type + ", " + value + ")";
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

}
