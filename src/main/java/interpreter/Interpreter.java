package interpreter;

import java.text.ParseException;

public class Interpreter {

	private Scanner scanner;
	private Parser parser;

	public Interpreter() {
		this.scanner = new Scanner();
		this.parser = new Parser();
	}

	public int execute(String input) throws ParseException {
		return parser.parse(scanner.scan(input));
	}

	public static void main(String[] args) throws ParseException {
		Interpreter i = new Interpreter();
		for (String arg : args) {
			System.out.println(i.execute(arg));
		}
	}

}
