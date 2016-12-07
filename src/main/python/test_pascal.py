
from calc1 import Interpreter, Parser, Lexer
import unittest

PROG = 'BEGIN ' \
        'BEGIN ' \
        'number := 2; ' \
        'a := number; ' \
        'b := 10 * a + 10 * number / 4; ' \
        'c := a - - b ' \
        'END; ' \
        'x := 11; ' \
        'END. '


class TestCalculator(unittest.TestCase):

    def init_interpreter(self, program):
        return Interpreter(Parser(Lexer(program)))

    def test_parse_example(self):
        self.assertIsNotNone(Parser(Lexer(PROG)).parse())

    def test_example(self):
        interpreter = self.init_interpreter(PROG)
        interpreter.interpret()
        self.assertEqual(interpreter.GLOBAL_SCOPE['number'], 2)
        self.assertEqual(interpreter.GLOBAL_SCOPE['a'], 2)
        self.assertEqual(interpreter.GLOBAL_SCOPE['b'], 25)
        self.assertEqual(interpreter.GLOBAL_SCOPE['c'], 27)
        self.assertEqual(interpreter.GLOBAL_SCOPE['x'], 11)

    def test_empty_statement(self):
        interpreter = self.init_interpreter('BEGIN END.')
        interpreter.interpret()
        self.assertIsNotNone(interpreter.GLOBAL_SCOPE)
        self.assertEqual(interpreter.GLOBAL_SCOPE, {})

    def test_error_missing_dot(self):
        with self.assertRaises(Exception):
            self.init_interpreter('BEGIN END').expr()

    def test_error_missing_end(self):
        with self.assertRaises(Exception):
            self.init_interpreter('BEGIN .').expr()


if __name__ == '__main__':
    unittest.main()
