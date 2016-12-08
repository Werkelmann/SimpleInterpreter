
from calc1 import Interpreter, Parser, Lexer
import unittest


class TestCalculator(unittest.TestCase):

    def init_interpreter(self, program):
        return Interpreter(Parser(Lexer(program)))

    def test_addition(self):
        self.assertEqual(self.init_interpreter('2+5').expr(), 7)
        self.assertEqual(self.init_interpreter('5+2').expr(), 7)
        self.assertEqual(self.init_interpreter('8+5').expr(), 13)

    def test_subtraction(self):
        self.assertEqual(self.init_interpreter('2-5').expr(), -3)
        self.assertEqual(self.init_interpreter('5-2').expr(), 3)
        self.assertEqual(self.init_interpreter('9-5').expr(), 4)

    def test_multi_digit_input(self):
        self.assertEqual(self.init_interpreter('12-5').expr(), 7)

    def test_input_with_whitespace(self):
        self.assertEqual(self.init_interpreter('12 - 5').expr(), 7)

    def test_multiplication(self):
        self.assertEqual(self.init_interpreter('2*5').expr(), 10)
        self.assertEqual(self.init_interpreter('5*2').expr(), 10)
        self.assertEqual(self.init_interpreter('8*5').expr(), 40)

    def test_division(self):
        self.assertEqual(self.init_interpreter('6 DIV 3').expr(), 2)
        self.assertEqual(self.init_interpreter('15 DIV 5').expr(), 3)
        self.assertEqual(self.init_interpreter('9 DIV 5').expr(), 1)
        self.assertEqual(self.init_interpreter('9 / 5').expr(), 1.8)

    def test_raise_exception_for_unknown_operator(self):
        with self.assertRaises(Exception):
            self.init_interpreter('3?4').expr()

    def test_longer_simple_expr(self):
        self.assertEqual(self.init_interpreter('3+5-2').expr(), 6)
        self.assertEqual(self.init_interpreter('3-5+2').expr(), 0)
        self.assertEqual(self.init_interpreter('3-5+5').expr(), 3)

    def test_longer_expr_with_precedence(self):
        self.assertEqual(self.init_interpreter('3*5-2').expr(), 13)
        self.assertEqual(self.init_interpreter('3-5*2').expr(), -7)
        self.assertEqual(self.init_interpreter('14 + 2 * 3 - 6  DIV  2').expr(), 17)

    def test_interpret_with_parenthesis(self):
        self.assertEqual(self.init_interpreter('(7-5)*2').expr(), 4)
        self.assertEqual(self.init_interpreter('((7-5)+2)*3').expr(), 12)

    def test_examples_chapter_seven(self):
        self.assertEqual(self.init_interpreter('7 + 3 * (10 DIV (12 DIV (3 + 1) - 1))').expr(), 22)
        self.assertEqual(self.init_interpreter('7 + 3 * (10 DIV (12 DIV (3 + 1) - 1)) DIV (2 + 3) - 5 - 3 + (8)')
                         .expr(), 10)
        self.assertEqual(self.init_interpreter('7 + (((3 + 2)))').expr(), 12)

    def test_unary_op(self):
        self.assertEqual(self.init_interpreter('5 - - 2').expr(), 7)
        self.assertEqual(self.init_interpreter('5 + - 2').expr(), 3)
        self.assertEqual(self.init_interpreter('5 - - - 2').expr(), 3)


if __name__ == '__main__':
    unittest.main()
