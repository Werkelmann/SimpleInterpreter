
from calc1 import Interpreter
import unittest


class TestCalculator(unittest.TestCase):

    def test_plus(self):
        self.assertEqual(Interpreter('2+5').expr(), 7)
        self.assertEqual(Interpreter('5+2').expr(), 7)
        self.assertEqual(Interpreter('8+5').expr(), 13)

    def test_minus(self):
        self.assertEqual(Interpreter('2-5').expr(), -3)
        self.assertEqual(Interpreter('5-2').expr(), 3)
        self.assertEqual(Interpreter('9-5').expr(), 4)

    def test_multi_digit_input(self):
        self.assertEqual(Interpreter('12-5').expr(), 7)

    def test_input_with_whitespace(self):
        self.assertEqual(Interpreter('12 - 5').expr(), 7)

if __name__ == '__main__':
    unittest.main()