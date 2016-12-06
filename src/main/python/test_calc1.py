
from calc1 import Interpreter, Parser, Lexer
import unittest


class TestCalculator(unittest.TestCase):

    def init_interpreter(self, program):
        return Interpreter(Parser(Lexer(program)))

    def test_addition(self):
        self.assertEqual(self.init_interpreter('2+5').interpret(), 7)
        self.assertEqual(self.init_interpreter('5+2').interpret(), 7)
        self.assertEqual(self.init_interpreter('8+5').interpret(), 13)

    def test_subtraction(self):
        self.assertEqual(self.init_interpreter('2-5').interpret(), -3)
        self.assertEqual(self.init_interpreter('5-2').interpret(), 3)
        self.assertEqual(self.init_interpreter('9-5').interpret(), 4)

    def test_multi_digit_input(self):
        self.assertEqual(self.init_interpreter('12-5').interpret(), 7)

    def test_input_with_whitespace(self):
        self.assertEqual(self.init_interpreter('12 - 5').interpret(), 7)

    def test_multiplication(self):
        self.assertEqual(self.init_interpreter('2*5').interpret(), 10)
        self.assertEqual(self.init_interpreter('5*2').interpret(), 10)
        self.assertEqual(self.init_interpreter('8*5').interpret(), 40)

    def test_division(self):
        self.assertEqual(self.init_interpreter('6/3').interpret(), 2)
        self.assertEqual(self.init_interpreter('15/5').interpret(), 3)
        self.assertEqual(self.init_interpreter('9/5').interpret(), 1.8)

    def test_raise_exception_for_unknown_operator(self):
        with self.assertRaises(Exception):
            self.init_interpreter('3?4').interpret()

    def test_longer_simple_expr(self):
        self.assertEqual(self.init_interpreter('3+5-2').interpret(), 6)
        self.assertEqual(self.init_interpreter('3-5+2').interpret(), 0)
        self.assertEqual(self.init_interpreter('3-5+5').interpret(), 3)

    def test_longer_expr_with_precedence(self):
        self.assertEqual(self.init_interpreter('3*5-2').interpret(), 13)
        self.assertEqual(self.init_interpreter('3-5*2').interpret(), -7)
        self.assertEqual(self.init_interpreter('14 + 2 * 3 - 6 / 2').interpret(), 17)

    def test_interpret_with_parenthesis(self):
        self.assertEqual(self.init_interpreter('(7-5)*2').interpret(), 4)
        self.assertEqual(self.init_interpreter('((7-5)+2)*3').interpret(), 12)

    def test_expr_chapter_seven(self):
        self.assertEqual(self.init_interpreter('7 + 3 * (10 / (12 / (3 + 1) - 1))').interpret(), 22)
        self.assertEqual(self.init_interpreter('7 + 3 * (10 / (12 / (3 + 1) - 1)) / (2 + 3) - 5 - 3 + (8)')
                         .interpret(), 10)
        self.assertEqual(self.init_interpreter('7 + (((3 + 2)))').interpret(), 12)

if __name__ == '__main__':
    unittest.main()
