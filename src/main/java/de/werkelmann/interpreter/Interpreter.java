package de.werkelmann.interpreter;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public class Interpreter {

	private Scanner scanner;
	private Parser parser;
	private AstVisitor visitor;

	public Interpreter() {
		this.scanner = new Scanner();
		this.parser = new Parser();
		this.visitor = new AstVisitor();
	}

	public int execute(String input) throws ParseException {
		return visitor.visit(parser.parse(scanner.scan(input)));
	}

	public Map<Integer, Integer> getVariables() {
		return visitor.globelScope;
	}

	public static void main(String[] args) throws ParseException {
		Interpreter i = new Interpreter();
		// for (String arg : args) {
		// System.out.println(i.execute(arg));
		// }

		String f = getFile("SampleProgram1");
		Parser p = new Parser();
		Scanner s = new Scanner();

		p.parse(s.scan(f));

	}

	private static String getFile(String fileName) {
		Interpreter i = new Interpreter();
		StringBuilder result = new StringBuilder("");

		// Get file from resources folder
		ClassLoader classLoader = i.getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		try (java.util.Scanner scanner = new java.util.Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}

			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();

	}

}
