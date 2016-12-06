
from calc1 import Interpreter, Lexer
import unittest


class TestCalculator(unittest.TestCase):

    def test_addition(self):
        self.assertEqual(Interpreter(Lexer('2+5')).expr(), 7)
        self.assertEqual(Interpreter(Lexer('5+2')).expr(), 7)
        self.assertEqual(Interpreter(Lexer('8+5')).expr(), 13)

    def test_subtraction(self):
        self.assertEqual(Interpreter(Lexer('2-5')).expr(), -3)
        self.assertEqual(Interpreter(Lexer('5-2')).expr(), 3)
        self.assertEqual(Interpreter(Lexer('9-5')).expr(), 4)

    def test_multi_digit_input(self):
        self.assertEqual(Interpreter(Lexer('12-5')).expr(), 7)

    def test_input_with_whitespace(self):
        self.assertEqual(Interpreter(Lexer('12 - 5')).expr(), 7)

    def test_multiplication(self):
        self.assertEqual(Interpreter(Lexer('2*5')).expr(), 10)
        self.assertEqual(Interpreter(Lexer('5*2')).expr(), 10)
        self.assertEqual(Interpreter(Lexer('8*5')).expr(), 40)

    def test_division(self):
        self.assertEqual(Interpreter(Lexer('6/3')).expr(), 2)
        self.assertEqual(Interpreter(Lexer('15/5')).expr(), 3)
        self.assertEqual(Interpreter(Lexer('9/5')).expr(), 1.8)

    def test_raise_exception_for_unknown_operator(self):
        with self.assertRaises(Exception):
            Interpreter(Lexer('3?4')).expr()

    def test_longer_simple_expression(self):
        self.assertEqual(Interpreter(Lexer('3+5-2')).expr(), 6)
        self.assertEqual(Interpreter(Lexer('3-5+2')).expr(), 0)
        self.assertEqual(Interpreter(Lexer('3-5+5')).expr(), 3)

    def test_longer_expression_with_precedence(self):
        self.assertEqual(Interpreter(Lexer('3*5-2')).expr(), 13)
        self.assertEqual(Interpreter(Lexer('3-5*2')).expr(), -7)
        self.assertEqual(Interpreter(Lexer('14 + 2 * 3 - 6 / 2')).expr(), 17)

if __name__ == '__main__':
    unittest.main()
