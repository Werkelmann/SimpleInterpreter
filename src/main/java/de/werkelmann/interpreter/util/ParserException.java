package de.werkelmann.interpreter.util;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = 8323155879584272789L;
	private final Position position;

	public ParserException(String message, Position position) {
		super(message);
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}

}
